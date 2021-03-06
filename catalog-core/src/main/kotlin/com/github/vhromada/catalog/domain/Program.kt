package com.github.vhromada.catalog.domain

import com.github.vhromada.catalog.common.Format
import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.entity.Movable
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents program.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "programs")
data class Program(
    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "program_generator", sequenceName = "programs_sq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_generator")
    override var id: Int?,

    /**
     * Name
     */
    @Column(name = "program_name")
    val name: String,

    /**
     * URL to english Wikipedia page about program
     */
    @Column(name = "wiki_en")
    val wikiEn: String?,

    /**
     * URL to czech Wikipedia page about program
     */
    @Column(name = "wiki_cz")
    val wikiCz: String?,

    /**
     * Count of media
     */
    @Column(name = "media_count")
    val mediaCount: Int,

    /**
     * Format
     */
    @Enumerated(EnumType.STRING)
    val format: Format,

    /**
     * True if there is crack
     */
    val crack: Boolean,

    /**
     * True if there is serial key
     */
    @Column(name = "serial_key")
    val serialKey: Boolean,

    /**
     * Other data
     */
    @Column(name = "other_data")
    val otherData: String?,

    /**
     * Note
     */
    val note: String?,

    /**
     * Position
     */
    override var position: Int?
) : Audit(), Movable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Program || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
