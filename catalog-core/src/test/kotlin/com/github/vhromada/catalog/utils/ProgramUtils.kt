package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.common.Format
import com.github.vhromada.catalog.entity.Program
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates program fields.
 *
 * @return updated program
 */
fun com.github.vhromada.catalog.domain.Program.updated(): com.github.vhromada.catalog.domain.Program {
    return copy(
        name = "Name",
        wikiEn = "enWiki",
        wikiCz = "czWiki",
        mediaCount = 1,
        format = Format.STEAM,
        crack = true,
        serialKey = true,
        otherData = "Other data",
        note = "Note"
    )
}

/**
 * Updates program fields.
 *
 * @return updated program
 */
fun Program.updated(): Program {
    return copy(
        name = "Name",
        wikiEn = "enWiki",
        wikiCz = "czWiki",
        mediaCount = 1,
        format = Format.STEAM,
        crack = true,
        serialKey = true,
        otherData = "Other data",
        note = "Note"
    )
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
    fun getPrograms(): List<com.github.vhromada.catalog.domain.Program> {
        val programs = mutableListOf<com.github.vhromada.catalog.domain.Program>()
        for (i in 1..PROGRAMS_COUNT) {
            programs.add(getProgramDomain(index = i))
        }

        return programs
    }

    /**
     * Returns program.
     *
     * @param id ID
     * @return program
     */
    fun newProgramDomain(id: Int?): com.github.vhromada.catalog.domain.Program {
        return com.github.vhromada.catalog.domain.Program(
            id = id,
            name = "",
            wikiEn = null,
            wikiCz = null,
            mediaCount = 0,
            format = Format.STEAM,
            crack = false,
            serialKey = false,
            otherData = null,
            note = null,
            position = if (id == null) null else id - 1
        ).updated()
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
            format = Format.STEAM,
            crack = false,
            serialKey = false,
            otherData = null,
            note = null,
            position = if (id == null) null else id - 1
        ).updated()
    }

    /**
     * Returns program for index.
     *
     * @param index index
     * @return program for index
     */
    fun getProgramDomain(index: Int): com.github.vhromada.catalog.domain.Program {
        val mediaCountMultiplier = 100

        return com.github.vhromada.catalog.domain.Program(
            id = index,
            name = "$PROGRAM$index name",
            wikiEn = "$PROGRAM$index English Wikipedia",
            wikiCz = "$PROGRAM$index Czech Wikipedia",
            mediaCount = index * mediaCountMultiplier,
            format = getFormat(index = index),
            crack = index == 3,
            serialKey = index != 1,
            otherData = if (index == 3) PROGRAM + "3 other data" else "",
            note = if (index == 3) PROGRAM + "3 note" else "",
            position = index + 9
        ).fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns format for index.
     *
     * @param index index
     * @return format for index
     */
    private fun getFormat(index: Int): Format {
        return when (index) {
            1 -> Format.ISO
            2 -> Format.STEAM
            3 -> Format.BINARY
            else -> throw IllegalArgumentException("Bad index")
        }
    }

    /**
     * Returns program.
     *
     * @param entityManager entity manager
     * @param id            program ID
     * @return program
     */
    fun getProgram(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Program? {
        return entityManager.find(com.github.vhromada.catalog.domain.Program::class.java, id)
    }

    /**
     * Returns program with updated fields.
     *
     * @param id            program ID
     * @param entityManager entity manager
     * @return program with updated fields
     */
    fun updateProgram(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Program {
        val program = getProgram(entityManager = entityManager, id = id)!!
        return program
            .updated()
            .copy(position = POSITION)
            .fillAudit(audit = program)
    }

    /**
     * Returns count of programs.
     *
     * @param entityManager entity manager
     * @return count of programs
     */
    @Suppress("JpaQlInspection")
    fun getProgramsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Program p", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts programs deep equals.
     *
     * @param expected expected list of programs
     * @param actual   actual list of programs
     */
    fun assertDomainProgramsDeepEquals(expected: List<com.github.vhromada.catalog.domain.Program>, actual: List<com.github.vhromada.catalog.domain.Program>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertProgramDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected program
     * @param actual   actual program
     */
    fun assertProgramDeepEquals(expected: com.github.vhromada.catalog.domain.Program, actual: com.github.vhromada.catalog.domain.Program) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.format).isEqualTo(expected.format)
            it.assertThat(actual.crack).isEqualTo(expected.crack)
            it.assertThat(actual.serialKey).isEqualTo(expected.serialKey)
            it.assertThat(actual.otherData).isEqualTo(expected.otherData)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected program
     * @param actual   actual program
     */
    fun assertProgramDeepEquals(expected: Program, actual: com.github.vhromada.catalog.domain.Program) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.format).isEqualTo(expected.format)
            it.assertThat(actual.crack).isEqualTo(expected.crack)
            it.assertThat(actual.serialKey).isEqualTo(expected.serialKey)
            it.assertThat(actual.otherData).isEqualTo(expected.otherData)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
        }
    }

    /**
     * Asserts programs deep equals.
     *
     * @param expected expected list of programs
     * @param actual   actual list of programs
     */
    fun assertProgramListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Program>, actual: List<Program>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertProgramDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts program deep equals.
     *
     * @param expected expected program
     * @param actual   actual program
     */
    fun assertProgramDeepEquals(expected: com.github.vhromada.catalog.domain.Program, actual: Program) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.format).isEqualTo(expected.format)
            it.assertThat(actual.crack).isEqualTo(expected.crack)
            it.assertThat(actual.serialKey).isEqualTo(expected.serialKey)
            it.assertThat(actual.otherData).isEqualTo(expected.otherData)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
