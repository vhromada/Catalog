package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for time.
 *
 * @author Vladimir Hromada
 */
@Component("webTimeMapper")
class TimeMapper : Mapper<Int, TimeFO> {

    override fun map(source: Int): TimeFO {
        val time = Time(length = source)
        return TimeFO(
            hours = time.getData(dataType = Time.TimeData.HOUR).toString(),
            minutes = time.getData(dataType = Time.TimeData.MINUTE).toString(),
            seconds = time.getData(dataType = Time.TimeData.SECOND).toString()
        )
    }

    override fun mapBack(source: TimeFO): Int {
        return Time(hours = source.hours!!.toInt(), minutes = source.minutes!!.toInt(), seconds = source.seconds!!.toInt()).length
    }

}
