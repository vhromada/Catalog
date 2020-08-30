package com.github.vhromada.catalog.rest

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * A class represents Spring boot application.
 *
 * @author Vladimir Hromada
 */
@SpringBootApplication
class RestApplication

fun main(args: Array<String>) {
    SpringApplication.run(RestApplication::class.java, *args)
}
