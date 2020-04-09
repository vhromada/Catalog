package cz.vhromada.catalog.domain

import cz.vhromada.common.domain.Audit
import cz.vhromada.common.domain.AuditEntity
import cz.vhromada.common.entity.Language
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents movie.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "movies")
data class Movie(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "movie_generator", sequenceName = "movies_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_generator")
        override var id: Int?,

        /**
         * Czech name
         */
        @Column(name = "czech_name")
        val czechName: String,

        /**
         * Original name
         */
        @Column(name = "original_name")
        val originalName: String,

        /**
         * Year
         */
        @Column(name = "movie_year")
        val year: Int,

        /**
         * Language
         */
        @Column(name = "movie_language")
        @Enumerated(EnumType.STRING)
        val language: Language,

        /**
         * Subtitles
         */
        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "movie_subtitles", joinColumns = [JoinColumn(name = "movie")])
        @Enumerated(EnumType.STRING)
        @Fetch(FetchMode.SELECT)
        val subtitles: List<Language>,

        /**
         * Media
         */
        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
        @JoinTable(name = "movie_media", joinColumns = [JoinColumn(name = "movie")], inverseJoinColumns = [JoinColumn(name = "medium")])
        @OrderBy("id")
        @Fetch(FetchMode.SELECT)
        val media: List<Medium>,

        /**
         * URL to ČSFD page about movie
         */
        val csfd: String?,

        /**
         * IMDB code
         */
        @Column(name = "imdb_code")
        val imdbCode: Int?,

        /**
         * URL to english Wikipedia page about movie
         */
        @Column(name = "wiki_en")
        val wikiEn: String?,

        /**
         * URL to czech Wikipedia page about movie
         */
        @Column(name = "wiki_cz")
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
        @OneToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "movie_genres", joinColumns = [JoinColumn(name = "movie")], inverseJoinColumns = [JoinColumn(name = "genre")])
        @OrderBy("id")
        @Fetch(FetchMode.SELECT)
        val genres: List<Genre>,

        /**
         * Audit
         */
        override var audit: Audit?) : AuditEntity(audit) {

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
