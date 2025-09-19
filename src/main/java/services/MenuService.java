/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.ArrayList;
import java.util.Comparator;
import model.*;

/**
 *
 * @author Administrator
 */
public class MenuService {

    ArrayList<Item> listaItem = new ArrayList<>();

    public ArrayList<Item> getListaItem() {
        return listaItem;
    }

    public MenuService() {
    }

    //Metodo para adcionar ao menu item
    public void adicionarItem(String nomeItem, String categoria, String[] ingridientes, double preco, int tempo, int quantidade) {
        for (int i = 0; i < listaItem.size(); i++) {
            if (nomeItem.equalsIgnoreCase(listaItem.get(i).getNomeItem())) {
                System.out.println("Produto ja existente no menu");
                return;
            }
        }

        if (preco < 0) {
            System.out.println("Preco invalido");
            return;
        }

        if (tempo < 0) {
            System.out.println("Tempo invalido");
            return;
        }

        Item novoItem = new Item(nomeItem, categoria, ingridientes, preco, tempo, quantidade);
        System.out.println("Adicionado com sucesso");
        listaItem.add(novoItem);

    }

    //metodo para remover item do menu
    public void removerItem(int id) {
        for (int i = 0; i < listaItem.size(); i++) {

            if (id == listaItem.get(i).getId()) {
                listaItem.get(i).setDisponivel(false);
                System.out.println("Produto removido com sucesso");
                break;
            } else {
                System.out.println("Produto inexistente");
            }
        }
    }

    //metodo para pesquisar o item apartir do nome
    public Item pesquisar(String nome) {
        for (int i = 0; i < listaItem.size(); i++) {
            if (nome.equalsIgnoreCase(listaItem.get(i).getNomeItem())) {
                return listaItem.get(i);
            }
        }
        return null;
    }

    //metodo para pesquisar o item apartir do ID
    public Item pesquisar(int id) {
        for (int i = 0; i < listaItem.size(); i++) {
            if (id == listaItem.get(id).getId()) {
                return listaItem.get(i);
            }
        }
        return null;
    }

    //metodo para ordenar items apartir do nome
    public ArrayList<Item> ordenarNome() {

        ArrayList copia = new ArrayList<>(listaItem);
        copia.sort(Comparator.comparing(Item::getNomeItem));
        return copia;
    }

    //metodo para ordenar items aparts do preco
    public ArrayList<Item> ordenarPreco() {

        ArrayList copia = new ArrayList<>(listaItem);
        copia.sort(Comparator.comparing(Item::getPreco));
        return copia;
    }

    //so mostrar items apartir da sua categoria
    public ArrayList<Item> filtraCategoria(String categoria) {
        ArrayList<Item> temp = new ArrayList<>();

        for (int i = 0; i < listaItem.size(); i++) {
            if (categoria.equalsIgnoreCase(listaItem.get(i).getCategoria())) {
                temp.add(listaItem.get(i));
            }
        }
        return temp;
    }

    //metodo para destacar certo item
    public void marcarComoDestaque(int id) {
        for (int i = 0; i < listaItem.size(); i++) {
            if (id == listaItem.get(i).getId()) {

                System.out.println("Em destaque: " + listaItem.get(id));
                break;
            } else {
                System.out.println("Produto nao existe");
            }
        }
    }

    //metodo para aplicar desconto
    public void aplicarDesconto(int id, double percentual) {
        for (int i = 0; i < listaItem.size(); i++) {
            if (id == listaItem.get(i).getId()) {
                listaItem.get(i).setPreco(listaItem.get(i).getPreco()-(listaItem.get(i).getPreco()*percentual));
                break;
            }else{
                System.out.println("Produto nao existente");
            }
        }
    }
}
