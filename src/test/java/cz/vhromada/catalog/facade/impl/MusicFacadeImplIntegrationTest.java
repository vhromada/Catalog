package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.MusicFacade;
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
 * A class represents integration test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class MusicFacadeImplIntegrationTest {

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
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link MusicFacade}
     */
    @Autowired
    private MusicFacade musicFacade;

    /**
     * Test method for {@link MusicFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void newData() {
        final Result<Void> result = musicFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MusicUtils.getMusicCount(entityManager), is(0));
        assertThat(SongUtils.getSongsCount(entityManager), is(0));
    }

    /**
     * Test method for {@link MusicFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final Result<List<Music>> result = musicFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));
        MusicUtils.assertMusicListDeepEquals(result.getData(), MusicUtils.getMusic());

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            final Result<Music> result = musicFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            MusicUtils.assertMusicDeepEquals(result.getData(), MusicUtils.getMusic(i));
        }

        final Result<Music> result = musicFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#get(Integer)} with null music.
     */
    @Test
    public void get_NullMusic() {
        final Result<Music> result = musicFacade.get(null);

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
     * Test method for {@link MusicFacade#add(Music)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final Result<Void> result = musicFacade.add(MusicUtils.newMusic(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Music addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusicDomain(MusicUtils.MUSIC_COUNT + 1), addedMusic);
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT + 1));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with null music.
     */
    @Test
    public void add_NullMusic() {
        final Result<Void> result = musicFacade.add(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with not null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = musicFacade.add(MusicUtils.newMusic(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_ID_NOT_NULL", "ID must be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null name.
     */
    @Test
    public void add_NullName() {
        final Music music = MusicUtils.newMusic(null);
        music.setName(null);

        final Result<Void> result = musicFacade.add(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Music music = MusicUtils.newMusic(null);
        music.setName("");

        final Result<Void> result = musicFacade.add(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null URL to english Wikipedia about music.
     */
    @Test
    public void add_NullWikiEn() {
        final Music music = MusicUtils.newMusic(null);
        music.setWikiEn(null);

        final Result<Void> result = musicFacade.add(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                "URL to english Wikipedia page about music mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null URL to czech Wikipedia about music.
     */
    @Test
    public void add_NullWikiCz() {
        final Music music = MusicUtils.newMusic(null);
        music.setWikiCz(null);

        final Result<Void> result = musicFacade.add(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL", "URL to czech Wikipedia page about music mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with not positive count of media.
     */
    @Test
    public void add_NotPositiveMediaCount() {
        final Music music = MusicUtils.newMusic(null);
        music.setMediaCount(0);

        final Result<Void> result = musicFacade.add(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null note.
     */
    @Test
    public void add_NullNote() {
        final Music music = MusicUtils.newMusic(null);
        music.setNote(null);

        final Result<Void> result = musicFacade.add(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Music music = MusicUtils.newMusic(1);

        final Result<Void> result = musicFacade.update(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        MusicUtils.assertMusicDeepEquals(music, updatedMusic);
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with null music.
     */
    @Test
    public void update_NullMusic() {
        final Result<Void> result = musicFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = musicFacade.update(MusicUtils.newMusic(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null name.
     */
    @Test
    public void update_NullName() {
        final Music music = MusicUtils.newMusic(1);
        music.setName(null);

        final Result<Void> result = musicFacade.update(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Music music = MusicUtils.newMusic(1);
        music.setName("");

        final Result<Void> result = musicFacade.update(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null URL to english Wikipedia about music.
     */
    @Test
    public void update_NullWikiEn() {
        final Music music = MusicUtils.newMusic(1);
        music.setWikiEn(null);

        final Result<Void> result = musicFacade.update(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                "URL to english Wikipedia page about music mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null URL to czech Wikipedia about music.
     */
    @Test
    public void update_NullWikiCz() {
        final Music music = MusicUtils.newMusic(1);
        music.setWikiCz(null);

        final Result<Void> result = musicFacade.update(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL", "URL to czech Wikipedia page about music mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with not positive count of media.
     */
    @Test
    public void update_NotPositiveMediaCount() {
        final Music music = MusicUtils.newMusic(1);
        music.setMediaCount(0);

        final Result<Void> result = musicFacade.update(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null note.
     */
    @Test
    public void update_NullNote() {
        final Music music = MusicUtils.newMusic(1);
        music.setNote(null);

        final Result<Void> result = musicFacade.update(music);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = musicFacade.update(MusicUtils.newMusic(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = musicFacade.remove(MusicUtils.newMusic(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MusicUtils.getMusic(entityManager, 1), is(nullValue()));
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT - 1));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT - SongUtils.SONGS_PER_MUSIC_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)} with null music.
     */
    @Test
    public void remove_NullMusic() {
        final Result<Void> result = musicFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)} with music with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = musicFacade.remove(MusicUtils.newMusic(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)} with music with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = musicFacade.remove(MusicUtils.newMusic(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Music music = MusicUtils.getMusic(MusicUtils.MUSIC_COUNT);
        music.setId(MusicUtils.MUSIC_COUNT + 1);
        for (final Song song : music.getSongs()) {
            song.setId(SongUtils.SONGS_COUNT + music.getSongs().indexOf(song) + 1);
        }

        final Result<Void> result = musicFacade.duplicate(MusicUtils.newMusic(MusicUtils.MUSIC_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Music duplicatedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(music, duplicatedMusic);
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT + 1));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT + SongUtils.SONGS_PER_MUSIC_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)} with null music.
     */
    @Test
    public void duplicate_NullMusic() {
        final Result<Void> result = musicFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)} with music with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = musicFacade.duplicate(MusicUtils.newMusic(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)} with music with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = musicFacade.duplicate(MusicUtils.newMusic(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.getMusic(1);
        music1.setPosition(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.getMusic(2);
        music2.setPosition(0);

        final Result<Void> result = musicFacade.moveUp(MusicUtils.newMusic(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        MusicUtils.assertMusicDeepEquals(music1, MusicUtils.getMusic(entityManager, 1));
        MusicUtils.assertMusicDeepEquals(music2, MusicUtils.getMusic(entityManager, 2));
        for (int i = 3; i <= MusicUtils.MUSIC_COUNT; i++) {
            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), MusicUtils.getMusic(entityManager, i));
        }
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with null music.
     */
    @Test
    public void moveUp_NullMusic() {
        final Result<Void> result = musicFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with music with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = musicFacade.moveUp(MusicUtils.newMusic(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with not movable music.
     */
    @Test
    public void moveUp_NotMovableMusic() {
        final Result<Void> result = musicFacade.moveUp(MusicUtils.newMusic(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NOT_MOVABLE", "Music can't be moved up.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with music with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = musicFacade.moveUp(MusicUtils.newMusic(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.getMusic(1);
        music1.setPosition(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.getMusic(2);
        music2.setPosition(0);

        final Result<Void> result = musicFacade.moveDown(MusicUtils.newMusic(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        MusicUtils.assertMusicDeepEquals(music1, MusicUtils.getMusic(entityManager, 1));
        MusicUtils.assertMusicDeepEquals(music2, MusicUtils.getMusic(entityManager, 2));
        for (int i = 3; i <= MusicUtils.MUSIC_COUNT; i++) {
            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), MusicUtils.getMusic(entityManager, i));
        }
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with null music.
     */
    @Test
    public void moveDown_NullMusic() {
        final Result<Void> result = musicFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with music with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = musicFacade.moveDown(MusicUtils.newMusic(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MUSIC_ID_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with not movable music.
     */
    @Test
    public void moveDown_NotMovableMusic() {
        final Result<Void> result = musicFacade.moveDown(MusicUtils.newMusic(MusicUtils.MUSIC_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NOT_MOVABLE", "Music can't be moved down.")));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with music with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = musicFacade.moveDown(MusicUtils.newMusic(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MUSIC_EVENT));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void updatePositions() {
        final Result<Void> result = musicFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), MusicUtils.getMusic(entityManager, i));
        }
        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final int count = 60;

        final Result<Integer> result = musicFacade.getTotalMediaCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(count));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#getTotalLength()}.
     */
    @Test
    public void getTotalLength() {
        final Time length = new Time(666);

        final Result<Time> result = musicFacade.getTotalLength();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(length));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#getSongsCount()}.
     */
    @Test
    public void getSongsCount() {
        final Result<Integer> result = musicFacade.getSongsCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(SongUtils.SONGS_COUNT));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

}
