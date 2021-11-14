package net.mbmedia.mHealth.backend.db;

import net.mbmedia.mHealth.backend.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.ParameterizedType;
import java.util.Properties;

public class BaseJPA<T>
{
    private static final Logger log = LoggerFactory.getLogger(BaseJPA.class);

    protected static EntityManagerFactory entityManagerFactory;

    public void setup()
    {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen())
        {
            entityManagerFactory = createEntityManagerFactory();
        }
    }

    public EntityManager getEntityManager()
    {
        setup();
        return entityManagerFactory.createEntityManager();
    }

    public void closeEntitiyManager(EntityManager em)
    {
        while (em.isOpen())
        {
            em.close();
        }
    }

    public EntityTransaction startTransaction(EntityManager em)
    {
        EntityTransaction et = em.getTransaction();
        et.begin();
        return et;
    }

    public EntityManagerFactory createEntityManagerFactory()
    {

        Configuration configuration = new Configuration();

        String user = configuration.load("jdbc.user");
        String pw = configuration.load("jdbc.pw");
        String jdbc_url = configuration.load("jdbc.url");


        //TODO: sql debug
        boolean show_sql = false;

        if (System.getProperty("jdbc.url") != null)
        {
            show_sql = false;
            jdbc_url = System.getProperty("jdbc.url");
        }

        Properties properties = new Properties();
        properties.put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        properties.put("hibernate.connection.username", user);
        properties.put("hibernate.connection.password", pw);
        properties.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.put("hibernate.connection.url", jdbc_url);
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.show_sql", show_sql);
        properties.put("hibernate.format_sql", "true");

        return Persistence.createEntityManagerFactory("persistence", properties);
    }

    public void removeAll()
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            System.out.println(getGenericName());
            em.createQuery("DELETE from " + getGenericName()).executeUpdate();
            et.commit();
        } catch (Exception e)
        {
            log.error(e.getMessage());
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    private String getGenericName()
    {
        return ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName();
    }

}
