package services;

import dao.MesaDAO;
import model.Mesa;
import org.hibernate.Session;
import util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MesaService {
    private final MesaDAO mesaDAO = new MesaDAO();

    public MesaService() {
    }

    public List<Mesa> getMesasDisponiveis(LocalDateTime dataInicial) {
        LocalDateTime dataFinal = dataInicial.plusMinutes(120);
        return getMesasDisponiveis(dataInicial, dataFinal);
    }

    public List<Mesa> getMesasDisponiveis(LocalDateTime dataInicio, LocalDateTime dataFim) {
        // por reserva actual estou a dizer as já existentes
        // nova mesa não pode iniciar antes da reserva actual terminar e
        // não pode terminar após uma reserva actual iniciar
        String hql = """
                    SELECT m 
                    FROM Mesa m
                    WHERE m.id NOT IN (
                        SELECT r.mesa.id FROM Reserva r
                        WHERE  r.status = 'ACTIVA' 
                            AND r.dataFim > :inicio
                            AND r.dataInicio < :fim
                    )
                    AND m.id NOT IN (
                        SELECT p.mesa.id FROM Pedido p
                        WHERE p.status IN ('ABERTO', 'PENDENTE')
                    )
                """;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Mesa> mesas = session.createQuery(hql, Mesa.class)
                    .setParameter("inicio", dataInicio)
                    .setParameter("fim", dataFim)
                    .getResultList();
            return mesas;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return List.of();
        }
    }
}
