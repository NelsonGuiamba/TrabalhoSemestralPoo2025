package view;

import dao.IngredienteDAO;
import dao.MenuItemDAO;
import dao.MesaDAO;
import dao.UserDAO;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AppInitializer {
    public static void main(String[] args) {
        carregar();
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }


    public static void carregar(){

        List<Map<String, String>> entradas = List.of(
                Map.of(
                        "nomeDoPrato", "Dúzia de Rissóis de Camarão",
                        "ingredientes", "camarao, alho, cebola, piripiri, farinha de trigo, sal",
                        "preco", "300.00",
                        "tempoPreparacao", "28",
                        "imagem", "/view/images/pratos/rissolcamarao.png"
                ),
                Map.of(
                        "nomeDoPrato", "Dúzia de Chamuças",
                        "ingredientes", "carne, alho, cebola, piripiri,farinha de trigo",
                        "preco", "300.00",
                        "tempoPreparacao", "22",
                        "imagem", "/view/images/pratos/chamucas.png"
                ),
                Map.of(
                        "nomeDoPrato", "Hotdog",
                        "ingredientes", "pão francês, salsicha, ketchup, mostarda, maionese",
                        "preco", "250.00",
                        "tempoPreparacao", "18",
                        "imagem", "/view/images/pratos/hotdog.png"
                ),
                Map.of(
                        "nomeDoPrato", "Batata Chips",
                        "ingredientes", "batata, sal, óleo, piripiri, ervas",
                        "preco", "350.00",
                        "tempoPreparacao", "15",
                        "imagem", "/view/images/pratos/batatachips.png"
                ),
                Map.of(
                        "nomeDoPrato", "Salada de Palmito e Côco",
                        "ingredientes", "palmito, côco, lima, alface, tomate",
                        "preco", "120.00",
                        "tempoPreparacao", "12",
                        "imagem", "/view/images/pratos/saladapalmito.png"
                )
        );

        List<Map<String, String>> pratosPrincipais = List.of(
                Map.of(
                        "nomeDoPrato", "Matapa com Camarão",
                        "ingredientes", "mandioca, amendoim, leite de côco, camarão, alho, cebola, tomate",
                        "preco", "130.00",
                        "tempoPreparacao", "65",
                        "imagem", "/view/images/pratos/matapacamarao.png"
                ),
                Map.of(
                        "nomeDoPrato", "Vaca assada",
                        "ingredientes", "carne de vaca, côco, amendoim, tomate, cebola, alho, piripiri",
                        "preco", "375.00",
                        "tempoPreparacao", "70",
                        "imagem", "/view/images/pratos/pedacosdevaca.png"
                ),
                Map.of(
                        "nomeDoPrato", "Frango frito",
                        "ingredientes", "frango,alho , cebola, piripiri, limão, coentro",
                        "preco", "210.00",
                        "tempoPreparacao", "38",
                        "imagem", "/view/images/pratos/frangofrito.png"
                ),
                Map.of(
                        "nomeDoPrato", "Caril de Amendoim (Frango)",
                        "ingredientes", "frango, amendoim, tomate, alho, cebola, coco",
                        "preco", "200.00",
                        "tempoPreparacao", "65",
                        "imagem", "/view/images/pratos/carilamendoim.png"
                ),
                Map.of(
                        "nomeDoPrato", "Peixe Grelhado com Piri-Piri",
                        "ingredientes", "peixe, piripiri, alho, limao, coentro",
                        "preco", "220.00",
                        "tempoPreparacao", "35",
                        "imagem", "/view/images/pratos/peixegrelhado.png"
                ),
                Map.of(
                        "nomeDoPrato", "Feijoada à Moçambicana",
                        "ingredientes", "feijão, carne de porco, cebola, alho, tomate, cenoura",
                        "preco", "130.00",
                        "tempoPreparacao", "95",
                        "imagem", "/view/images/pratos/feijoada.png"
                ),
                Map.of(
                        "nomeDoPrato", "Galinha à Zambeziana",
                        "ingredientes", "frango, côco, alho, tomate, piripiri, limão",
                        "preco", "250.00",
                        "tempoPreparacao", "60",
                        "imagem", "/view/images/pratos/frangoassado.png"
                )
        );

        List<Map<String, String>> sobremesas = List.of(
                Map.of(
                        "nomeDoPrato", "Pudim de caramelo",
                        "ingredientes", "batata doce, amendoim, açúcar, ovo, côco",
                        "preco", "280.00",
                        "tempoPreparacao", "55",
                        "imagem", "/view/images/pratos/pudimdecaramelo.png"
                ),
                Map.of(
                        "nomeDoPrato", "Sorvete de côco",
                        "ingredientes", "mandioca, côco, açúcar, ovo",
                        "preco", "190.50",
                        "tempoPreparacao", "20",
                        "imagem", "/view/images/pratos/sorvetedecoco.png"
                ),
                Map.of(
                        "nomeDoPrato", "Mousse de chocolate",
                        "ingredientes", "chocolate, leite, açúcar, ovo",
                        "preco", "400.00",
                        "tempoPreparacao", "25",
                        "imagem", "/view/images/pratos/moussedechocolate.png"
                ),
                Map.of(
                        "nomeDoPrato", "Salada de frutas",
                        "ingredientes", "maça, banana, laranja, melancia, uva, morango",
                        "preco", "145.00",
                        "tempoPreparacao", "10",
                        "imagem", "/view/images/pratos/saladadefrutas.png"
                )
        );


        IngredienteDAO ingredienteDAO = new IngredienteDAO();
        MenuItemDAO menuItemDAO = new MenuItemDAO();
        if(!menuItemDAO.findAll().isEmpty()) {
            return;
        }
        for(Map<String, String> entrada : entradas) {
            String nome = entrada.get("nomeDoPrato");
            double preco = Double.parseDouble(entrada.get("preco"));
            int tempoPreparacao = Integer.parseInt(entrada.get("tempoPreparacao"));
            String imagem = entrada.get("imagem");
            MenuItem menuItem = new MenuItem();
            menuItem.setNomeDoPrato(nome);
            menuItem.setPreco(preco);
            menuItem.setTempoPreparacao(tempoPreparacao);
            menuItem.setCategoria(ItemCategory.ENTRADA);
            menuItem.setImagem(imagem);
            menuItem.setIngredientes(new ArrayList<>());
            for(String ingrediente : entrada.getOrDefault("ingredientes", "").split(",")) {
                Ingrediente obj = ingredienteDAO.getOrCreate(ingrediente.toLowerCase().strip());
                if(obj!= null) menuItem.getIngredientes().add(obj);
            }
            menuItemDAO.save(menuItem);
        }

        for(Map<String, String> entrada : pratosPrincipais) {
            String nome = entrada.get("nomeDoPrato");
            double preco = Double.parseDouble(entrada.get("preco"));
            int tempoPreparacao = Integer.parseInt(entrada.get("tempoPreparacao"));
            String imagem = entrada.get("imagem");
            MenuItem menuItem = new MenuItem();
            menuItem.setNomeDoPrato(nome);
            menuItem.setPreco(preco);
            menuItem.setTempoPreparacao(tempoPreparacao);
            menuItem.setCategoria(ItemCategory.PRATO_PRINCIPAL);
            menuItem.setImagem(imagem);
            menuItem.setIngredientes(new ArrayList<>());
            for(String ingrediente : entrada.getOrDefault("ingredientes", "").split(",")) {
                Ingrediente obj = ingredienteDAO.getOrCreate(ingrediente.toLowerCase().strip());
                if(obj!= null) menuItem.getIngredientes().add(obj);
            }
            menuItemDAO.save(menuItem);
        }

        for(Map<String, String> entrada : sobremesas) {
            String nome = entrada.get("nomeDoPrato");
            double preco = Double.parseDouble(entrada.get("preco"));
            int tempoPreparacao = Integer.parseInt(entrada.get("tempoPreparacao"));
            String imagem = entrada.get("imagem");
            MenuItem menuItem = new MenuItem();
            menuItem.setNomeDoPrato(nome);
            menuItem.setPreco(preco);
            menuItem.setTempoPreparacao(tempoPreparacao);
            menuItem.setCategoria(ItemCategory.SOBREMESA);
            menuItem.setImagem(imagem);
            menuItem.setIngredientes(new ArrayList<>());
            for(String ingrediente : entrada.getOrDefault("ingredientes", "").split(",")) {
                Ingrediente obj = ingredienteDAO.getOrCreate(ingrediente.toLowerCase().strip());
                if(obj!= null) menuItem.getIngredientes().add(obj);
            }
            menuItemDAO.save(menuItem);
            System.out.println("ALL DATA WAS SAVED");
        }

        UserDAO userDAO = new UserDAO();
        User user = new User();
        user.setType(UserType.CLIENT);
        user.setPassword("a12345");
        user.setEmail("nelson@gmail.com");
        user.setName("Nelson");
        userDAO.save(user);

        user.setType(UserType.WORKER);
        user.setPassword("a12345");
        user.setEmail("ana@gmail.com");
        user.setName("Ana");
        userDAO.save(user);

        MesaDAO mesaDAO = new MesaDAO();
        for(Integer i : List.of(4,4,8,8,8,10,12)){
            Mesa mesa = new Mesa();
            mesa.setCapacidade(i);
            mesaDAO.save(mesa);
        }

    }
}
