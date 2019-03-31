package cz.vhromada.catalog.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.validator.AbstractMovableValidator;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link MusicValidator}.
 *
 * @author Vladimir Hromada
 */
class MusicValidatorTest extends MovableValidatorTest<Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Test method for {@link MusicValidator#MusicValidator(MovableService)} with null service for music.
     */
    @Test
    void constructor_NullMusicService() {
        assertThatThrownBy(() -> new MusicValidator(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullName() {
        final Music music = getValidatingData(1);
        music.setName(null);

        final Result<Void> result = getValidator().validate(music, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    void validate_Deep_EmptyName() {
        final Music music = getValidatingData(1);
        music.setName("");

        final Result<Void> result = getValidator().validate(music, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about music.
     */
    @Test
    void validate_Deep_NullWikiEn() {
        final Music music = getValidatingData(1);
        music.setWikiEn(null);

        final Result<Void> result = getValidator().validate(music, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                "URL to english Wikipedia page about music mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about music.
     */
    @Test
    void validate_Deep_NullWikiCz() {
        final Music music = getValidatingData(1);
        music.setWikiCz(null);

        final Result<Void> result = getValidator().validate(music, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about music mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * count of media.
     */
    @Test
    void validate_Deep_NotPositiveMediaCount() {
        final Music music = getValidatingData(1);
        music.setMediaCount(0);

        final Result<Void> result = getValidator().validate(music, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Music music = getValidatingData(1);
        music.setNote(null);

        final Result<Void> result = getValidator().validate(music, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    @Override
    protected MovableValidator<Music> getValidator() {
        return new MusicValidator(getService());
    }

    @Override
    protected Music getValidatingData(final Integer id) {
        return MusicUtils.newMusic(id);
    }

    @Override
    protected Music getValidatingData(final Integer id, final Integer position) {
        final Music music = MusicUtils.newMusic(id);
        music.setPosition(position);

        return music;
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getRepositoryData(final Music validatingData) {
        return MusicUtils.newMusicDomain(validatingData.getId());
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getItem1() {
        return MusicUtils.newMusicDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getItem2() {
        return MusicUtils.newMusicDomain(2);
    }

    @Override
    protected String getName() {
        return "Music";
    }

}
