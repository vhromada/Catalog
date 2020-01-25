package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import cz.vhromada.catalog.entity.Program
import cz.vhromada.catalog.utils.ProgramUtils
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Severity
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.validator.MovableValidatorTest
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.common.validator.ValidationType
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [ProgramValidator].
 *
 * @author Vladimir Hromada
 */
class ProgramValidatorTest : MovableValidatorTest<Program, cz.vhromada.catalog.domain.Program>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validateDeepNullName() {
        val program = getValidatingData(1)
                .copy(name = null)

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with empty name.
     */
    @Test
    fun validateDeepEmptyName() {
        val program = getValidatingData(1)
                .copy(name = "")

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to english Wikipedia page about program.
     */
    @Test
    fun validateDeepNullWikiEn() {
        val program = getValidatingData(1)
                .copy(wikiEn = null)

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL", "URL to english Wikipedia page about program mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to czech Wikipedia page about program.
     */
    @Test
    fun validateDeepNullWikiCz() {
        val program = getValidatingData(1)
                .copy(wikiCz = null)

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about program mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null count of media.
     */
    @Test
    fun validateDeepNullMediaCount() {
        val program = getValidatingData(1)
                .copy(mediaCount = null)

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with not positive count of media.
     */
    @Test
    fun validateDeepNotPositiveMediaCount() {
        val program = getValidatingData(1)
                .copy(mediaCount = 0)

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null other data.
     */
    @Test
    fun validateDeepNullOtherData() {
        val program = getValidatingData(1)
                .copy(otherData = null)

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validateDeepNullNote() {
        val program = getValidatingData(1)
                .copy(note = null)

        val result = getValidator().validate(program, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Program> {
        return ProgramValidator(service)
    }

    override fun getValidatingData(id: Int?): Program {
        return ProgramUtils.newProgram(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Program {
        return ProgramUtils.newProgram(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Program): cz.vhromada.catalog.domain.Program {
        return ProgramUtils.newProgramDomain(validatingData.id)
    }

    override fun getItem1(): cz.vhromada.catalog.domain.Program {
        return ProgramUtils.newProgramDomain(1)
    }

    override fun getItem2(): cz.vhromada.catalog.domain.Program {
        return ProgramUtils.newProgramDomain(2)
    }

    override fun getName(): String {
        return "Program"
    }

}
