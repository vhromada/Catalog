package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.entity.Movable
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
    override var position: Int?
) : Audit(), Movable {

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
