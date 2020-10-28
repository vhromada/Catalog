package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.domain.Auditable
import java.util.Objects
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents cheat's data.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "cheat_data")
data class CheatData(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "cheat_data_generator", sequenceName = "cheat_data_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cheat_data_generator")
        var id: Int?,

        /**
         * Action
         */
        val action: String,

        /**
         * Description
         */
        val description: String,

        /**
         * Audit
         */
        @Embedded
        override var audit: Audit?) : Auditable {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is CheatData || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
