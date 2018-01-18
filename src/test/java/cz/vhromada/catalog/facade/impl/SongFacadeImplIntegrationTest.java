package cz.vhromada.catalog.facade.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.facade.CatalogChildFacade;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A class represents integration test for class {@link SongFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class SongFacadeImplIntegrationTest extends AbstractChildFacadeIntegrationTest<Song, cz.vhromada.catalog.domain.Song, Music> {

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
     * Test method for {@link SongFacade#add(Music, Song)} with song with null name.
     */
    @Test
    void add_NullName() {
        final Song song = newChildData(null);
        song.setName(null);

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with empty string as name.
     */
    @Test
    void add_EmptyName() {
        final Song song = newChildData(null);
        song.setName("");

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with negative length.
     */
    @Test
    void add_NegativeLength() {
        final Song song = newChildData(null);
        song.setLength(-1);

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with null note.
     */
    @Test
    void add_NullNote() {
        final Song song = newChildData(null);
        song.setNote(null);

        final Result<Void> result = songFacade.add(MusicUtils.newMusic(1), song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null name.
     */
    @Test
    void update_NullName() {
        final Song song = newChildData(1);
        song.setName(null);

        final Result<Void> result = songFacade.update(song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with empty string as name.
     */
    @Test
    void update_EmptyName() {
        final Song song = newChildData(1);
        song.setName("");

        final Result<Void> result = songFacade.update(song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with negative length.
     */
    @Test
    void update_NegativeLength() {
        final Song song = newChildData(1);
        song.setLength(-1);

        final Result<Void> result = songFacade.update(song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null note.
     */
    @Test
    void update_NullNote() {
        final Song song = newChildData(1);
        song.setNote(null);

        final Result<Void> result = songFacade.update(song);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    @Override
    protected CatalogChildFacade<Song, Music> getCatalogChildFacade() {
        return songFacade;
    }

    @Override
    protected Integer getDefaultParentDataCount() {
        return MusicUtils.MUSIC_COUNT;
    }

    @Override
    protected Integer getDefaultChildDataCount() {
        return SongUtils.SONGS_COUNT;
    }

    @Override
    protected Integer getRepositoryParentDataCount() {
        return MusicUtils.getMusicCount(entityManager);
    }

    @Override
    protected Integer getRepositoryChildDataCount() {
        return SongUtils.getSongsCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Song> getDataList(final Integer parentId) {
        return SongUtils.getSongs(parentId);
    }

    @Override
    protected cz.vhromada.catalog.domain.Song getDomainData(final Integer index) {
        return SongUtils.getSong(index);
    }

    @Override
    protected Music newParentData(final Integer id) {
        return MusicUtils.newMusic(id);
    }

    @Override
    protected Song newChildData(final Integer id) {
        return SongUtils.newSong(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Song newDomainData(final Integer id) {
        return SongUtils.newSongDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Song getRepositoryData(final Integer id) {
        return SongUtils.getSong(entityManager, id);
    }

    @Override
    protected String getParentName() {
        return "Music";
    }

    @Override
    protected String getChildName() {
        return "Song";
    }

    @Override
    protected void assertDataListDeepEquals(final List<Song> expected, final List<cz.vhromada.catalog.domain.Song> actual) {
        SongUtils.assertSongListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Song expected, final cz.vhromada.catalog.domain.Song actual) {
        SongUtils.assertSongDeepEquals(expected, actual);

    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Song expected, final cz.vhromada.catalog.domain.Song actual) {
        SongUtils.assertSongDeepEquals(expected, actual);
    }

}
