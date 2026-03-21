package Restaurante_Bar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Autenticacao {

    public Usuario login(String username, String password) {
        String sql = "SELECT id, username, password, nivel FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());
            stmt.setString(2, password.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nivel")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar utilizador: " + e.getMessage(), e);
        }

        return null;
    }
}