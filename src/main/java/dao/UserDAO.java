package dao;

import model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class UserDAO extends BaseDAO<User, Integer> {

    public UserDAO() {
        super(User.class);
    }

    // Authenticate user by name and password
    public User authenticateUser(String name, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "from Usuarios u where u.name = :name and u.password = :password", User.class);
            query.setParameter("name", name);
            query.setParameter("password", password);
            return query.uniqueResult(); // Hibernate 6 replacement for uniqueResult()
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserByName(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Usuarios u WHERE u.name = :nome";
            return session.createQuery(hql, User.class)
                    .setParameter("nome", nome)
                    .uniqueResult(); // retorna null se não encontrar
        } catch (Exception ex) {
            System.out.println("Erro ao buscar usuário: " + ex.getMessage());
            return null;
        }
    }

}
