package dao;

import model.MenuItem;
import org.hibernate.Session;
import util.HibernateUtil;

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


    public MenuItem findByNameTempo(String nome, int tempoPreparacao) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
            FROM MenuItem m
            WHERE m.nomeDoPrato = :nome
              AND m.tempoPreparacao = :tempo
        """;

            return session.createQuery(hql, MenuItem.class)
                    .setParameter("nome", nome)
                    .setParameter("tempo", tempoPreparacao)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MenuItem> findAllActive() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
            FROM MenuItem m
            WHERE m.active = true
        """;

            return session.createQuery(hql, MenuItem.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
