package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            String config = HibernateUtil.class.getClassLoader().getResource("hibernate.local.cfg.xml") == null
                    ? "hibernate.cfg.xml" : "hibernate.local.cfg.xml";
            sessionFactory = new Configuration().configure(config).buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
