package cz.vhromada.catalog.domain

import cz.vhromada.common.domain.Audit
import cz.vhromada.common.domain.AuditEntity
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents genre.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "genres")
data class Genre(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "genre_generator", sequenceName = "genres_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_generator")
        override var id: Int?,

        /**
         * Name
         */
        @Column(name = "genre_name")
        val name: String,

        /**
         * Position
         */
        override var position: Int?,

        /**
         * Audit
         */
        override var audit: Audit?) : AuditEntity(audit) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Genre || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
