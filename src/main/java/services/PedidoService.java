package services;

import dao.*;
import model.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import util.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class PedidoService {
    // TODO : Adicionar estados de erro nos retornos eg
    //        int PEDIDO_NOT_FOUND = -1
    //        return PEDIDO_NOT_FOUND (inside methods)
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final UserDAO userDAO = new UserDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final PedidoItemDAO pedidoItemDAO = new PedidoItemDAO();
    private final MesaDAO mesaDAO = new MesaDAO();

    public PedidoService() {
    }

    public int criarPedidoTakeway(int idClient, Map<String, String> cart) {
        Optional<User> clienteWrapper = userDAO.findById(idClient);
        if (clienteWrapper.isEmpty()) {
            System.out.println("ID do cliente nao encontrado");
            return -1;
        }
        User user = clienteWrapper.get();
        Pedido pedido = new Pedido();
        pedido.setClient(user);
        pedido.setWorker(null);
        pedido.setTakeway(true);
        pedido.setStatus(PedidoStatus.PENDENTE);
        pedido.setMesa(null);
        pedido.setItems(new ArrayList<>());
        pedidoDAO.save(pedido);
        for (MenuItem item : menuItemDAO.findAll()) {
            String key = "" + item.getId();
            String value = cart.getOrDefault(key, "-1");
            int qtd = Integer.parseInt(value);
            if (qtd > 0) {
                PedidoItem pedidoItem = new PedidoItem();
                pedidoItem.setMenuItem(item);
                pedidoItem.setPedido(pedido);
                pedidoItem.setQuantidade(qtd);
                pedidoItem.setPreco(item.getPreco());
                pedidoItemDAO.save(pedidoItem);
            }
        }
        return pedido.getId();
    }

    public int criarPedidoNormal(String idClient, int idWorker, int idMesa, Map<String, String> cart) {
        User clienteWrapper = userDAO.findUserByName(idClient);
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime fimDoDia = hoje.atTime(LocalTime.MAX);
        if (clienteWrapper == null) {
            System.out.println("ID do cliente nao encontrado");
            return -1;
        }
        Optional<User> workerWrapper = userDAO.findById(idWorker);
        if (workerWrapper.isEmpty()) {
            System.out.println("ID do cliente nao encontrado");
            return -1;
        }
        Optional<Mesa> mesaWrapper = mesaDAO.findById(idMesa);
        if (mesaWrapper.isEmpty()) {
            System.out.println("ID do mesa nao encontrado");
            return -1;
        }
        Mesa mesa = mesaWrapper.get();
        User worker = workerWrapper.get();
        User user = clienteWrapper;
        Pedido pedido = null;
        String hql = """
                    FROM Pedido p
                    WHERE p.status = 'PENDENTE'
                      AND p.dataCompra BETWEEN :inicio AND :fim 
                      AND p.mesa.id = :mesaId
                """;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            pedido = session.createQuery(hql, Pedido.class)
                    .setParameter("inicio", inicioDoDia)
                    .setParameter("fim", fimDoDia)
                    .setParameter("mesaId", idMesa)
                    .setMaxResults(1) // 🔹 limita a 1 registro
                    .uniqueResult();  // 🔹 retorna um único objeto ou null
        } catch (Exception ex) {
            System.out.println("Erro ao buscar pedido: " + ex.getMessage());
        }
        if (pedido == null) {
            pedido = new Pedido();
            pedido.setClient(user);
            pedido.setWorker(worker);
            pedido.setTakeway(false);
            pedido.setStatus(PedidoStatus.PENDENTE);
            pedido.setMesa(mesa);
            pedido.setItems(new ArrayList<>());
            pedidoDAO.save(pedido);
        }

        for (MenuItem item : menuItemDAO.findAll()) {
            String key = "" + item.getId();
            String value = cart.getOrDefault(key, "-1");
            int qtd = Integer.parseInt(value);
            if (qtd > 0) {
                PedidoItem pedidoItem = new PedidoItem();
                pedidoItem.setMenuItem(item);
                pedidoItem.setPedido(pedido);
                pedidoItem.setQuantidade(qtd);
                pedidoItem.setPreco(item.getPreco());
                pedidoItemDAO.save(pedidoItem);
            }
        }
        return pedido.getId();
    }

    public int criarPedido(int idCLient, int idWorker, int idMesa, List<PedidoItem> initialItems) {
        Optional<User> clienteWrapper = userDAO.findById(idCLient);
        if (clienteWrapper.isEmpty()) {
            System.out.println("ID do cliente nao encontrado");
            return -1;
        }
        Optional<User> workerWrapper = userDAO.findById(idWorker);
        if (workerWrapper.isEmpty()) {
            System.out.println("ID do worker nao encontrado");
            return -1;
        }
        User cliente = clienteWrapper.get();
        if (!cliente.getType().equals(UserType.CLIENT)) {
            System.out.println("Cliente deve ser um cliente");
            return -1;
        }
        User worker = workerWrapper.get();
        if (!worker.getType().equals(UserType.WORKER)) {
            System.out.println("Worker deve ser um worker");
            return -1;
        }
        if (!pedidoItemDAO.isValidList(initialItems)) {
            System.out.println("Lista de items invalida");
            return -1;
        }

        Optional<Mesa> mesaWrapper = mesaDAO.findById(idMesa);
        if (mesaWrapper.isEmpty()) {
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

    public int finalizarPedido(int idPedido, PedidoStatus pedidoStatus) {
        Optional<Pedido> pedidoWrapper = pedidoDAO.findById(idPedido);
        if (pedidoWrapper.isEmpty()) {
            System.out.println("Pedido nao existe");
            return -1;
        }
        Pedido pedido = pedidoWrapper.get();
        if (!pedido.getStatus().equals(PedidoStatus.PENDENTE)) {
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

    public void mudarQuantidadeItem(int idPedido, PedidoItem item, int quantidade) {
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

        if (quantidade <= 0) {
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

    public void removerItem(int idPedido, PedidoItem item) {
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

        if (remover) pedidoDAO.update(pedido);
    }

    public double calcularTotalPedido(int idPedido) {
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

    public Map<String, Integer> getUsers() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime fimDoDia = hoje.atTime(LocalTime.MAX);
        Map<String, Integer> mapa = new HashMap<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> usuarios = session.createQuery("FROM Usuarios", User.class).getResultList();
            for (User u : usuarios) {
                if (u.getType().equals(UserType.CLIENT))
                    mapa.put(u.getName(), -1);
            }

            String hql = """
                        SELECT p.client.name, p.mesa.id
                        FROM Pedido p
                        WHERE p.status = 'PENDENTE'
                            AND p.dataCompra BETWEEN :inicio AND :fim 
                            AND p.eTakeway = false 
                    """;

            List<Object[]> resultados = session.createQuery(hql, Object[].class)
                    .setParameter("inicio", inicioDoDia)
                    .setParameter("fim", fimDoDia)
                    .getResultList();

            for (Object[] row : resultados) {


                String nome = (String) row[0];
                Integer mesaId = (Integer) row[1];
                mapa.put(nome, mesaId);
            }

            return mapa;

        } catch (Exception ex) {
            System.out.println("Erro ao obter pedidos ativos: " + ex.getMessage());
            return Map.of();
        }
    }


    public ArrayList<String> getMesasDisponiveis() {
        MesaService mesaService = new MesaService();
        ArrayList<String> mesasDisponiveis = new ArrayList<>();
        for (Mesa m : mesaService.getMesasDisponiveis(LocalDateTime.now())) {
            mesasDisponiveis.add("" + m.getId());
        }
        return mesasDisponiveis;
    }

    public List<Pedido> getPedidosDeHoje() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime fimDoDia = hoje.atTime(LocalTime.MAX);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = """
            FROM Pedido p
            LEFT JOIN FETCH p.mesa m
            WHERE p.status = 'PENDENTE'
              AND p.dataCompra BETWEEN :inicio AND :fim
        """;

            return session.createQuery(hql, Pedido.class)
                    .setParameter("inicio", inicioDoDia)
                    .setParameter("fim", fimDoDia)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
