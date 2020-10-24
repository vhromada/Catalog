package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for program.
 *
 * @author Vladimir Hromada
 */
@Component("programMapper")
class ProgramMapper : Mapper<Program, com.github.vhromada.catalog.domain.Program> {

    override fun map(source: Program): com.github.vhromada.catalog.domain.Program {
        return com.github.vhromada.catalog.domain.Program(
                id = source.id,
                name = source.name!!,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!,
                format = source.format!!,
                crack = source.crack!!,
                serialKey = source.serialKey!!,
                otherData = source.otherData,
                note = source.note,
                position = source.position,
                audit = null)
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Program): Program {
        return Program(
                id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount,
                format = source.format,
                crack = source.crack,
                serialKey = source.serialKey,
                otherData = source.otherData,
                note = source.note,
                position = source.position)
    }

}
