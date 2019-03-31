package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.utils.ProgramUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Program} and {@link Program}.
 *
 * @author Vladimir Hromada
 */
class ProgramMapperTest {

    private ProgramMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProgramMapper.class);
    }

    /**
     * Test method for {@link ProgramMapper#map(Program)}.
     */
    @Test
    void map() {
        final Program program = ProgramUtils.newProgram(1);
        final cz.vhromada.catalog.domain.Program programDomain = mapper.map(program);

        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

    /**
     * Test method for {@link ProgramMapper#map(Program)} with null program.
     */
    @Test
    void map_NullProgram() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link ProgramMapper#mapBack(cz.vhromada.catalog.domain.Program)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Program programDomain = ProgramUtils.newProgramDomain(1);
        final Program program = mapper.mapBack(programDomain);

        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

    /**
     * Test method for {@link ProgramMapper#mapBack(cz.vhromada.catalog.domain.Program)} with null program.
     */
    @Test
    void mapBack_NullProgram() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
