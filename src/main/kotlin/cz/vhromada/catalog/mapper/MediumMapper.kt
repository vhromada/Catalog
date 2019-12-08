package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Medium
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for medium.
 *
 * @author Vladimir Hromada
 */
@Component("mediumMapper")
class MediumMapper : Mapper<Medium, cz.vhromada.catalog.domain.Medium> {

    override fun map(source: Medium): cz.vhromada.catalog.domain.Medium {
        return cz.vhromada.catalog.domain.Medium(
                id = source.id,
                number = source.number!!,
                length = source.length!!)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Medium): Medium {
        return Medium(
                id = source.id,
                number = source.number,
                length = source.length)
    }

}
