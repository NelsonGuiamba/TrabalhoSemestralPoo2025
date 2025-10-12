package services;

import dao.MesaDAO;
import dao.ReservaDAO;
import dao.UserDAO;
import model.*;
import org.hibernate.Session;
import util.HibernateUtil;
import util.Utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReservaService {
    private MesaDAO mesaDAO = new MesaDAO();
    private UserDAO userDAO = new UserDAO();
    private ReservaDAO reservaDAO = new ReservaDAO();

    public ReservaService() {
    }

    public int criarReserva(int idCliente, int idMesa, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        Optional<User> clienteWrapper = userDAO.findById(idCliente);
        if (clienteWrapper.isEmpty()) {
            System.out.println("ID do cliente nao encontrado");
            return -1;
        }
        User cliente = clienteWrapper.get();
        if (!cliente.getType().equals(UserType.CLIENT)) {
            System.out.println("Sem permissão para criar Reserva");
            return -1;
        }
        Optional<Mesa> mesaWrapper = mesaDAO.findById(idMesa);
        if (mesaWrapper.isEmpty()) {
            System.out.println("ID do mesa nao encontrado");
            return -1;
        }
        Mesa mesa = mesaWrapper.get();
        if (dataFinal.getHour() > Utils.getHoraDeFecho()) {
            System.out.println("Hora de fecho invalida");
            return -1;
        }
        if (dataInicial.getHour()  < Utils.getHoraDeAbertura()) {
            System.out.println("Hora de abertura invalida");
            return -1;
        }
        for (Reserva reserva : reservaDAO.getReservasDoDia(dataInicial.toLocalDate())) {
            System.out.println(reserva);
            if (reserva.getCliente().getId() == cliente.getId()) {
                System.out.println("Usuário ja tem reservas para hoje");
                return -22;
            }
        }
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setMesa(mesa);
        reserva.setDataInicio(dataInicial);
        reserva.setDataFim(dataFinal);
        reservaDAO.save(reserva);
        return reserva.getId();
    }

    public int finalizarReserva(int idReserva, ReservaStatus status) {
        Optional<Reserva> reservaWrapper = reservaDAO.findById(idReserva);
        if (reservaWrapper.isEmpty()) {
            System.out.println("ID do reserva nao encontrado");
            return -1;
        }
        Reserva reserva = reservaWrapper.get();
        if (!reserva.getStatus().equals(ReservaStatus.ACTIVA)) {
            System.out.println("Reserva ja finalizada");
            return -1;
        }
        reservaDAO.mudarStatus(idReserva, status);
        return reserva.getId();
    }

    public static double calcularPreco(int idMesa, int duracao) {
        MesaDAO mesaDAO = new MesaDAO();
        Optional<Mesa> o = mesaDAO.findById(idMesa);
        int capacidade;
        if (o.isEmpty()) {
            capacidade = 8;
        }else {
            capacidade = o.get().getCapacidade();
        }
        // Preço base para 90 minutos
        double precoBase;
        switch (capacidade) {
            case 4:
                precoBase = 800.0;
                break;
            case 8:
                precoBase = 1000.0;
                break;
            case 10:
                precoBase = 1200.0;
                break;
            case 12:
                precoBase = 1500;
                break;
            default:
                precoBase = 1500.0;
        }

        // Multiplicador baseado na duração
        double multiplicador;
        switch (duracao) {
            case 90:
                multiplicador = 1.0;
                break;
            case 120:
                multiplicador = 2.0;
                break;
            case 180:
                multiplicador = 3.0;
                break;
            case 240:
                multiplicador = 4.0;
                break;
            default:
                multiplicador = 1.0;
        }

        return precoBase * multiplicador;
    }

    public List<Reserva> getReservasDeUser(int id) {
        String hql = """
                                    SELECT r
                                    FROM Reserva r
                                    WHERE r.cliente.id = :clienteId
                                      AND r.status = 'ACTIVA'
                                    ORDER BY r.dataInicio ASC
                """;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Reserva> list = session.createQuery(hql, Reserva.class)
                    .setParameter("clienteId", id)
                    .getResultList();
            return list;
        }catch (Exception e){
            return List.of();
        }
    }
}
