package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ProgramUtils;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Program} and {@link Program}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class ProgramConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO.
     */
    @Test
    public void testConvertProgram() {
        final cz.vhromada.catalog.domain.Program program = ProgramUtils.newProgram(1);
        final Program programTO = converter.convert(program, Program.class);

        ProgramUtils.assertProgramDeepEquals(programTO, program);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertProgram_NullArgument() {
        assertNull(converter.convert(null, Program.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertProgramTO() {
        final Program programTO = ProgramUtils.newProgramTO(1);
        final cz.vhromada.catalog.domain.Program program = converter.convert(programTO, cz.vhromada.catalog.domain.Program.class);

        assertNotNull(program);
        ProgramUtils.assertProgramDeepEquals(programTO, program);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertProgramTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Program.class));
    }

}
