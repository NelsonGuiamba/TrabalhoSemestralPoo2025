package services;

import dao.MenuItemDAO;
import model.ItemCategory;
import model.MenuItem;

import java.awt.*;
import java.util.Optional;

public class AdminMenuService {
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    public  AdminMenuService() {}

    public boolean alterarStatus(int id){
        Optional<MenuItem> wrapper = menuItemDAO.findById(id);
        if(wrapper.isPresent()){
            MenuItem item = wrapper.get();
            item.setActive(!item.isActive());
            menuItemDAO.update(item);
            return item.isActive();
        }
        return false;
    }

    public boolean alterarObjecto(int id, String nome, double preco, String imagem, String categoria){
        ItemCategory itemCategory= null;
        if(categoria.toLowerCase().startsWith("e")){
            itemCategory = ItemCategory.ENTRADA;
        }else if(categoria.toLowerCase().startsWith("p")){
            itemCategory = ItemCategory.PRATO_PRINCIPAL;
        }else{
            itemCategory = ItemCategory.SOBREMESA;
        }
        if(id == -1){
            MenuItem menuItem = new MenuItem();
            menuItem.setActive(true);
            menuItem.setNomeDoPrato(nome);
            menuItem.setPreco(preco);
            menuItem.setImagem(imagem);
            menuItem.setCategoria(itemCategory);
            menuItemDAO.save(menuItem);
            System.out.println(menuItem.toString());
            return true;
        }
        Optional<MenuItem> wrapper = menuItemDAO.findById(id);
        if(wrapper.isPresent()){
            MenuItem item = wrapper.get();
            item.setNomeDoPrato(nome);
            item.setPreco(preco);
            item.setCategoria(itemCategory);
            if(!imagem.equals("null"))
                item.setImagem(imagem);
            menuItemDAO.update(item);
            return true;
        }
        return false;
    }
}
