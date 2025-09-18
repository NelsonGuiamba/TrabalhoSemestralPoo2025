/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Administrator
 */
public class Item {
    private String nomeItem;
    private String categoria;
    private String[] ingridentes;
    private double preco;
    private int tempo;

    public Item(String nomeItem, String categoria, String[] ingridentes, double preco, int tempo) {
        this.nomeItem = nomeItem;
        this.categoria = categoria;
        this.ingridentes = ingridentes;
        this.preco = preco;
        this.tempo = tempo;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String[] getIngridentes() {
        return ingridentes;
    }

    public void setIngridentes(String[] ingridentes) {
        this.ingridentes = ingridentes;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
    
    
    
}
