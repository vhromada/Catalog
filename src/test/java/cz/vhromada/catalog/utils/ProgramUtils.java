package cz.vhromada.catalog.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Program;

/**
 * A class represents utility class for programs.
 *
 * @author Vladimir Hromada
 */
public final class ProgramUtils {

    /**
     * ID
     */
    public static final int ID = 1;

    /**
     * Position
     */
    public static final int POSITION = 10;

    /**
     * Count of programs
     */
    public static final int PROGRAMS_COUNT = 3;

    /**
     * Program name
     */
    private static final String PROGRAM = "Program ";

    /**
     * Creates a new instance of ProgramUtils.
     */
    private ProgramUtils() {
    }

    /**
     * Returns program.
     *
     * @param id ID
     * @return program
     */
    public static cz.vhromada.catalog.domain.Program newProgramDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Program program = new cz.vhromada.catalog.domain.Program();
        updateProgram(program);
        if (id != null) {
            program.setId(id);
            program.setPosition(id - 1);
        }

        return program;
    }

    /**
     * Updates program fields.
     *
     * @param program program
     */
    @SuppressWarnings("Duplicates")
    public static void updateProgram(final cz.vhromada.catalog.domain.Program program) {
        program.setName("Name");
        program.setWikiEn("enWiki");
        program.setWikiCz("czWiki");
        program.setMediaCount(1);
        program.setCrack(true);
        program.setSerialKey(true);
        program.setOtherData("Other data");
        program.setNote("Note");
    }

    /**
     * Returns program.
     *
     * @param id ID
     * @return program
     */
    public static Program newProgram(final Integer id) {
        final Program program = new Program();
        updateProgram(program);
        if (id != null) {
            program.setId(id);
            program.setPosition(id - 1);
        }

        return program;
    }

    /**
     * Updates program fields.
     *
     * @param program program
     */
    @SuppressWarnings("Duplicates")
    public static void updateProgram(final Program program) {
        program.setName("Name");
        program.setWikiEn("enWiki");
        program.setWikiCz("czWiki");
        program.setMediaCount(1);
        program.setCrack(true);
        program.setSerialKey(true);
        program.setOtherData("Other data");
        program.setNote("Note");
    }

    /**
     * Returns programs.
     *
     * @return programs
     */
    public static List<cz.vhromada.catalog.domain.Program> getPrograms() {
        final List<cz.vhromada.catalog.domain.Program> programs = new ArrayList<>();
        for (int i = 0; i < PROGRAMS_COUNT; i++) {
            programs.add(getProgram(i + 1));
        }

        return programs;
    }

    /**
     * Returns program for index.
     *
     * @param index index
     * @return program for index
     */
    public static cz.vhromada.catalog.domain.Program getProgram(final int index) {
        final int mediaCountMultiplier = 100;

        final cz.vhromada.catalog.domain.Program program = new cz.vhromada.catalog.domain.Program();
        program.setId(index);
        program.setName(PROGRAM + index + " name");
        program.setWikiEn(PROGRAM + index + " English Wikipedia");
        program.setWikiCz(PROGRAM + index + " Czech Wikipedia");
        program.setMediaCount(index * mediaCountMultiplier);
        program.setCrack(index == 3);
        program.setSerialKey(index != 1);
        program.setOtherData(index == 3 ? PROGRAM + "3 other data" : "");
        program.setNote(index == 3 ? PROGRAM + "3 note" : "");
        program.setPosition(index - 1);

        return program;
    }

    /**
     * Returns program.
     *
     * @param entityManager entity manager
     * @param id            program ID
     * @return program
     */
    public static cz.vhromada.catalog.domain.Program getProgram(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Program.class, id);
    }

    /**
     * Returns program with updated fields.
     *
     * @param id            program ID
     * @param entityManager entity manager
     * @return program with updated fields
     */
    public static cz.vhromada.catalog.domain.Program updateProgram(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Program program = getProgram(entityManager, id);
        updateProgram(program);
        program.setPosition(POSITION);

        return program;
    }

    /**
     * Returns count of programs.
     *
     * @param entityManager entity manager
     * @return count of programs
     */
    public static int getProgramsCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Program p", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts programs deep equals.
     *
     * @param expected expected programs
     * @param actual   actual programs
     */
    public static void assertProgramsDeepEquals(final List<cz.vhromada.catalog.domain.Program> expected,
        final List<cz.vhromada.catalog.domain.Program> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertProgramDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected program
     * @param actual   actual program
     */
    @SuppressWarnings("Duplicates")
    public static void assertProgramDeepEquals(final cz.vhromada.catalog.domain.Program expected, final cz.vhromada.catalog.domain.Program actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getName()).isEqualTo(actual.getName());
            softly.assertThat(expected.getWikiEn()).isEqualTo(actual.getWikiEn());
            softly.assertThat(expected.getWikiCz()).isEqualTo(actual.getWikiCz());
            softly.assertThat(expected.getMediaCount()).isEqualTo(actual.getMediaCount());
            softly.assertThat(expected.getCrack()).isEqualTo(actual.getCrack());
            softly.assertThat(expected.getSerialKey()).isEqualTo(actual.getSerialKey());
            softly.assertThat(expected.getOtherData()).isEqualTo(actual.getOtherData());
            softly.assertThat(expected.getNote()).isEqualTo(actual.getNote());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
        });
    }

    /**
     * Asserts programs deep equals.
     *
     * @param expected expected list of program
     * @param actual   actual programs
     */
    public static void assertProgramListDeepEquals(final List<Program> expected, final List<cz.vhromada.catalog.domain.Program> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertProgramDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected program
     * @param actual   actual program
     */
    @SuppressWarnings("Duplicates")
    public static void assertProgramDeepEquals(final Program expected, final cz.vhromada.catalog.domain.Program actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getName()).isEqualTo(actual.getName());
            softly.assertThat(expected.getWikiEn()).isEqualTo(actual.getWikiEn());
            softly.assertThat(expected.getWikiCz()).isEqualTo(actual.getWikiCz());
            softly.assertThat(expected.getMediaCount()).isEqualTo(actual.getMediaCount());
            softly.assertThat(expected.getCrack()).isEqualTo(actual.getCrack());
            softly.assertThat(expected.getSerialKey()).isEqualTo(actual.getSerialKey());
            softly.assertThat(expected.getOtherData()).isEqualTo(actual.getOtherData());
            softly.assertThat(expected.getNote()).isEqualTo(actual.getNote());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
        });
    }

}
