package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ProgramUtils;
import cz.vhromada.catalog.entities.Program;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link Program} and {@link ProgramTO}.
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
        final Program program = ProgramUtils.newProgram(1);
        final ProgramTO programTO = converter.convert(program, ProgramTO.class);

        ProgramUtils.assertProgramDeepEquals(programTO, program);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertProgram_NullArgument() {
        assertNull(converter.convert(null, ProgramTO.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertProgramTO() {
        final ProgramTO programTO = ProgramUtils.newProgramTO(1);
        final Program program = converter.convert(programTO, Program.class);

        assertNotNull(program);
        ProgramUtils.assertProgramDeepEquals(programTO, program);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertProgramTO_NullArgument() {
        assertNull(converter.convert(null, Program.class));
    }

}
