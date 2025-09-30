/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
@Entity(name = "Mesa")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int capacidade;

    @OneToMany(mappedBy = "mesa")
    private List<Pedido> pedidos = new ArrayList<>();

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    public Mesa(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}
