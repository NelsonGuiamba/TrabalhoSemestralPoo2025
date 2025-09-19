/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import services.MenuService;

/**
 *
 * @author Administrator
 */
public class Main {
    public static void main(String[] args){
        MenuService menu = new MenuService();
        
        String[] ingridientesFrango = new String[4];
        
        menu.adicionarItem("Frango assado", "Grill", ingridientesFrango, 250, 15,10);
        menu.adicionarItem("Porco Assado", "Grill", ingridientesFrango, 330, 10,10);
        menu.adicionarItem("Porco Assado", "Grill", ingridientesFrango, 330, 10,10);
        menu.getListaItem().get(1).setId(1);
        
        menu.removerItem(0);
        
        for(int i=0; i<menu.getListaItem().size(); i++){
            System.out.println(menu.getListaItem().get(i));
        }
        
    }
}
