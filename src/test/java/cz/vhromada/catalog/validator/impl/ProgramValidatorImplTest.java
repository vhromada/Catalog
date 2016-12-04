package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.catalog.validator.ProgramValidator;

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
     * Initializes validator for program.
     */
    @Before
    public void setUp() {
        programValidator = new ProgramValidatorImpl();
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NullArgument() {
        programValidator.validateNewProgram(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NotNullId() {
        programValidator.validateNewProgram(ProgramUtils.newProgram(1));
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NullName() {
        final Program program = ProgramUtils.newProgram(null);
        program.setName(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_EmptyName() {
        final Program program = ProgramUtils.newProgram(null);
        program.setName("");

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with null URL to english Wikipedia page about program is
     * null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NullWikiEn() {
        final Program program = ProgramUtils.newProgram(null);
        program.setWikiEn(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with null URL to czech Wikipedia page about program is
     * null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NullWikiCz() {
        final Program program = ProgramUtils.newProgram(null);
        program.setWikiCz(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgram(null);
        program.setMediaCount(0);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NullOtherData() {
        final Program program = ProgramUtils.newProgram(null);
        program.setOtherData(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgram(Program)} with program with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgram_NullNote() {
        final Program program = ProgramUtils.newProgram(null);
        program.setNote(null);

        programValidator.validateNewProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_NullArgument() {
        programValidator.validateExistingProgram(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with program with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_NullId() {
        programValidator.validateExistingProgram(ProgramUtils.newProgram(null));
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with program with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_NullName() {
        final Program program = ProgramUtils.newProgram(1);
        program.setName(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with program with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_EmptyName() {
        final Program program = ProgramUtils.newProgram(1);
        program.setName("");

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with program with null URL to Wikipedia page about program is
     * null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_NullWiki() {
        final Program program = ProgramUtils.newProgram(1);
        program.setWikiCz(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with program with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgram(1);
        program.setMediaCount(0);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with program with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_NullOtherData() {
        final Program program = ProgramUtils.newProgram(1);
        program.setOtherData(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgram(Program)} with program with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgram_NullNote() {
        final Program program = ProgramUtils.newProgram(1);
        program.setNote(null);

        programValidator.validateExistingProgram(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateProgramWithId(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateProgramWithId_NullArgument() {
        programValidator.validateProgramWithId(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateProgramWithId(Program)} with program with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateProgramWithId_NullId() {
        programValidator.validateProgramWithId(new Program());
    }

}
