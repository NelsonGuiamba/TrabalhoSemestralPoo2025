package view;

import dao.*;
import model.*;
import services.PedidoService;
import services.UsuarioService;

import java.util.ArrayList;

// IMPORTANT NOTE : A base de dados remove os dados a cada execucao do projecto
// para os dados nao serem removidos mude a linha 19 do ficheiro hibernate.cfg.xml
// de createdrop para update

public class TestPedido {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        User cliente = new User();
        cliente.setName("Cliente1");
        cliente.setPassword("12345678");
        cliente.setType(UserType.CLIENT);
        userDAO.save(cliente);

        User worker = new User();
        worker.setName("Worker 1");
        worker.setPassword("12345678");
        worker.setType(UserType.WORKER);
        userDAO.save(worker);

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
        int idPedido = pedidoService.criarPedido(cliente.getId(), worker.getId(), new ArrayList<>());
        pedidoService.adicionarOuIncrementarItem(idPedido, pedidoItem);
        pedidoService.adicionarOuIncrementarItem(idPedido, pedidoItem);
        pedidoService.adicionarOuIncrementarItem(idPedido, pedidoItem);
        pedidoService.mudarQuantidadeItem(idPedido, pedidoItem, 10);
        pedidoService.removerItem(idPedido, pedidoItem);
        pedidoService.finalizarPedido(idPedido, PedidoStatus.CONCLUIDO);
        System.out.println("Total a pagar " + pedidoService.calcularTotalPedido(idPedido));


    }
}
