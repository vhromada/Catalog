package com.github.vhromada.catalog

import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.Optional

/**
 * A class represents Spring configuration for catalog.
 *
 * @author Vladimir Hromada
 */
@Configuration
@ComponentScan("com.github.vhromada.catalog")
@EnableAspectJAutoProxy
@EnableJpaRepositories("com.github.vhromada.catalog.repository")
@EntityScan("com.github.vhromada.catalog.domain")
@EnableJpaAuditing(auditorAwareRef = "userAuditor", dateTimeProviderRef = "dateTimeProvider")
class CatalogConfiguration {

    @Bean
    fun userAuditor(accountProvider: AccountProvider): AuditorAware<String> {
        return AuditorAware { Optional.ofNullable(accountProvider.getAccount().uuid) }
    }

    @Bean
    fun dateTimeProvider(timeProvider: TimeProvider): DateTimeProvider {
        return DateTimeProvider { Optional.of(timeProvider.getTime()) }
    }

}
