package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [ProgramValidator].
 *
 * @author Vladimir Hromada
 */
class ProgramValidatorTest {

    /**
     * Instance of [ProgramValidator]
     */
    private lateinit var validator: ProgramValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = ProgramValidator()
    }

    /**
     * Test method for [ProgramValidator.validate] with correct new program.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = ProgramUtils.newProgram(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with null new program.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NULL", message = "Program mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with null name.
     */
    @Test
    fun validateNewNullName() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(name = null)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with empty name.
     */
    @Test
    fun validateNewEmptyName() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(name = "")

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with null URL to english Wikipedia page about program.
     */
    @Test
    fun validateNewNullWikiEn() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(wikiEn = null)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_EN_NULL", message = "URL to english Wikipedia page about program mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with null URL to czech Wikipedia page about program.
     */
    @Test
    fun validateNewNullWikiCz() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(wikiCz = null)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about program mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with null count of media.
     */
    @Test
    fun validateNewNullMediaCount() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(mediaCount = null)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with  not positive count of media.
     */
    @Test
    fun validateNewNotPositiveMediaCount() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(mediaCount = 0)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with null format.
     */
    @Test
    fun validateNewNullFormat() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(format = null)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_FORMAT_NULL", message = "Format mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with null other data.
     */
    @Test
    fun validateNewNullOtherData() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(otherData = null)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with new program with null note.
     */
    @Test
    fun validateNewNullNote() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(note = null)

        val result = validator.validate(data = program, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with with update correct program.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = ProgramUtils.newProgram(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with null update program.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NULL", message = "Program mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(id = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(position = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null name.
     */
    @Test
    fun validateUpdateNullName() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(name = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with empty name.
     */
    @Test
    fun validateUpdateEmptyName() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(name = "")

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null URL to english Wikipedia page about program.
     */
    @Test
    fun validateUpdateNullWikiEn() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(wikiEn = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_EN_NULL", message = "URL to english Wikipedia page about program mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null URL to czech Wikipedia page about program.
     */
    @Test
    fun validateUpdateNullWikiCz() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(wikiCz = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about program mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null count of media.
     */
    @Test
    fun validateUpdateNullMediaCount() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(mediaCount = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with not positive count of media.
     */
    @Test
    fun validateUpdateNotPositiveMediaCount() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(mediaCount = 0)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null format.
     */
    @Test
    fun validateUpdateNullFormat() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(format = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_FORMAT_NULL", message = "Format mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null other data.
     */
    @Test
    fun validateUpdateNullOtherData() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(otherData = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validate] with update program with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(note = null)

        val result = validator.validate(data = program, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validateExists] with correct program.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(ProgramUtils.newProgramDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [ProgramValidator.validateExists] with invalid program.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOT_EXIST", message = "Program doesn't exist.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validateMovingData] with correct up program.
     */
    @Test
    fun validateMovingDataUp() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        val result = validator.validateMovingData(data = programs[1], list = programs, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [ProgramValidator.validateMovingData] with with invalid up program.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        val result = validator.validateMovingData(data = programs[0], list = programs, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOT_MOVABLE", message = "Program can't be moved up.")))
        }
    }

    /**
     * Test method for [ProgramValidator.validateMovingData] with correct down program.
     */
    @Test
    fun validateMovingDataDown() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        val result = validator.validateMovingData(data = programs[0], list = programs, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [ProgramValidator.validateMovingData] with with invalid down program.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        val result = validator.validateMovingData(data = programs[1], list = programs, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOT_MOVABLE", message = "Program can't be moved down.")))
        }
    }

}
