package dao;

import model.MenuItem;
import model.Pedido;
import model.PedidoItem;
import model.PedidoStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class PedidoDAO extends BaseDAO<Pedido, Integer>{
    public PedidoDAO(){
        super(Pedido.class);
    }

    public void mudarStatus(int pedidoId, PedidoStatus status){
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Pedido pedido = session.get(Pedido.class, pedidoId);
            if (pedido != null) {
                pedido.setStatus(status);
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro mudando status", e);
        }
    }

    public Optional<Pedido> findByIdWithItems(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String jpql = "SELECT p FROM Pedido p LEFT JOIN FETCH p.items WHERE p.id = :id";
            Pedido pedido = session.createQuery(jpql, Pedido.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(pedido);
        }
    }
}
