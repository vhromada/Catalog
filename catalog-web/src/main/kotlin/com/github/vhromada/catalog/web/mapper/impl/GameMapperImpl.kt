package com.github.vhromada.catalog.web.mapper.impl

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.web.fo.GameFO
import com.github.vhromada.catalog.web.mapper.GameMapper
import org.springframework.stereotype.Component

/**
 * A class represents implementation of mapper for games.
 *
 * @author Vladimir Hromada
 */
@Component("webGameMapper")
class GameMapperImpl : GameMapper {

    override fun map(source: Game): GameFO {
        return GameFO(id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!.toString(),
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

    override fun mapBack(source: GameFO): Game {
        return Game(id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!.toInt(),
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
