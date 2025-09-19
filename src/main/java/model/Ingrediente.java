package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Ingrediente")
//@Table(name = "ingrediente", uniqueConstraints = @UniqueConstraint(columnNames = "ingrediente"))
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ingrediente;

    @ManyToMany(mappedBy = "ingredientes")
    private List<MenuItem> pratos = new ArrayList<>();

    public Ingrediente(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public List<MenuItem> getPratos() {
        return pratos;
    }

    public void setPratos(List<MenuItem> pratos) {
        this.pratos = pratos;
    }
}
