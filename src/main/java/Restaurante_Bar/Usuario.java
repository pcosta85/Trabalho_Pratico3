package Restaurante_Bar;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String nivelAcesso;

    public Usuario(int id, String username, String password, String nivelAcesso) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nivelAcesso = nivelAcesso;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }
}