package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link SongFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class SongFacadeImplIntegrationTest {

    /**
     * Event for null music
     */
    private static final Event NULL_MUSIC_EVENT = new Event(Severity.ERROR, "MUSIC_NULL", "Music mustn't be null.");

    /**
     * Event for music with null ID
     */
    private static final Event NULL_MUSIC_ID_EVENT = new Event(Severity.ERROR, "MUSIC_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing music
     */
    private static final Event NOT_EXIST_MUSIC_EVENT = new Event(Severity.ERROR, "MUSIC_NOT_EXIST", "Music doesn't exist.");

    /**
     * Event for null song
     */
    private static final Event NULL_SONG_EVENT = new Event(Severity.ERROR, "SONG_NULL", "Song mustn't be null.");

    /**
     * Event for song with null ID
     */
    private static final Event NULL_SONG_ID_EVENT = new Event(Severity.ERROR, "SONG_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing song
     */
    private static final Event NOT_EXIST_SONG_EVENT = new Event(Severity.ERROR, "SONG_NOT_EXIST", "Song doesn't exist.");

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of (@link SongFacade}
     */
    @Autowired
    private SongFacade songFacade;

    /**
     * Test method for {@link SongFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= SongUtils.SONGS_COUNT; i++) {
            final Result<Song> result = songFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            SongUtils.assertSongDeepEquals(result.getData(), SongUtils.getSong(i));
        }

        final Result<Song> result = songFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#get(Integer)} with null song.
     */
    @Test
    public void get_NullSong() {
        final Result<Song> result = songFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final cz.vhromada.catalog.domain.Song expectedSong = SongUtils.newSongDomain(SongUtils.SONGS_COUNT + 1);
        expectedSong.setPosition(Integer.MAX_VALUE);

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Song addedSong = SongUtils.getSong(entityManager, SongUtils.SONGS_COUNT + 1);
        SongUtils.assertSongDeepEquals(expectedSong, addedSong);
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT + 1));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with null music.
     */
    @Test
    public void add_NullMusic() {
        final Result<Void> result = songFacade.add(null, SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with music with null ID.
     */
    @Test
    public void add_NullId() {
        final Result<Void> result = songFacade.add(MusicUtils.newMusic(null), SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with not existing music.
     */
    @Test
    public void add_NotExistingMusic() {
        final Result<Void> result = songFacade.add(MusicUtils.newMusic(Integer.MAX_VALUE), SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with null song.
     */
    @Test
    public void add_NullSong() {
        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), SongUtils.newSong(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_ID_NOT_NULL", "ID must be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with null name.
     */
    @Test
    public void add_NullName() {
        final Song song = SongUtils.newSong(null);
        song.setName(null);

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Song song = SongUtils.newSong(null);
        song.setName("");

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with negative length.
     */
    @Test
    public void add_NegativeLength() {
        final Song song = SongUtils.newSong(null);
        song.setLength(-1);

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with null note.
     */
    @Test
    public void add_NullNote() {
        final Song song = SongUtils.newSong(null);
        song.setNote(null);

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Song song = SongUtils.newSong(1);

        final Result<Void> result = songFacade.update(song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Song updatedSong = SongUtils.getSong(entityManager, 1);
        SongUtils.assertSongDeepEquals(song, updatedSong);
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with null song.
     */
    @Test
    public void update_NullSong() {
        final Result<Void> result = songFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = songFacade.update(SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null name.
     */
    @Test
    public void update_NullName() {
        final Song song = SongUtils.newSong(1);
        song.setName(null);

        final Result<Void> result = songFacade.update(song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Song song = SongUtils.newSong(1);
        song.setName("");

        final Result<Void> result = songFacade.update(song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with negative length.
     */
    @Test
    public void update_NegativeLength() {
        final Song song = SongUtils.newSong(1);
        song.setLength(-1);

        final Result<Void> result = songFacade.update(song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null note.
     */
    @Test
    public void update_NullNote() {
        final Song song = SongUtils.newSong(1);
        song.setNote(null);

        final Result<Void> result = songFacade.update(song);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = songFacade.update(SongUtils.newSong(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = songFacade.remove(SongUtils.newSong(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(SongUtils.getSong(entityManager, 1), is(nullValue()));
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT - 1));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with null song.
     */
    @Test
    public void remove_NullSong() {
        final Result<Void> result = songFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with song with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = songFacade.remove(SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with song with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = songFacade.remove(SongUtils.newSong(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Song song = SongUtils.getSong(1);
        song.setId(SongUtils.SONGS_COUNT + 1);

        final Result<Void> result = songFacade.duplicate(SongUtils.newSong(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Song duplicatedSong = SongUtils.getSong(entityManager, SongUtils.SONGS_COUNT + 1);
        SongUtils.assertSongDeepEquals(song, duplicatedSong);
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT + 1));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with null song.
     */
    @Test
    public void duplicate_NullSong() {
        final Result<Void> result = songFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with song with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = songFacade.duplicate(SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with song with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = songFacade.duplicate(SongUtils.newSong(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Song song1 = SongUtils.getSong(1);
        song1.setPosition(1);
        final cz.vhromada.catalog.domain.Song song2 = SongUtils.getSong(2);
        song2.setPosition(0);

        final Result<Void> result = songFacade.moveUp(SongUtils.newSong(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        SongUtils.assertSongDeepEquals(song1, SongUtils.getSong(entityManager, 1));
        SongUtils.assertSongDeepEquals(song2, SongUtils.getSong(entityManager, 2));
        for (int i = 3; i <= SongUtils.SONGS_COUNT; i++) {
            SongUtils.assertSongDeepEquals(SongUtils.getSong(i), SongUtils.getSong(entityManager, i));
        }
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with null song.
     */
    @Test
    public void moveUp_NullSong() {
        final Result<Void> result = songFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with song with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = songFacade.moveUp(SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with not movable song.
     */
    @Test
    public void moveUp_NotMovableSong() {
        final Result<Void> result = songFacade.moveUp(SongUtils.newSong(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NOT_MOVABLE", "Song can't be moved up.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with song with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = songFacade.moveUp(SongUtils.newSong(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Song song1 = SongUtils.getSong(1);
        song1.setPosition(1);
        final cz.vhromada.catalog.domain.Song song2 = SongUtils.getSong(2);
        song2.setPosition(0);

        final Result<Void> result = songFacade.moveDown(SongUtils.newSong(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        SongUtils.assertSongDeepEquals(song1, SongUtils.getSong(entityManager, 1));
        SongUtils.assertSongDeepEquals(song2, SongUtils.getSong(entityManager, 2));
        for (int i = 3; i <= SongUtils.SONGS_COUNT; i++) {
            SongUtils.assertSongDeepEquals(SongUtils.getSong(i), SongUtils.getSong(entityManager, i));
        }
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with null song.
     */
    @Test
    public void moveDown_NullSong() {
        final Result<Void> result = songFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with song with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = songFacade.moveDown(SongUtils.newSong(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_SONG_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with not movable song.
     */
    @Test
    public void moveDown_NotMovableSong() {
        final Result<Void> result = songFacade.moveDown(SongUtils.newSong(SongUtils.SONGS_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SONG_NOT_MOVABLE", "Song can't be moved down.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with song with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = songFacade.moveDown(SongUtils.newSong(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_SONG_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#find(Music)}.
     */
    @Test
    public void find() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            final Result<List<Song>> result = songFacade.find(MusicUtils.newMusic(i));

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            SongUtils.assertSongListDeepEquals(result.getData(), SongUtils.getSongs(i));
        }

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#find(Music)} with null music.
     */
    @Test
    public void find_NullMusic() {
        final Result<List<Song>> result = songFacade.find(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#find(Music)} with music with null ID.
     */
    @Test
    public void find_NullId() {
        final Result<List<Song>> result = songFacade.find(MusicUtils.newMusic(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#find(Music)} with bad ID.
     */
    @Test
    public void find_BadId() {
        final Result<List<Song>> result = songFacade.find(MusicUtils.newMusic(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

}
