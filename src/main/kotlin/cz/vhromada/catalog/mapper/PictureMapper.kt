package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Picture
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for picture.
 *
 * @author Vladimir Hromada
 */
@Component("pictureMapper")
class PictureMapper : Mapper<Picture, cz.vhromada.catalog.domain.Picture> {

    override fun map(source: Picture): cz.vhromada.catalog.domain.Picture {
        return cz.vhromada.catalog.domain.Picture(
                id = source.id,
                content = source.content!!.copyOf(),
                position = source.position)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Picture): Picture {
        return Picture(
                id = source.id,
                content = source.content.copyOf(),
                position = source.position)
    }

}
