package cz.vhromada.catalog.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link SongValidator}.
 *
 * @author Vladimir Hromada
 */
class SongValidatorTest extends MovableValidatorTest<Song, Music> {

    /**
     * Test method for {@link SongValidator#SongValidator(MovableService)} with null service for shows.
     */
    @Test
    void constructor_NullShowService() {
        assertThatThrownBy(() -> new SongValidator(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SongValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullName() {
        final Song song = getValidatingData(1);
        song.setName(null);

        final Result<Void> result = getMovableValidator().validate(song, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link SongValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    void validate_Deep_EmptyName() {
        final Song song = getValidatingData(1);
        song.setName("");

        final Result<Void> result = getMovableValidator().validate(song, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link SongValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with negative length of
     * song.
     */
    @Test
    void validate_Deep_NegativeLength() {
        final Song song = getValidatingData(1);
        song.setLength(-1);

        final Result<Void> result = getMovableValidator().validate(song, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link SongValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Song song = getValidatingData(1);
        song.setNote(null);

        final Result<Void> result = getMovableValidator().validate(song, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    @Override
    protected MovableValidator<Song> getMovableValidator() {
        return new SongValidator(getMovableService());
    }

    @Override
    protected Song getValidatingData(final Integer id) {
        return SongUtils.newSong(id);
    }

    @Override
    protected Song getValidatingData(final Integer id, final Integer position) {
        final Song song = SongUtils.newSong(id);
        song.setPosition(position);

        return song;
    }

    @Override
    protected Music getRepositoryData(final Song validatingData) {
        return MusicUtils.newMusicWithSongs(validatingData.getId());
    }

    @Override
    protected Music getItem1() {
        return null;
    }

    @Override
    protected Music getItem2() {
        return null;
    }

    @Override
    protected String getName() {
        return "Song";
    }

    @Override
    protected void initExistsMock(final Song validatingData, final boolean exists) {
        final Music music = exists ? MusicUtils.newMusicWithSongs(validatingData.getId()) : MusicUtils.newMusicDomain(Integer.MAX_VALUE);

        when(getMovableService().getAll()).thenReturn(CollectionUtils.newList(music));
    }

    @Override
    protected void verifyExistsMock(final Song validatingData) {
        verify(getMovableService()).getAll();
        verifyNoMoreInteractions(getMovableService());
    }

    @Override
    protected void initMovingMock(final Song validatingData, final boolean up, final boolean valid) {
        final List<cz.vhromada.catalog.domain.Song> songs;
        if (up && valid || !up && !valid) {
            songs = CollectionUtils.newList(SongUtils.newSongDomain(1), SongUtils.newSongDomain(validatingData.getId()));
        } else {
            songs = CollectionUtils.newList(SongUtils.newSongDomain(validatingData.getId()), SongUtils.newSongDomain(Integer.MAX_VALUE));
        }
        final Music music = MusicUtils.newMusicDomain(1);
        music.setSongs(songs);

        when(getMovableService().getAll()).thenReturn(CollectionUtils.newList(music));
    }

    @Override
    protected void verifyMovingMock(final Song validatingData) {
        verify(getMovableService(), times(2)).getAll();
        verifyNoMoreInteractions(getMovableService());
    }

}
