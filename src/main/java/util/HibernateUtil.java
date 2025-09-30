package util;

import model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml") // config file
                    .build();

            MetadataSources sources = new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Pedido.class)
                    .addAnnotatedClass(Mesa.class)
                    .addAnnotatedClass(Ingrediente.class)
                    .addAnnotatedClass(MenuItem.class)
                    .addAnnotatedClass(PedidoItem.class)
                    .addAnnotatedClass(Reserva.class);

            Metadata metadata = sources.getMetadataBuilder().build();

            return metadata.getSessionFactoryBuilder().build();

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
