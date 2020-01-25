package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.utils.MusicUtils
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
 * A class represents test for class [MusicValidator].
 *
 * @author Vladimir Hromada
 */
class MusicValidatorTest : MovableValidatorTest<Music, cz.vhromada.catalog.domain.Music>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validateDeepNullName() {
        val music = getValidatingData(1)
                .copy(name = null)

        val result = getValidator().validate(music, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with empty name.
     */
    @Test
    fun validateDeepEmptyName() {
        val music = getValidatingData(1)
                .copy(name = "")

        val result = getValidator().validate(music, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to english Wikipedia page about music.
     */
    @Test
    fun validateDeepNullWikiEn() {
        val music = getValidatingData(1)
                .copy(wikiEn = null)

        val result = getValidator().validate(music, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                    "URL to english Wikipedia page about music mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to czech Wikipedia page about music.
     */
    @Test
    fun validateDeepNullWikiCz() {
        val music = getValidatingData(1)
                .copy(wikiCz = null)

        val result = getValidator().validate(music, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about music mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null count of media.
     */
    @Test
    fun validateDeepNullMediaCount() {
        val music = getValidatingData(1)
                .copy(mediaCount = null)

        val result = getValidator().validate(music, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with not positive count of media.
     */
    @Test
    fun validateDeepNotPositiveMediaCount() {
        val music = getValidatingData(1)
                .copy(mediaCount = 0)

        val result = getValidator().validate(music, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validateDeepNullNote() {
        val music = getValidatingData(1)
                .copy(note = null)

        val result = getValidator().validate(music, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Music> {
        return MusicValidator(service)
    }

    override fun getValidatingData(id: Int?): Music {
        return MusicUtils.newMusic(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Music {
        return MusicUtils.newMusic(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Music): cz.vhromada.catalog.domain.Music {
        return MusicUtils.newMusicDomain(validatingData.id)
    }

    override fun getItem1(): cz.vhromada.catalog.domain.Music {
        return MusicUtils.newMusicDomain(1)
    }

    override fun getItem2(): cz.vhromada.catalog.domain.Music {
        return MusicUtils.newMusicDomain(2)
    }

    override fun getName(): String {
        return "Music"
    }

}
