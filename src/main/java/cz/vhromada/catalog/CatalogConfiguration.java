package cz.vhromada.catalog;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import cz.vhromada.converters.dozer.DozerConverter;

import net.sf.ehcache.CacheManager;
import org.dozer.DozerBeanMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author vladimir.hromada
 */
@Configuration
@ComponentScan("cz.vhromada.catalog")
@EnableAspectJAutoProxy
@EnableCaching
@EnableJpaRepositories("cz.vhromada.catalog.repository")
public class CatalogConfiguration {

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
        ClassPathResource classPathResource = new ClassPathResource("ehcache.xml");

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

    @Bean
    public DozerBeanMapper catalogMapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public DozerConverter catalogDozerConverter(final DozerBeanMapper catalogMapper) {
        return new DozerConverter(catalogMapper);
    }

}
