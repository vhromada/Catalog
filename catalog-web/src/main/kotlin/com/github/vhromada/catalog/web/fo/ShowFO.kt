package com.github.vhromada.catalog.web.fo

import com.github.vhromada.catalog.web.validator.constraints.Imdb
import com.github.vhromada.catalog.web.validator.constraints.ImdbCode
import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A class represents FO for show.
 *
 * @author Vladimir Hromada
 */
@Imdb
data class ShowFO(
    /**
     * ID
     */
    val id: Int?,

    /**
     * Czech name
     */
    @field:NotBlank
    val czechName: String?,

    /**
     * Original name
     */
    @field:NotBlank
    val originalName: String?,

    /**
     * URL to ČSFD page about show
     */
    val csfd: String?,

    /**
     * True if IMDB is selected
     */
    val imdb: Boolean,

    /**
     * IMDB code
     */
    @field:ImdbCode
    val imdbCode: String?,

    /**
     * URL to english Wikipedia page about show
     */
    val wikiEn: String?,

    /**
     * URL to czech Wikipedia page about show
     */
    val wikiCz: String?,

    /**
     * Picture's ID
     */
    val picture: Int?,

    /**
     * Note
     */
    val note: String?,

    /**
     * Position
     */
    val position: Int?,

    /**
     * Genres
     */
    @field:NotNull
    @field:Size(min = 1)
    val genres: List<Int?>?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is ShowFO || id == null) {
            false
        } else id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
