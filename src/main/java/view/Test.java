package view;

import model.UserType;
import services.UsuarioService;
import util.HibernateUtil;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    private static final UsuarioService service = new UsuarioService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        boolean running = true;

        while (running) {
            showMenu();
            System.out.print("Escolha uma opção: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    login();
                    break;
                case "2":
                    logout();
                    break;
                case "3":
                    addUser();
                    break;
                case "4":
                    removeUser();
                    break;
                case "5":
                    showUsers();
                    break;
                case "0":
                    running = false;
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Login");
        System.out.println("2. Logout");
        System.out.println("3. Adicionar Usuário");
        System.out.println("4. Remover Usuário");
        System.out.println("5. Ver Usuários");
        System.out.println("0. Sair");
    }

    private static void login() {
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        if (service.login(name, password)) {
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.println("Falha no login!");
        }
    }

    private static void logout() {
        service.logout();
        System.out.println("Logout realizado com sucesso!");
    }

    private static void addUser() {
        System.out.print("Nome do usuário: ");
        String name = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        System.out.println("Tipos disponíveis: 1-CLIENT, 2-WORKER, 3-ADMIN");
        System.out.print("Escolha o tipo de usuário: ");
        String typeInput = scanner.nextLine();

        UserType type;
        switch (typeInput) {
            case "1":
                type = UserType.CLIENT;
                break;
            case "2":
                type = UserType.WORKER;
                break;
            case "3":
                type = UserType.ADMIN;
                break;
            default:
                System.out.println("Tipo inválido!");
                return;
        }

        if (service.register(name, password, type)) {
            System.out.println("Usuário criado com sucesso!");
        } else {
            System.out.println("Falha ao criar usuário!");
        }
    }

    private static void removeUser() {
        System.out.print("ID do usuário a remover: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
            return;
        }

        if (service.removerUsuario(id)) {
            System.out.println("Usuário removido com sucesso!");
        } else {
            System.out.println("Sem permissão ou usuário não encontrado!");
        }
    }

    private static void showUsers() {
        List<?> users = service.getAll();
        if (users.isEmpty()) {
            System.out.println("Nenhum usuário encontrado!");
            return;
        }

        System.out.printf("%-5s %-20s %-10s%n", "ID", "Nome", "Tipo");
        System.out.println("-------------------------------------");
        for (Object obj : users) {
            model.User u = (model.User) obj;
            System.out.printf("%-5d %-20s %-10s%n", u.getId(), u.getName(), u.getType());
        }
    }
}
