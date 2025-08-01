package tgbot.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import tgbot.model.Subcategory;
import tgbot.model.User;

import java.util.Properties;

public class HibernateConfig {

    private static Dotenv dotenv = Dotenv.load();

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, dotenv.get("DB_URL"));
                settings.put(Environment.USER, dotenv.get("POSTGRES_USER"));
                settings.put(Environment.PASS, dotenv.get("POSTGRES_PASSWORD"));
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Subcategory.class);
                configuration.addAnnotatedClass(User.class);

                StandardServiceRegistry  serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                System.out.println("⚙️ Инициализация HibernateConfig...");
                System.out.println("📦 URL: " + dotenv.get("DB_URL"));

                System.out.println("✅ SessionFactory initialized");
                System.out.println("🧪 Registered classes: " + sessionFactory.getMetamodel().getEntities());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sessionFactory;
    }


}
