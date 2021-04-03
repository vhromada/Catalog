package com.github.vhromada.catalog.rest

import com.github.vhromada.catalog.CatalogConfiguration
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.provider.UuidProvider
import com.github.vhromada.common.web.WebConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.time.LocalDateTime
import java.util.UUID

/**
 * A class represents Spring configuration for catalog.
 *
 * @author Vladimir Hromada
 */
@Configuration
@Import(WebConfiguration::class, CatalogConfiguration::class)
@EnableOpenApi
class CatalogRestConfiguration : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun accountProvider(): AccountProvider {
        return object : AccountProvider {

            override fun getAccount(): Account {
                return SecurityContextHolder.getContext().authentication.principal as Account
            }

        }
    }

    @Bean
    fun timeProvider(): TimeProvider {
        return object : TimeProvider {

            override fun getTime(): LocalDateTime {
                return LocalDateTime.now()
            }

        }
    }

    @Bean
    fun uuidProvider(): UuidProvider {
        return object : UuidProvider {

            override fun getUuid(): String {
                return UUID.randomUUID().toString()
            }

        }
    }

    @Bean
    fun applicationApi(): Docket {
        val info = ApiInfoBuilder()
            .title("Catalog")
            .description("Catalog of movies, games, music and programs ")
            .version("1.0.0")
            .build()
        return Docket(DocumentationType.OAS_30)
            .groupName("catalog")
            .apiInfo(info)
            .select()
            .paths(PathSelectors.regex("/catalog.*"))
            .build()
    }

}
