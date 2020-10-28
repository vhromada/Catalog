package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.web.fo.SeasonFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("webSeasonMapper")
class SeasonMapper : Mapper<Season, SeasonFO> {

    override fun map(source: Season): SeasonFO {
        return SeasonFO(id = source.id,
                number = source.number.toString(),
                startYear = source.startYear.toString(),
                endYear = source.endYear.toString(),
                language = source.language,
                subtitles = source.subtitles!!.filterNotNull(),
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: SeasonFO): Season {
        return Season(id = source.id,
                number = source.number!!.toInt(),
                startYear = source.startYear!!.toInt(),
                endYear = source.endYear!!.toInt(),
                language = source.language,
                subtitles = source.subtitles,
                note = source.note,
                position = source.position)
    }

}
