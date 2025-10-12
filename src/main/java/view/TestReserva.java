package view;

import dao.*;
import model.*;
import services.MesaService;
import services.PedidoService;
import services.ReservaService;
import services.UsuarioService;

import java.time.LocalDateTime;
import java.util.ArrayList;

// IMPORTANT NOTE : A base de dados remove os dados a cada execucao do projecto
// para os dados nao serem removidos mude a linha 19 do ficheiro hibernate.cfg.xml
// de createdrop para update

public class TestReserva {
    public static void main(String[] args) {
        PedidoDAO pedidoDAO = new PedidoDAO();
        for(PedidoItem p : pedidoDAO.findByIdWithItems(2).get().getItems()) {
            System.out.println(p.getMenuItem().getNomeDoPrato());
        }
    }
    public static void mainn(String[] args) {

        // usuarios
        UserDAO userDAO = new UserDAO();
        User cliente = new User();
        cliente.setName("Cliente1");
        cliente.setPassword("12345678");
        cliente.setType(UserType.CLIENT);
        userDAO.save(cliente);

        User cliente2 = new User();
        cliente2.setName("Cliente2");
        cliente2.setPassword("12345678");
        cliente2.setType(UserType.CLIENT);
        userDAO.save(cliente2);

        User worker = new User();
        worker.setName("Worker 1");
        worker.setPassword("12345678");
        worker.setType(UserType.WORKER);
        userDAO.save(worker);

        // igredientes

        IngredienteDAO ingredienteDAO = new IngredienteDAO();
        MenuItemDAO menuItemDAO = new MenuItemDAO();
        PedidoItemDAO pedidoItemDAO = new PedidoItemDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();

        Ingrediente ingrediente1 = new Ingrediente();
        ingrediente1.setIngrediente("Batata Frita");

        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente2.setIngrediente("Frango");

        ingredienteDAO.save(ingrediente1);
        ingredienteDAO.save(ingrediente2);

        // menu items
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setCategoria(ItemCategory.PRATO_PRINCIPAL);
        menuItem1.setDescricao("Delicioso frango marinado em leite de coco, " +
                "alho, piri-piri e limão, assado na brasa até a pele ficar " +
                " estaladiça e saborosa. Uma receita tradicional da província da Zambézia, " +
                " que traz o sabor autêntico da culinária moçambicana.");
        menuItem1.setNomeDoPrato("Frango a Zambeziana");
        menuItem1.setPreco(500);
        menuItemDAO.save(menuItem1);

        PedidoService pedidoService = new PedidoService();
        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setMenuItem(menuItem1);
        pedidoItem.setQuantidade(0);
        pedidoItemDAO.save(pedidoItem);

        // mesas
        MesaDAO mesaDAO = new MesaDAO();
        for (int i = 0; i < 10; i++) {
            Mesa mesa = new Mesa();
            mesa.setCapacidade(10);
            mesaDAO.save(mesa);
        }

        // reservas
        ReservaService reservaService = new ReservaService();
        int idReserva = reservaService.criarReserva(cliente2.getId(), 5, LocalDateTime.now().plusMinutes(119), LocalDateTime.now().plusMinutes(169));



        MesaService mesaService = new MesaService();
        System.out.println("Mesas antes de finalizar");
        for(Mesa mesa : mesaService.getMesasDisponiveis(LocalDateTime.now())) {
            System.out.print(mesa.getId() + " -> ");
        }

        int idPedido = pedidoService.criarPedido(cliente.getId(), worker.getId(), 1, new ArrayList<PedidoItem>());
        pedidoService.adicionarOuIncrementarItem(idPedido, pedidoItem);
        System.out.println("\n");
        pedidoService.finalizarPedido(idPedido, PedidoStatus.CONCLUIDO);
        System.out.println("Total a pagar " + pedidoService.calcularTotalPedido(idPedido));
        System.out.println("Mesas depois de finalizar");
        for(Mesa mesa : mesaService.getMesasDisponiveis(LocalDateTime.now().plusMinutes(31))) {
            System.out.print(mesa.getId() + " -> ");
        }
        System.out.println("\n");
        System.out.println("Mesas depois de cancelar mas procurando dentro do tempo da reserva");
        if(idReserva > 0)
            reservaService.finalizarReserva(idReserva, ReservaStatus.FINALIZADA);
        for(Mesa mesa : mesaService.getMesasDisponiveis(LocalDateTime.now().plusMinutes(31))) {
            System.out.print(mesa.getId() + " -> ");
        }
        System.out.println("\n");

    }
}
