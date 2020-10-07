package com.github.vhromada.catalog.web

import com.github.vhromada.common.account.AccountConfiguration
import com.github.vhromada.common.account.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * A class represents Spring configuration for security.
 *
 * @author Vladimir Hromada
 */
@Configuration
@EnableWebSecurity
@Import(AccountConfiguration::class)
class CatalogSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var service: AccountService

    @Autowired
    private lateinit var encoder: PasswordEncoder

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(service).passwordEncoder(encoder)
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/login", "/login/error", "/registration").permitAll()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/logout").authenticated()
                .antMatchers("/accounts", "/accounts/", "/accounts/list").hasAnyRole("ADMIN")
                .anyRequest().hasAnyRole("ADMIN", "USER")
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login/error")
        http.exceptionHandling()
                .accessDeniedPage("/access-denied")
    }

}
