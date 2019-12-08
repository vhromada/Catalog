package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Game
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameMapper")
class GameMapper : Mapper<Game, cz.vhromada.catalog.domain.Game> {

    override fun map(source: Game): cz.vhromada.catalog.domain.Game {
        return cz.vhromada.catalog.domain.Game(
                id = source.id,
                name = source.name!!,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!,
                crack = source.crack!!,
                serialKey = source.serialKey!!,
                patch = source.patch!!,
                trainer = source.trainer!!,
                trainerData = source.trainerData!!,
                editor = source.editor!!,
                saves = source.saves!!,
                otherData = source.otherData,
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Game): Game {
        return Game(
                id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount,
                crack = source.crack,
                serialKey = source.serialKey,
                patch = source.patch,
                trainer = source.trainer,
                trainerData = source.trainerData,
                editor = source.editor,
                saves = source.saves,
                otherData = source.otherData,
                note = source.note,
                position = source.position)
    }

}
