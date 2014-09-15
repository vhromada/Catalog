package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.MUSIC_COUNT;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.impl.MusicDAOImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link MusicDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class MusicDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link MusicDAO} */
	@Autowired
	private MusicDAO musicDAO;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE music_sq RESTART WITH 4").executeUpdate();
	}

	/** Test method for {@link MusicDAO#getMusic()}. */
	@Test
	public void testGetMusic() {
		DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(), musicDAO.getMusic());
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicDAO#getMusic(Integer)}. */
	@Test
	public void testGetMusicById() {
		for (int i = 1; i <= MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), musicDAO.getMusic(i));
		}

		assertNull(musicDAO.getMusic(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicDAO#add(Music)}. */
	@Test
	public void testAdd() {
		final Music music = EntityGenerator.createMusic();
		final Music expectedMusic = EntityGenerator.createMusic(MUSIC_COUNT + 1);
		expectedMusic.setPosition(MUSIC_COUNT);

		musicDAO.add(music);

		DeepAsserts.assertNotNull(music.getId());
		DeepAsserts.assertEquals(MUSIC_COUNT + 1, music.getId());
		DeepAsserts.assertEquals(MUSIC_COUNT, music.getPosition());
		final Music addedMusic = SpringUtils.getMusic(entityManager, MUSIC_COUNT + 1);
		DeepAsserts.assertEquals(music, addedMusic);
		DeepAsserts.assertEquals(expectedMusic, addedMusic);
		DeepAsserts.assertEquals(MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicDAO#update(Music)}. */
	@Test
	public void testUpdate() {
		final Music music = SpringEntitiesUtils.updateMusic(SpringUtils.getMusic(entityManager, 1));
		final Music expectedMusic = EntityGenerator.createMusic(1);

		musicDAO.update(music);

		final Music updatedMusic = SpringUtils.getMusic(entityManager, 1);
		DeepAsserts.assertEquals(music, updatedMusic);
		DeepAsserts.assertEquals(expectedMusic, updatedMusic);
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

	/** Test method for {@link MusicDAO#remove(Music)}. */
	@Test
	public void testRemove() {
		final Music music = EntityGenerator.createMusic();
		entityManager.persist(music);
		DeepAsserts.assertEquals(MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));

		musicDAO.remove(music);

		assertNull(SpringUtils.getMusic(entityManager, music.getId()));
		DeepAsserts.assertEquals(MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
	}

}
