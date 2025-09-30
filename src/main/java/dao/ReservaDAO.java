package dao;

import model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservaDAO extends BaseDAO<Reserva, Integer> {
    public ReservaDAO() {
        super(Reserva.class);
    }


    public List<Reserva> getReservasDeHoje(){
        String hql = """
                    SELECT r 
                    FROM Reserva r
                    WHERE r.dataInicio >= :inicio AND r.dataFim <= :fim                    
                """;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDateTime dataInicio = LocalDate.now().atStartOfDay();
            List<Reserva> reservas = session.createQuery(hql, Reserva.class)
                    .setParameter("inicio", dataInicio)
                    .setParameter("fim", dataInicio.plusDays(1))
                    .getResultList();
            return reservas;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return List.of();
        }
    }

    public void mudarStatus(int reservaId, ReservaStatus status){
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Reserva reserva = session.get(Reserva.class, reservaId);
            if (reserva != null) {
                reserva.setStatus(status);
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro mudando status", e);
        }
    }
}
