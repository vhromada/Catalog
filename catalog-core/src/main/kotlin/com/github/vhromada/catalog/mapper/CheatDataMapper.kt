package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for cheat's data.
 *
 * @author Vladimir Hromada
 */
@Component("cheatDataMapper")
class CheatDataMapper : Mapper<CheatData, com.github.vhromada.catalog.domain.CheatData> {

    override fun map(source: CheatData): com.github.vhromada.catalog.domain.CheatData {
        return com.github.vhromada.catalog.domain.CheatData(
                id = source.id,
                action = source.action!!,
                description = source.description!!,
                position = source.position,
                audit = null)
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.CheatData): CheatData {
        return CheatData(
                id = source.id,
                action = source.action,
                description = source.description,
                position = source.position)
    }

}
