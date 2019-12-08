package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Program
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for program.
 *
 * @author Vladimir Hromada
 */
@Component("programMapper")
class ProgramMapper : Mapper<Program, cz.vhromada.catalog.domain.Program> {

    override fun map(source: Program): cz.vhromada.catalog.domain.Program {
        return cz.vhromada.catalog.domain.Program(
                id = source.id,
                name = source.name!!,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!,
                crack = source.crack!!,
                serialKey = source.serialKey!!,
                otherData = source.otherData,
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Program): Program {
        return Program(
                id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount,
                crack = source.crack,
                serialKey = source.serialKey,
                otherData = source.otherData,
                note = source.note,
                position = source.position)
    }

}
