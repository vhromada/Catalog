package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.web.fo.GameFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for games.
 *
 * @author Vladimir Hromada
 */
object GameUtils {

    /**
     * Returns FO for game.
     *
     * @return FO for game
     */
    fun getGameFO(): GameFO {
        return GameFO(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                mediaCount = CatalogUtils.MEDIA.toString(),
                crack = true,
                serialKey = true,
                patch = true,
                trainer = true,
                trainerData = true,
                editor = true,
                saves = true,
                otherData = "Other data",
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Returns game.
     *
     * @return game
     */
    fun getGame(): Game {
        return Game(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                mediaCount = CatalogUtils.MEDIA,
                crack = true,
                serialKey = true,
                patch = true,
                trainer = true,
                trainerData = true,
                editor = true,
                saves = true,
                otherData = "Other data",
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION)
    }

    /**
     * Asserts game deep equals.
     *
     * @param expected expected FO for game
     * @param actual   actual game
     */
    fun assertGameDeepEquals(expected: GameFO?, actual: Game?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount?.toInt())
            it.assertThat(actual.crack).isEqualTo(expected.crack)
            it.assertThat(actual.serialKey).isEqualTo(expected.serialKey)
            it.assertThat(actual.patch).isEqualTo(expected.patch)
            it.assertThat(actual.trainer).isEqualTo(expected.trainer)
            it.assertThat(actual.trainerData).isEqualTo(expected.trainerData)
            it.assertThat(actual.editor).isEqualTo(expected.editor)
            it.assertThat(actual.saves).isEqualTo(expected.saves)
            it.assertThat(actual.otherData).isEqualTo(expected.otherData)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
