package view;

import dao.UserDAO;
import model.UserType;

public class AppContext {
    private static AppContext instance = new AppContext();
    private int usuarioLogado;
    private UserType user;
    private String route;

    private AppContext() {
        usuarioLogado = -1;
        user = null;
        route = "";
    }
    public static AppContext getInstance() {
        return instance;
    }

    public int getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(int u) { this.usuarioLogado = u; }

    public UserType getUser() { return user; }
    public void setUser(UserType u) { this.user = u; }

    public String getRoute() { return route; }
    public void setRoute(String r) { this.route = r; }

    public boolean isUserType(UserType userType) {
        if(userType ==null)return false;
        return userType.equals(this.user);
    }

    public String getUserName() {
        if(usuarioLogado == -1)return "Usuario Anonimo";
        UserDAO dao = new UserDAO();
        return dao.findById(usuarioLogado).get().getName();
    }
}