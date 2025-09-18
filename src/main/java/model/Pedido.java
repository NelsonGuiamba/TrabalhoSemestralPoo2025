/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class Pedido {
    private User client;
    private Item item;
    private User worker;
    private Date dataCompra;
    private Mesa mesa;
    private boolean status;

    public Pedido(User client, Item item, User worker, Date dataCompra, Mesa mesa) {
        this.client = client;
        this.item = item;
        this.worker = worker;
        this.dataCompra = dataCompra;
        this.mesa = mesa;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item menu) {
        this.item = menu;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
}
