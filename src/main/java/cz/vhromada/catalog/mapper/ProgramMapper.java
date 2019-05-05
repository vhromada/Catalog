package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Program;

import org.mapstruct.Mapper;

/**
 * An interface represents mapper for program.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface ProgramMapper {

    /**
     * Maps entity program to domain program.
     *
     * @param source entity program
     * @return mapped domain program
     */
    cz.vhromada.catalog.domain.Program map(Program source);

    /**
     * Maps domain program to entity program.
     *
     * @param source domain program
     * @return mapped entity program
     */
    Program mapBack(cz.vhromada.catalog.domain.Program source);

}
