package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A class represents integration test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class ProgramFacadeImplIntegrationTest extends AbstractParentFacadeIntegrationTest<Program, cz.vhromada.catalog.domain.Program> {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link ProgramFacade}
     */
    @Autowired
    private ProgramFacade programFacade;

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null name.
     */
    @Test
    public void add_NullName() {
        final Program program = newData(null);
        program.setName(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Program program = newData(null);
        program.setName("");

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test
    public void add_NullWikiEn() {
        final Program program = newData(null);
        program.setWikiEn(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0),
                is(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL", "URL to english Wikipedia page about program mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test
    public void add_NullWikiCz() {
        final Program program = newData(null);
        program.setWikiCz(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0),
                is(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL", "URL to czech Wikipedia page about program mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with not positive count of media.
     */
    @Test
    public void add_NotPositiveMediaCount() {
        final Program program = newData(null);
        program.setMediaCount(0);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null other data.
     */
    @Test
    public void add_NullOtherData() {
        final Program program = newData(null);
        program.setOtherData(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null note.
     */
    @Test
    public void add_NullNote() {
        final Program program = newData(null);
        program.setNote(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null name.
     */
    @Test
    public void update_NullName() {
        final Program program = newData(1);
        program.setName(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Program program = newData(1);
        program.setName("");

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test
    public void update_NullWikiEn() {
        final Program program = newData(1);
        program.setWikiEn(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0),
                is(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL", "URL to english Wikipedia page about program mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test
    public void update_NullWikiCz() {
        final Program program = newData(1);
        program.setWikiCz(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0),
                is(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL", "URL to czech Wikipedia page about program mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with not positive count of media.
     */
    @Test
    public void update_NotPositiveMediaCount() {
        final Program program = newData(1);
        program.setMediaCount(0);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null other data.
     */
    @Test
    public void update_NullOtherData() {
        final Program program = newData(1);
        program.setOtherData(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null note.
     */
    @Test
    public void update_NullNote() {
        final Program program = newData(1);
        program.setNote(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final int count = 600;

        final Result<Integer> result = programFacade.getTotalMediaCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(count));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertDefaultRepositoryData();
    }

    @Override
    protected CatalogParentFacade<Program> getCatalogParentFacade() {
        return programFacade;
    }

    @Override
    protected Integer getDefaultDataCount() {
        return ProgramUtils.PROGRAMS_COUNT;
    }

    @Override
    protected Integer getRepositoryDataCount() {
        return ProgramUtils.getProgramsCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Program> getDataList() {
        return ProgramUtils.getPrograms();
    }

    @Override
    protected cz.vhromada.catalog.domain.Program getDomainData(final Integer index) {
        return ProgramUtils.getProgram(index);
    }

    @Override
    protected Program newData(final Integer id) {
        return ProgramUtils.newProgram(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Program newDomainData(final Integer id) {
        return ProgramUtils.newProgramDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Program getRepositoryData(final Integer id) {
        return ProgramUtils.getProgram(entityManager, id);
    }

    @Override
    protected String getName() {
        return "Program";
    }

    @Override
    protected void clearReferencedData() {
    }

    @Override
    protected void assertDataListDeepEquals(final List<Program> expected, final List<cz.vhromada.catalog.domain.Program> actual) {
        ProgramUtils.assertProgramListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Program expected, final cz.vhromada.catalog.domain.Program actual) {
        ProgramUtils.assertProgramDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Program expected, final cz.vhromada.catalog.domain.Program actual) {
        ProgramUtils.assertProgramDeepEquals(expected, actual);
    }

}
