//package view;
//
//import dao.*;
//import model.*;
//import services.PedidoService;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//
//// IMPORTANT NOTE : A base de dados remove os dados a cada execucao do projecto
//// para os dados nao serem removidos mude a linha 19 do ficheiro hibernate.cfg.xml
//// de createdrop para update
//
//public class TestPedido {
//    public static void main(String[] args) {
//        ReservaDAO reservaDAO = new ReservaDAO();
//        MesaDAO mesaDAO = new MesaDAO();
//        Mesa mesa = new Mesa();
//        mesa.setCapacidade(10);
//        mesaDAO.save(mesa);
//        UserDAO userDAO = new UserDAO();
//        User user = new User();
//        user.setName("Nelson");
//        user.setPassword("a12345");
//        user.setEmail("nelson@gmail.com");
//        user.setType(UserType.CLIENT);
//        userDAO.save(user);
//        Reserva reserva = new Reserva();
//        LocalDateTime horaInicio = LocalDate.now().plusDays(1).atTime(9, 32, 0);
//        LocalDateTime horaFim = horaInicio.plusMinutes(90);
//        reserva.setMesa(mesa);
//        reserva.setCliente(user);
//        reserva.setStatus(ReservaStatus.ACTIVA);
//        reserva.setDataFim(horaFim);
//        reserva.setDataInicio(horaInicio);
//        reservaDAO.save(reserva);
//
//        reserva = new Reserva();
//        horaInicio = horaInicio.plusHours(4);
//        horaFim = horaFim.plusHours(4);
//        reserva.setMesa(mesa);
//        reserva.setCliente(user);
//        reserva.setStatus(ReservaStatus.ACTIVA);
//        reserva.setDataFim(horaFim);
//        reserva.setDataInicio(horaInicio);
//        reservaDAO.save(reserva);
//
//        reserva = new Reserva();
//        horaInicio = horaInicio.plusHours(7);
//        horaFim = horaFim.plusHours(7);
//        reserva.setMesa(mesa);
//        reserva.setCliente(user);
//        reserva.setStatus(ReservaStatus.ACTIVA);
//        reserva.setDataFim(horaFim);
//        reserva.setDataInicio(horaInicio);
//        reservaDAO.save(reserva);
//    }
//    public static void mainm(String[] args) {
//        UserDAO userDAO = new UserDAO();
//        User cliente = new User();
//        cliente.setName("Cliente1");
//        cliente.setPassword("12345678");
//        cliente.setType(UserType.CLIENT);
//        userDAO.save(cliente);
//
//        User worker = new User();
//        worker.setName("Worker 1");
//        worker.setPassword("12345678");
//        worker.setType(UserType.WORKER);
//        userDAO.save(worker);
//
//        IngredienteDAO ingredienteDAO = new IngredienteDAO();
//        MenuItemDAO menuItemDAO = new MenuItemDAO();
//        PedidoItemDAO pedidoItemDAO = new PedidoItemDAO();
//        PedidoDAO pedidoDAO = new PedidoDAO();
//
//        Ingrediente ingrediente1 = new Ingrediente();
//        ingrediente1.setIngrediente("Batata Frita");
//
//        Ingrediente ingrediente2 = new Ingrediente();
//        ingrediente2.setIngrediente("Frango");
//
//        ingredienteDAO.save(ingrediente1);
//        ingredienteDAO.save(ingrediente2);
//
//        MenuItem menuItem1 = new MenuItem();
//        menuItem1.setCategoria(ItemCategory.PRATO_PRINCIPAL);
//        menuItem1.setDescricao("Delicioso frango marinado em leite de coco, " +
//                "alho, piri-piri e limão, assado na brasa até a pele ficar " +
//                " estaladiça e saborosa. Uma receita tradicional da província da Zambézia, " +
//                " que traz o sabor autêntico da culinária moçambicana.");
//        menuItem1.setNomeDoPrato("Frango a Zambeziana");
//        menuItem1.setPreco(500);
//        menuItemDAO.save(menuItem1);
//
//        PedidoService pedidoService = new PedidoService();
//        PedidoItem pedidoItem = new PedidoItem();
//        pedidoItem.setMenuItem(menuItem1);
//        pedidoItem.setQuantidade(0);
//        pedidoItemDAO.save(pedidoItem);
//        int idPedido = pedidoService.criarPedido(cliente.getId(), worker.getId(), 1, new ArrayList<PedidoItem>());
//        pedidoService.adicionarOuIncrementarItem(idPedido, pedidoItem);
//        pedidoService.adicionarOuIncrementarItem(idPedido, pedidoItem);
//        pedidoService.adicionarOuIncrementarItem(idPedido, pedidoItem);
//        pedidoService.mudarQuantidadeItem(idPedido, pedidoItem, 10);
//        pedidoService.removerItem(idPedido, pedidoItem);
//        pedidoService.finalizarPedido(idPedido, PedidoStatus.CONCLUIDO);
//        System.out.println("Total a pagar " + pedidoService.calcularTotalPedido(idPedido));
//
//
//    }
//}
