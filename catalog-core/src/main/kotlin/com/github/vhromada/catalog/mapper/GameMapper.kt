package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameMapper")
class GameMapper : Mapper<Game, com.github.vhromada.catalog.domain.Game> {

    override fun map(source: Game): com.github.vhromada.catalog.domain.Game {
        return com.github.vhromada.catalog.domain.Game(
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
                position = source.position,
                audit = null)
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Game): Game {
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
