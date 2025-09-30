package services;

import dao.*;
import model.*;
import org.hibernate.Hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PedidoService {
    // TODO : Adicionar estados de erro nos retornos eg
    //        int PEDIDO_NOT_FOUND = -1
    //        return PEDIDO_NOT_FOUND (inside methods)
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final UserDAO userDAO = new UserDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final PedidoItemDAO pedidoItemDAO = new PedidoItemDAO();
    private final MesaDAO mesaDAO = new MesaDAO();
    public PedidoService(){}

    public int criarPedido(int idCLient, int idWorker, int idMesa, List<PedidoItem> initialItems){
        Optional<User> clienteWrapper = userDAO.findById(idCLient);
        if (clienteWrapper.isEmpty()){
            System.out.println("ID do cliente nao encontrado");
            return -1;
        }
        Optional<User> workerWrapper = userDAO.findById(idWorker);
        if(workerWrapper.isEmpty()){
            System.out.println("ID do worker nao encontrado");
            return -1;
        }
        User cliente = clienteWrapper.get();
        if(!cliente.getType().equals(UserType.CLIENT)){
            System.out.println("Cliente deve ser um cliente");
            return -1;
        }
        User worker = workerWrapper.get();
        if(!worker.getType().equals(UserType.WORKER)){
            System.out.println("Worker deve ser um worker");
            return -1;
        }

        if(!pedidoItemDAO.isValidList(initialItems)){
            System.out.println("Lista de items invalida");
            return -1;
        }

        Optional<Mesa> mesaWrapper = mesaDAO.findById(idMesa);
        if (mesaWrapper.isEmpty()){
            System.out.println("ID do mesa nao encontrado");
            return -1;
        }
        Mesa mesa = mesaWrapper.get();
        Pedido pedido = new Pedido();
        pedido.setStatus(PedidoStatus.PENDENTE);
        pedido.setItems(initialItems);
        pedido.setClient(cliente);
        pedido.setWorker(worker);
        pedido.setMesa(mesa);

        return pedidoDAO.save(pedido);
    }

    public int finalizarPedido(int idPedido, PedidoStatus pedidoStatus){
        Optional<Pedido> pedidoWrapper = pedidoDAO.findById(idPedido);
        if(pedidoWrapper.isEmpty()){
            System.out.println("Pedido nao existe");
            return -1;
        }
        Pedido pedido = pedidoWrapper.get();
        if(!pedido.getStatus().equals(PedidoStatus.PENDENTE)){
            System.out.println("Pedido ja esta finalizado");
            return -1;
        }
        pedidoDAO.mudarStatus(pedido.getId(), pedidoStatus);
        return 1;
    }

    public void adicionarOuIncrementarItem(int idPedido, PedidoItem item) {
        Optional<Pedido> pedidoWrapper = pedidoDAO.findByIdWithItems(idPedido);
        if (pedidoWrapper.isEmpty()) {
            System.out.println("Pedido nao existe");
            return;
        }

        Pedido pedido = pedidoWrapper.get();
        if (!pedido.getStatus().equals(PedidoStatus.PENDENTE)) {
            System.out.println("Pedido ja esta finalizado");
            return;
        }

        boolean encontrou = false;
        Iterator<PedidoItem> iterator = pedido.getItems().iterator();

        while (iterator.hasNext()) {
            PedidoItem itemSearch = iterator.next();
            if (itemSearch.getId() == item.getId()) {
                itemSearch.increaseQuantidade();
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            pedido.getItems().add(item);
            item.setPedido(pedido);
            item.setQuantidade(1);
        }

        pedidoDAO.update(pedido);
    }

    public void mudarQuantidadeItem(int idPedido, PedidoItem item, int quantidade){
        Optional<Pedido> pedidoWrapper = pedidoDAO.findByIdWithItems(idPedido);
        if (pedidoWrapper.isEmpty()) {
            System.out.println("Pedido nao existe");
            return;
        }

        Pedido pedido = pedidoWrapper.get();
        if (!pedido.getStatus().equals(PedidoStatus.PENDENTE)) {
            System.out.println("Pedido ja esta finalizado");
            return;
        }

        if(quantidade <= 0){
            System.out.println("Quantidade invalida");
            return;
        }
        boolean encontrou = false;
        Hibernate.initialize(pedido.getItems());
        Iterator<PedidoItem> iterator = pedido.getItems().iterator();

        while (iterator.hasNext()) {
            PedidoItem itemSearch = iterator.next();
            if (itemSearch.getId() == item.getId()) {
                itemSearch.setQuantidade(quantidade);
                encontrou = true;
                break;
            }
        }

        pedidoDAO.update(pedido);
    }

    public void removerItem(int idPedido, PedidoItem item){
        Optional<Pedido> pedidoWrapper = pedidoDAO.findByIdWithItems(idPedido);
        if (pedidoWrapper.isEmpty()) {
            System.out.println("Pedido nao existe");
            return;
        }

        Pedido pedido = pedidoWrapper.get();
        if (!pedido.getStatus().equals(PedidoStatus.PENDENTE)) {
            System.out.println("Pedido ja esta finalizado");
            return;
        }
        Hibernate.initialize(pedido.getItems());

        Iterator<PedidoItem> iterator = pedido.getItems().iterator();
        boolean remover = false;
        while (iterator.hasNext()) {
            PedidoItem itemSearch = iterator.next();
            if (itemSearch.getId() == item.getId()) {
                iterator.remove();
                remover = true;
                break;
            }
        }

        if(remover) pedidoDAO.update(pedido);
    }

    public double calcularTotalPedido(int idPedido){
        Optional<Pedido> pedidoWrapper = pedidoDAO.findByIdWithItems(idPedido);
        if (pedidoWrapper.isEmpty()) {
            System.out.println("Pedido nao existe");
            return 0;
        }
        Pedido pedido = pedidoWrapper.get();
        Hibernate.initialize(pedido.getItems());
        Iterator<PedidoItem> iterator = pedido.getItems().iterator();
        double total = 0;
        while (iterator.hasNext()) {
            PedidoItem itemSearch = iterator.next();
            total = total + itemSearch.getQuantidade() * itemSearch.getMenuItem().getPreco();
        }
        return total;
    }

}
