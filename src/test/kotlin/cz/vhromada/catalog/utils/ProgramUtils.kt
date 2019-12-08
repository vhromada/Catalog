package cz.vhromada.catalog.utils

import cz.vhromada.catalog.entity.Program
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates program fields.
 *
 * @return updated program
 */
fun cz.vhromada.catalog.domain.Program.updated(): cz.vhromada.catalog.domain.Program {
    return copy(name = "Name",
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            mediaCount = 1,
            crack = true,
            serialKey = true,
            otherData = "Other data",
            note = "Note")
}

/**
 * Updates program fields.
 *
 * @return updated program
 */
fun Program.updated(): Program {
    return copy(name = "Name",
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            mediaCount = 1,
            crack = true,
            serialKey = true,
            otherData = "Other data",
            note = "Note")
}

/**
 * A class represents utility class for programs.
 *
 * @author Vladimir Hromada
 */
object ProgramUtils {

    /**
     * Count of programs
     */
    const val PROGRAMS_COUNT = 3

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Program name
     */
    private const val PROGRAM = "Program "

    /**
     * Returns programs.
     *
     * @return programs
     */
    fun getPrograms(): List<cz.vhromada.catalog.domain.Program> {
        val programs = mutableListOf<cz.vhromada.catalog.domain.Program>()
        for (i in 0 until PROGRAMS_COUNT) {
            programs.add(getProgram(i + 1))
        }

        return programs
    }

    /**
     * Returns program.
     *
     * @param id ID
     * @return program
     */
    fun newProgramDomain(id: Int?): cz.vhromada.catalog.domain.Program {
        return cz.vhromada.catalog.domain.Program(
                id = id,
                name = "",
                wikiEn = null,
                wikiCz = null,
                mediaCount = 0,
                crack = false,
                serialKey = false,
                otherData = null,
                note = null,
                position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns program.
     *
     * @param id ID
     * @return program
     */
    fun newProgram(id: Int?): Program {
        return Program(
                id = id,
                name = "",
                wikiEn = null,
                wikiCz = null,
                mediaCount = 0,
                crack = false,
                serialKey = false,
                otherData = null,
                note = null,
                position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns program for index.
     *
     * @param index index
     * @return program for index
     */
    fun getProgram(index: Int): cz.vhromada.catalog.domain.Program {
        val mediaCountMultiplier = 100

        return cz.vhromada.catalog.domain.Program(
                id = index,
                name = "$PROGRAM$index name",
                wikiEn = "$PROGRAM$index English Wikipedia",
                wikiCz = "$PROGRAM$index Czech Wikipedia",
                mediaCount = index * mediaCountMultiplier,
                crack = index == 3,
                serialKey = index != 1,
                otherData = if (index == 3) PROGRAM + "3 other data" else "",
                note = if (index == 3) PROGRAM + "3 note" else "",
                position = index - 1)
    }

    /**
     * Returns program.
     *
     * @param entityManager entity manager
     * @param id            program ID
     * @return program
     */
    fun getProgram(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Program? {
        return entityManager.find(cz.vhromada.catalog.domain.Program::class.java, id)
    }

    /**
     * Returns program with updated fields.
     *
     * @param id            program ID
     * @param entityManager entity manager
     * @return program with updated fields
     */
    fun updateProgram(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Program {
        return getProgram(entityManager, id)!!
                .updated()
                .copy(position = POSITION)
    }

    /**
     * Returns count of programs.
     *
     * @param entityManager entity manager
     * @return count of programs
     */
    @Suppress("CheckStyle")
    fun getProgramsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Program p", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts programs deep equals.
     *
     * @param expected expected programs
     * @param actual   actual programs
     */
    fun assertProgramsDeepEquals(expected: List<cz.vhromada.catalog.domain.Program?>?, actual: List<cz.vhromada.catalog.domain.Program?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertProgramDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected program
     * @param actual   actual program
     */
    fun assertProgramDeepEquals(expected: cz.vhromada.catalog.domain.Program?, actual: cz.vhromada.catalog.domain.Program?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.name).isEqualTo(actual.name)
            it.assertThat(expected.wikiEn).isEqualTo(actual.wikiEn)
            it.assertThat(expected.wikiCz).isEqualTo(actual.wikiCz)
            it.assertThat(expected.mediaCount).isEqualTo(actual.mediaCount)
            it.assertThat(expected.crack).isEqualTo(actual.crack)
            it.assertThat(expected.serialKey).isEqualTo(actual.serialKey)
            it.assertThat(expected.otherData).isEqualTo(actual.otherData)
            it.assertThat(expected.note).isEqualTo(actual.note)
            it.assertThat(expected.position).isEqualTo(actual.position)
        }
    }

    /**
     * Asserts programs deep equals.
     *
     * @param expected expected list of program
     * @param actual   actual programs
     */
    fun assertProgramListDeepEquals(expected: List<Program?>?, actual: List<cz.vhromada.catalog.domain.Program?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertProgramDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected program
     * @param actual   actual program
     */
    fun assertProgramDeepEquals(expected: Program?, actual: cz.vhromada.catalog.domain.Program?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.crack).isEqualTo(expected.crack)
            it.assertThat(actual.serialKey).isEqualTo(expected.serialKey)
            it.assertThat(actual.otherData).isEqualTo(expected.otherData)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
