package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class ProgramFacadeImplIntegrationTest {

    /**
     * Event for null program
     */
    private static final Event NULL_PROGRAM_EVENT = new Event(Severity.ERROR, "PROGRAM_NULL", "Program mustn't be null.");

    /**
     * Event for program with null ID
     */
    private static final Event NULL_PROGRAM_ID_EVENT = new Event(Severity.ERROR, "PROGRAM_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing program
     */
    private static final Event NOT_EXIST_PROGRAM_EVENT = new Event(Severity.ERROR, "PROGRAM_NOT_EXIST", "Program doesn't exist.");

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
     * Test method for {@link ProgramFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void newData() {
        final Result<Void> result = programFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(0));
    }

    /**
     * Test method for {@link ProgramFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final Result<List<Program>> result = programFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));
        ProgramUtils.assertProgramListDeepEquals(result.getData(), ProgramUtils.getPrograms());

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            final Result<Program> result = programFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            ProgramUtils.assertProgramDeepEquals(result.getData(), ProgramUtils.getProgram(i));
        }

        final Result<Program> result = programFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#get(Integer)} with null program.
     */
    @Test
    public void get_NullProgram() {
        final Result<Program> result = programFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final Result<Void> result = programFacade.add(ProgramUtils.newProgram(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(ProgramUtils.newProgramDomain(ProgramUtils.PROGRAMS_COUNT + 1), addedProgram);
        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT + 1));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with null program.
     */
    @Test
    public void add_NullProgram() {
        final Result<Void> result = programFacade.add(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with not null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = programFacade.add(ProgramUtils.newProgram(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_ID_NOT_NULL", "ID must be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null name.
     */
    @Test
    public void add_NullName() {
        final Program program = ProgramUtils.newProgram(null);
        program.setName(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Program program = ProgramUtils.newProgram(null);
        program.setName("");

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test
    public void add_NullWikiEn() {
        final Program program = ProgramUtils.newProgram(null);
        program.setWikiEn(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL",
                "URL to english Wikipedia page about program mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test
    public void add_NullWikiCz() {
        final Program program = ProgramUtils.newProgram(null);
        program.setWikiCz(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about program mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with not positive count of media.
     */
    @Test
    public void add_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgram(null);
        program.setMediaCount(0);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null other data.
     */
    @Test
    public void add_NullOtherData() {
        final Program program = ProgramUtils.newProgram(null);
        program.setOtherData(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null note.
     */
    @Test
    public void add_NullNote() {
        final Program program = ProgramUtils.newProgram(null);
        program.setNote(null);

        final Result<Void> result = programFacade.add(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Program program = ProgramUtils.newProgram(1);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Program updatedProgram = ProgramUtils.getProgram(entityManager, 1);
        ProgramUtils.assertProgramDeepEquals(program, updatedProgram);
        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with null program.
     */
    @Test
    public void update_NullProgram() {
        final Result<Void> result = programFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = programFacade.update(ProgramUtils.newProgram(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_ID_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null name.
     */
    @Test
    public void update_NullName() {
        final Program program = ProgramUtils.newProgram(1);
        program.setName(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Program program = ProgramUtils.newProgram(1);
        program.setName("");

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test
    public void update_NullWikiEn() {
        final Program program = ProgramUtils.newProgram(1);
        program.setWikiEn(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL",
                "URL to english Wikipedia page about program mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test
    public void update_NullWikiCz() {
        final Program program = ProgramUtils.newProgram(1);
        program.setWikiCz(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about program mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with not positive count of media.
     */
    @Test
    public void update_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgram(1);
        program.setMediaCount(0);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null other data.
     */
    @Test
    public void update_NullOtherData() {
        final Program program = ProgramUtils.newProgram(1);
        program.setOtherData(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null note.
     */
    @Test
    public void update_NullNote() {
        final Program program = ProgramUtils.newProgram(1);
        program.setNote(null);

        final Result<Void> result = programFacade.update(program);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = programFacade.update(ProgramUtils.newProgram(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#remove(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = programFacade.remove(ProgramUtils.newProgram(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(ProgramUtils.getProgram(entityManager, 1), is(nullValue()));
        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT - 1));
    }

    /**
     * Test method for {@link ProgramFacade#remove(cz.vhromada.catalog.common.Movable)} with null program.
     */
    @Test
    public void remove_NullProgram() {
        final Result<Void> result = programFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#remove(cz.vhromada.catalog.common.Movable)} with program with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = programFacade.remove(ProgramUtils.newProgram(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_ID_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#remove(cz.vhromada.catalog.common.Movable)} with program with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = programFacade.remove(ProgramUtils.newProgram(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Program program = ProgramUtils.getProgram(ProgramUtils.PROGRAMS_COUNT);
        program.setId(ProgramUtils.PROGRAMS_COUNT + 1);

        final Result<Void> result = programFacade.duplicate(ProgramUtils.newProgram(ProgramUtils.PROGRAMS_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Program duplicatedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(program, duplicatedProgram);
        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT + 1));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(cz.vhromada.catalog.common.Movable)} with null program.
     */
    @Test
    public void duplicate_NullProgram() {
        final Result<Void> result = programFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(cz.vhromada.catalog.common.Movable)} with program with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = programFacade.duplicate(ProgramUtils.newProgram(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_ID_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(cz.vhromada.catalog.common.Movable)} with program with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = programFacade.duplicate(ProgramUtils.newProgram(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Program program1 = ProgramUtils.getProgram(1);
        program1.setPosition(1);
        final cz.vhromada.catalog.domain.Program program2 = ProgramUtils.getProgram(2);
        program2.setPosition(0);

        final Result<Void> result = programFacade.moveUp(ProgramUtils.newProgram(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        ProgramUtils.assertProgramDeepEquals(program1, ProgramUtils.getProgram(entityManager, 1));
        ProgramUtils.assertProgramDeepEquals(program2, ProgramUtils.getProgram(entityManager, 2));
        for (int i = 3; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }
        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(cz.vhromada.catalog.common.Movable)} with null program.
     */
    @Test
    public void moveUp_NullProgram() {
        final Result<Void> result = programFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(cz.vhromada.catalog.common.Movable)} with program with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = programFacade.moveUp(ProgramUtils.newProgram(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_ID_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(cz.vhromada.catalog.common.Movable)} with not movable program.
     */
    @Test
    public void moveUp_NotMovableProgram() {
        final Result<Void> result = programFacade.moveUp(ProgramUtils.newProgram(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NOT_MOVABLE", "Program can't be moved up.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(cz.vhromada.catalog.common.Movable)} with program with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = programFacade.moveUp(ProgramUtils.newProgram(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Program program1 = ProgramUtils.getProgram(1);
        program1.setPosition(1);
        final cz.vhromada.catalog.domain.Program program2 = ProgramUtils.getProgram(2);
        program2.setPosition(0);

        final Result<Void> result = programFacade.moveDown(ProgramUtils.newProgram(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        ProgramUtils.assertProgramDeepEquals(program1, ProgramUtils.getProgram(entityManager, 1));
        ProgramUtils.assertProgramDeepEquals(program2, ProgramUtils.getProgram(entityManager, 2));
        for (int i = 3; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }
        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(cz.vhromada.catalog.common.Movable)} with null program.
     */
    @Test
    public void moveDown_NullProgram() {
        final Result<Void> result = programFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(cz.vhromada.catalog.common.Movable)} with program with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = programFacade.moveDown(ProgramUtils.newProgram(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_PROGRAM_ID_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(cz.vhromada.catalog.common.Movable)} with not movable program.
     */
    @Test
    public void moveDown_NotMovableProgram() {
        final Result<Void> result = programFacade.moveDown(ProgramUtils.newProgram(ProgramUtils.PROGRAMS_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "PROGRAM_NOT_MOVABLE", "Program can't be moved down.")));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(cz.vhromada.catalog.common.Movable)} with program with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = programFacade.moveDown(ProgramUtils.newProgram(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_PROGRAM_EVENT));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void updatePositions() {
        final Result<Void> result = programFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }
        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
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

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

}
