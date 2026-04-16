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

public class Tmp {
    public static void main(String[] args) {
        carregar();
        System.out.println("fim");
    }

    public static void carregar() {

        List<Map<String, String>> entradas = List.of(
                Map.of(
                        "nomeDoPrato", "Rissóis de Camarão",
                        "ingredientes", "camarao, alho, cebola, piripiri, farinha",
                        "preco", "160.00",
                        "tempoPreparacao", "28",
                        "imagem", "/view/images/pratos/rissolcamarao.png"
                ),
                Map.of(
                        "nomeDoPrato", "Duzia de chamuças",
                        "ingredientes", "carne, alho, cebola, piripiri, trigo",
                        "preco", "220.00",
                        "tempoPreparacao", "22",
                        "imagem", "/view/images/pratos/chamucas.png"
                ),
                Map.of(
                        "nomeDoPrato", "Hotdog",
                        "ingredientes", "paofrances, salsicha, ketchup, mostarda, maionese",
                        "preco", "180.00",
                        "tempoPreparacao", "18",
                        "imagem", "/view/images/pratos/hotdog.png"
                ),
                Map.of(
                        "nomeDoPrato", "Batata Chips",
                        "ingredientes", "batata, sal, oleo, piripiri, ervas",
                        "preco", "130.00",
                        "tempoPreparacao", "15",
                        "imagem", "/view/images/pratos/batatachips.png"
                ),
                Map.of(
                        "nomeDoPrato", "Salada de Palmito e Coco",
                        "ingredientes", "palmito, coco, lima, alface, tomate",
                        "preco", "160.00",
                        "tempoPreparacao", "12",
                        "imagem", "/view/images/pratos/saladapalmito.png"
                )
        );

        List<Map<String, String>> pratosPrincipais = List.of(
                Map.of(
                        "nomeDoPrato", "Matapa com Camarão",
                        "ingredientes", "mandioca, amendoim, leitecoco, camarao, alho, cebola, tomate",
                        "preco", "420.00",
                        "tempoPreparacao", "65",
                        "imagem", "/view/images/pratos/matapacamarao.png"
                ),
                Map.of(
                        "nomeDoPrato", "Vaca assada",
                        "ingredientes", "carnevaca, coco, amendoim, tomate, cebola, alho, piripiri",
                        "preco", "470.00",
                        "tempoPreparacao", "70",
                        "imagem", "/view/images/pratos/pedacosdevaca.png"
                ),
                Map.of(
                        "nomeDoPrato", "Frango frito",
                        "ingredientes", "frango, alho, cebola, piripiri, limao, coentro",
                        "preco", "320.00",
                        "tempoPreparacao", "38",
                        "imagem", "/view/images/pratos/frangofrito.png"
                ),
                Map.of(
                        "nomeDoPrato", "Caril de Amendoim (Frango)",
                        "ingredientes", "frango, amendoim, tomate, alho, cebola, coco",
                        "preco", "360.00",
                        "tempoPreparacao", "58",
                        "imagem", "/view/images/pratos/carilamendoim.png"
                ),
                Map.of(
                        "nomeDoPrato", "Peixe Grelhado com Piri-Piri",
                        "ingredientes", "peixe, piripiri, alho, limao, coentro",
                        "preco", "380.00",
                        "tempoPreparacao", "35",
                        "imagem", "/view/images/pratos/peixegrelhado.png"
                ),
                Map.of(
                        "nomeDoPrato", "Feijoada à Moçambicana",
                        "ingredientes", "feijao, carneporco, cebola, alho, tomate, amendoim",
                        "preco", "350.00",
                        "tempoPreparacao", "95",
                        "imagem", "/view/images/pratos/feijoada.png"
                ),
                Map.of(
                        "nomeDoPrato", "Galinha à Zambeziana",
                        "ingredientes", "frango, coco, alho, tomate, piripiri, limao",
                        "preco", "400.00",
                        "tempoPreparacao", "62",
                        "imagem", "/view/images/pratos/frangoassado.png"
                )
        );

        List<Map<String, String>> sobremesas = List.of(
                Map.of(
                        "nomeDoPrato", "Pudim de caramelo",
                        "ingredientes", "batatadoce, amendoim, acucar, ovo, coco",
                        "preco", "220.00",
                        "tempoPreparacao", "55",
                        "imagem", "/view/images/pratos/pudimdecaramelo.png"
                ),
                Map.of(
                        "nomeDoPrato", "Sorvete de coco",
                        "ingredientes", "mandioca, coco, acucar, ovo",
                        "preco", "180.00",
                        "tempoPreparacao", "40",
                        "imagem", "/view/images/pratos/sorvetedecoco.png"
                ),
                Map.of(
                        "nomeDoPrato", "Mousse de chocolate",
                        "ingredientes", "chocolate, leite, acucar, ovo",
                        "preco", "200.00",
                        "tempoPreparacao", "25",
                        "imagem", "/view/images/pratos/moussedechocolate.png"
                ),
                Map.of(
                        "nomeDoPrato", "Salada de frutas",
                        "ingredientes", "maca, banana, laranja, melancia, mamao",
                        "preco", "150.00",
                        "tempoPreparacao", "10",
                        "imagem", "/view/images/pratos/saladadefrutas.png"
                )
        );

        IngredienteDAO ingredienteDAO = new IngredienteDAO();
        MenuItemDAO menuItemDAO = new MenuItemDAO();
//        if (!menuItemDAO.findAll().isEmpty()) {
//            return;
//        }

        for (Map<String, String> entrada : entradas) {
            String nome = entrada.get("nomeDoPrato");
            double preco = Double.parseDouble(entrada.get("preco"));
            int tempoPreparacao = Integer.parseInt(entrada.get("tempoPreparacao"));
            String imagem = entrada.get("imagem");
            MenuItem menuItem = menuItemDAO.findByNameTempo(nome, tempoPreparacao);
            if(menuItem != null){
                menuItem.setPreco(preco);
                menuItemDAO.update(menuItem);
            }
        }

        for (Map<String, String> entrada : pratosPrincipais) {
            String nome = entrada.get("nomeDoPrato");
            double preco = Double.parseDouble(entrada.get("preco"));
            int tempoPreparacao = Integer.parseInt(entrada.get("tempoPreparacao"));
            String imagem = entrada.get("imagem");
            MenuItem menuItem = menuItemDAO.findByNameTempo(nome, tempoPreparacao);
//            System.out.println(menuItem);
            if(menuItem != null){
                menuItem.setPreco(preco);
                menuItemDAO.update(menuItem);
            }
        }

        for (Map<String, String> entrada : sobremesas) {
            String nome = entrada.get("nomeDoPrato");
            double preco = Double.parseDouble(entrada.get("preco"));
            int tempoPreparacao = Integer.parseInt(entrada.get("tempoPreparacao"));
            String imagem = entrada.get("imagem");
            MenuItem menuItem = menuItemDAO.findByNameTempo(nome, tempoPreparacao);
            if(menuItem != null){
                menuItem.setPreco(preco);
                menuItemDAO.update(menuItem);
            }
        }
    }
}
