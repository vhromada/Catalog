package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

/**
 * A class represents integration test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class ProgramFacadeImplIntegrationTest extends MovableParentFacadeIntegrationTest<Program, cz.vhromada.catalog.domain.Program> {

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
    private ProgramFacade facade;

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null name.
     */
    @Test
    void add_NullName() {
        final Program program = newData(null);
        program.setName(null);

        final Result<Void> result = facade.add(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with empty string as name.
     */
    @Test
    void add_EmptyName() {
        final Program program = newData(null);
        program.setName("");

        final Result<Void> result = facade.add(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test
    void add_NullWikiEn() {
        final Program program = newData(null);
        program.setWikiEn(null);

        final Result<Void> result = facade.add(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL",
                "URL to english Wikipedia page about program mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test
    void add_NullWikiCz() {
        final Program program = newData(null);
        program.setWikiCz(null);

        final Result<Void> result = facade.add(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about program mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with not positive count of media.
     */
    @Test
    void add_NotPositiveMediaCount() {
        final Program program = newData(null);
        program.setMediaCount(0);

        final Result<Void> result = facade.add(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null other data.
     */
    @Test
    void add_NullOtherData() {
        final Program program = newData(null);
        program.setOtherData(null);

        final Result<Void> result = facade.add(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null note.
     */
    @Test
    void add_NullNote() {
        final Program program = newData(null);
        program.setNote(null);

        final Result<Void> result = facade.add(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null name.
     */
    @Test
    void update_NullName() {
        final Program program = newData(1);
        program.setName(null);

        final Result<Void> result = facade.update(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with empty string as name.
     */
    @Test
    void update_EmptyName() {
        final Program program = newData(1);
        program.setName("");

        final Result<Void> result = facade.update(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test
    void update_NullWikiEn() {
        final Program program = newData(1);
        program.setWikiEn(null);

        final Result<Void> result = facade.update(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL",
                "URL to english Wikipedia page about program mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test
    void update_NullWikiCz() {
        final Program program = newData(1);
        program.setWikiCz(null);

        final Result<Void> result = facade.update(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about program mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with not positive count of media.
     */
    @Test
    void update_NotPositiveMediaCount() {
        final Program program = newData(1);
        program.setMediaCount(0);

        final Result<Void> result = facade.update(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null other data.
     */
    @Test
    void update_NullOtherData() {
        final Program program = newData(1);
        program.setOtherData(null);

        final Result<Void> result = facade.update(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null note.
     */
    @Test
    void update_NullNote() {
        final Program program = newData(1);
        program.setNote(null);

        final Result<Void> result = facade.update(program);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link ProgramFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final int count = 600;

        final Result<Integer> result = facade.getTotalMediaCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(count);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        assertDefaultRepositoryData();
    }

    @Override
    protected MovableParentFacade<Program> getFacade() {
        return facade;
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
