package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.MUSIC_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SONGS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SONGS_PER_MUSIC_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.MEDIA_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.NAME;
import static cz.vhromada.catalog.commons.TestConstants.NOTE;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.TOTAL_LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_CZ;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_EN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.impl.SongFacadeImpl;
import cz.vhromada.catalog.facade.to.MusicTO;
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
 * A class represents test for class {@link SongFacadeImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
//TODO vhromada 31.10.2014: implement object generator
public class SongFacadeImplSpringTest {

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
		for (Music music : SpringEntitiesUtils.getMusic()) {
			music.setId(null);
			SpringUtils.persist(transactionManager, entityManager, music);
		}
		for (int i = 1; i <= MUSIC_COUNT; i++) {
			for (Song song : SpringEntitiesUtils.getSongs(i)) {
				song.setId(null);
				SpringUtils.persist(transactionManager, entityManager, song);
			}
		}
	}

	/** Test method for {@link SongFacade#getSong(Integer)}. */
	@Test
	public void testGetSong() {
		for (int i = 0; i < SONGS_COUNT; i++) {
			final int musicNumber = i / SONGS_PER_MUSIC_COUNT + 1;
			final int songNumber = i % SONGS_PER_MUSIC_COUNT + 1;
			DeepAsserts.assertEquals(SpringToUtils.getSong(musicNumber, songNumber), songFacade.getSong(i + 1), "songsCount", "totalLength");
		}

		assertNull(songFacade.getSong(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#getSong(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetSongWithNullArgument() {
		songFacade.getSong(null);
	}

	/** Test method for {@link SongFacade#add(SongTO)}. */
	@Test
	public void testAdd() {
		final SongTO song = ToGenerator.createSong(SpringToUtils.getMusic(1));
		final Song expectedSong = EntityGenerator.createSong(SONGS_COUNT + 1, SpringEntitiesUtils.getMusic(1));
		expectedSong.setPosition(SONGS_COUNT);

		songFacade.add(song);

		DeepAsserts.assertNotNull(song.getId());
		DeepAsserts.assertEquals(SONGS_COUNT + 1, song.getId());
		DeepAsserts.assertEquals(SONGS_COUNT, song.getPosition());
		final Song addedSong = SpringUtils.getSong(entityManager, SONGS_COUNT + 1);
		DeepAsserts.assertEquals(song, addedSong, "songsCount", "totalLength");
		DeepAsserts.assertEquals(expectedSong, addedSong);
		DeepAsserts.assertEquals(SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#add(SongTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		songFacade.add(null);
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithSongWithNotNullId() {
		songFacade.add(ToGenerator.createSong(Integer.MAX_VALUE, createMusic(INNER_ID)));
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithSongWithNullName() {
		final SongTO song = ToGenerator.createSong(createMusic(INNER_ID));
		song.setName(null);

		songFacade.add(song);
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithSongWithEmptyName() {
		final SongTO song = ToGenerator.createSong(createMusic(INNER_ID));
		song.setName("");

		songFacade.add(song);
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with negative length. */
	@Test(expected = ValidationException.class)
	public void testAddWithSongWithNegativeLength() {
		final SongTO song = ToGenerator.createSong(createMusic(INNER_ID));
		song.setLength(-1);

		songFacade.add(song);
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithSongWithNullNote() {
		final SongTO song = ToGenerator.createSong(createMusic(INNER_ID));
		song.setNote(null);

		songFacade.add(song);
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with null TO for music. */
	@Test(expected = ValidationException.class)
	public void testAddWithSongWithNullMusic() {
		songFacade.add(ToGenerator.createSong());
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with TO for music with null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithSongWithMusicWithNullId() {
		songFacade.add(ToGenerator.createSong(createMusic()));
	}

	/** Test method for {@link SongFacade#add(SongTO)} with song with not existing music. */
	@Test(expected = RecordNotFoundException.class)
	public void testAddWithSongWithNotExistingMusic() {
		songFacade.add(ToGenerator.createSong(createMusic(Integer.MAX_VALUE)));
	}

	/** Test method for {@link SongFacade#update(SongTO)}. */
	@Test
	public void testUpdate() {
		final SongTO song = ToGenerator.createSong(1, SpringToUtils.getMusic(1));
		final Song expectedSong = EntityGenerator.createSong(1, SpringEntitiesUtils.getMusic(1));

		songFacade.update(song);

		final Song updatedSong = SpringUtils.getSong(entityManager, 1);
		DeepAsserts.assertEquals(song, updatedSong, "songsCount", "totalLength");
		DeepAsserts.assertEquals(expectedSong, updatedSong);
		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#update(SongTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		songFacade.update(null);
	}

	/** Test method for {@link SongFacade#update(SongTO)} with song with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSongWithNullId() {
		songFacade.update(ToGenerator.createSong(createMusic(INNER_ID)));
	}

	/** Test method for {@link SongFacade#update(SongTO)} with song with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSongWithNullName() {
		final SongTO song = ToGenerator.createSong(PRIMARY_ID, createMusic(INNER_ID));
		song.setName(null);

		songFacade.update(song);
	}

	/** Test method for {@link SongFacade#update(SongTO)} with song with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSongWithEmptyName() {
		final SongTO song = ToGenerator.createSong(PRIMARY_ID, createMusic(INNER_ID));
		song.setName(null);

		songFacade.update(song);
	}

	/** Test method for {@link SongFacade#update(SongTO)} with song with negative length. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSongWithNegativeLength() {
		final SongTO song = ToGenerator.createSong(PRIMARY_ID, createMusic(INNER_ID));
		song.setLength(-1);

		songFacade.update(song);
	}

	/** Test method for {@link SongFacade#update(SongTO)} with song with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSongWithNullNote() {
		final SongTO song = ToGenerator.createSong(PRIMARY_ID, createMusic(INNER_ID));
		song.setNote(null);

		songFacade.update(song);
	}

	/** Test method for {@link SongFacade#update(SongTO)} with song with null TO for music. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSongWithNullMusic() {
		songFacade.update(ToGenerator.createSong(PRIMARY_ID));
	}

	/** Test method for {@link SongFacade#update(SongTO)} with song with null TO for music. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithSongWithMusicWithNullId() {
		songFacade.update(ToGenerator.createSong(PRIMARY_ID, createMusic()));
	}

	/** Test method for {@link SongFacade#update(SongTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithBadId() {
		songFacade.update(ToGenerator.createSong(Integer.MAX_VALUE, createMusic(INNER_ID)));
	}

	/** Test method for {@link SongFacade#update(SongTO)} with not existing music. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithNotExistingMusic() {
		songFacade.update(ToGenerator.createSong(PRIMARY_ID, createMusic(Integer.MAX_VALUE)));
	}

	/** Test method for {@link SongFacade#remove(SongTO)}. */
	@Test
	public void testRemove() {
		songFacade.remove(ToGenerator.createSong(1));

		assertNull(SpringUtils.getSong(entityManager, 1));
		DeepAsserts.assertEquals(SONGS_COUNT - 1, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#remove(SongTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		songFacade.remove(null);
	}

	/** Test method for {@link SongFacade#remove(SongTO)} with song with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithSongWithNullId() {
		songFacade.remove(ToGenerator.createSong());
	}

	/** Test method for {@link SongFacade#remove(SongTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithBadId() {
		songFacade.remove(ToGenerator.createSong(Integer.MAX_VALUE));
	}

	/** Test method for {@link SongFacade#duplicate(SongTO)}. */
	@Test
	public void testDuplicate() {
		final Song song = SpringEntitiesUtils.getSong(MUSIC_COUNT, SONGS_PER_MUSIC_COUNT);
		song.setId(SONGS_COUNT + 1);

		songFacade.duplicate(ToGenerator.createSong(SONGS_COUNT));

		final Song duplicatedSong = SpringUtils.getSong(entityManager, SONGS_COUNT + 1);
		DeepAsserts.assertEquals(song, duplicatedSong);
		DeepAsserts.assertEquals(SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#duplicate(SongTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		songFacade.duplicate(null);
	}

	/** Test method for {@link SongFacade#duplicate(SongTO)} with song with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithSongWithNullId() {
		songFacade.duplicate(ToGenerator.createSong());
	}

	/** Test method for {@link SongFacade#duplicate(SongTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithBadId() {
		songFacade.duplicate(ToGenerator.createSong(Integer.MAX_VALUE));
	}

	/** Test method for {@link SongFacade#moveUp(SongTO)}. */
	@Test
	public void testMoveUp() {
		final Song song1 = SpringEntitiesUtils.getSong(1, 1);
		song1.setPosition(1);
		final Song song2 = SpringEntitiesUtils.getSong(1, 2);
		song2.setPosition(0);

		songFacade.moveUp(ToGenerator.createSong(2));
		DeepAsserts.assertEquals(song1, SpringUtils.getSong(entityManager, 1));
		DeepAsserts.assertEquals(song2, SpringUtils.getSong(entityManager, 2));
		for (int i = 2; i < SONGS_COUNT; i++) {
			final int musicNumber = i / SONGS_PER_MUSIC_COUNT + 1;
			final int songNumber = i % SONGS_PER_MUSIC_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#moveUp(SongTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		songFacade.moveUp(null);
	}

	/** Test method for {@link SongFacade#moveUp(SongTO)} with song with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithSongWithNullId() {
		songFacade.moveUp(ToGenerator.createSong());
	}

	/** Test method for {@link SongFacade#moveUp(SongTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		songFacade.moveUp(ToGenerator.createSong(1));
	}

	/** Test method for {@link SongFacade#moveUp(SongTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		songFacade.moveUp(ToGenerator.createSong(Integer.MAX_VALUE));
	}

	/** Test method for {@link SongFacade#moveDown(SongTO)}. */
	@Test
	public void testMoveDown() {
		final Song song1 = SpringEntitiesUtils.getSong(1, 1);
		song1.setPosition(1);
		final Song song2 = SpringEntitiesUtils.getSong(1, 2);
		song2.setPosition(0);

		songFacade.moveDown(ToGenerator.createSong(1));
		DeepAsserts.assertEquals(song1, SpringUtils.getSong(entityManager, 1));
		DeepAsserts.assertEquals(song2, SpringUtils.getSong(entityManager, 2));
		for (int i = 2; i < SONGS_COUNT; i++) {
			final int musicNumber = i / SONGS_PER_MUSIC_COUNT + 1;
			final int songNumber = i % SONGS_PER_MUSIC_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#moveDown(SongTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		songFacade.moveDown(null);
	}

	/** Test method for {@link SongFacade#moveDown(SongTO)} with song with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithSongWithNullId() {
		songFacade.moveDown(ToGenerator.createSong());
	}

	/** Test method for {@link SongFacade#moveDown(SongTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		songFacade.moveDown(ToGenerator.createSong(SONGS_COUNT));
	}

	/** Test method for {@link SongFacade#moveDown(SongTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		songFacade.moveDown(ToGenerator.createSong(Integer.MAX_VALUE));
	}

	/** Test method for {@link SongFacade#exists(SongTO)}. */
	@Test
	public void testExists() {
		for (int i = 1; i <= SONGS_COUNT; i++) {
			assertTrue(songFacade.exists(ToGenerator.createSong(i)));
		}

		assertFalse(songFacade.exists(ToGenerator.createSong(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#exists(SongTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		songFacade.exists(null);
	}

	/** Test method for {@link SongFacade#exists(SongTO)} with song with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithSongWithNullId() {
		songFacade.exists(ToGenerator.createSong());
	}

	/** Test method for {@link SongFacade#findSongsByMusic(MusicTO)}. */
	@Test
	public void testFindSongsByMusic() {
		for (int i = 1; i <= MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getSongs(i), songFacade.findSongsByMusic(createMusic(i)), "songsCount", "totalLength");
		}
		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testFindSongsByMusicWithNullArgument() {
		songFacade.findSongsByMusic(null);
	}

	/** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with music with null ID. */
	@Test(expected = ValidationException.class)
	public void testFindSongsByMusicWithNullId() {
		songFacade.findSongsByMusic(createMusic());
	}

	/** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testFindSongsByMusicWithBadId() {
		songFacade.findSongsByMusic(createMusic(Integer.MAX_VALUE));
	}

	/**
	 * Returns new TO for music.
	 *
	 * @return new TO for music
	 */
	@Deprecated
	private static MusicTO createMusic() {
		final MusicTO music = new MusicTO();
		music.setName(NAME);
		music.setWikiEn(WIKIPEDIA_EN);
		music.setWikiCz(WIKIPEDIA_CZ);
		music.setMediaCount(MEDIA_COUNT);
		music.setSongsCount(INNER_COUNT);
		music.setTotalLength(TOTAL_LENGTH);
		music.setNote(NOTE);
		music.setPosition(POSITION);

		return music;
	}

	/**
	 * Returns new TO for music with specified ID.
	 *
	 * @param id ID
	 * @return new TO for music with specified ID
	 */
	@Deprecated
	private static MusicTO createMusic(final Integer id) {
		final MusicTO music = createMusic();
		music.setId(id);
		return music;
	}

}
