package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.web.fo.CheatDataFO
import com.github.vhromada.catalog.web.fo.CheatFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for cheats.
 *
 * @author Vladimir Hromada
 */
@Component("webCheatMapper")
class CheatMapper(private val cheatDataMapper: Mapper<CheatData, CheatDataFO>) : Mapper<Cheat, CheatFO> {

    override fun map(source: Cheat): CheatFO {
        return CheatFO(id = source.id,
                gameSetting = source.gameSetting,
                cheatSetting = source.cheatSetting,
                data = cheatDataMapper.map(source.data!!.filterNotNull()),
                position = source.position)
    }

    override fun mapBack(source: CheatFO): Cheat {
        return Cheat(id = source.id,
                gameSetting = source.gameSetting,
                cheatSetting = source.cheatSetting,
                data = cheatDataMapper.mapBack(source.data!!.filterNotNull()),
                position = source.position)
    }

}
