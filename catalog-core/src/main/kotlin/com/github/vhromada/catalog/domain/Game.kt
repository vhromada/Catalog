package com.github.vhromada.catalog.domain

import com.github.vhromada.catalog.common.Format
import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.entity.Movable
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents game.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "games")
data class Game(
    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "game_generator", sequenceName = "games_sq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_generator")
    override var id: Int?,

    /**
     * Name
     */
    @Column(name = "game_name")
    val name: String,

    /**
     * URL to english Wikipedia page about game
     */
    @Column(name = "wiki_en")
    val wikiEn: String?,

    /**
     * URL to czech Wikipedia page about game
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
     * Cheat
     */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "cheat")
    var cheat: Cheat?,

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
     * True if there is patch
     */
    val patch: Boolean,

    /**
     * True if there is trainer
     */
    val trainer: Boolean,

    /**
     * True if there is data for trainer
     */
    @Column(name = "trainer_data")
    val trainerData: Boolean,

    /**
     * True if there is editor
     */
    @Column(name = "editor")
    val editor: Boolean,

    /**
     * True if there are saves
     */
    val saves: Boolean,

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
        return if (other !is Game || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
