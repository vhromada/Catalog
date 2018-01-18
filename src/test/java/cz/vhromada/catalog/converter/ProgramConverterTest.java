package cz.vhromada.catalog.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Program} and {@link Program}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class ProgramConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertProgramDomain() {
        final cz.vhromada.catalog.domain.Program programDomain = ProgramUtils.newProgramDomain(1);
        final Program program = converter.convert(programDomain, Program.class);

        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null program.
     */
    @Test
    void convertProgramDomain_NullProgram() {
        assertNull(converter.convert(null, Program.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertProgram() {
        final Program program = ProgramUtils.newProgram(1);
        final cz.vhromada.catalog.domain.Program programDomain = converter.convert(program, cz.vhromada.catalog.domain.Program.class);

        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null program.
     */
    @Test
    void convertProgram_NullProgram() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Program.class));
    }

}
