package cz.vhromada.catalog.domain

import cz.vhromada.common.Movable
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents episode.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "episodes")
data class Episode(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "episode_generator", sequenceName = "episodes_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "episode_generator")
        override var id: Int?,

        /**
         * Number of episode
         */
        @Column(name = "episode_number")
        val number: Int,

        /**
         * Name
         */
        @Column(name = "episode_name")
        val name: String,

        /**
         * Length
         */
        @Column(name = "episode_length")
        val length: Int,

        /**
         * Note
         */
        val note: String?,

        /**
         * Position
         */
        override var position: Int?) : Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Episode || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
