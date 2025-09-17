package dao;

import model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class UserDAO {

    public UserDAO() {}

    // Add a new user
    public void addUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Get all users
    public List<User> getUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Usuarios ", User.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Remove a user by ID
    public void removeUser(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User userToRemove = session.get(User.class, id);
            if (userToRemove == null) {
                System.out.println("User to remove does not exist");
            } else {
                session.remove(userToRemove);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
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
}
