package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.entity.Movable
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
    override var position: Int?
) : Audit(), Movable {

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
