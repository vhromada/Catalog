package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.MUSIC_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.SONGS_COUNT;
import static cz.vhromada.catalog.common.TestConstants.NEGATIVE_TIME;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringToUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.impl.MusicFacadeImpl;
import cz.vhromada.catalog.facade.to.MusicTO;
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
 * A class represents test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class MusicFacadeImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link PlatformTransactionManager} */
	@Autowired
	private PlatformTransactionManager transactionManager;

	/** Instance of {@link MusicFacade} */
	@Autowired
	private MusicFacade musicFacade;

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

	/** Test method for {@link MusicFacade#newData()}. */
	@Test
	public void testNewData() {
		musicFacade.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#getMusic()}. */
	@Test
	public void testGetMusic() {
		DeepAsserts.assertEquals(SpringToUtils.getMusic(), musicFacade.getMusic());
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#getMusic(Integer)}. */
	@Test
	public void testGetMusicById() {
		for (int i = 1; i <= MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getMusic(i), musicFacade.getMusic(i));
		}

		assertNull(musicFacade.getMusic(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#getMusic(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetMusicByIdWithNullArgument() {
		musicFacade.getMusic(null);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)}. */
	@Test
	public void testAdd() {
		final MusicTO music = ToGenerator.createMusic();
		final Music expectedMusic = EntityGenerator.createMusic(MUSIC_COUNT + 1);
		expectedMusic.setPosition(MUSIC_COUNT);

		musicFacade.add(music);

		DeepAsserts.assertNotNull(music.getId());
		DeepAsserts.assertEquals(MUSIC_COUNT + 1, music.getId());
		final Music addedMusic = SpringUtils.getMusic(entityManager, MUSIC_COUNT + 1);
		DeepAsserts.assertEquals(music, addedMusic, "songsCount", "totalLength");
		DeepAsserts.assertEquals(expectedMusic, addedMusic);
		DeepAsserts.assertEquals(MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		musicFacade.add(null);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNotNullId() {
		musicFacade.add(ToGenerator.createMusic(Integer.MAX_VALUE));
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullName() {
		final MusicTO music = ToGenerator.createMusic();
		music.setName(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithEmptyName() {
		final MusicTO music = ToGenerator.createMusic();
		music.setName("");

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to english Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullWikiEn() {
		final MusicTO music = ToGenerator.createMusic();
		music.setWikiEn(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to czech Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullWikiCz() {
		final MusicTO music = ToGenerator.createMusic();
		music.setWikiCz(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNotPositiveMediaCount() {
		final MusicTO music = ToGenerator.createMusic();
		music.setMediaCount(0);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with negative count of songs. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNotNegativeSongsCount() {
		final MusicTO music = ToGenerator.createMusic();
		music.setSongsCount(-1);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null total length. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullTotalLength() {
		final MusicTO music = ToGenerator.createMusic();
		music.setTotalLength(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with negative total length. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNegativeTotalLength() {
		final MusicTO music = ToGenerator.createMusic();
		music.setTotalLength(NEGATIVE_TIME);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullNote() {
		final MusicTO music = ToGenerator.createMusic();
		music.setNote(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)}. */
	@Test
	public void testUpdate() {
		final MusicTO music = ToGenerator.createMusic(1);
		final Music expectedMusic = EntityGenerator.createMusic(1);

		musicFacade.update(music);

		final Music updatedMusic = SpringUtils.getMusic(entityManager, 1);
		DeepAsserts.assertEquals(music, updatedMusic, "songsCount", "totalLength");
		DeepAsserts.assertEquals(expectedMusic, updatedMusic);
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		musicFacade.update(null);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullId() {
		musicFacade.update(ToGenerator.createMusic());
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullName() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setName(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithEmptyName() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setName(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to english Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullWikiEn() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setWikiEn(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to czech Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullWikiCz() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setWikiCz(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNotPositiveMediaCount() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setMediaCount(0);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with negative count of songs. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNotNegativeSongsCount() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setSongsCount(-1);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null total length. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullTotalLength() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setTotalLength(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with negative total length. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNegativeTotalLength() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setTotalLength(NEGATIVE_TIME);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullNote() {
		final MusicTO music = ToGenerator.createMusic(PRIMARY_ID);
		music.setNote(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithMusicWithBadId() {
		musicFacade.update(ToGenerator.createMusic(Integer.MAX_VALUE));
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)}. */
	@Test
	public void testRemove() {
		musicFacade.remove(ToGenerator.createMusic(1));

		assertNull(SpringUtils.getMusic(entityManager, 1));
		DeepAsserts.assertEquals(MUSIC_COUNT - 1, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		musicFacade.remove(null);
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)} with music with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithMusicWithNullId() {
		musicFacade.remove(ToGenerator.createMusic());
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)} with music with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithMusicWithBadId() {
		musicFacade.remove(ToGenerator.createMusic(Integer.MAX_VALUE));
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)}. */
	@Test
	public void testDuplicate() {
		final Music music = SpringEntitiesUtils.getMusic(MUSIC_COUNT);
		music.setId(MUSIC_COUNT + 1);

		musicFacade.duplicate(ToGenerator.createMusic(MUSIC_COUNT));

		final Music duplicatedMusic = SpringUtils.getMusic(entityManager, MUSIC_COUNT + 1);
		DeepAsserts.assertEquals(music, duplicatedMusic);
		DeepAsserts.assertEquals(MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		musicFacade.duplicate(null);
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)} with music with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithMusicWithNullId() {
		musicFacade.duplicate(ToGenerator.createMusic());
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)} with music with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithMusicWithBadId() {
		musicFacade.duplicate(ToGenerator.createMusic(Integer.MAX_VALUE));
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)}. */
	@Test
	public void testMoveUp() {
		final Music music1 = SpringEntitiesUtils.getMusic(1);
		music1.setPosition(1);
		final Music music2 = SpringEntitiesUtils.getMusic(2);
		music2.setPosition(0);

		musicFacade.moveUp(ToGenerator.createMusic(2));
		DeepAsserts.assertEquals(music1, SpringUtils.getMusic(entityManager, 1));
		DeepAsserts.assertEquals(music2, SpringUtils.getMusic(entityManager, 2));
		for (int i = 3; i <= MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		musicFacade.moveUp(null);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with music with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithMusicWithNullId() {
		musicFacade.moveUp(ToGenerator.createMusic());
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		musicFacade.moveUp(ToGenerator.createMusic(1));
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		musicFacade.moveUp(ToGenerator.createMusic(Integer.MAX_VALUE));
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)}. */
	@Test
	public void testMoveDown() {
		final MusicTO music = ToGenerator.createMusic(1);
		final Music music1 = SpringEntitiesUtils.getMusic(1);
		music1.setPosition(1);
		final Music music2 = SpringEntitiesUtils.getMusic(2);
		music2.setPosition(0);

		musicFacade.moveDown(music);
		DeepAsserts.assertEquals(music1, SpringUtils.getMusic(entityManager, 1));
		DeepAsserts.assertEquals(music2, SpringUtils.getMusic(entityManager, 2));
		for (int i = 3; i <= MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		musicFacade.moveDown(null);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with music with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithMusicWithNullId() {
		musicFacade.moveDown(ToGenerator.createMusic());
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		musicFacade.moveDown(ToGenerator.createMusic(MUSIC_COUNT));
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		musicFacade.moveDown(ToGenerator.createMusic(Integer.MAX_VALUE));
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with existing music. */
	@Test
	public void testExists() {
		for (int i = 1; i <= MUSIC_COUNT; i++) {
			assertTrue(musicFacade.exists(ToGenerator.createMusic(i)));
		}

		assertFalse(musicFacade.exists(ToGenerator.createMusic(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		musicFacade.exists(null);
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with music with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithMusicWithNullId() {
		musicFacade.exists(ToGenerator.createMusic());
	}

	/** Test method for {@link MusicFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		musicFacade.updatePositions();

		for (int i = 1; i <= MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		DeepAsserts.assertEquals(60, musicFacade.getTotalMediaCount());
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		DeepAsserts.assertEquals(new Time(666), musicFacade.getTotalLength());
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicFacade#getSongsCount()}. */
	@Test
	public void testGetSongsCount() {
		DeepAsserts.assertEquals(SONGS_COUNT, musicFacade.getSongsCount());
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

}