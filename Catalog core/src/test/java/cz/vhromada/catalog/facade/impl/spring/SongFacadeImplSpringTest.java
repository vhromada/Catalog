package cz.vhromada.catalog.facade.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.SongFacadeImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class SongFacadeImplSpringTest {

    /** Count of songs */
    private static final String SONGS_COUNT_FIELD = "songsCount";

    /** Total length field */
    private static final String TOTAL_LENGTH_FIELD = "totalLength";

    /** Instance of {@link EntityManager} */
    @Autowired
    private EntityManager entityManager;

    /** Instance of {@link PlatformTransactionManager} */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /** Instance of (@link SongFacade} */
    @Autowired
    private SongFacade songFacade;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Initializes database. */
    @Before
    public void setUp() {
        SpringUtils.remove(transactionManager, entityManager, Song.class);
        SpringUtils.remove(transactionManager, entityManager, Music.class);
        SpringUtils.updateSequence(transactionManager, entityManager, "music_sq");
        SpringUtils.updateSequence(transactionManager, entityManager, "songs_sq");
        for (final Music music : SpringEntitiesUtils.getMusic()) {
            music.setId(null);
            SpringUtils.persist(transactionManager, entityManager, music);
        }
        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
            for (final Song song : SpringEntitiesUtils.getSongs(i)) {
                song.setId(null);
                SpringUtils.persist(transactionManager, entityManager, song);
            }
        }
    }

    /** Test method for {@link SongFacade#getSong(Integer)}. */
    @Test
    public void testGetSong() {
        for (int i = 0; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringToUtils.getSong(musicNumber, songNumber), songFacade.getSong(i + 1), SONGS_COUNT_FIELD, TOTAL_LENGTH_FIELD);
        }

        assertNull(songFacade.getSong(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#getSong(Integer)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSongWithNullArgument() {
        songFacade.getSong(null);
    }

    /** Test method for {@link SongFacade#add(SongTO)}. */
    @Test
    public void testAdd() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);

        songFacade.add(song);

        DeepAsserts.assertNotNull(song.getId());
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, song.getId());
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, song.getPosition());
        final Song addedSong = SpringUtils.getSong(entityManager, SpringUtils.SONGS_COUNT + 1);
        DeepAsserts.assertEquals(song, addedSong, SONGS_COUNT_FIELD, TOTAL_LENGTH_FIELD);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#add(SongTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullArgument() {
        songFacade.add(null);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with not null ID. */
    @Test(expected = ValidationException.class)
    public void testAddWithSongWithNotNullId() {
        songFacade.add(SpringToUtils.newSongWithId(objectGenerator));
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with null name. */
    @Test(expected = ValidationException.class)
    public void testAddWithSongWithNullName() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);
        song.setName(null);

        songFacade.add(song);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with empty string as name. */
    @Test(expected = ValidationException.class)
    public void testAddWithSongWithEmptyName() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);
        song.setName("");

        songFacade.add(song);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with negative length. */
    @Test(expected = ValidationException.class)
    public void testAddWithSongWithNegativeLength() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);
        song.setLength(-1);

        songFacade.add(song);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with null note. */
    @Test(expected = ValidationException.class)
    public void testAddWithSongWithNullNote() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);
        song.setNote(null);

        songFacade.add(song);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with null TO for music. */
    @Test(expected = ValidationException.class)
    public void testAddWithSongWithNullMusic() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);
        song.setMusic(null);

        songFacade.add(song);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with TO for music with null ID. */
    @Test(expected = ValidationException.class)
    public void testAddWithSongWithMusicWithNullId() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);
        song.getMusic().setId(null);

        songFacade.add(song);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with song with not existing music. */
    @Test(expected = RecordNotFoundException.class)
    public void testAddWithSongWithNotExistingMusic() {
        final SongTO song = SpringToUtils.newSong(objectGenerator);
        song.getMusic().setId(Integer.MAX_VALUE);

        songFacade.add(song);
    }

    /** Test method for {@link SongFacade#update(SongTO)}. */
    @Test
    public void testUpdate() {
        final SongTO song = SpringToUtils.newSong(objectGenerator, 1);

        songFacade.update(song);

        final Song updatedSong = SpringUtils.getSong(entityManager, 1);
        DeepAsserts.assertEquals(song, updatedSong, SONGS_COUNT_FIELD, TOTAL_LENGTH_FIELD);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#update(SongTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullArgument() {
        songFacade.update(null);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with song with null ID. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSongWithNullId() {
        songFacade.update(SpringToUtils.newSong(objectGenerator));
    }

    /** Test method for {@link SongFacade#update(SongTO)} with song with null name. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSongWithNullName() {
        final SongTO song = SpringToUtils.newSongWithId(objectGenerator);
        song.setName(null);

        songFacade.update(song);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with song with empty string as name. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSongWithEmptyName() {
        final SongTO song = SpringToUtils.newSongWithId(objectGenerator);
        song.setName(null);

        songFacade.update(song);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with song with negative length. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSongWithNegativeLength() {
        final SongTO song = SpringToUtils.newSongWithId(objectGenerator);
        song.setLength(-1);

        songFacade.update(song);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with song with null note. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSongWithNullNote() {
        final SongTO song = SpringToUtils.newSongWithId(objectGenerator);
        song.setNote(null);

        songFacade.update(song);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with song with null TO for music. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSongWithNullMusic() {
        final SongTO song = SpringToUtils.newSongWithId(objectGenerator);
        song.setMusic(null);

        songFacade.update(song);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with song with null TO for music. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithSongWithMusicWithNullId() {
        final SongTO song = SpringToUtils.newSongWithId(objectGenerator);
        song.getMusic().setId(null);

        songFacade.update(song);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithBadId() {
        songFacade.update(SpringToUtils.newSong(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SongFacade#update(SongTO)} with not existing music. */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithNotExistingMusic() {
        final SongTO song = SpringToUtils.newSongWithId(objectGenerator);
        song.getMusic().setId(Integer.MAX_VALUE);

        songFacade.update(song);
    }

    /** Test method for {@link SongFacade#remove(SongTO)}. */
    @Test
    public void testRemove() {
        songFacade.remove(SpringToUtils.newSong(objectGenerator, 1));

        assertNull(SpringUtils.getSong(entityManager, 1));
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT - 1, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullArgument() {
        songFacade.remove(null);
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with song with null ID. */
    @Test(expected = ValidationException.class)
    public void testRemoveWithSongWithNullId() {
        songFacade.remove(SpringToUtils.newSong(objectGenerator));
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testRemoveWithBadId() {
        songFacade.remove(SpringToUtils.newSong(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)}. */
    @Test
    public void testDuplicate() {
        final Song song = SpringEntitiesUtils.getSong(SpringUtils.MUSIC_COUNT, SpringUtils.SONGS_PER_MUSIC_COUNT);
        song.setId(SpringUtils.SONGS_COUNT + 1);

        songFacade.duplicate(SpringToUtils.newSong(objectGenerator, SpringUtils.SONGS_COUNT));

        final Song duplicatedSong = SpringUtils.getSong(entityManager, SpringUtils.SONGS_COUNT + 1);
        DeepAsserts.assertEquals(song, duplicatedSong);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateWithNullArgument() {
        songFacade.duplicate(null);
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with song with null ID. */
    @Test(expected = ValidationException.class)
    public void testDuplicateWithSongWithNullId() {
        songFacade.duplicate(SpringToUtils.newSong(objectGenerator));
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicateWithBadId() {
        songFacade.duplicate(SpringToUtils.newSong(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)}. */
    @Test
    public void testMoveUp() {
        final Song song1 = SpringEntitiesUtils.getSong(1, 1);
        song1.setPosition(1);
        final Song song2 = SpringEntitiesUtils.getSong(1, 2);
        song2.setPosition(0);

        songFacade.moveUp(SpringToUtils.newSong(objectGenerator, 2));
        DeepAsserts.assertEquals(song1, SpringUtils.getSong(entityManager, 1));
        DeepAsserts.assertEquals(song2, SpringUtils.getSong(entityManager, 2));
        for (int i = 2; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUpWithNullArgument() {
        songFacade.moveUp(null);
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with song with null ID. */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithSongWithNullId() {
        songFacade.moveUp(SpringToUtils.newSong(objectGenerator));
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with not moveable argument. */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithNotMoveableArgument() {
        songFacade.moveUp(SpringToUtils.newSong(objectGenerator, 1));
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUpWithBadId() {
        songFacade.moveUp(SpringToUtils.newSong(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)}. */
    @Test
    public void testMoveDown() {
        final Song song1 = SpringEntitiesUtils.getSong(1, 1);
        song1.setPosition(1);
        final Song song2 = SpringEntitiesUtils.getSong(1, 2);
        song2.setPosition(0);

        songFacade.moveDown(SpringToUtils.newSong(objectGenerator, 1));
        DeepAsserts.assertEquals(song1, SpringUtils.getSong(entityManager, 1));
        DeepAsserts.assertEquals(song2, SpringUtils.getSong(entityManager, 2));
        for (int i = 2; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDownWithNullArgument() {
        songFacade.moveDown(null);
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with song with null ID. */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithSongWithNullId() {
        songFacade.moveDown(SpringToUtils.newSong(objectGenerator));
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with not moveable argument. */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithNotMoveableArgument() {
        songFacade.moveDown(SpringToUtils.newSong(objectGenerator, SpringUtils.SONGS_COUNT));
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDownWithBadId() {
        songFacade.moveDown(SpringToUtils.newSong(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link SongFacade#exists(SongTO)}. */
    @Test
    public void testExists() {
        for (int i = 1; i <= SpringUtils.SONGS_COUNT; i++) {
            assertTrue(songFacade.exists(SpringToUtils.newSong(objectGenerator, i)));
        }

        assertFalse(songFacade.exists(SpringToUtils.newSong(objectGenerator, Integer.MAX_VALUE)));

        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsWithNullArgument() {
        songFacade.exists(null);
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with song with null ID. */
    @Test(expected = ValidationException.class)
    public void testExistsWithSongWithNullId() {
        songFacade.exists(SpringToUtils.newSong(objectGenerator));
    }

    /** Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)}. */
    @Test
    public void testFindSongsByMusic() {
        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
            DeepAsserts.assertEquals(SpringToUtils.getSongs(i), songFacade.findSongsByMusic(SpringToUtils.newMusic(objectGenerator, i)), SONGS_COUNT_FIELD,
                    TOTAL_LENGTH_FIELD);
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
    }

    /** Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusicWithNullArgument() {
        songFacade.findSongsByMusic(null);
    }

    /** Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)} with music with null ID. */
    @Test(expected = ValidationException.class)
    public void testFindSongsByMusicWithNullId() {
        songFacade.findSongsByMusic(SpringToUtils.newMusic(objectGenerator));
    }

    /** Test method for {@link SongFacade#findSongsByMusic(cz.vhromada.catalog.facade.to.MusicTO)} with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testFindSongsByMusicWithBadId() {
        songFacade.findSongsByMusic(SpringToUtils.newMusic(objectGenerator, Integer.MAX_VALUE));
    }

}
