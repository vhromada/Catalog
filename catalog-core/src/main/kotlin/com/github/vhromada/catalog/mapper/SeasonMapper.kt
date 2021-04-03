package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonMapper")
class SeasonMapper : Mapper<Season, com.github.vhromada.catalog.domain.Season> {

    override fun map(source: Season): com.github.vhromada.catalog.domain.Season {
        return com.github.vhromada.catalog.domain.Season(
            id = source.id,
            number = source.number!!,
            startYear = source.startYear!!,
            endYear = source.endYear!!,
            language = source.language!!,
            subtitles = source.subtitles!!.filterNotNull(),
            note = source.note,
            position = source.position,
            episodes = mutableListOf()
        )
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Season): Season {
        return Season(
            id = source.id,
            number = source.number,
            startYear = source.startYear,
            endYear = source.endYear,
            language = source.language,
            subtitles = source.subtitles,
            note = source.note,
            position = source.position
        )
    }

}
