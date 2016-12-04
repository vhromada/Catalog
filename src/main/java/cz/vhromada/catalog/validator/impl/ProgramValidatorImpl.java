package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.validator.ProgramValidator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for program.
 *
 * @author Vladimir Hromada
 */
@Component("programValidator")
public class ProgramValidatorImpl implements ProgramValidator {

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewProgram(final Program program) {
        validateProgram(program);
        Assert.isNull(program.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingProgram(final Program program) {
        validateProgram(program);
        Assert.notNull(program.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateProgramWithId(final Program program) {
        Assert.notNull(program, "Program mustn't be null.");
        Assert.notNull(program.getId(), "ID mustn't be null.");
    }

    /**
     * Validates program.
     *
     * @param program program
     * @throws IllegalArgumentException if program is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about program is null
     *                                  or URL to czech Wikipedia page about program is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    private static void validateProgram(final Program program) {
        Assert.notNull(program, "Program mustn't be null.");
        Assert.notNull(program.getName(), "Name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(program.getName()) && !StringUtils.isEmpty(program.getName().trim()), "Name mustn't be empty string.");
        Assert.notNull(program.getWikiEn(), "URL to english Wikipedia page about program mustn't be null.");
        Assert.notNull(program.getWikiCz(), "URL to czech Wikipedia page about program mustn't be null.");
        Assert.isTrue(program.getMediaCount() > 0, "Count of media must be positive number.");
        Assert.notNull(program.getOtherData(), "Other data mustn't be null.");
        Assert.notNull(program.getNote(), "Note mustn't be null.");
    }

}
