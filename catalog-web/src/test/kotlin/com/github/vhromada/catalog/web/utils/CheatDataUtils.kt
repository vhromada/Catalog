package com.github.vhromada.catalog.web.utils

import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.web.fo.CheatDataFO
import org.assertj.core.api.Assertions.assertThat
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
        return CheatData(id = TestConstants.ID, action = "Action", description = "Description")
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertCheatDataDeepEquals(expected: List<CheatDataFO?>?, actual: List<CheatData?>?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(expected.size).isEqualTo(actual!!.size)
            if (expected.isNotEmpty()) {
                for (i in expected.indices) {
                    assertCheatDataDeepEquals(expected = expected[i], actual = actual[i])
                }
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
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertSoftly {
                it.assertThat(actual!!.id).isNull()
                it.assertThat(actual.action).isEqualTo(expected.action)
                it.assertThat(actual.description).isEqualTo(expected.description)
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertCheatDataListDeepEquals(expected: List<CheatData?>?, actual: List<CheatDataFO?>?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(expected.size).isEqualTo(actual!!.size)
            if (expected.isNotEmpty()) {
                for (i in expected.indices) {
                    assertCheatDataDeepEquals(expected = expected[i], actual = actual[i])
                }
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected FO for cheat's data
     * @param actual   actual cheat's data
     */
    fun assertCheatDataDeepEquals(expected: CheatData?, actual: CheatDataFO?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertSoftly {
                it.assertThat(actual!!.action).isEqualTo(expected.action)
                it.assertThat(actual.description).isEqualTo(expected.description)
            }
        }
    }

}
