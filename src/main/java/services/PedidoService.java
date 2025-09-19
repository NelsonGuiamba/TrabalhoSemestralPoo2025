/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import java.util.ArrayList;
import java.util.Date;
import model.*;
/**
 *
 * @author Administrator
 */
public class PedidoService {
  
    ArrayList<Pedido> listaPedidos = new ArrayList<>();
    public void criarPedido(User client, String nomeDoPrato, User worker, Date dataCompra, Mesa mesa, MenuService menu){
        for(int i=0; i<menu.getListaItem().size(); i++){
            if(nomeDoPrato.equalsIgnoreCase(menu.getListaItem().get(i).getNomeItem())){
            Pedido novoPedido = new Pedido(client, menu.getListaItem().get(i), worker, dataCompra, mesa);
             listaPedidos.add(novoPedido);
            }
           
        }
        }
    }
    
