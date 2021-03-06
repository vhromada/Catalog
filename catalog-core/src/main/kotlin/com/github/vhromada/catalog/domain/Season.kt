package com.github.vhromada.catalog.domain

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.entity.Movable
import com.github.vhromada.common.utils.sorted
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator
import javax.persistence.Table

/**
 * A class represents season.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "seasons")
data class Season(
    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "season_generator", sequenceName = "seasons_sq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "season_generator")
    override var id: Int?,

    /**
     * Number of season
     */
    @Column(name = "season_number")
    val number: Int,

    /**
     * Starting year
     */
    @Column(name = "start_year")
    val startYear: Int,

    /**
     * Ending year
     */
    @Column(name = "end_year")
    val endYear: Int,

    /**
     * Language
     */
    @Column(name = "season_language")
    @Enumerated(EnumType.STRING)
    val language: Language,

    /**
     * Subtitles
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "season_subtitles", joinColumns = [JoinColumn(name = "season")])
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SELECT)
    val subtitles: List<Language>,

    /**
     * Note
     */
    val note: String?,

    /**
     * Position
     */
    override var position: Int?,

    /**
     * Episodes
     */
    @OneToMany(mappedBy = "season", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("position, id")
    @Fetch(FetchMode.SELECT)
    val episodes: MutableList<Episode>
) : Audit(), Movable {

    /**
     * Show
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_show")
    var show: Show? = null

    override fun updatePosition(position: Int) {
        super.updatePosition(position)

        for (i in episodes.sorted().indices) {
            episodes[i].updatePosition(i)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is Season || id == null) {
            false
        } else {
            id == other.id
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}
