package dao;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseDAOInterface<T, ID extends Serializable> {
    ID save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void update(T entity);
    void deleteById(ID id);
    void delete(T entity);
    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}