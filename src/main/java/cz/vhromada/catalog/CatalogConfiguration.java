package cz.vhromada.catalog;

import cz.vhromada.converter.orika.OrikaConfiguration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * A class represents Spring configuration.
 *
 * @author Vladimir Hromada
 */
@Configuration
@ComponentScan("cz.vhromada.catalog")
@EnableAspectJAutoProxy
@EnableCaching
@EnableJpaRepositories("cz.vhromada.catalog.repository")
@EntityScan("cz.vhromada.catalog.domain")
@Import(OrikaConfiguration.class)
public class CatalogConfiguration {
}
