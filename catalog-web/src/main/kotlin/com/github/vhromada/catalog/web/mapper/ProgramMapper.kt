package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.web.fo.ProgramFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for programs.
 *
 * @author Vladimir Hromada
 */
@Component("webProgramMapper")
class ProgramMapper : Mapper<Program, ProgramFO> {

    override fun map(source: Program): ProgramFO {
        return ProgramFO(id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!.toString(),
                format = source.format,
                crack = source.crack,
                serialKey = source.serialKey,
                otherData = source.otherData,
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: ProgramFO): Program {
        return Program(id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!.toInt(),
                format = source.format,
                crack = source.crack,
                serialKey = source.serialKey,
                otherData = source.otherData,
                note = source.note,
                position = source.position)
    }

}
