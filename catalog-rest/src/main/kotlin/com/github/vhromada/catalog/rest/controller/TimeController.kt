package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.web.exception.InputException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * A class represents controller for time.
 *
 * @author Vladimir Hromada
 */
@RestController("timeController")
@RequestMapping("/catalog/time")
class TimeController {

    /**
     * Returns time.
     * <br></br>
     * Validation errors:
     *
     *  * Time is negative number
     *
     * @param time time
     * @return time
     */
    @GetMapping("/{time}")
    fun getTime(@PathVariable time: Int): String {
        if (time < 0) {
            throw InputException(key = "TIME_NEGATIVE", message = "Time mustn't be negative number.")
        }
        return Time(length = time).toString()
    }

}
