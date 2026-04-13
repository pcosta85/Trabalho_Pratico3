package Restaurante_Bar;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class RestauranteMain extends JFrame {

    private final RestauranteSistema sistema = new RestauranteSistema();
    private final Usuario usuarioLogado;

    private final JPanel painelPrincipal = new JPanel(new CardLayout());
    private final JLabel titulo = new JLabel("", SwingConstants.CENTER);

    private JTextField txtNomeConsulta;
    private JTable tabelaPedidos;
    private DefaultTableModel modeloPedidos;

    public RestauranteMain(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Restaurante/Bar CENTRAL");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel barraLateral = new JPanel();
        barraLateral.setLayout(new BoxLayout(barraLateral, BoxLayout.Y_AXIS));
        barraLateral.setBackground(new Color(50, 50, 50));
        barraLateral.setPreferredSize(new Dimension(220, getHeight()));

        JButton btnInicio = criarBotaoMenu("Início");
        JButton btnInserirCliente = criarBotaoMenu("Inserir Cliente");
        JButton btnRegistarPedido = criarBotaoMenu("Registar Pedido");
        JButton btnConsultar = criarBotaoMenu("Consultar Cliente");
        JButton btnGerarFatura = criarBotaoMenu("Gerar Fatura");
        JButton btnAlterarCliente = criarBotaoMenu("Alterar Cliente");
        JButton btnEliminarCliente = criarBotaoMenu("Eliminar Cliente");
        JButton btnAlterarPedido = criarBotaoMenu("Alterar Pedido");
        JButton btnEliminarPedido = criarBotaoMenu("Eliminar Pedido");
        JButton btnGerirUsuarios = criarBotaoMenu("Gerir Utilizadores");
        JButton btnLogout = criarBotaoMenu("Terminar Sessão");

        adicionarBotao(barraLateral, btnInicio);
        adicionarBotao(barraLateral, btnInserirCliente);
        adicionarBotao(barraLateral, btnRegistarPedido);
        adicionarBotao(barraLateral, btnConsultar);
        adicionarBotao(barraLateral, btnGerarFatura);

        if (podeAlterarOuEliminarCliente()) {
            adicionarBotao(barraLateral, btnAlterarCliente);
            adicionarBotao(barraLateral, btnEliminarCliente);
        }

        if (podeAlterarOuEliminarPedido()) {
            adicionarBotao(barraLateral, btnAlterarPedido);
            adicionarBotao(barraLateral, btnEliminarPedido);
        }

        if (podeGerirUtilizadores()) {
            adicionarBotao(barraLateral, btnGerirUsuarios);
        }

        barraLateral.add(Box.createVerticalGlue());
        adicionarBotao(barraLateral, btnLogout);

        painelPrincipal.add(criarPaginaInicio(), "INICIO");
        painelPrincipal.add(criarPaginaInserirCliente(), "INSERIR_CLIENTE");
        painelPrincipal.add(criarPaginaRegistarPedido(), "REGISTAR_PEDIDO");
        painelPrincipal.add(criarPaginaConsultarCliente(), "CONSULTAR");
        painelPrincipal.add(criarPaginaGerarFatura(), "GERAR_FATURA");
        painelPrincipal.add(criarPaginaAlterarCliente(), "ALTERAR_CLIENTE");
        painelPrincipal.add(criarPaginaEliminarCliente(), "ELIMINAR_CLIENTE");
        painelPrincipal.add(criarPaginaAlterarPedido(), "ALTERAR_PEDIDO");
        painelPrincipal.add(criarPaginaEliminarPedido(), "ELIMINAR_PEDIDO");
        painelPrincipal.add(criarPaginaGerirUsuarios(), "GERIR_USUARIOS");

        btnInicio.addActionListener(e -> mostrar("INICIO"));
        btnInserirCliente.addActionListener(e -> mostrar("INSERIR_CLIENTE"));
        btnRegistarPedido.addActionListener(e -> mostrar("REGISTAR_PEDIDO"));
        btnConsultar.addActionListener(e -> mostrar("CONSULTAR"));
        btnGerarFatura.addActionListener(e -> mostrar("GERAR_FATURA"));
        btnAlterarCliente.addActionListener(e -> mostrar("ALTERAR_CLIENTE"));
        btnEliminarCliente.addActionListener(e -> mostrar("ELIMINAR_CLIENTE"));
        btnAlterarPedido.addActionListener(e -> mostrar("ALTERAR_PEDIDO"));
        btnEliminarPedido.addActionListener(e -> mostrar("ELIMINAR_PEDIDO"));
        btnGerirUsuarios.addActionListener(e -> mostrar("GERIR_USUARIOS"));

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(barraLateral, BorderLayout.WEST);
        getContentPane().add(painelPrincipal, BorderLayout.CENTER);

        mostrar("INICIO");
    }

    private boolean isAdmin() {
        return usuarioLogado != null && "ADMIN".equalsIgnoreCase(usuarioLogado.getNivelAcesso());
    }

    private boolean isGestor() {
        return usuarioLogado != null && "GESTOR".equalsIgnoreCase(usuarioLogado.getNivelAcesso());
    }

    private boolean isAtendente() {
        return usuarioLogado != null && "ATENDENTE".equalsIgnoreCase(usuarioLogado.getNivelAcesso());
    }

    private boolean podeGerirUtilizadores() {
        return isAdmin();
    }

    private boolean podeAlterarOuEliminarCliente() {
        return isAdmin() || isGestor();
    }

    private boolean podeAlterarOuEliminarPedido() {
        return isAdmin() || isGestor();
    }

    private void adicionarBotao(JPanel painel, JButton botao) {
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(botao);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton b = new JButton(texto);
        b.setMaximumSize(new Dimension(999, 35));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    private void mostrar(String card) {
        CardLayout cl = (CardLayout) painelPrincipal.getLayout();
        cl.show(painelPrincipal, card);
    }

    private JPanel criarPaginaInicio() {
        JPanel p = new JPanel(new BorderLayout());
        String nome = usuarioLogado != null ? usuarioLogado.getUsername() : "";
        String nivel = usuarioLogado != null ? usuarioLogado.getNivelAcesso() : "";
        titulo.setText("Bem-vindo ao Restaurante/Bar CENTRAL - " + nome + " (" + nivel + ")");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        p.add(titulo, BorderLayout.CENTER);
        return p;
    }

    private JPanel criarPaginaInserirCliente() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JLabel lbl = new JLabel("Nome do Cliente:");
        JTextField txtNome = new JTextField(20);
        JButton btn = new JButton("Inserir");

        gc.gridx = 0;
        gc.gridy = 0;
        p.add(lbl, gc);

        gc.gridx = 1;
        p.add(txtNome, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        p.add(btn, gc);

        btn.addActionListener(e -> {
            String nome = txtNome.getText().trim();

            if (nome.isEmpty()) {
                aviso("Preencha o nome.");
                return;
            }

            boolean ok = sistema.adicionarCliente(nome);
            if (ok) {
                info("Cliente adicionado!");
                txtNome.setText("");
            } else {
                aviso("Não foi possível adicionar o cliente.");
            }
        });

        return p;
    }

    private JPanel criarPaginaRegistarPedido() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JTextField txtNome = new JTextField(18);
        JTextField txtProduto = new JTextField(18);
        JTextField txtPreco = new JTextField(10);
        JTextField txtQtd = new JTextField(10);
        JButton btn = new JButton("Registar");

        int y = 0;
        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Cliente:"), gc);
        gc.gridx = 1; p.add(txtNome, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Produto:"), gc);
        gc.gridx = 1; p.add(txtProduto, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Preço:"), gc);
        gc.gridx = 1; p.add(txtPreco, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Quantidade:"), gc);
        gc.gridx = 1; p.add(txtQtd, gc); y++;

        gc.gridx = 1; gc.gridy = y; p.add(btn, gc);

        btn.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String prod = txtProduto.getText().trim();

            if (nome.isEmpty() || prod.isEmpty() ||
                txtPreco.getText().trim().isEmpty() ||
                txtQtd.getText().trim().isEmpty()) {
                aviso("Preencha todos os campos.");
                return;
            }

            try {
                double preco = Double.parseDouble(txtPreco.getText().trim());
                int qtd = Integer.parseInt(txtQtd.getText().trim());

                boolean ok = sistema.registrarPedido(nome, new Produto(prod, preco), qtd);

                if (ok) {
                    info("Pedido registado!");
                    txtProduto.setText("");
                    txtPreco.setText("");
                    txtQtd.setText("");
                } else {
                    aviso("Erro: cliente não existe.");
                }

            } catch (NumberFormatException ex) {
                erro("Preço ou quantidade inválidos.");
            }
        });

        return p;
    }

    private JPanel criarPaginaConsultarCliente() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Nome do Cliente:"));
        txtNomeConsulta = new JTextField(18);
        JButton btn = new JButton("Consultar");
        topo.add(txtNomeConsulta);
        topo.add(btn);

        String[] cols = {"Índice", "ID Pedido", "Produto", "Quantidade", "Preço", "Total"};
        modeloPedidos = new DefaultTableModel(cols, 0);
        tabelaPedidos = new JTable(modeloPedidos);

        p.add(topo, BorderLayout.NORTH);
        p.add(new JScrollPane(tabelaPedidos), BorderLayout.CENTER);

        btn.addActionListener(e -> atualizarTabelaPedidos());

        return p;
    }

    private void atualizarTabelaPedidos() {
        String nome = txtNomeConsulta.getText().trim();
        modeloPedidos.setRowCount(0);

        if (nome.isEmpty()) {
            aviso("Digite o nome do cliente.");
            return;
        }

        Cliente c = sistema.procurarCliente(nome);
        if (c == null) {
            aviso("Cliente não existe.");
            return;
        }

        List<Pedido> pedidos = sistema.obterPedidosCliente(c);

        int i = 0;
        for (Pedido ped : pedidos) {
            modeloPedidos.addRow(new Object[]{
                i,
                ped.getId(),
                ped.getProduto().getNome(),
                ped.getQuantidade(),
                ped.getProduto().getPreco(),
                ped.getTotal()
            });
            i++;
        }
    }

    private JPanel criarPaginaGerarFatura() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JTextField txtNome = new JTextField(20);
        JButton btn = new JButton("Gerar Fatura");

        gc.gridx = 0;
        gc.gridy = 0;
        p.add(new JLabel("Nome do Cliente:"), gc);

        gc.gridx = 1;
        p.add(txtNome, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        p.add(btn, gc);

        btn.addActionListener(e -> {
            String nome = txtNome.getText().trim();

            if (nome.isEmpty()) {
                aviso("Digite o nome.");
                return;
            }

            Cliente c = sistema.procurarCliente(nome);
            if (c == null) {
                aviso("Cliente não existe.");
                return;
            }

            List<Pedido> pedidos = sistema.obterPedidosCliente(c);
            FaturaRestaurante.gerar(c, pedidos);
            info("Fatura gerada!");
        });

        return p;
    }

    private JPanel criarPaginaAlterarCliente() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JTextField atual = new JTextField(18);
        JTextField novo = new JTextField(18);
        JButton btn = new JButton("Alterar");

        int y = 0;
        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Nome atual:"), gc);
        gc.gridx = 1; p.add(atual, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Novo nome:"), gc);
        gc.gridx = 1; p.add(novo, gc); y++;

        gc.gridx = 1; gc.gridy = y; p.add(btn, gc);

        btn.addActionListener(e -> {
            if (!podeAlterarOuEliminarCliente()) {
                aviso("Sem permissão para esta operação.");
                return;
            }

            boolean ok = sistema.alterarNomeCliente(atual.getText().trim(), novo.getText().trim());
            if (ok) {
                info("Cliente alterado com sucesso!");
            } else {
                aviso("Erro: cliente não existe ou nome duplicado.");
            }
        });

        return p;
    }

    private JPanel criarPaginaEliminarCliente() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JTextField nome = new JTextField(20);
        JButton btn = new JButton("Eliminar");

        gc.gridx = 0; gc.gridy = 0; p.add(new JLabel("Nome do cliente:"), gc);
        gc.gridx = 1; p.add(nome, gc);

        gc.gridx = 1; gc.gridy = 1; p.add(btn, gc);

        btn.addActionListener(e -> {
            if (!podeAlterarOuEliminarCliente()) {
                aviso("Sem permissão para esta operação.");
                return;
            }

            String n = nome.getText().trim();
            if (n.isEmpty()) {
                aviso("Digite o nome.");
                return;
            }

            int conf = JOptionPane.showConfirmDialog(this,
                    "Eliminar o cliente \"" + n + "\"?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (conf != JOptionPane.YES_OPTION) {
                return;
            }

            boolean ok = sistema.eliminarCliente(n);
            if (ok) {
                info("Cliente eliminado!");
            } else {
                aviso("Cliente não encontrado.");
            }
        });

        return p;
    }

    private JPanel criarPaginaAlterarPedido() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JTextField nome = new JTextField(18);
        JTextField idx = new JTextField(6);
        JTextField prod = new JTextField(18);
        JTextField preco = new JTextField(10);
        JTextField qtd = new JTextField(10);
        JButton btn = new JButton("Alterar Pedido");

        int y = 0;
        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Cliente:"), gc);
        gc.gridx = 1; p.add(nome, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Índice do pedido:"), gc);
        gc.gridx = 1; p.add(idx, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Novo produto:"), gc);
        gc.gridx = 1; p.add(prod, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Novo preço:"), gc);
        gc.gridx = 1; p.add(preco, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Nova quantidade:"), gc);
        gc.gridx = 1; p.add(qtd, gc); y++;

        gc.gridx = 1; gc.gridy = y; p.add(btn, gc);

        btn.addActionListener(e -> {
            if (!podeAlterarOuEliminarPedido()) {
                aviso("Sem permissão para esta operação.");
                return;
            }

            try {
                String n = nome.getText().trim();
                int i = Integer.parseInt(idx.getText().trim());
                String pr = prod.getText().trim();
                double pc = Double.parseDouble(preco.getText().trim());
                int q = Integer.parseInt(qtd.getText().trim());

                boolean ok = sistema.alterarPedido(n, i, pr, pc, q);
                if (ok) {
                    info("Pedido alterado!");
                } else {
                    aviso("Erro ao alterar pedido.");
                }

            } catch (NumberFormatException ex) {
                erro("Índice, preço ou quantidade inválidos.");
            }
        });

        return p;
    }

    private JPanel criarPaginaEliminarPedido() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JTextField nome = new JTextField(18);
        JTextField idx = new JTextField(6);
        JButton btn = new JButton("Eliminar Pedido");

        gc.gridx = 0; gc.gridy = 0; p.add(new JLabel("Cliente:"), gc);
        gc.gridx = 1; p.add(nome, gc);

        gc.gridx = 0; gc.gridy = 1; p.add(new JLabel("Índice do pedido:"), gc);
        gc.gridx = 1; p.add(idx, gc);

        gc.gridx = 1; gc.gridy = 2; p.add(btn, gc);

        btn.addActionListener(e -> {
            if (!podeAlterarOuEliminarPedido()) {
                aviso("Sem permissão para esta operação.");
                return;
            }

            try {
                String n = nome.getText().trim();
                int i = Integer.parseInt(idx.getText().trim());

                int conf = JOptionPane.showConfirmDialog(this,
                        "Eliminar o pedido índice " + i + " do cliente \"" + n + "\"?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION);

                if (conf != JOptionPane.YES_OPTION) {
                    return;
                }

                boolean ok = sistema.eliminarPedido(n, i);
                if (ok) {
                    info("Pedido eliminado!");
                } else {
                    aviso("Erro ao eliminar pedido.");
                }

            } catch (NumberFormatException ex) {
                erro("Índice inválido.");
            }
        });

        return p;
    }

    private JPanel criarPaginaGerirUsuarios() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = baseGC();

        JTextField txtId = new JTextField(6);
        JTextField txtUsername = new JTextField(18);
        JTextField txtPassword = new JTextField(18);
        JTextField txtNivel = new JTextField(18);

        JButton btnCriar = new JButton("Criar Utilizador");
        JButton btnAtualizar = new JButton("Atualizar Utilizador");
        JButton btnEliminar = new JButton("Eliminar Utilizador");

        int y = 0;
        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("ID:"), gc);
        gc.gridx = 1; p.add(txtId, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Username:"), gc);
        gc.gridx = 1; p.add(txtUsername, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Password:"), gc);
        gc.gridx = 1; p.add(txtPassword, gc); y++;

        gc.gridx = 0; gc.gridy = y; p.add(new JLabel("Nível (ADMIN/GESTOR/ATENDENTE):"), gc);
        gc.gridx = 1; p.add(txtNivel, gc); y++;

        gc.gridx = 1; gc.gridy = y; p.add(btnCriar, gc); y++;
        gc.gridx = 1; gc.gridy = y; p.add(btnAtualizar, gc); y++;
        gc.gridx = 1; gc.gridy = y; p.add(btnEliminar, gc);

        btnCriar.addActionListener(e -> {
            if (!podeGerirUtilizadores()) {
                aviso("Sem permissão para esta operação.");
                return;
            }

            boolean ok = sistema.criarUsuario(
                    txtUsername.getText().trim(),
                    txtPassword.getText().trim(),
                    txtNivel.getText().trim()
            );

            if (ok) {
                info("Utilizador criado com sucesso!");
            } else {
                aviso("Não foi possível criar o utilizador.");
            }
        });

        btnAtualizar.addActionListener(e -> {
            if (!podeGerirUtilizadores()) {
                aviso("Sem permissão para esta operação.");
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText().trim());

                boolean ok = sistema.atualizarUsuario(
                        id,
                        txtUsername.getText().trim(),
                        txtPassword.getText().trim(),
                        txtNivel.getText().trim()
                );

                if (ok) {
                    info("Utilizador atualizado com sucesso!");
                } else {
                    aviso("Não foi possível atualizar o utilizador.");
                }

            } catch (NumberFormatException ex) {
                erro("ID inválido.");
            }
        });

        btnEliminar.addActionListener(e -> {
            if (!podeGerirUtilizadores()) {
                aviso("Sem permissão para esta operação.");
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText().trim());

                boolean ok = sistema.eliminarUsuario(id);

                if (ok) {
                    info("Utilizador eliminado com sucesso!");
                } else {
                    aviso("Não foi possível eliminar o utilizador.");
                }

            } catch (NumberFormatException ex) {
                erro("ID inválido.");
            }
        });

        return p;
    }

    private GridBagConstraints baseGC() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.anchor = GridBagConstraints.WEST;
        return gc;
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}