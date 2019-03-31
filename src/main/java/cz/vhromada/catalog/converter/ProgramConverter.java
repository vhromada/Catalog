package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.mapper.ProgramMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for program.
 *
 * @author Vladimir Hromada
 */
@Component
public class ProgramConverter implements MovableConverter<Program, cz.vhromada.catalog.domain.Program> {

    private ProgramMapper mapper;

    public ProgramConverter() {
        this.mapper = Mappers.getMapper(ProgramMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Program convert(final Program source) {
        return mapper.map(source);
    }

    @Override
    public Program convertBack(final cz.vhromada.catalog.domain.Program source) {
        return mapper.mapBack(source);
    }

}
