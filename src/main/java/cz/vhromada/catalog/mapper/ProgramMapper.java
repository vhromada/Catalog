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

    cz.vhromada.catalog.domain.Program map(Program source);

    Program mapBack(cz.vhromada.catalog.domain.Program source);

}
