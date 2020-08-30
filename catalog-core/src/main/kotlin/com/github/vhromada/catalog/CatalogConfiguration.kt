package com.github.vhromada.catalog

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * A class represents Spring configuration for catalog.
 *
 * @author Vladimir Hromada
 */
@Configuration
@ComponentScan("com.github.vhromada.catalog")
@EnableAspectJAutoProxy
@EnableCaching
@EnableJpaRepositories("com.github.vhromada.catalog.repository")
@EntityScan("com.github.vhromada.catalog.domain")
class CatalogConfiguration
