package services;

import dao.ReservaDAO;
import model.Reserva;
import org.hibernate.Session;
import util.HibernateUtil;
import util.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class ReservaTest {
    public static void main(String[] args) {

      MesaService mesaService = new MesaService();
        LogManager.getLogManager().getLogger("").setLevel(Level.SEVERE);
        ReservaDAO dao = new ReservaDAO();
        List<Reserva>  todas=dao.findAll();
        Reserva reserva = new Reserva();
        reserva.setDataInicio(LocalDate.now().atTime(8, 30));
        reserva.setDataFim(LocalDate.now().atTime(10, 0));

        Reserva reserva1 = new Reserva();
        reserva1.setDataInicio(LocalDate.now().atTime(7, 30));
        reserva1.setDataFim(LocalDate.now().atTime(8,  29));
       System.out.println(mesaService.checkForOverlap(reserva, reserva1));
        //System.out.println(mesaService.isAfterClosing(LocalDate.now().atTime(22, 30), 31));
//        System.out.println(todas);
//        System.out.println(mesaService.getReservasNoIntervalo(
//                mesaService.getDataInicio(todas.getFirst().getDataInicio()),
//                mesaService.getDataFim(todas.getFirst().getDataFim())
//        ));
        System.out.println(mesaService.getHorariosDisponiveisParaMesa(1, LocalDate.of(2025, 10, 15), 60));
    }
}
