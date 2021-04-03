package com.github.vhromada.catalog.web.fo

import java.io.Serializable
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A class represents FO for cheat.
 *
 * @author Vladimir Hromada
 */
data class CheatFO(
    /**
     * ID
     */
    val id: Int?,

    /**
     * Setting for game
     */
    val gameSetting: String?,

    /**
     * Setting for cheat
     */
    val cheatSetting: String?,

    /**
     * Data
     */
    @field:NotNull
    @field:Size(min = 1)
    @field:Valid
    var data: List<CheatDataFO?>?
) : Serializable
