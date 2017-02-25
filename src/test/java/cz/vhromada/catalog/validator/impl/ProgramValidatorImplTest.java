package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link ProgramValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class ProgramValidatorImplTest extends AbstractValidatorTest<Program, cz.vhromada.catalog.domain.Program> {

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    public void validate_Deep_NullName() {
        final Program program = getValidatingData();
        program.setName(null);

        final Result<Void> result = getCatalogValidator().validate(program, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    public void validate_Deep_EmptyName() {
        final Program program = getValidatingData();
        program.setName("");

        final Result<Void> result = getCatalogValidator().validate(program, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about program.
     */
    @Test
    public void validate_Deep_NullWikiEn() {
        final Program program = getValidatingData();
        program.setWikiEn(null);

        final Result<Void> result = getCatalogValidator().validate(program, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL",
                "URL to english Wikipedia page about program mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about program.
     */
    @Test
    public void validate_Deep_NullWikiCz() {
        final Program program = getValidatingData();
        program.setWikiCz(null);

        final Result<Void> result = getCatalogValidator().validate(program, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about program mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * count of media.
     */
    @Test
    public void validate_Deep_NotPositiveMediaCount() {
        final Program program = getValidatingData();
        program.setMediaCount(0);

        final Result<Void> result = getCatalogValidator().validate(program, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null other data.
     */
    @Test
    public void validate_Deep_NullOtherData() {
        final Program program = getValidatingData();
        program.setOtherData(null);

        final Result<Void> result = getCatalogValidator().validate(program, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    public void validate_Deep_NullNote() {
        final Program program = getValidatingData();
        program.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(program, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Program> getCatalogValidator() {
        return new ProgramValidatorImpl(getCatalogService());
    }

    @Override
    protected Program getValidatingData() {
        return ProgramUtils.newProgram(null);
    }

    @Override
    protected cz.vhromada.catalog.domain.Program getRepositoryData() {
        return ProgramUtils.newProgramDomain(null);
    }

    @Override
    protected cz.vhromada.catalog.domain.Program getItem1() {
        return ProgramUtils.newProgramDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Program getItem2() {
        return ProgramUtils.newProgramDomain(2);
    }

    @Override
    protected String getName() {
        return "Program";
    }

    @Override
    protected String getPrefix() {
        return "PROGRAM";
    }

}
