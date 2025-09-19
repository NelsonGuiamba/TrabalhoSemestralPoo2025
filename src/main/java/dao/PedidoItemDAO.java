package dao;

import model.PedidoItem;

import java.util.List;

public class PedidoItemDAO extends BaseDAO<PedidoItem, Integer>{
    public PedidoItemDAO() {
        super(PedidoItem.class);
    }

    public boolean isValidList(List<PedidoItem> list){
        for(PedidoItem item : list){
            if(!this.existsById(item.getId()))
                return false;
        }
        return true;
    }
}
