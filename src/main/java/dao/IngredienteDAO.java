package dao;
import model.Ingrediente;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;

public class IngredienteDAO extends BaseDAO<Ingrediente, Integer> {
    public IngredienteDAO() {
        super(Ingrediente.class);
    }

    public Ingrediente getOrCreate(String nome) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Ingrediente existente = session.createQuery(
                            "FROM Ingrediente i WHERE i.ingrediente = :nome", Ingrediente.class)
                    .setParameter("nome", nome)
                    .uniqueResult();

            if (existente != null) {
                return existente;
            }

            Ingrediente novo = new Ingrediente();
            novo.setIngrediente(nome);
            Integer id = (Integer) session.save(novo);

            tx.commit();
            return novo;

        } catch (HibernateException e) {
            if (tx != null && tx.getStatus().canRollback()) tx.rollback();
            System.out.println("Erro no getOrCreate: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }


}
