package com.github.vhromada.catalog.web.utils

import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.entity.Time
import org.assertj.core.api.SoftAssertions

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
        return TimeFO(
            hours = "1",
            minutes = "2",
            seconds = "3"
        )
    }

    /**
     * Asserts time deep equals.
     *
     * @param softly   soft assertions
     * @param expected expected time
     * @param actual   actual length
     */
    fun assertTimeDeepEquals(softly: SoftAssertions, expected: TimeFO?, actual: Int?) {
        if (expected == null) {
            softly.assertThat(actual).isNull()
        } else {
            val actualTime = Time(length = actual!!)

            softly.assertThat(actualTime.getData(dataType = Time.TimeData.HOUR)).isEqualTo(expected.hours!!.toInt())
            softly.assertThat(actualTime.getData(dataType = Time.TimeData.MINUTE)).isEqualTo(expected.minutes!!.toInt())
            softly.assertThat(actualTime.getData(dataType = Time.TimeData.SECOND)).isEqualTo(expected.seconds!!.toInt())
        }
    }

    /**
     * Asserts time deep equals.
     *
     * @param softly   soft assertions
     * @param expected expected length
     * @param actual   actual time
     */
    fun assertTimeDeepEquals(softly: SoftAssertions, expected: Int?, actual: TimeFO?) {
        if (expected == null) {
            softly.assertThat(actual).isNull()
        } else {
            val expectedTime = Time(length = expected)

            softly.assertThat(actual!!.hours).isEqualTo(expectedTime.getData(dataType = Time.TimeData.HOUR).toString())
            softly.assertThat(actual.minutes).isEqualTo(expectedTime.getData(dataType = Time.TimeData.MINUTE).toString())
            softly.assertThat(actual.seconds).isEqualTo(expectedTime.getData(dataType = Time.TimeData.SECOND).toString())
        }
    }

}
