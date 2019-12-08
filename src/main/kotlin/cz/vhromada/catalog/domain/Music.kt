package cz.vhromada.catalog.domain

import cz.vhromada.common.Movable
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
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents music.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "music")
data class Music(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "music_generator", sequenceName = "music_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_generator")
        override var id: Int?,

        /**
         * Name
         */
        @Column(name = "music_name")
        val name: String,

        /**
         * URL to english Wikipedia page about music
         */
        @Column(name = "wiki_en")
        val wikiEn: String?,

        /**
         * URL to czech Wikipedia page about music
         */
        @Column(name = "wiki_cz")
        val wikiCz: String?,

        /**
         * Count of media
         */
        @Column(name = "media_count")
        val mediaCount: Int,

        /**
         * Note
         */
        val note: String?,

        /**
         * Position
         */
        override var position: Int?,

        /**
         * Songs
         */
        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
        @JoinColumn(name = "music", referencedColumnName = "id")
        @OrderBy("position, id")
        @Fetch(FetchMode.SELECT)
        val songs: List<Song>) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Music || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
