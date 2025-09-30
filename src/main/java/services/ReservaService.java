package services;

import dao.MesaDAO;
import dao.ReservaDAO;
import dao.UserDAO;
import model.*;
import util.Utils;

import java.time.LocalDateTime;
import java.util.Optional;

public class ReservaService {
    private MesaDAO mesaDAO =  new MesaDAO();
    private UserDAO userDAO =  new UserDAO();
    private ReservaDAO reservaDAO = new ReservaDAO();
    public ReservaService(){}

    public int criarReserva(int idCliente, int idMesa, LocalDateTime dataInicial, LocalDateTime dataFinal ) {
        Optional<User> clienteWrapper = userDAO.findById(idCliente);
        if (clienteWrapper.isEmpty()){
            System.out.println("ID do cliente nao encontrado");
            return -1;
        }
        User cliente = clienteWrapper.get();
        if(!cliente.getType().equals(UserType.CLIENT)){
            System.out.println("Sem permissão para criar Reserva");
            return -1;
        }
        Optional<Mesa> mesaWrapper = mesaDAO.findById(idMesa);
        if (mesaWrapper.isEmpty()){
            System.out.println("ID do mesa nao encontrado");
            return -1;
        }
        Mesa mesa = mesaWrapper.get();
        if(dataFinal.getHour() + 1 >= Utils.getHoraDeFecho()){
            System.out.println("Hora de fecho invalida");
            return -1;
        }
        if(dataInicial.getHour() + 1 <= Utils.getHoraDeAbertura()){
            System.out.println("Hora de abertura invalida");
            return -1;
        }
        for(Reserva reserva : reservaDAO.getReservasDeHoje()){
            if(reserva.getCliente().getId() == cliente.getId()){
                System.out.println("Usuário ja tem reservas para hoje");
                return -1;
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

    public int finalizarReserva(int idReserva, ReservaStatus status){
        Optional<Reserva> reservaWrapper = reservaDAO.findById(idReserva);
        if(reservaWrapper.isEmpty()){
            System.out.println("ID do reserva nao encontrado");
            return -1;
        }
        Reserva reserva = reservaWrapper.get();
        if(!reserva.getStatus().equals(ReservaStatus.ACTIVA)){
            System.out.println("Reserva ja finalizada");
            return -1;
        }
        reservaDAO.mudarStatus(idReserva, status);
        return reserva.getId();
    }
}
