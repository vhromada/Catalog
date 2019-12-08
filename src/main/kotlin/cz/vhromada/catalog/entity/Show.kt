package cz.vhromada.catalog.entity

import cz.vhromada.common.Movable
import java.util.Objects

/**
 * A class represents show.
 *
 * @author Vladimir Hromada
 */
data class Show(

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
         * URL to ČSFD page about show
         */
        val csfd: String?,

        /**
         * IMDB code
         */
        val imdbCode: Int?,

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
        override var position: Int?,

        /**
         * Genres
         */
        val genres: List<Genre?>?) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Show || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
