package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entities.Program;
import cz.vhromada.catalog.facade.to.ProgramTO;

/**
 * A class represents utility class for programs.
 *
 * @author Vladimir Hromada
 */
public final class ProgramUtils {

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Position
     */
    public static final Integer POSITION = 10;

    /**
     * Count of programs
     */
    public static final int PROGRAMS_COUNT = 3;

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
     * Returns TO for program.
     *
     * @param id ID
     * @return TO for program
     */
    public static ProgramTO newProgramTO(final Integer id) {
        final ProgramTO program = new ProgramTO();
        updateProgramTO(program);
        if (id != null) {
            program.setId(id);
            program.setPosition(id - 1);
        }

        return program;
    }

    /**
     * Updates TO for program fields.
     *
     * @param program TO for program
     */
    public static void updateProgramTO(final ProgramTO program) {
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
    public static List<Program> getPrograms() {
        final List<Program> programs = new ArrayList<>();
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
    public static Program getProgram(final int index) {
        final Program program = new Program();
        program.setId(index);
        program.setName("Program " + index + " name");
        program.setWikiEn("Program " + index + " English Wikipedia");
        program.setWikiCz("Program " + index + " Czech Wikipedia");
        program.setMediaCount(index * 100);
        program.setCrack(index == 3);
        program.setSerialKey(index != 1);
        program.setOtherData(index == 3 ? "Program 3 other data" : "");
        program.setNote(index == 3 ? "Program 3 note" : "");
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
    public static Program getProgram(final EntityManager entityManager, final int id) {
        return entityManager.find(Program.class, id);
    }

    /**
     * Returns program with updated fields.
     *
     * @param id            program ID
     * @param entityManager entity manager
     * @return program with updated fields
     */
    public static Program updateProgram(final EntityManager entityManager, final int id) {
        final Program program = getProgram(entityManager, id);
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
    public static void assertProgramsDeepEquals(final List<Program> expected, final List<Program> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
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
    public static void assertProgramDeepEquals(final Program expected, final Program actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWikiEn(), actual.getWikiEn());
        assertEquals(expected.getWikiCz(), actual.getWikiCz());
        assertEquals(expected.getCrack(), actual.getCrack());
        assertEquals(expected.getSerialKey(), actual.getSerialKey());
        assertEquals(expected.getOtherData(), actual.getOtherData());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

}
