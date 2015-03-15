package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.facade.validators.ProgramTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link ProgramTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class ProgramTOValidatorImplTest extends ObjectGeneratorTest {

    /**
     * Instance of {@link ProgramTOValidator}
     */
    private ProgramTOValidator programTOValidator;

    /**
     * Initializes validator for TO for program.
     */
    @Before
    public void setUp() {
        programTOValidator = new ProgramTOValidatorImpl();
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgramTOWithNullArgument() {
        programTOValidator.validateNewProgramTO(null);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithNotNullId() {
        programTOValidator.validateNewProgramTO(ToGenerator.newProgramWithId(getObjectGenerator()));
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithNullName() {
        final ProgramTO program = ToGenerator.newProgram(getObjectGenerator());
        program.setName(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithEmptyName() {
        final ProgramTO program = ToGenerator.newProgram(getObjectGenerator());
        program.setName("");
        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to english Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithNullWikiEn() {
        final ProgramTO program = ToGenerator.newProgram(getObjectGenerator());
        program.setWikiEn(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to czech Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithNullWikiCz() {
        final ProgramTO program = ToGenerator.newProgram(getObjectGenerator());
        program.setWikiCz(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithNotPositiveMediaCount() {
        final ProgramTO program = ToGenerator.newProgram(getObjectGenerator());
        program.setMediaCount(0);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithNullOtherData() {
        final ProgramTO program = ToGenerator.newProgram(getObjectGenerator());
        program.setOtherData(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTOWithNullNote() {
        final ProgramTO program = ToGenerator.newProgram(getObjectGenerator());
        program.setNote(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgramTOWithNullArgument() {
        programTOValidator.validateExistingProgramTO(null);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTOWithNullId() {
        programTOValidator.validateExistingProgramTO(ToGenerator.newProgram(getObjectGenerator()));
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTOWithNullName() {
        final ProgramTO program = ToGenerator.newProgramWithId(getObjectGenerator());
        program.setName(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTOWithEmptyName() {
        final ProgramTO program = ToGenerator.newProgramWithId(getObjectGenerator());
        program.setName("");

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null URL to Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTOWithNullWiki() {
        final ProgramTO program = ToGenerator.newProgramWithId(getObjectGenerator());
        program.setWikiCz(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTOWithNotPositiveMediaCount() {
        final ProgramTO program = ToGenerator.newProgramWithId(getObjectGenerator());
        program.setMediaCount(0);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTOWithNullOtherData() {
        final ProgramTO program = ToGenerator.newProgramWithId(getObjectGenerator());
        program.setOtherData(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTOWithNullNote() {
        final ProgramTO program = ToGenerator.newProgramWithId(getObjectGenerator());
        program.setNote(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateProgramTOWithId(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateProgramTOWithIdWithNullArgument() {
        programTOValidator.validateProgramTOWithId(null);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateProgramTOWithId(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateProgramTOWithIdWithNullId() {
        programTOValidator.validateProgramTOWithId(ToGenerator.newProgram(getObjectGenerator()));
    }

}
