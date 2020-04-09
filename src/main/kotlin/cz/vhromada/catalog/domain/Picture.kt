package cz.vhromada.catalog.domain

import cz.vhromada.common.domain.Audit
import cz.vhromada.common.domain.AuditEntity
import java.util.Objects
import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents picture.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "pictures")
data class Picture(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "picture_generator", sequenceName = "pictures_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "picture_generator")
        override var id: Int?,

        /**
         * Picture
         */
        @Lob
        @Basic(fetch = FetchType.LAZY)
        val content: ByteArray,

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
        return if (other !is Picture || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
