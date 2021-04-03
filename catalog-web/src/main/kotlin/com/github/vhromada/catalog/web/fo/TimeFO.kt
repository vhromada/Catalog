package com.github.vhromada.catalog.web.fo

import com.github.vhromada.catalog.web.validator.constraints.Time
import org.hibernate.validator.constraints.Range
import java.io.Serializable

/**
 * A class represents FO for time.
 *
 * @author Vladimir Hromada
 */
@Time
data class TimeFO(
    /**
     * Hours
     */
    @field:Range(min = 0, max = 23)
    var hours: String?,

    /**
     * Minutes
     */
    @field:Range(min = 0, max = 59)
    var minutes: String?,

    /**
     * Seconds
     */
    @field:Range(min = 0, max = 59)
    var seconds: String?
) : Serializable {

    constructor() : this(hours = null, minutes = null, seconds = null)

}
