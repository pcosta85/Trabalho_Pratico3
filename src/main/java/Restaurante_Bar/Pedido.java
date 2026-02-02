package Restaurante_Bar;
public class Pedido {
    private Produto produto;
    private int quantidade;

    public Pedido(Produto produto, int quantidade) {
        if (produto == null) throw new IllegalArgumentException("Produto obrigatório.");
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida.");
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }

    public double getTotal() {
        return quantidade * produto.getPreco();
    }

    public void setProduto(Produto produto) {
    this.produto = produto;
    }


    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}