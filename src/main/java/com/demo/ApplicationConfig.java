package com.demo;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
// @EnableJpaRepositories( basePackages = "com",
// repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class )
public class ApplicationConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ApplicationConfig.class );

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource()
    {
        // Get the JDBC username and password.
        String usr = env.getProperty( "spring.datasource.username" );
        String pwd = env.getProperty( "spring.datasource.password" );
        String url = env.getProperty( "spring.datasource.url" );
        String driver = env.getProperty( "spring.datasource.driverClassName" );

        DataSourceBuilder factory = DataSourceBuilder
                .create()
                .username( usr )
                .password( pwd )
                .url( url )
                .driverClassName( driver );

        DataSource ds = factory.build();

        // For debugging.
        LOGGER.info( "DataSource class is {}", ds.getClass().getName() );

        return ds;
    }

    private static final String[] HIBERNATE_PROP_NAMES = {
            AvailableSettings.DIALECT,
            AvailableSettings.IMPLICIT_NAMING_STRATEGY,
            AvailableSettings.HBM2DDL_AUTO,
            AvailableSettings.FORMAT_SQL,
            AvailableSettings.SHOW_SQL,
            // AvailableSettings.GENERATE_STATISTICS,
            // AvailableSettings.USE_QUERY_CACHE,
            // AvailableSettings.USE_SECOND_LEVEL_CACHE,
            // AvailableSettings.CACHE_REGION_FACTORY
    };

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter vend = new HibernateJpaVendorAdapter();
        vend.setGenerateDdl( true );

        String dialect = env.getRequiredProperty( "hibernate.dialect" );
        if ( dialect.contains( "PostgreSQL" ) )
        {
            vend.setDatabase( Database.POSTGRESQL );
        }
        else if ( dialect.contains( "H2" ) )
        {
            vend.setDatabase( Database.H2 );
        }

        bean.setDataSource( dataSource() );
        bean.setJpaVendorAdapter( vend );
        bean.setPackagesToScan( "com.demo" );
        bean.setPersistenceUnitName( "mercury-demo" );
        bean.setJpaProperties( getJpaProperties( env, HIBERNATE_PROP_NAMES ) );

        bean.afterPropertiesSet();
        return bean;
    }

    private static Properties getJpaProperties( Environment env, String... requiredNames )
    {
        Properties props = new Properties();
        for ( String name : requiredNames )
        {
            String value = env.getRequiredProperty( name );
            LOGGER.info( "Found property:'{}' value:'{}'", name, value );
            props.put( name, value );
        }
        return props;
    }

    @Bean
    public JpaTransactionManager
        transactionManager( LocalContainerEntityManagerFactoryBean factory )
    {
        return new JpaTransactionManager( factory.getObject() );
    }
}
