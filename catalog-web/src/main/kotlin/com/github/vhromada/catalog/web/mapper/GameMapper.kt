package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.web.fo.GameFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for games.
 *
 * @author Vladimir Hromada
 */
@Component("webGameMapper")
class GameMapper : Mapper<Game, GameFO> {

    override fun map(source: Game): GameFO {
        return GameFO(
            id = source.id,
            name = source.name,
            wikiEn = source.wikiEn,
            wikiCz = source.wikiCz,
            mediaCount = source.mediaCount!!.toString(),
            format = source.format,
            crack = source.crack,
            serialKey = source.serialKey,
            patch = source.patch,
            trainer = source.trainer,
            trainerData = source.trainerData,
            editor = source.editor,
            saves = source.saves,
            otherData = source.otherData,
            note = source.note,
            position = source.position
        )
    }

    override fun mapBack(source: GameFO): Game {
        return Game(
            id = source.id,
            name = source.name,
            wikiEn = source.wikiEn,
            wikiCz = source.wikiCz,
            mediaCount = source.mediaCount!!.toInt(),
            format = source.format,
            crack = source.crack,
            serialKey = source.serialKey,
            patch = source.patch,
            trainer = source.trainer,
            trainerData = source.trainerData,
            editor = source.editor,
            saves = source.saves,
            otherData = source.otherData,
            note = source.note,
            position = source.position
        )
    }

}
