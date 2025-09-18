/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import java.util.ArrayList;
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

   
    
    public void adicionarItem(String nomeItem, String categoria, String[] ingridientes, double preco, int tempo){
        Item novoItem = new Item(nomeItem, categoria, ingridientes, preco, tempo);
        
        listaItem.add(novoItem);
        
        
    }
}
