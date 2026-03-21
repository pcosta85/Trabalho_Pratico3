package Restaurante_Bar;

public class Pedido {
    private int id;
    private Produto produto;
    private int quantidade;

    public Pedido(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto obrigatório.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Pedido(int id, Produto produto, int quantidade) {
        this(produto, quantidade);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getTotal() {
        return quantidade * produto.getPreco();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}