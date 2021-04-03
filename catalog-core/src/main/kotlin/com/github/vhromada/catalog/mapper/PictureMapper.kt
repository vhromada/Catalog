package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for picture.
 *
 * @author Vladimir Hromada
 */
@Component("pictureMapper")
class PictureMapper : Mapper<Picture, com.github.vhromada.catalog.domain.Picture> {

    override fun map(source: Picture): com.github.vhromada.catalog.domain.Picture {
        return com.github.vhromada.catalog.domain.Picture(
            id = source.id,
            content = source.content!!.copyOf(),
            position = source.position
        )
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Picture): Picture {
        return Picture(
            id = source.id,
            content = source.content.copyOf(),
            position = source.position
        )
    }

}
