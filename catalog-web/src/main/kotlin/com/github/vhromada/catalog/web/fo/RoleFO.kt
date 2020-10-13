package com.github.vhromada.catalog.web.fo

import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A class represents FO for role.
 *
 * @author Vladimir Hromada
 */
data class RoleFO(
        /**
         * Roles
         */
        @field:NotNull
        @field:Size(min = 1)
        val roles: List<String>?) : Serializable