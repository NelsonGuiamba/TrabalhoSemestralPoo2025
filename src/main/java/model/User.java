package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Usuarios")
@Table(name = "Usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserType type;
    @OneToMany(mappedBy = "client")
    private List<Pedido> pedidosFeitos = new ArrayList<>();

    @OneToMany(mappedBy = "worker")
    private List<Pedido> pedidosAtendidos = new ArrayList<>();

    public User() {

    }

    public User(String name, String password, UserType type) {
        this.name = name;
        this.password = password;
        this.type = type;
        this.email = name + "@domain.com";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
