package dao;

import model.MenuItem;

import java.util.List;

public class MenuItemDAO extends BaseDAO<MenuItem, Integer>{
    public MenuItemDAO() {
        super(MenuItem.class);
    }

    public boolean isValidList(List<MenuItem> list){
        for(MenuItem item : list){
            if(!this.existsById(item.getId()))
                return false;
        }
        return true;
    }
}
