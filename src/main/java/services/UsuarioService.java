package services;

import dao.UserDAO;
import model.User;
import model.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {
    private final UserDAO dao = new UserDAO();
    private int retriesCount = 0;
    private User user = null;

    public boolean login(String name, String password) {
        if (user != null)
            return true;
        if (retriesCount < 5) {
            user = dao.authenticateUser(name, password);
            if (user == null) {
                retriesCount++;
                return false;
            } else {
                return true;
            }
        } else {
            System.out.println("Limite de tentativas execedido");
            return false;
        }
    }

    public void logout() {
        user = null;
    }

    public boolean register(String name, String password, UserType userType) {
        if (name.length() < 4) {
            System.out.println("Nome curto");
            return false;
        } else if (password.length() < 5) {
            System.out.println("Palavra passe curta");
            return false;
        }
        // Nem todos podem criar usuarios do tipo admin ou worke
        // Todos podem criar usuarios clientes
        if (user == null && userType != UserType.CLIENT) {
            System.out.println("Sem permissao para criar usuario ");
            return false;
        } else if (user != null && !user.getType().equals(UserType.ADMIN) && !userType.equals(UserType.CLIENT)) {
            System.out.println("Sem permissao para criar usuario");
            return false;
        }

        dao.addUser(new User(name, password, userType));

        return true;
    }

    public boolean removerUsuario(int id) {
        if (user == null || user.getType() != UserType.ADMIN)
            return false;

        dao.removeUser((long) id);
        return true;
    }

    public List<User> getAll() {
        if (user == null) {
            return new ArrayList<>();
        } else if (user.getType().equals(UserType.ADMIN)) {
            return dao.getUsers();
        } else {
            return dao.getUsers().stream()
                    .filter(u -> u.getType() == UserType.CLIENT)
                    .collect(Collectors.toList());
        }
    }

    public void pagar(){
        if(user == null){
            System.out.println("Primeiro deve logar");
            return;
        }
        if(!user.getType().equals(UserType.CLIENT)){
            System.out.println("Metodo so para clientes");
            return;
        }
        // Pedido p = dao.getPedidoByUser(user)
        // etc

    }


}
