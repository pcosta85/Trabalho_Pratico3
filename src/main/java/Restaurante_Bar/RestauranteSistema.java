package Restaurante_Bar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RestauranteSistema {

    public boolean adicionarCliente(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO clientes (nome) VALUES (?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome.trim());
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cliente procurarCliente(String nome) {
        String sql = "SELECT id, nome FROM clientes WHERE TRIM(LOWER(nome)) = TRIM(LOWER(?))";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome == null ? "" : nome.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome")
                    );

                    cliente.setPedidos(obterPedidosCliente(cliente));
                    return cliente;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean registrarPedido(String nomeCliente, Produto produto, int quantidade) {
        String sqlCliente = "SELECT id FROM clientes WHERE TRIM(LOWER(nome)) = TRIM(LOWER(?))";
        String sqlProduto = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        String sqlPedido = "INSERT INTO pedidos (cliente_id, produto_id, quantidade, total) VALUES (?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);

            int clienteId;

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setString(1, nomeCliente == null ? "" : nomeCliente.trim());

                try (ResultSet rsCliente = stmtCliente.executeQuery()) {
                    if (!rsCliente.next()) {
                        conn.rollback();
                        return false;
                    }
                    clienteId = rsCliente.getInt("id");
                }
            }

            int produtoId;

            try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto, Statement.RETURN_GENERATED_KEYS)) {
                stmtProduto.setString(1, produto.getNome().trim());
                stmtProduto.setDouble(2, produto.getPreco());
                stmtProduto.executeUpdate();

                try (ResultSet rsProduto = stmtProduto.getGeneratedKeys()) {
                    if (!rsProduto.next()) {
                        conn.rollback();
                        return false;
                    }
                    produtoId = rsProduto.getInt(1);
                }
            }

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido)) {
                double total = produto.getPreco() * quantidade;

                stmtPedido.setInt(1, clienteId);
                stmtPedido.setInt(2, produtoId);
                stmtPedido.setInt(3, quantidade);
                stmtPedido.setDouble(4, total);
                stmtPedido.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Pedido> obterPedidosCliente(Cliente cliente) {
        List<Pedido> lista = new ArrayList<Pedido>();

        String sql = "SELECT p.id AS pedido_id, pr.id AS produto_id, pr.nome, pr.preco, p.quantidade " +
                     "FROM pedidos p " +
                     "INNER JOIN produtos pr ON p.produto_id = pr.id " +
                     "WHERE p.cliente_id = ? " +
                     "ORDER BY p.id";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto prod = new Produto(
                        rs.getInt("produto_id"),
                        rs.getString("nome"),
                        rs.getDouble("preco")
                    );

                    Pedido ped = new Pedido(
                        rs.getInt("pedido_id"),
                        prod,
                        rs.getInt("quantidade")
                    );

                    lista.add(ped);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean alterarNomeCliente(String nomeAtual, String novoNome) {
        if (nomeAtual == null || nomeAtual.trim().isEmpty() ||
            novoNome == null || novoNome.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE clientes SET nome = ? WHERE TRIM(LOWER(nome)) = TRIM(LOWER(?))";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome.trim());
            stmt.setString(2, nomeAtual.trim());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarCliente(String nome) {
        String sql = "DELETE FROM clientes WHERE TRIM(LOWER(nome)) = TRIM(LOWER(?))";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome == null ? "" : nome.trim());
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterarPedido(String nomeCliente, int indice, String novoProduto, double novoPreco, int novaQuantidade) {
        if (indice < 0) {
            return false;
        }

        String sqlCliente = "SELECT id FROM clientes WHERE TRIM(LOWER(nome)) = TRIM(LOWER(?))";
        String sqlPedidoPorIndice = "SELECT p.id FROM pedidos p WHERE p.cliente_id = ? ORDER BY p.id LIMIT ?, 1";
        String sqlProdutoAtualDoPedido = "SELECT produto_id FROM pedidos WHERE id = ?";
        String sqlUpdateProduto = "UPDATE produtos SET nome = ?, preco = ? WHERE id = ?";
        String sqlUpdatePedido = "UPDATE pedidos SET quantidade = ?, total = ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);

            int clienteId;
            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setString(1, nomeCliente == null ? "" : nomeCliente.trim());

                try (ResultSet rs = stmtCliente.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    clienteId = rs.getInt("id");
                }
            }

            int pedidoId;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedidoPorIndice)) {
                stmtPedido.setInt(1, clienteId);
                stmtPedido.setInt(2, indice);

                try (ResultSet rs = stmtPedido.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    pedidoId = rs.getInt("id");
                }
            }

            int produtoId;
            try (PreparedStatement stmtProdAtual = conn.prepareStatement(sqlProdutoAtualDoPedido)) {
                stmtProdAtual.setInt(1, pedidoId);

                try (ResultSet rs = stmtProdAtual.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    produtoId = rs.getInt("produto_id");
                }
            }

            try (PreparedStatement stmtUpdateProd = conn.prepareStatement(sqlUpdateProduto)) {
                stmtUpdateProd.setString(1, novoProduto.trim());
                stmtUpdateProd.setDouble(2, novoPreco);
                stmtUpdateProd.setInt(3, produtoId);
                stmtUpdateProd.executeUpdate();
            }

            try (PreparedStatement stmtUpdatePed = conn.prepareStatement(sqlUpdatePedido)) {
                double novoTotal = novoPreco * novaQuantidade;
                stmtUpdatePed.setInt(1, novaQuantidade);
                stmtUpdatePed.setDouble(2, novoTotal);
                stmtUpdatePed.setInt(3, pedidoId);
                stmtUpdatePed.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean eliminarPedido(String nomeCliente, int indice) {
        if (indice < 0) {
            return false;
        }

        String sqlCliente = "SELECT id FROM clientes WHERE TRIM(LOWER(nome)) = TRIM(LOWER(?))";
        String sqlPedidoPorIndice = "SELECT p.id FROM pedidos p WHERE p.cliente_id = ? ORDER BY p.id LIMIT ?, 1";
        String sqlProdutoDoPedido = "SELECT produto_id FROM pedidos WHERE id = ?";
        String sqlDeletePedido = "DELETE FROM pedidos WHERE id = ?";
        String sqlDeleteProduto = "DELETE FROM produtos WHERE id = ?";

        Connection conn = null;

        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);

            int clienteId;
            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setString(1, nomeCliente == null ? "" : nomeCliente.trim());

                try (ResultSet rs = stmtCliente.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    clienteId = rs.getInt("id");
                }
            }

            int pedidoId;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedidoPorIndice)) {
                stmtPedido.setInt(1, clienteId);
                stmtPedido.setInt(2, indice);

                try (ResultSet rs = stmtPedido.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    pedidoId = rs.getInt("id");
                }
            }

            int produtoId;
            try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProdutoDoPedido)) {
                stmtProduto.setInt(1, pedidoId);

                try (ResultSet rs = stmtProduto.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    produtoId = rs.getInt("produto_id");
                }
            }

            try (PreparedStatement stmtDeletePedido = conn.prepareStatement(sqlDeletePedido)) {
                stmtDeletePedido.setInt(1, pedidoId);
                stmtDeletePedido.executeUpdate();
            }

            try (PreparedStatement stmtDeleteProduto = conn.prepareStatement(sqlDeleteProduto)) {
                stmtDeleteProduto.setInt(1, produtoId);
                stmtDeleteProduto.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}