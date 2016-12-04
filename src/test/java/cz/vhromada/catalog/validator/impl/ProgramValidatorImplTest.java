package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.ProgramUtils;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.validator.ProgramValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link ProgramValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class ProgramValidatorImplTest {

    /**
     * Instance of {@link ProgramValidator}
     */
    private ProgramValidator programValidator;

    /**
     * Initializes validator for TO for program.
     */
    @Before
    public void setUp() {
        programValidator = new ProgramValidatorImpl();
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgramTO_NullArgument() {
        programValidator.validateNewProgram(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NotNullId() {
        programValidator.validateNewProgram(ProgramUtils.newProgramTO(1));
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullName() {
        final Program program = ProgramUtils.newProgramTO(null);
        program.setName(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_EmptyName() {
        final Program program = ProgramUtils.newProgramTO(null);
        program.setName("");

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to english Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullWikiEn() {
        final Program program = ProgramUtils.newProgramTO(null);
        program.setWikiEn(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to czech Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullWikiCz() {
        final Program program = ProgramUtils.newProgramTO(null);
        program.setWikiCz(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgramTO(null);
        program.setMediaCount(0);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullOtherData() {
        final Program program = ProgramUtils.newProgramTO(null);
        program.setOtherData(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullNote() {
        final Program program = ProgramUtils.newProgramTO(null);
        program.setNote(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgramTO_NullArgument() {
        programValidator.validateExistingProgram(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullId() {
        programValidator.validateExistingProgram(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullName() {
        final Program program = ProgramUtils.newProgramTO(1);
        program.setName(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_EmptyName() {
        final Program program = ProgramUtils.newProgramTO(1);
        program.setName("");

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null URL to Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullWiki() {
        final Program program = ProgramUtils.newProgramTO(1);
        program.setWikiCz(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgramTO(1);
        program.setMediaCount(0);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullOtherData() {
        final Program program = ProgramUtils.newProgramTO(1);
        program.setOtherData(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullNote() {
        final Program program = ProgramUtils.newProgramTO(1);
        program.setNote(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateProgramWithId(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateProgramTOWithId_NullArgument() {
        programValidator.validateProgramWithId(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateProgramTOWithId(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateProgramTOWithId_NullId() {
        programValidator.validateProgramWithId(new Program());
    }

}
