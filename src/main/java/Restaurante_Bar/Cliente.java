package Restaurante_Bar;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nome;
    private List<Pedido> pedidos;

    public Cliente(String nome) {
        this.nome = nome;
        this.pedidos = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public List<Pedido> getPedidos() { return pedidos; }

    public void adicionarPedido(Pedido p) {
        pedidos.add(p);
    }

    public void setNome(String novoNome) {
    this.nome = novoNome;
    }
}