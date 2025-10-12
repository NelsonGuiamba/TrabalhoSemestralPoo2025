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
    public static int EMAILEXISTS = -1;
    public static int OTHERERROR = -2;
    public static int PASSWORDERROR = -3;
    public static int LOGINERROR = -4;
    private User user = null;
    private UserType userType = null;

    public int login(String email, String password) {
        for(User user : dao.findAll()) {
            if(user.getEmail().equals(email)) {
                if(user.getPassword().equals(password)) {
                    userType = user.getType();
                    return user.getId();
                }
                return PASSWORDERROR;
            }
        }
        return LOGINERROR;
    }

    public void logout() {
        user = null;
    }

    public int register(String name, String password, String email, UserType userType) {
        if (name.length() < 4) {
            System.out.println("Nome curto");
            return OTHERERROR;
        } else if (password.length() < 4) {
            System.out.println("Palavra passe curta");
            return OTHERERROR;
        }
        // Nem todos podem criar usuarios do tipo admin ou worke
        // Todos podem criar usuarios clientes
        if (user == null && userType != UserType.CLIENT) {
            System.out.println("Sem permissao para criar usuario ");
            return OTHERERROR;
        } else if (user != null && !user.getType().equals(UserType.ADMIN) && !userType.equals(UserType.CLIENT)) {
            System.out.println("Sem permissao para criar usuario");
            return OTHERERROR;
        }

        for(User user: dao.findAll()){
                System.out.println(user.getEmail() + " vs " + email);
            if(user.getEmail().equals(email)){
                return EMAILEXISTS;
            }
        }

        Integer id = dao.save(new User(name, email, password, userType));
        if(id == null) {
            return OTHERERROR;
        }
        return id;
    }

    public boolean removerUsuario(int id) {
        if (user == null || user.getType() != UserType.ADMIN)
            return false;

        dao.deleteById(id);
        return true;
    }

    public List<User> getAll() {
        if (user == null) {
            return new ArrayList<>();
        } else if (user.getType().equals(UserType.ADMIN)) {
            return dao.findAll();
        } else {
            return dao.findAll().stream()
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


    public UserType getUserType() {
        return userType;
    }
}
