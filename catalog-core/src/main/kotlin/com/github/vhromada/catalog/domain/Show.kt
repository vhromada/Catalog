package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.domain.AuditEntity
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
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
 * A class represents show.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "tv_shows")
data class Show(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "show_generator", sequenceName = "tv_shows_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "show_generator")
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
         * URL to ČSFD page about show
         */
        val csfd: String?,

        /**
         * IMDB code
         */
        @Column(name = "imdb_code")
        val imdbCode: Int?,

        /**
         * URL to english Wikipedia page about show
         */
        @Column(name = "wiki_en")
        val wikiEn: String?,

        /**
         * URL to czech Wikipedia page about show
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
        @JoinTable(name = "tv_show_genres", joinColumns = [JoinColumn(name = "tv_show")], inverseJoinColumns = [JoinColumn(name = "genre")])
        @Fetch(FetchMode.SELECT)
        val genres: List<Genre>,

        /**
         * Seasons
         */
        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
        @JoinColumn(name = "tv_show", referencedColumnName = "id")
        @OrderBy("position, id")
        @Fetch(FetchMode.SELECT)
        val seasons: List<Season>,

        /**
         * Audit
         */
        override var audit: Audit?) : AuditEntity(audit) {

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
