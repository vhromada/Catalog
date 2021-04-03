package com.github.vhromada.catalog.utils

import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.utils.Constants
import java.time.LocalDateTime

/**
 * A class represents constants for tests.
 *
 * @author Vladimir Hromada
 */
object TestConstants {

    /**
     * Bad minimal year
     */
    const val BAD_MIN_YEAR = Constants.MIN_YEAR - 1

    /**
     * Bad maximal year
     */
    val BAD_MAX_YEAR = Constants.CURRENT_YEAR + 1

    /**
     * Bad minimum IMDB code
     */
    const val BAD_MIN_IMDB_CODE = -2

    /**
     * Bad maximum IMDB code
     */
    const val BAD_MAX_IMDB_CODE = Constants.MAX_IMDB_CODE + 1

    /**
     * Time
     */
    val TIME: LocalDateTime = LocalDateTime.of(2000, 2, 4, 10, 45, 55, 70)

    /**
     * Account
     */
    val ACCOUNT = Account(id = 10, uuid = "d53b2577-a3de-4df7-a8dd-2e6d9e5c1014", username = "", password = "", roles = listOf("ROLE_USER"))

    /**
     * Admin
     */
    val ADMIN = ACCOUNT.copy(roles = listOf("ROLE_ADMIN"))

    /**
     * Result for invalid data
     */
    val INVALID_DATA_RESULT = Result.error<Unit>(key = "DATA_INVALID", message = "Data must be valid.")

    /**
     * Event for invalid movie year
     */
    val INVALID_MOVIE_YEAR_EVENT = Event(severity = Severity.ERROR, key = "MOVIE_YEAR_NOT_VALID", message = "Year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}.")

    /**
     * Event for invalid movie IMDB code
     */
    val INVALID_MOVIE_IMDB_CODE_EVENT = Event(severity = Severity.ERROR, key = "MOVIE_IMDB_CODE_NOT_VALID", message = "IMDB code must be between 1 and 9999999 or -1.")

    /**
     * Event for invalid movie IMDB code
     */
    val INVALID_SHOW_IMDB_CODE_EVENT = Event(severity = Severity.ERROR, key = "SHOW_IMDB_CODE_NOT_VALID", message = "IMDB code must be between 1 and 9999999 or -1.")

    /**
     * Event for invalid starting year
     */
    val INVALID_STARTING_YEAR_EVENT = Event(
        severity = Severity.ERROR,
        key = "SEASON_START_YEAR_NOT_VALID",
        message = "Starting year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."
    )

    /**
     * Event for invalid ending year
     */
    val INVALID_ENDING_YEAR_EVENT = Event(
        severity = Severity.ERROR,
        key = "SEASON_END_YEAR_NOT_VALID",
        message = "Ending year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."
    )

}
