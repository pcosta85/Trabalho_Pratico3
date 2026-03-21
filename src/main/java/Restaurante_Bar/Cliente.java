package Restaurante_Bar;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nome;
    private List<Pedido> pedidos;

    public Cliente(String nome) {
        this.nome = nome;
        this.pedidos = new ArrayList<Pedido>();
    }

    public Cliente(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.pedidos = new ArrayList<Pedido>();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void adicionarPedido(Pedido p) {
        pedidos.add(p);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}