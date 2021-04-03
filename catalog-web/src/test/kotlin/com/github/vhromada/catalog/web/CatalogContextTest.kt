package com.github.vhromada.catalog.web

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
class CatalogContextTest {

    @Autowired
    private lateinit var context: ApplicationContext

    @Test
    fun app() {
        KotlinLogging.logger {}.info { "Spring context test - OK [beans=${context.beanDefinitionNames.size}]" }
    }

}
