package services;

import dao.PedidoDAO;
import model.ItemCategory;
import model.MenuItem;
import model.Pedido;
import model.PedidoItem;
import org.hibernate.Session;
import util.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class AdminServices {
    private final PedidoDAO pedidodao = new PedidoDAO();
    private Map<Integer, Double> totalPedidoChart = new HashMap<>();
    private Map<String, Integer> maisVendidos = new TreeMap<>();
    private Map<String, Double> totalPedidoCategoriaChart = new HashMap<>();
    private Map<String, Double> pedidoWorker = new HashMap<>();
    private int totalPedido;
    private int totalClientes;
    private Double totalReceita;
    public AdminServices() {
        totalClientes = 0;
        totalPedido = 0;
        totalReceita = 0.0;
        totalPedidoCategoriaChart.put(String.valueOf(ItemCategory.ENTRADA), 0.0);
        totalPedidoCategoriaChart.put(String.valueOf(ItemCategory.PRATO_PRINCIPAL), 0.0);
        totalPedidoCategoriaChart.put(String.valueOf(ItemCategory.SOBREMESA), 0.0);
        receitasData();
    }
    private List<Pedido> getPedidosNoIntervalo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        String hql = """
                                    SELECT DISTINCT p
                                    FROM Pedido p
                                    LEFT JOIN FETCH p.client
                                    LEFT JOIN FETCH p.items                                    
                                    WHERE p.dataCompra >= :inicioDoDia
                                      AND p.dataCompra <= :fimDoDia
                                    ORDER BY p.dataCompra ASC
                """;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println(dataInicio);
            System.out.println(dataFim);
            List<Pedido> list= session.createQuery(hql,  Pedido.class)
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

    private double calcularTotalPedido(Pedido pedido) {
        Iterator<PedidoItem> iterator = pedido.getItems().iterator();
        double total = 0;
        while (iterator.hasNext()) {
            PedidoItem itemSearch = iterator.next();
            double prev = totalPedidoCategoriaChart.getOrDefault(itemSearch.getMenuItem().getCategoria().toString(), 0.0);
            prev +=itemSearch.getQuantidade() * itemSearch.getMenuItem().getPreco();
            int prevv = maisVendidos.getOrDefault(itemSearch.getMenuItem().getNomeDoPrato().toString(), 0);
            prevv += itemSearch.getQuantidade();
            maisVendidos.put(itemSearch.getMenuItem().getNomeDoPrato().toString(), prevv);
            totalPedidoCategoriaChart.put(itemSearch.getMenuItem().getCategoria().toString(), prev);
            total = total + itemSearch.getQuantidade() * itemSearch.getMenuItem().getPreco();
        }
        return total;
    }

    private Map<Integer, Double> receitasData(){
        totalPedidoChart = new TreeMap<>();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        Set<Integer> clientes = new HashSet<>();
        totalPedido = 0;
        totalReceita = 0.0;
        List<Pedido> listPedido = this.getPedidosNoIntervalo(startDate.atStartOfDay(),
                LocalDateTime.now());
        System.out.println(listPedido.size());
        for(Pedido p : listPedido){
            if(p.getWorker() != null){
                double prevv = pedidoWorker.getOrDefault(p.getWorker().getName(), 0.0);
                pedidoWorker.put(p.getWorker().getName(), prevv+1);
            }
            Double actual = totalPedidoChart.getOrDefault(p.getDataCompra().getDayOfMonth(), 0.0);
            actual += calcularTotalPedido(p);
            totalReceita += calcularTotalPedido(p);
            clientes.add(p.getClient().getId());
            totalPedidoChart.put(p.getDataCompra().getDayOfMonth(), actual);
            totalPedido++;
            System.out.println("incr pedido");
        }
        totalClientes = clientes.size();
        return totalPedidoChart;
    }

    public Map<Integer, Double> getReceitaData(){
        return totalPedidoChart;
    }

    public int getTotalPedido(){
        return totalPedido;
    }

    public int getTotalClientes() {
        return totalClientes;
    }
    public Double getTotalReceita() {
        return totalReceita;
    }

    public Map<String, Double> getReceitaDataByCategoria() {
        return totalPedidoCategoriaChart;
    }

    public Map<String, Integer> getMaisVendidos() {
        Map<String, Integer> map = new HashMap<>();

        int n = 5;
        maisVendidos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .forEach(e -> map.put(e.getKey(), e.getValue()));
        System.out.println(map);
        return map;

    }

    public Map<String, Double> getPedidoTrabalhador() {
        return pedidoWorker;
    }
}
