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
    private int id;
    private String nomeItem;
    private String categoria;
    private String[] ingridentes;
    private double preco;
    private int tempo;
    private boolean disponivel;
    int quantidade;

    public Item(String nomeItem, String categoria, String[] ingridentes, double preco, int tempo, int quantidade) {
        this.id=id;
        this.nomeItem = nomeItem;
        this.categoria = categoria;
        this.ingridentes = ingridentes;
        this.preco = preco;
        this.tempo = tempo;
        this.quantidade= quantidade;
        if(this.quantidade>0){
        this.disponivel=true;
        }else{
        disponivel=false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Item{" + "nomeItem=" + nomeItem + ", categoria=" + categoria + ", ingridentes=" + ingridentes + ", preco=" + preco + ", tempo=" + tempo + ", disponivel=" + disponivel + ", quantidade=" + quantidade + '}';
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
