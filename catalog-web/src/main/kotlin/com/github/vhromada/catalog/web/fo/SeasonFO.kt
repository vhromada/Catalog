package com.github.vhromada.catalog.web.fo

import com.github.vhromada.catalog.web.validator.constraints.DateRange
import com.github.vhromada.catalog.web.validator.constraints.Years
import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.utils.Constants
import org.hibernate.validator.constraints.Range
import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.NotNull

/**
 * A class represents FO for season.
 *
 * @author Vladimir Hromada
 */
@Years
data class SeasonFO(

        /**
         * ID
         */
        val id: Int?,

        /**
         * Number of season
         */
        @field:Range(min = 1, max = 100)
        val number: String?,

        /**
         * Starting year
         */
        @field:DateRange(Constants.MIN_YEAR)
        val startYear: String?,

        /**
         * Ending year
         */
        @field:DateRange(Constants.MIN_YEAR)
        val endYear: String?,

        /**
         * Language
         */
        @field:NotNull
        val language: Language?,

        /**
         * Subtitles
         */
        val subtitles: List<Language>?,

        /**
         * Note
         */
        val note: String?,

        /**
         * Position
         */
        val position: Int?) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is SeasonFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
