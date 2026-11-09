package util;

import Model.Product;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory= buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try {
            return new Configuration().configure().addAnnotatedClass(Product.class).buildSessionFactory();
        }catch (Throwable ex){
            System.err.println("Session Factory creation failed");
            throw new  ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory(){
        return  sessionFactory;
    }
    public static void shutdown(){
        getSessionFactory().close();
    }
}
