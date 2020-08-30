package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.domain.AuditEntity
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents song.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "songs")
data class Song(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "song_generator", sequenceName = "songs_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "song_generator")
        override var id: Int?,

        /**
         * Name
         */
        @Column(name = "song_name")
        val name: String,

        /**
         * Length
         */
        @Column(name = "song_length")
        val length: Int,

        /**
         * Note
         */
        val note: String?,

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
        return if (other !is Song || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
