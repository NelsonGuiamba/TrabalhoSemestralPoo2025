/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Administrator
 */
public class Menu {
    private String[] categoria;
    private String nomeDoPrato;
    private String descricao;
    private String ingridentes;
    private double preco;
    private int tempoPreparacao;

    
    
    public String[] getCategoria() {
        return categoria;
    }

    public void setCategoria(String[] categoria) {
        this.categoria = categoria;
    }

    public String getNomeDoPrato() {
        return nomeDoPrato;
    }

    public void setNomeDoPrato(String nomeDoPrato) {
        this.nomeDoPrato = nomeDoPrato;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIngridentes() {
        return ingridentes;
    }

    public void setIngridentes(String ingridentes) {
        this.ingridentes = ingridentes;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getTempoPreparacao() {
        return tempoPreparacao;
    }

    public void setTempoPreparacao(int tempoPreparacao) {
        this.tempoPreparacao = tempoPreparacao;
    }
    
    
}
