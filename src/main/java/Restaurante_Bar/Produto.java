package Restaurante_Bar;

public class Produto {
    private int id;
    private String nome;
    private double preco;

    public Produto(String nome, double preco) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto obrigatório.");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço inválido.");
        }
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(int id, String nome, double preco) {
        this(nome, preco);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setId(int id) {
        this.id = id;
    }
}