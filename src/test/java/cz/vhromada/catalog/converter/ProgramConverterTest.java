package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Program} and {@link Program}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class ProgramConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void testConvertProgramDomain() {
        final cz.vhromada.catalog.domain.Program programDomain = ProgramUtils.newProgramDomain(1);
        final Program program = converter.convert(programDomain, Program.class);

        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertProgramDomain_NullArgument() {
        assertNull(converter.convert(null, Program.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertProgram() {
        final Program program = ProgramUtils.newProgram(1);
        final cz.vhromada.catalog.domain.Program programDomain = converter.convert(program, cz.vhromada.catalog.domain.Program.class);

        assertNotNull(program);
        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertProgram_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Program.class));
    }

}
