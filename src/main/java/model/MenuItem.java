/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.persistence.*;
import java.util.List;

/**
 *
 * @author Administrator
 */

// Cada item do menu do restaurante
@Entity(name = "MenuItem")
//@Table(name = "MenuItem", uniqueConstraints = @UniqueConstraint(columnNames = "nomeDoPrato"))
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private ItemCategory categoria;
    private String nomeDoPrato;
    private String descricao;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Ingrediente> ingredientes;
    private double preco;
    private int tempoPreparacao; // em segundos
    private String imagem;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemCategory getCategoria() {
        return categoria;
    }

    public void setCategoria(ItemCategory categoria) {
        this.categoria = categoria;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
