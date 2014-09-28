package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.MUSIC_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SONGS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SONGS_PER_MUSIC_COUNT;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.impl.SongDAOImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link SongDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class SongDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link SongDAO} */
	@Autowired
	private SongDAO songDAO;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE songs_sq RESTART WITH 10").executeUpdate();
	}

	/** Test method for {@link SongDAO#getSong(Integer)}. */
	@Test
	public void testGetSong() {
		for (int i = 0; i < SONGS_COUNT; i++) {
			final int musicNumber = i / SONGS_PER_MUSIC_COUNT + 1;
			final int songNumber = i % SONGS_PER_MUSIC_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), songDAO.getSong(i + 1));
		}

		assertNull(songDAO.getSong(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongDAO#add(Song)}. */
	@Test
	public void testAdd() {
		final Music music = SpringUtils.getMusic(entityManager, 1);
		final Song song = EntityGenerator.createSong(music);
		final Song expectedSong = EntityGenerator.createSong(SONGS_COUNT + 1, music);
		expectedSong.setPosition(SONGS_COUNT);

		songDAO.add(song);

		DeepAsserts.assertNotNull(song.getId());
		DeepAsserts.assertEquals(SONGS_COUNT + 1, song.getId());
		DeepAsserts.assertEquals(SONGS_COUNT, song.getPosition());
		final Song addedSong = SpringUtils.getSong(entityManager, SONGS_COUNT + 1);
		DeepAsserts.assertEquals(song, addedSong);
		DeepAsserts.assertEquals(expectedSong, addedSong);
		DeepAsserts.assertEquals(SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongDAO#update(Song)}. */
	@Test
	public void testUpdate() {
		final Song song = SpringEntitiesUtils.updateSong(SpringUtils.getSong(entityManager, 1));
		final Song expectedSong = EntityGenerator.createSong(1, SpringUtils.getMusic(entityManager, 1));

		songDAO.update(song);

		final Song updatedSong = SpringUtils.getSong(entityManager, 1);
		DeepAsserts.assertEquals(song, updatedSong);
		DeepAsserts.assertEquals(expectedSong, updatedSong);
		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongDAO#remove(Song)}. */
	@Test
	public void testRemove() {
		songDAO.remove(SpringUtils.getSong(entityManager, 1));

		assertNull(SpringUtils.getSong(entityManager, 1));
		DeepAsserts.assertEquals(SONGS_COUNT - 1, SpringUtils.getSongsCount(entityManager));
	}

	/** Test method for {@link SongDAO#findSongsByMusic(Music)}. */
	@Test
	public void testFindSongsByMusic() {
		for (int i = 1; i <= MUSIC_COUNT; i++) {
			final Music music = SpringUtils.getMusic(entityManager, i);
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSongs(i), songDAO.findSongsByMusic(music));
		}
		DeepAsserts.assertEquals(SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
	}

}
