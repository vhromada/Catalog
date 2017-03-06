package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link SongValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class SongValidatorImplTest extends AbstractValidatorTest<Song, Music> {

    /**
     * Test method for {@link SongValidatorImpl#SongValidatorImpl(CatalogService)} with null service for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullMusicService() {
        new SongValidatorImpl(null);
    }

    /**
     * Test method for {@link SongValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    public void validate_Deep_NullName() {
        final Song song = getValidatingData(1);
        song.setName(null);

        final Result<Void> result = getCatalogValidator().validate(song, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SongValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    public void validate_Deep_EmptyName() {
        final Song song = getValidatingData(1);
        song.setName("");

        final Result<Void> result = getCatalogValidator().validate(song, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SongValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with negative length of song.
     */
    @Test
    public void validate_Deep_NegativeLeng() {
        final Song song = getValidatingData(1);
        song.setLength(-1);

        final Result<Void> result = getCatalogValidator().validate(song, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SongValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    public void validate_Deep_NullNote() {
        final Song song = getValidatingData(1);
        song.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(song, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Song> getCatalogValidator() {
        return new SongValidatorImpl(getCatalogService());
    }

    @Override
    protected Song getValidatingData(final Integer id) {
        return SongUtils.newSong(id);
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

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(music));
    }

    @Override
    protected void verifyExistsMock(final Song validatingData) {
        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
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

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(music));
    }

    @Override
    protected void verifyMovingMock(final Song validatingData) {
        verify(getCatalogService(), times(2)).getAll();
        verifyNoMoreInteractions(getCatalogService());
    }

}
