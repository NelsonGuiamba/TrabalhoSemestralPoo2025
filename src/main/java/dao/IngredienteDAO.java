package dao;
import model.Ingrediente;

import javax.persistence.criteria.CriteriaBuilder;

public class IngredienteDAO extends BaseDAO<Ingrediente, Integer> {
    public IngredienteDAO() {
        super(Ingrediente.class);
    }

}
