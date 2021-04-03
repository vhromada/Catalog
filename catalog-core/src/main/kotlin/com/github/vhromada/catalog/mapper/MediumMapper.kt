package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for medium.
 *
 * @author Vladimir Hromada
 */
@Component("mediumMapper")
class MediumMapper : Mapper<Medium, com.github.vhromada.catalog.domain.Medium> {

    override fun map(source: Medium): com.github.vhromada.catalog.domain.Medium {
        return com.github.vhromada.catalog.domain.Medium(
            id = source.id,
            number = source.number!!,
            length = source.length!!
        )
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Medium): Medium {
        return Medium(
            id = source.id,
            number = source.number,
            length = source.length
        )
    }

}
