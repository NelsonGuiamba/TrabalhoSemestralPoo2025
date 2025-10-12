package services;

import dao.MesaDAO;
import model.Mesa;
import model.Reserva;
import org.hibernate.Session;
import util.HibernateUtil;
import util.Utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MesaService {
    private final MesaDAO mesaDAO = new MesaDAO();

    public MesaService() {
    }

    public List<Mesa> getMesasDisponiveis(LocalDateTime dataInicial) {
        return mesaDAO.findAll();
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

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Mesa> mesas = session.createQuery(hql, Mesa.class)
                    .setParameter("inicio", dataInicio)
                    .setParameter("fim", dataFim)
                    .getResultList();
            return mesas;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return List.of();
        }
    }

    public List<Reserva> getReservasNoIntervalo(int mesaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        String hql = """
                                    SELECT r
                                    FROM Reserva r
                                    WHERE r.mesa.id = :mesaId
                                      AND r.status = 'ACTIVA'
                                      AND r.dataInicio >= :inicioDoDia
                                      AND r.dataFim <= :fimDoDia
                                    ORDER BY r.dataInicio ASC
                """;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println(dataInicio);
            System.out.println(dataFim);
            List<Reserva> list= session.createQuery(hql,  Reserva.class)
                    .setParameter("mesaId", mesaId)
                    .setParameter("inicioDoDia", dataInicio)
                    .setParameter("fimDoDia", dataFim)
                    .getResultList();
            return list;
        } catch (Exception e) {
            System.out.println("something went wrong");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
    public boolean checkForOverlap(Reserva r1,  Reserva r2) {
        return !(r1.getDataFim().isBefore(r2.getDataInicio()) ||
                r1.getDataInicio().isAfter(r2.getDataFim()));
    }

    public boolean isAfterClosing(LocalDateTime dataInicio, int duration) {
        return dataInicio.plusMinutes(duration).isAfter(getDataFim(dataInicio));
    }

    public LocalDateTime getDataFim(LocalDateTime data) {
        LocalDateTime fim = data.toLocalDate().atTime(Utils.getHoraDeFecho(data.toLocalDate()), 0);
        return fim;
    }
    public LocalDateTime getDataInicio(LocalDateTime data) {
        return data.toLocalDate().atTime(Utils.getHoraDeAbertura(), 0);
    }
    public List<String> getHorariosDisponiveisParaMesa(int mesaId, LocalDate data, int duration){
        ArrayList<String> horariosDisponiveis = new ArrayList<>();
        LocalDateTime dataInical = getDataInicio(data.atStartOfDay());
        System.out.println(dataInical);
        List<Reserva> reservas = getReservasNoIntervalo(
                mesaId,
                getDataInicio(data.atStartOfDay()),
                getDataFim(data.atStartOfDay())
        );

        if(reservas.isEmpty()){
            while(!isAfterClosing(dataInical, duration)){
                horariosDisponiveis.add(""+dataInical.getHour()+":"+dataInical.getMinute());
                dataInical = dataInical.plusMinutes(30);
            }
        }else{
            Reserva current = reservas.get(0);
            int pos = 1;
            while(!isAfterClosing(dataInical, duration)){
                Reserva tmp = new Reserva();
                tmp.setDataInicio(dataInical);
                tmp.setDataFim(dataInical.plusMinutes(duration));
                if(current !=null){
                    if(checkForOverlap(current, tmp)){
                        dataInical = current.getDataFim().plusMinutes(5);
                        if(pos >= reservas.size()){
                            current = null;
                        }else{
                            current = reservas.get(pos);
                            pos++;
                        }
                        continue;
                    }else{
                        horariosDisponiveis.add(getHorario(dataInical));
                        dataInical = dataInical.plusMinutes(duration);
                    }
                }else{
                    horariosDisponiveis.add(getHorario(dataInical));
                    dataInical = dataInical.plusMinutes(duration);
                }
            }
        }
        return horariosDisponiveis;
    }

    public String getHorario(LocalDateTime date){
        StringBuilder sb = new StringBuilder();
        if(date.getHour() < 10){
            sb.append("0"+date.getHour());
        }else{
            sb.append(date.getHour());
        }
        sb.append(":");
        if(date.getMinute() < 10){
            sb.append("0"+date.getMinute());
        }else {
            sb.append(date.getMinute());
        }
        return sb.toString();
    }

}
