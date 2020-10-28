package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.web.fo.CheatFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for cheats.
 *
 * @author Vladimir Hromada
 */
object CheatUtils {

    /**
     * Returns FO for cheat.
     *
     * @return FO for cheat
     */
    fun getCheatFO(): CheatFO {
        return CheatFO(id = CatalogUtils.ID,
                gameSetting = "Game setting",
                cheatSetting = "Cheat setting",
                data = listOf(CheatDataUtils.getCheatDataFO()),
                position = CatalogUtils.POSITION)
    }

    /**
     * Returns cheat.
     *
     * @return cheat
     */
    fun getCheat(): Cheat {
        return Cheat(id = CatalogUtils.ID,
                gameSetting = "Game setting",
                cheatSetting = "Cheat setting",
                data = listOf(CheatDataUtils.getCheatData()),
                position = CatalogUtils.POSITION)
    }

    /**
     * Asserts cheat deep equals.
     *
     * @param expected expected FO for cheat
     * @param actual   actual cheat
     */
    fun assertCheatDeepEquals(expected: CheatFO?, actual: Cheat?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.gameSetting).isEqualTo(expected.gameSetting)
            it.assertThat(actual.cheatSetting).isEqualTo(expected.cheatSetting)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        CheatDataUtils.assertCheatDataDeepEquals(expected!!.data, actual!!.data)
    }

}
