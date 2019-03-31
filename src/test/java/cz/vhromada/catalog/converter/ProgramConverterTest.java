package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.utils.ProgramUtils;

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
     * Instance of {@link ProgramConverter}
     */
    @Autowired
    private ProgramConverter converter;

    /**
     * Test method for {@link ProgramConverter#convert(Program)}.
     */
    @Test
    void convert() {
        final Program program = ProgramUtils.newProgram(1);
        final cz.vhromada.catalog.domain.Program programDomain = converter.convert(program);

        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

    /**
     * Test method for {@link ProgramConverter#convertBack(cz.vhromada.catalog.domain.Program)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Program programDomain = ProgramUtils.newProgramDomain(1);
        final Program program = converter.convertBack(programDomain);

        ProgramUtils.assertProgramDeepEquals(program, programDomain);
    }

}
