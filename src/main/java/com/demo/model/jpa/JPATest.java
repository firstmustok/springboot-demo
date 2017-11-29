package com.demo.model.jpa;

import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.demo.model.UserInfo;

public class JPATest
{
    private static final String         PERSISTENCE_UNIT_NAME = "demo";
    private static EntityManagerFactory factory;

    public static void main( String[] args )
    {
        // factory = Persistence.createEntityManagerFactory(
        // PERSISTENCE_UNIT_NAME );
        factory = getEntityManagerFactory().getNativeEntityManagerFactory();

        EntityManager em = factory.createEntityManager();

        // Create new user
        em.getTransaction().begin();
        UserInfo user = new UserInfo();
        user.setName( "Tom Johnson" );
        user.setPassword( "pass" );
        em.persist( user );
        em.getTransaction().commit();

        // Read the existing entries and write to console
        Query q = em.createQuery( "SELECT u FROM UserInfo u" );
        List<UserInfo> userList = q.getResultList();
        for ( UserInfo u : userList )
        {
            System.out.println( "########### record: " + u.getName() );
        }
        System.out.println( "Size: " + userList.size() );

        em.close();
    }

    public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter vend = new HibernateJpaVendorAdapter();
        vend.setGenerateDdl( true );

        vend.setDatabase( Database.POSTGRESQL );

        bean.setDataSource( dataSource() );
        bean.setJpaVendorAdapter( vend );
        bean.setPackagesToScan( "com" );
        bean.setPersistenceUnitName( "mercury-demo" );

        bean.setJpaProperties( getJpaProperties() );

        bean.afterPropertiesSet();
        return bean;
    }

    public static DataSource dataSource()
    {
        // Get the JDBC username and password.
        String usr = "mercury";
        String pwd = "password";
        String url = "jdbc:postgresql://localhost:5432/mercury";
        String driver = "org.postgresql.Driver";

        DataSourceBuilder factory = DataSourceBuilder
                .create()
                .username( usr )
                .password( pwd )
                .url( url )
                .driverClassName( driver );

        DataSource ds = factory.build();

        return ds;
    }

    // for hibernate
    private static Properties getJpaProperties()
    {
        // AvailableSettings.DIALECT,
        // AvailableSettings.IMPLICIT_NAMING_STRATEGY,
        // AvailableSettings.HBM2DDL_AUTO,
        // AvailableSettings.FORMAT_SQL,
        // AvailableSettings.SHOW_SQL,
        // AvailableSettings.GENERATE_STATISTICS,
        // AvailableSettings.USE_QUERY_CACHE,
        // AvailableSettings.USE_SECOND_LEVEL_CACHE,
        // AvailableSettings.CACHE_REGION_FACTORY

        Properties props = new Properties();

        props.put( AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect" );
        props.put( AvailableSettings.IMPLICIT_NAMING_STRATEGY, "jpa" );
        props.put( AvailableSettings.HBM2DDL_AUTO, "create" );
        props.put( AvailableSettings.FORMAT_SQL, true );
        props.put( AvailableSettings.SHOW_SQL, true );

        return props;
    }

}
