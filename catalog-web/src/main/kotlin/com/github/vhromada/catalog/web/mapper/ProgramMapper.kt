package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.web.fo.ProgramFO

/**
 * An interface represents mapper for programs.
 *
 * @author Vladimir Hromada
 */
interface ProgramMapper {

    /**
     * Returns FO for program.
     *
     * @param source program
     * @return FO for program
     */
    fun map(source: Program): ProgramFO

    /**
     * Returns program.
     *
     * @param source FO for program
     * @return program
     */
    fun mapBack(source: ProgramFO): Program

}
