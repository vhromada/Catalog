package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.domain.AuditEntity
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents cheat.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "cheats")
data class Cheat(

        /**
         * ID
         */
        @Id
        @SequenceGenerator(name = "cheat_generator", sequenceName = "cheats_sq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cheat_generator")
        override var id: Int?,

        /**
         * Setting for game
         */
        @Column(name = "game_setting")
        val gameSetting: String,

        /**
         * Setting for cheat
         */
        @Column(name = "cheat_setting")
        val cheatSetting: String,

        /**
         * Data
         */
        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
        @JoinTable(name = "cheat_cheat_data", joinColumns = [JoinColumn(name = "cheat")], inverseJoinColumns = [JoinColumn(name = "cheat_data")])
        @OrderBy("id")
        @Fetch(FetchMode.SELECT)
        val data: List<CheatData>,

        /**
         * Position
         */
        @Transient
        override var position: Int?,

        /**
         * Audit
         */
        @Embedded
        override var audit: Audit?) : AuditEntity(audit) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Cheat || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
