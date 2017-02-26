package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;

import org.junit.Ignore;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Ignore
public class SongFacadeImplIntegrationTest {

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
     * Test method for {@link SongFacade#getSong(Integer)}.
     */
    @Test
    public void testGetSong() {
        for (int i = 1; i <= SongUtils.SONGS_COUNT; i++) {
            SongUtils.assertSongDeepEquals(songFacade.getSong(i), SongUtils.getSong(i));
        }

        assertNull(songFacade.getSong(Integer.MAX_VALUE));

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#getSong(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSong_NullArgument() {
        songFacade.getSong(null);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final cz.vhromada.catalog.domain.Song expectedSong = SongUtils.newSongDomain(SongUtils.SONGS_COUNT + 1);
        expectedSong.setPosition(Integer.MAX_VALUE);

        songFacade.add(MusicUtils.newMusic(1), SongUtils.newSong(null));

        final cz.vhromada.catalog.domain.Song addedSong = SongUtils.getSong(entityManager, SongUtils.SONGS_COUNT + 1);
        SongUtils.assertSongDeepEquals(expectedSong, addedSong);

        assertEquals(SongUtils.SONGS_COUNT + 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with null music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullMusicEpisode() {
        songFacade.add(null, SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with null song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSongEpisode() {
        songFacade.add(MusicUtils.newMusic(1), null);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with music with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullId() {
        songFacade.add(MusicUtils.newMusic(null), SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        songFacade.add(MusicUtils.newMusic(1), SongUtils.newSong(1));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullName() {
        final Song song = SongUtils.newSong(null);
        song.setName(null);

        songFacade.add(MusicUtils.newMusic(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyName() {
        final Song song = SongUtils.newSong(null);
        song.setName("");

        songFacade.add(MusicUtils.newMusic(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NegativeLength() {
        final Song song = SongUtils.newSong(null);
        song.setLength(-1);

        songFacade.add(MusicUtils.newMusic(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullNote() {
        final Song song = SongUtils.newSong(null);
        song.setNote(null);

        songFacade.add(MusicUtils.newMusic(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with not existing music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotExistingMusic() {
        songFacade.add(MusicUtils.newMusic(Integer.MAX_VALUE), SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#update(Song)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Song song = SongUtils.newSong(1);

        songFacade.update(song);

        final cz.vhromada.catalog.domain.Song updatedSong = SongUtils.getSong(entityManager, 1);
        SongUtils.assertSongDeepEquals(song, updatedSong);

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        songFacade.update(null);
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        songFacade.update(SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullName() {
        final Song song = SongUtils.newSong(1);
        song.setName(null);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyName() {
        final Song song = SongUtils.newSong(1);
        song.setName(null);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NegativeLength() {
        final Song song = SongUtils.newSong(1);
        song.setLength(-1);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with song with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullNote() {
        final Song song = SongUtils.newSong(1);
        song.setNote(null);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        songFacade.update(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        songFacade.remove(SongUtils.newSong(1));

        assertNull(SongUtils.getSong(entityManager, 1));

        assertEquals(SongUtils.SONGS_COUNT - 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        songFacade.remove(null);
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with song with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        songFacade.remove(SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        songFacade.remove(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Song song = SongUtils.getSong(1);
        song.setId(SongUtils.SONGS_COUNT + 1);

        songFacade.duplicate(SongUtils.newSong(1));

        final cz.vhromada.catalog.domain.Song duplicatedSong = SongUtils.getSong(entityManager, SongUtils.SONGS_COUNT + 1);
        SongUtils.assertSongDeepEquals(song, duplicatedSong);

        assertEquals(SongUtils.SONGS_COUNT + 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        songFacade.duplicate(null);
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with song with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        songFacade.duplicate(SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        songFacade.duplicate(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Song song1 = SongUtils.getSong(1);
        song1.setPosition(1);
        final cz.vhromada.catalog.domain.Song song2 = SongUtils.getSong(2);
        song2.setPosition(0);

        songFacade.moveUp(SongUtils.newSong(2));

        SongUtils.assertSongDeepEquals(song1, SongUtils.getSong(entityManager, 1));
        SongUtils.assertSongDeepEquals(song2, SongUtils.getSong(entityManager, 2));
        for (int i = 3; i <= SongUtils.SONGS_COUNT; i++) {
            SongUtils.assertSongDeepEquals(SongUtils.getSong(i), SongUtils.getSong(entityManager, i));
        }

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        songFacade.moveUp(null);
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with song with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullId() {
        songFacade.moveUp(SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        songFacade.moveUp(SongUtils.newSong(1));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadId() {
        songFacade.moveUp(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Song song1 = SongUtils.getSong(1);
        song1.setPosition(1);
        final cz.vhromada.catalog.domain.Song song2 = SongUtils.getSong(2);
        song2.setPosition(0);

        songFacade.moveDown(SongUtils.newSong(1));

        SongUtils.assertSongDeepEquals(song1, SongUtils.getSong(entityManager, 1));
        SongUtils.assertSongDeepEquals(song2, SongUtils.getSong(entityManager, 2));
        for (int i = 3; i <= SongUtils.SONGS_COUNT; i++) {
            SongUtils.assertSongDeepEquals(SongUtils.getSong(i), SongUtils.getSong(entityManager, i));
        }

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        songFacade.moveDown(null);
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with song with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullId() {
        songFacade.moveDown(SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        songFacade.moveDown(SongUtils.newSong(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadId() {
        songFacade.moveDown(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)}.
     */
    @Test
    public void testFindSongsByMusic() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            SongUtils.assertSongListDeepEquals(songFacade.findSongsByMusic(MusicUtils.newMusic(i)), SongUtils.getSongs(i));
        }

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_NullArgument() {
        songFacade.findSongsByMusic(null);
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)} with music with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_NullId() {
        songFacade.findSongsByMusic(MusicUtils.newMusic(null));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_BadId() {
        songFacade.findSongsByMusic(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

}
