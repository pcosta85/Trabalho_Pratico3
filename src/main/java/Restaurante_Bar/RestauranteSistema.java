package Restaurante_Bar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestauranteSistema {

    private final List<Cliente> clientes = new ArrayList<>();

    //  define PRIMEIRO a pasta existente
    private static final Path BD_DIR = Paths.get("BD");

    //  depois define os ficheiros
    private static final Path CLIENTES_FILE = BD_DIR.resolve("clientes.csv");
    private static final Path PEDIDOS_FILE  = BD_DIR.resolve("pedidos.csv");

    

    // separador mais seguro que vírgula
    private static final String SEP = ";";

    public RestauranteSistema() {
        garantirDiretorioBD();
        carregarClientes();
        carregarPedidos();
    }

    private void garantirDiretorioBD() {
    if (!Files.exists(BD_DIR) || !Files.isDirectory(BD_DIR)) {
        throw new RuntimeException(
            "Pasta BD não encontrada em: " + BD_DIR.toAbsolutePath() +
            "\nCria a pasta 'BD' na raiz do projeto (onde está o pom.xml)."
        );
    }
}

    // =================== CLIENTES ===================

    public boolean adicionarCliente(String nome) {
        if (nome == null) return false;

        String n = nome.trim();
        if (n.isEmpty()) return false;

        if (procurarCliente(n) != null) return false; // evita duplicados

        Cliente c = new Cliente(n);
        clientes.add(c);
        salvarCliente(c);
        return true;
    }

    public Cliente procurarCliente(String nome) {
        if (nome == null) return null;
        String n = nome.trim();
        for (Cliente c : clientes) {
            if (c.getNome().equalsIgnoreCase(n)) return c;
        }
        return null;
    }

    public boolean alterarNomeCliente(String nomeAtual, String novoNome) {
        Cliente c = procurarCliente(nomeAtual);
        if (c == null) return false;

        if (novoNome == null || novoNome.trim().isEmpty()) return false;

        // evitar duplicados
        if (procurarCliente(novoNome) != null) return false;

        c.setNome(novoNome.trim());

        reescreverClientesCSV();
        reescreverPedidosCSV();
        return true;
    }

    public boolean eliminarCliente(String nome) {
        Cliente c = procurarCliente(nome);
        if (c == null) return false;

        clientes.remove(c);

        reescreverClientesCSV();
        reescreverPedidosCSV();
        return true;
    }

    // =================== PEDIDOS ===================

    public boolean registrarPedido(String nomeCliente, Produto prod, int quant) {
        if (prod == null || quant <= 0) return false;

        Cliente c = procurarCliente(nomeCliente);
        if (c == null) return false;

        Pedido p = new Pedido(prod, quant);
        c.adicionarPedido(p);
        salvarPedido(c, p);
        return true;
    }

    public List<Pedido> obterPedidosCliente(Cliente c) {
        if (c == null) return Collections.emptyList();
        return c.getPedidos();
    }

    public boolean alterarPedido(String nomeCliente, int indicePedido,
                                 String novoProduto, double novoPreco, int novaQtd) {

        Cliente c = procurarCliente(nomeCliente);
        if (c == null) return false;

        List<Pedido> pedidos = c.getPedidos();
        if (indicePedido < 0 || indicePedido >= pedidos.size()) return false;

        if (novoProduto == null || novoProduto.trim().isEmpty()) return false;
        if (novoPreco < 0) return false;
        if (novaQtd <= 0) return false;

        Pedido p = pedidos.get(indicePedido);
        p.setProduto(new Produto(novoProduto.trim(), novoPreco));
        p.setQuantidade(novaQtd);

        reescreverPedidosCSV();
        return true;
    }

    public boolean eliminarPedido(String nomeCliente, int indicePedido) {
        Cliente c = procurarCliente(nomeCliente);
        if (c == null) return false;

        List<Pedido> pedidos = c.getPedidos();
        if (indicePedido < 0 || indicePedido >= pedidos.size()) return false;

        pedidos.remove(indicePedido);

        reescreverPedidosCSV();
        return true;
    }

    // =================== PERSISTÊNCIA ===================

    private void salvarCliente(Cliente c) {
        try (BufferedWriter bw = Files.newBufferedWriter(
                CLIENTES_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(c.getNome());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarPedido(Cliente c, Pedido p) {
        try (BufferedWriter bw = Files.newBufferedWriter(
                PEDIDOS_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            bw.write(c.getNome() + SEP
                    + p.getProduto().getNome() + SEP
                    + p.getProduto().getPreco() + SEP
                    + p.getQuantidade());
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarClientes() {
        if (!Files.exists(CLIENTES_FILE)) return;

        try (BufferedReader br = Files.newBufferedReader(CLIENTES_FILE, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String nome = linha.trim();
                if (!nome.isEmpty()) {
                    // evita duplicar na memória se o ficheiro tiver repetidos
                    if (procurarCliente(nome) == null) {
                        clientes.add(new Cliente(nome));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarPedidos() {
        if (!Files.exists(PEDIDOS_FILE)) return;

        try (BufferedReader br = Files.newBufferedReader(PEDIDOS_FILE, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(SEP, -1);
                if (partes.length == 4) {
                    String nomeCliente = partes[0].trim();
                    String nomeProduto = partes[1].trim();

                    double preco;
                    int qtd;

                    try {
                        preco = Double.parseDouble(partes[2].trim());
                        qtd = Integer.parseInt(partes[3].trim());
                    } catch (NumberFormatException ex) {
                        continue; // ignora linha inválida
                    }

                    Cliente c = procurarCliente(nomeCliente);
                    if (c != null) {
                        c.adicionarPedido(new Pedido(new Produto(nomeProduto, preco), qtd));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =================== REESCREVER ===================

    private void reescreverClientesCSV() {
        try (BufferedWriter bw = Files.newBufferedWriter(
                CLIENTES_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Cliente c : clientes) {
                bw.write(c.getNome());
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reescreverPedidosCSV() {
        try (BufferedWriter bw = Files.newBufferedWriter(
                PEDIDOS_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Cliente c : clientes) {
                for (Pedido p : c.getPedidos()) {
                    bw.write(c.getNome() + SEP
                            + p.getProduto().getNome() + SEP
                            + p.getProduto().getPreco() + SEP
                            + p.getQuantidade());
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
