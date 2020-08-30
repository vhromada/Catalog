package com.github.vhromada.catalog.web.mapper.impl

import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.catalog.web.mapper.TimeMapper
import com.github.vhromada.common.entity.Time
import org.springframework.stereotype.Component

/**
 * A class represents implementation of mapper for time.
 *
 * @author Vladimir Hromada
 */
@Component("webTimeMapper")
class TimeMapperImpl : TimeMapper {

    override fun map(source: Int): TimeFO {
        val time = Time(source)
        return TimeFO(hours = time.getData(Time.TimeData.HOUR).toString(),
                minutes = time.getData(Time.TimeData.MINUTE).toString(),
                seconds = time.getData(Time.TimeData.SECOND).toString())
    }

    override fun mapBack(source: TimeFO): Int {
        return Time(source.hours!!.toInt(), source.minutes!!.toInt(), source.seconds!!.toInt()).length
    }

}
