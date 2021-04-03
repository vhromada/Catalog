package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents medium.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "media")
data class Medium(
    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "medium_generator", sequenceName = "media_sq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medium_generator")
    var id: Int?,

    /**
     * Number
     */
    @Column(name = "medium_number")
    val number: Int,

    /**
     * Length
     */
    @Column(name = "medium_length")
    val length: Int
) : Audit() {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Medium || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
