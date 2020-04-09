package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Season
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonMapper")
class SeasonMapper : Mapper<Season, cz.vhromada.catalog.domain.Season> {

    override fun map(source: Season): cz.vhromada.catalog.domain.Season {
        return cz.vhromada.catalog.domain.Season(
                id = source.id,
                number = source.number!!,
                startYear = source.startYear!!,
                endYear = source.endYear!!,
                language = source.language!!,
                subtitles = source.subtitles!!.filterNotNull(),
                note = source.note,
                position = source.position,
                episodes = emptyList(),
                audit = null)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Season): Season {
        return Season(
                id = source.id,
                number = source.number,
                startYear = source.startYear,
                endYear = source.endYear,
                language = source.language,
                subtitles = source.subtitles,
                note = source.note,
                position = source.position)
    }

}
