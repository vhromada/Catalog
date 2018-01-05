package cz.vhromada.catalog;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents Spring configuration for tests.
 *
 * @author Vladimir Hromada
 */
@Configuration
@Import(CatalogConfiguration.class)
public class CatalogTestConfiguration {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("catalog.sql", "data.sql")
                .build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(final DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("cz.vhromada.catalog.domain");
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    @Bean
    public SharedEntityManagerBean containerManagedEntityManager(final EntityManagerFactory entityManagerFactory) {
        final SharedEntityManagerBean entityManagerBean = new SharedEntityManagerBean();
        entityManagerBean.setEntityManagerFactory(entityManagerFactory);

        return entityManagerBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final DataSource dataSource) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory(dataSource));

        return transactionManager;
    }

    @Bean
    public CacheManager cacheManagerFactory() {
        final ClassPathResource classPathResource = new ClassPathResource("ehcache.xml");

        final EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(classPathResource);
        cacheManagerFactoryBean.setShared(true);
        cacheManagerFactoryBean.afterPropertiesSet();

        return cacheManagerFactoryBean.getObject();
    }

    @Bean
    public EhCacheCacheManager cacheManager(final CacheManager cacheManagerFactory) {
        final EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(cacheManagerFactory);

        return cacheManager;
    }

}
