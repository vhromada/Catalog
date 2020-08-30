package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.entity.Time
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for time.
 *
 * @author Vladimir Hromada
 */
object TimeUtils {

    /**
     * Returns FO for time.
     *
     * @return FO for time
     */
    fun getTimeFO(): TimeFO {
        return TimeFO(hours = "1",
                minutes = "2",
                seconds = "3")
    }

    /**
     * Asserts time deep equals.
     *
     * @param expected expected time
     * @param actual   actual length
     */
    fun assertTimeDeepEquals(expected: TimeFO?, actual: Int?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertTimeDeepEquals(expected!!, Time(actual!!))
    }

    /**
     * Asserts time deep equals.
     *
     * @param expected expected time
     * @param actual   actual length
     */
    private fun assertTimeDeepEquals(expected: TimeFO, actual: Time) {
        assertSoftly {
            it.assertThat(actual.getData(Time.TimeData.HOUR)).isEqualTo(Integer.parseInt(expected.hours))
            it.assertThat(actual.getData(Time.TimeData.MINUTE)).isEqualTo(Integer.parseInt(expected.minutes))
            it.assertThat(actual.getData(Time.TimeData.SECOND)).isEqualTo(Integer.parseInt(expected.seconds))
        }
    }

}
