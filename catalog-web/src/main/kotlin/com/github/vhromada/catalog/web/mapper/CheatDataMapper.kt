package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.web.fo.CheatDataFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for cheat's data.
 *
 * @author Vladimir Hromada
 */
@Component("webCheatDataMapper")
class CheatDataMapper : Mapper<CheatData, CheatDataFO> {

    override fun map(source: CheatData): CheatDataFO {
        return CheatDataFO(action = source.action, description = source.description)
    }

    override fun mapBack(source: CheatDataFO): CheatData {
        return CheatData(id = null, action = source.action, description = source.description)
    }

}
