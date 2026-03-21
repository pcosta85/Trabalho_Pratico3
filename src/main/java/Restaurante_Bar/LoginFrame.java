package Restaurante_Bar;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private Autenticacao auth;

    public LoginFrame() {
        auth = new Autenticacao();

        setTitle("Login - Restaurante/Bar CENTRAL");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Acesso ao Sistema", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.anchor = GridBagConstraints.WEST;

        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha = new JLabel("Senha:");

        txtUsuario = new JTextField(15);
        txtSenha = new JPasswordField(15);

        JButton btnEntrar = new JButton("Entrar");

        gc.gridx = 0;
        gc.gridy = 0;
        painel.add(lblUsuario, gc);

        gc.gridx = 1;
        painel.add(txtUsuario, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        painel.add(lblSenha, gc);

        gc.gridx = 1;
        painel.add(txtSenha, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        painel.add(btnEntrar, gc);

        add(painel, BorderLayout.CENTER);

        btnEntrar.addActionListener(e -> fazerLogin());
    }

    private void fazerLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha usuário e senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario u = auth.login(usuario, senha);

            if (u != null) {
                JOptionPane.showMessageDialog(this,
                        "Login efetuado com sucesso!\nNível: " + u.getNivelAcesso(),
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
                new RestauranteMain(u).setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuário ou senha inválidos.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de ligação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}