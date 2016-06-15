package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.MusicUtils;
import cz.vhromada.catalog.commons.SongUtils;
import cz.vhromada.catalog.entities.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

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
@ContextConfiguration("classpath:testFacadeContext.xml")
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
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final SongTO expectedSong = SongUtils.newSongTO(SongUtils.SONGS_COUNT + 1);
        expectedSong.setPosition(Integer.MAX_VALUE);

        songFacade.add(MusicUtils.newMusicTO(1), SongUtils.newSongTO(null));

        final Song addedSong = SongUtils.getSong(entityManager, SongUtils.SONGS_COUNT + 1);
        SongUtils.assertSongDeepEquals(expectedSong, addedSong);

        assertEquals(SongUtils.SONGS_COUNT + 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with null TO for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullMusicTO() {
        songFacade.add(null, SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with null TO for song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSongTO() {
        songFacade.add(MusicUtils.newMusicTO(1), null);
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullId() {
        songFacade.add(MusicUtils.newMusicTO(null), SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with song with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        songFacade.add(MusicUtils.newMusicTO(1), SongUtils.newSongTO(1));
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullName() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setName(null);

        songFacade.add(MusicUtils.newMusicTO(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyName() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setName("");

        songFacade.add(MusicUtils.newMusicTO(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with song with negative length.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NegativeLength() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setLength(-1);

        songFacade.add(MusicUtils.newMusicTO(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullNote() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setNote(null);

        songFacade.add(MusicUtils.newMusicTO(1), song);
    }

    /**
     * Test method for {@link SongFacade#add(cz.vhromada.catalog.facade.to.MusicTO, SongTO)} with song with not existing music.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingMusic() {
        songFacade.add(MusicUtils.newMusicTO(Integer.MAX_VALUE), SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final SongTO song = SongUtils.newSongTO(1);

        songFacade.update(song);

        final Song updatedSong = SongUtils.getSong(entityManager, 1);
        SongUtils.assertSongDeepEquals(song, updatedSong);

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        songFacade.update(null);
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        songFacade.update(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullName() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setName(null);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyName() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setName(null);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with song with negative length.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NegativeLength() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setLength(-1);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullNote() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setNote(null);

        songFacade.update(song);
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        songFacade.update(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        songFacade.remove(SongUtils.newSongTO(1));

        assertNull(SongUtils.getSong(entityManager, 1));

        assertEquals(SongUtils.SONGS_COUNT - 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        songFacade.remove(null);
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)} with song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        songFacade.remove(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        songFacade.remove(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Song song = SongUtils.getSong(1);
        song.setId(SongUtils.SONGS_COUNT + 1);

        songFacade.duplicate(SongUtils.newSongTO(1));

        final Song duplicatedSong = SongUtils.getSong(entityManager, SongUtils.SONGS_COUNT + 1);
        SongUtils.assertSongDeepEquals(song, duplicatedSong);

        assertEquals(SongUtils.SONGS_COUNT + 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        songFacade.duplicate(null);
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)} with song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        songFacade.duplicate(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        songFacade.duplicate(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final Song song1 = SongUtils.getSong(1);
        song1.setPosition(1);
        final Song song2 = SongUtils.getSong(2);
        song2.setPosition(0);

        songFacade.moveUp(SongUtils.newSongTO(2));
        SongUtils.assertSongDeepEquals(song1, SongUtils.getSong(entityManager, 1));
        SongUtils.assertSongDeepEquals(song2, SongUtils.getSong(entityManager, 2));
        for (int i = 3; i <= SongUtils.SONGS_COUNT; i++) {
            SongUtils.assertSongDeepEquals(SongUtils.getSong(i), SongUtils.getSong(entityManager, i));
        }

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        songFacade.moveUp(null);
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NullId() {
        songFacade.moveUp(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        songFacade.moveUp(SongUtils.newSongTO(1));
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_BadId() {
        songFacade.moveUp(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final Song song1 = SongUtils.getSong(1);
        song1.setPosition(1);
        final Song song2 = SongUtils.getSong(2);
        song2.setPosition(0);

        songFacade.moveDown(SongUtils.newSongTO(1));
        SongUtils.assertSongDeepEquals(song1, SongUtils.getSong(entityManager, 1));
        SongUtils.assertSongDeepEquals(song2, SongUtils.getSong(entityManager, 2));
        for (int i = 3; i <= SongUtils.SONGS_COUNT; i++) {
            SongUtils.assertSongDeepEquals(SongUtils.getSong(i), SongUtils.getSong(entityManager, i));
        }

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        songFacade.moveDown(null);
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NullId() {
        songFacade.moveDown(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        songFacade.moveDown(SongUtils.newSongTO(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_BadId() {
        songFacade.moveDown(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)}.
     */
    @Test
    public void testFindSongsByMusic() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            SongUtils.assertSongListDeepEquals(songFacade.findSongsByMusic(MusicUtils.newMusicTO(i)), SongUtils.getSongs(i));
        }

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_NullArgument() {
        songFacade.findSongsByMusic(null);
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)} with music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testFindSongsByMusic_NullId() {
        songFacade.findSongsByMusic(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testFindSongsByMusic_BadId() {
        songFacade.findSongsByMusic(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

}
