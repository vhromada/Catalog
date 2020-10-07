package com.github.vhromada.catalog.web.fo

import java.io.Serializable
import javax.validation.constraints.NotBlank

/**
 * A class represents FO for account.
 *
 * @author Vladimir Hromada
 */
data class AccountFO(

        /**
         * Username
         */
        @field:NotBlank
        val username: String?,

        /**
         * Password
         */
        @field:NotBlank
        val password: String?) : Serializable
