package cz.vhromada.catalog.validators.impl;

import cz.vhromada.catalog.common.ProgramUtils;
import cz.vhromada.catalog.entity.ProgramTO;
import cz.vhromada.catalog.validators.ProgramTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link ProgramTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class ProgramTOValidatorImplTest {

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
    public void testValidateNewProgramTO_NullArgument() {
        programTOValidator.validateNewProgramTO(null);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NotNullId() {
        programTOValidator.validateNewProgramTO(ProgramUtils.newProgramTO(1));
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullName() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setName(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_EmptyName() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setName("");

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to english Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullWikiEn() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setWikiEn(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null URL to czech Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullWikiCz() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setWikiCz(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NotPositiveMediaCount() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setMediaCount(0);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullOtherData() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setOtherData(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateNewProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewProgramTO_NullNote() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setNote(null);

        programTOValidator.validateNewProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingProgramTO_NullArgument() {
        programTOValidator.validateExistingProgramTO(null);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullId() {
        programTOValidator.validateExistingProgramTO(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullName() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setName(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_EmptyName() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setName("");

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null URL to Wikipedia page about program is
     * null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullWiki() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setWikiCz(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NotPositiveMediaCount() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setMediaCount(0);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullOtherData() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setOtherData(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateExistingProgramTO(ProgramTO)} with TO for program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingProgramTO_NullNote() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setNote(null);

        programTOValidator.validateExistingProgramTO(program);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateProgramTOWithId(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateProgramTOWithId_NullArgument() {
        programTOValidator.validateProgramTOWithId(null);
    }

    /**
     * Test method for {@link ProgramTOValidator#validateProgramTOWithId(ProgramTO)} with TO for program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateProgramTOWithId_NullId() {
        programTOValidator.validateProgramTOWithId(new ProgramTO());
    }

}
