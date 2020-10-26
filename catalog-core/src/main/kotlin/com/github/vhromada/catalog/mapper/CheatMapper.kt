package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for cheat.
 *
 * @author Vladimir Hromada
 */
@Component("cheatMapper")
class CheatMapper(private val cheatDataMapper: CheatDataMapper) : Mapper<Cheat, com.github.vhromada.catalog.domain.Cheat> {

    override fun map(source: Cheat): com.github.vhromada.catalog.domain.Cheat {
        return com.github.vhromada.catalog.domain.Cheat(
                id = source.id,
                gameSetting = source.gameSetting!!,
                cheatSetting = source.cheatSetting!!,
                data = cheatDataMapper.map(source.data!!.filterNotNull()),
                position = source.position,
                audit = null)
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Cheat): Cheat {
        return Cheat(
                id = source.id,
                gameSetting = source.gameSetting,
                cheatSetting = source.cheatSetting,
                data = cheatDataMapper.mapBack(source.data),
                position = source.position)
    }

}
