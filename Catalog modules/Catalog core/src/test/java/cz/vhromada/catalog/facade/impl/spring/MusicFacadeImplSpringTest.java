package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.MUSIC_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SONGS_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.NEGATIVE_TIME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.impl.MusicFacadeImpl;
import cz.vhromada.catalog.facade.to.MusicTO;
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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);

		musicFacade.add(music);

		DeepAsserts.assertNotNull(music.getId());
		DeepAsserts.assertEquals(MUSIC_COUNT + 1, music.getId());
		final Music addedMusic = SpringUtils.getMusic(entityManager, MUSIC_COUNT + 1);
		DeepAsserts.assertEquals(music, addedMusic, "songsCount", "totalLength");
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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(Integer.MAX_VALUE);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullName() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setName(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithEmptyName() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setName("");

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to english Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullWikiEn() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setWikiEn(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to czech Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullWikiCz() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setWikiCz(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNotPositiveMediaCount() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setMediaCount(0);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with negative count of songs. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNotNegativeSongsCount() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setSongsCount(-1);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null total length. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullTotalLength() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setTotalLength(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with negative total length. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNegativeTotalLength() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setTotalLength(NEGATIVE_TIME);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with music with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithMusicWithNullNote() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);
		music.setNote(null);

		musicFacade.add(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)}. */
	@Test
	public void testUpdate() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(1);

		musicFacade.update(music);

		final Music updatedMusic = SpringUtils.getMusic(entityManager, 1);
		DeepAsserts.assertEquals(music, updatedMusic, "songsCount", "totalLength");
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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullName() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setName(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithEmptyName() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setName(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to english Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullWikiEn() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setWikiEn(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to czech Wikipedia about music. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullWikiCz() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setWikiCz(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNotPositiveMediaCount() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setMediaCount(0);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with negative count of songs. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNotNegativeSongsCount() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setSongsCount(-1);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null total length. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullTotalLength() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setTotalLength(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with negative total length. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNegativeTotalLength() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setTotalLength(NEGATIVE_TIME);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithMusicWithNullNote() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setNote(null);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with music with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithMusicWithBadId() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(Integer.MAX_VALUE);

		musicFacade.update(music);
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)}. */
	@Test
	public void testRemove() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(1);

		musicFacade.remove(music);

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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);

		musicFacade.remove(music);
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)} with music with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithMusicWithBadId() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(Integer.MAX_VALUE);

		musicFacade.remove(music);
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)}. */
	@Test
	public void testDuplicate() {
		final MusicTO musicTO = objectGenerator.generate(MusicTO.class);
		musicTO.setId(MUSIC_COUNT);
		final Music music = SpringEntitiesUtils.getMusic(MUSIC_COUNT);
		music.setId(MUSIC_COUNT + 1);

		musicFacade.duplicate(musicTO);

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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);

		musicFacade.duplicate(music);
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)} with music with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithMusicWithBadId() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(Integer.MAX_VALUE);

		musicFacade.duplicate(music);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)}. */
	@Test
	public void testMoveUp() {
		final MusicTO musicTO = objectGenerator.generate(MusicTO.class);
		musicTO.setId(2);
		final Music music1 = SpringEntitiesUtils.getMusic(1);
		music1.setPosition(1);
		final Music music2 = SpringEntitiesUtils.getMusic(2);
		music2.setPosition(0);

		musicFacade.moveUp(musicTO);
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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);

		musicFacade.moveUp(music);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(1);

		musicFacade.moveUp(music);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(Integer.MAX_VALUE);

		musicFacade.moveUp(music);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)}. */
	@Test
	public void testMoveDown() {
		final MusicTO musicTO = objectGenerator.generate(MusicTO.class);
		musicTO.setId(1);
		final Music music1 = SpringEntitiesUtils.getMusic(1);
		music1.setPosition(1);
		final Music music2 = SpringEntitiesUtils.getMusic(2);
		music2.setPosition(0);

		musicFacade.moveDown(musicTO);
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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);

		musicFacade.moveDown(music);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(MUSIC_COUNT);

		musicFacade.moveDown(music);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(Integer.MAX_VALUE);

		musicFacade.moveDown(music);
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with existing music. */
	@Test
	public void testExists() {
		for (int i = 1; i <= MUSIC_COUNT; i++) {
			final MusicTO music = objectGenerator.generate(MusicTO.class);
			music.setId(i);
			assertTrue(musicFacade.exists(music));
		}

		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(Integer.MAX_VALUE);
		assertFalse(musicFacade.exists(music));

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
		final MusicTO music = objectGenerator.generate(MusicTO.class);
		music.setId(null);

		musicFacade.exists(music);
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
