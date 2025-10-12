package dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseDAO<T, ID extends Serializable> implements BaseDAOInterface<T, ID> {
    private final Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public ID save(T entity) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            ID id = (ID) session.save(entity);
            tx.commit();
            return id;
        } catch (HibernateException e) {
            if (tx != null && tx.getStatus().canRollback()) tx.rollback();
            System.out.println("Erro ao salvar: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (HibernateException e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<T> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM " + getEntityName();
            Query<T> query = session.createQuery(hql, entityClass);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void update(T entity) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.getStatus().canRollback()) tx.rollback();
            e.printStackTrace();
            System.out.println("Erro ao atualizar: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void deleteById(ID id) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.getStatus().canRollback()) tx.rollback();
            e.printStackTrace();
            System.out.println("Erro ao deletar: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void delete(T entity) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.getStatus().canRollback()) tx.rollback();
            e.printStackTrace();
            System.out.println("Erro ao deletar: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    private String getEntityName() {
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        if (entityAnnotation != null && !entityAnnotation.name().isEmpty()) {
            return entityAnnotation.name();  // Returns "Usuarios"
        }
        return entityClass.getSimpleName();  // Returns "User"
    }
}
