package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.web.fo.CheatDataFO
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for cheat's data.
 *
 * @author Vladimir Hromada
 */
object CheatDataUtils {

    /**
     * Returns FO for cheat's data.
     *
     * @return FO for cheat's data
     */
    fun getCheatDataFO(): CheatDataFO {
        return CheatDataFO(action = "Action", description = "Description")
    }

    /**
     * Returns cheat's data.
     *
     * @return cheat's data
     */
    fun getCheatData(): CheatData {
        return CheatData(id = CatalogUtils.ID, action = "Action", description = "Description")
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertCheatDataDeepEquals(expected: List<CheatDataFO?>?, actual: List<CheatData?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        Assertions.assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDataDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected FO for cheat's data
     * @param actual   actual cheat's data
     */
    fun assertCheatDataDeepEquals(expected: CheatDataFO?, actual: CheatData?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.action).isEqualTo(expected!!.action)
            it.assertThat(actual.description).isEqualTo(expected.description)
        }
    }

}
