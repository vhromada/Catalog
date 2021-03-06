package com.github.vhromada.catalog.entity

import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.entity.Movable
import java.util.Objects

/**
 * A class represents movie.
 *
 * @author Vladimir Hromada
 */
data class Movie(
    /**
     * ID
     */
    override var id: Int?,

    /**
     * Czech name
     */
    val czechName: String?,

    /**
     * Original name
     */
    val originalName: String?,

    /**
     * Year
     */
    val year: Int?,

    /**
     * Language
     */
    val language: Language?,

    /**
     * Subtitles
     */
    val subtitles: List<Language?>?,

    /**
     * Media
     */
    val media: List<Medium?>?,

    /**
     * URL to ČSFD page about movie
     */
    val csfd: String?,

    /**
     * IMDB code
     */
    val imdbCode: Int?,

    /**
     * URL to english Wikipedia page about movie
     */
    val wikiEn: String?,

    /**
     * URL to czech Wikipedia page about movie
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
    override var position: Int?,

    /**
     * Genres
     */
    val genres: List<Genre?>?
) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Movie || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
