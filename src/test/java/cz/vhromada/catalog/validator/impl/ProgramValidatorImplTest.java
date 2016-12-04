package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.ProgramUtils;
import cz.vhromada.catalog.entity.ProgramTO;
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
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewProgramTO_NullArgument() {
        programValidator.validateNewProgramTO(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NotNullId() {
        programValidator.validateNewProgramTO(ProgramUtils.newProgramTO(1));
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullName() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setName(null);

        programValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_EmptyName() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setName("");

        programValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to english Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullWikiEn() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setWikiEn(null);

        programValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to czech Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullWikiCz() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setWikiCz(null);

        programValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NotPositiveMediaCount() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setMediaCount(0);

        programValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullOtherData() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setOtherData(null);

        programValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateNewProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullNote() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setNote(null);

        programValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgramTO_NullArgument() {
        programValidator.validateExistingProgramTO(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullId() {
        programValidator.validateExistingProgramTO(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullName() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setName(null);

        programValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_EmptyName() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setName("");

        programValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null URL to Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullWiki() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setWikiCz(null);

        programValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NotPositiveMediaCount() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setMediaCount(0);

        programValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullOtherData() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setOtherData(null);

        programValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullNote() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setNote(null);

        programValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramValidator#validateProgramTOWithId(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateProgramTOWithId_NullArgument() {
        programValidator.validateProgramTOWithId(null);
    }

    /**
     * Test method for {@link ProgramValidator#validateProgramTOWithId(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateProgramTOWithId_NullId() {
        programValidator.validateProgramTOWithId(new ProgramTO());
    }

}
