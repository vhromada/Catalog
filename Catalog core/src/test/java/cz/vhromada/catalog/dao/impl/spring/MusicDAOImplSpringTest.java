package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.MusicDAOImpl} with Spring framework.
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

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Restarts sequence. */
    @Before
    public void setUp() {
        entityManager.createNativeQuery("ALTER SEQUENCE music_sq RESTART WITH 4").executeUpdate();
    }

    /** Test method for {@link MusicDAO#getMusic()}. */
    @Test
    public void testGetMusic() {
        DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(), musicDAO.getMusic());
        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
    }

    /** Test method for {@link MusicDAO#getMusic(Integer)}. */
    @Test
    public void testGetMusicById() {
        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), musicDAO.getMusic(i));
        }

        assertNull(musicDAO.getMusic(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
    }

    /** Test method for {@link MusicDAO#add(Music)}. */
    @Test
    public void testAdd() {
        final Music music = SpringEntitiesUtils.newMusic(objectGenerator);

        musicDAO.add(music);

        DeepAsserts.assertNotNull(music.getId());
        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, music.getId());
        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, music.getPosition());
        final Music addedMusic = SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT + 1);
        DeepAsserts.assertEquals(music, addedMusic);
        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
    }

    /** Test method for {@link MusicDAO#update(Music)}. */
    @Test
    public void testUpdate() {
        final Music music = SpringEntitiesUtils.updateMusic(1, objectGenerator, entityManager);

        musicDAO.update(music);

        final Music updatedMusic = SpringUtils.getMusic(entityManager, 1);
        DeepAsserts.assertEquals(music, updatedMusic);
        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
    }

    /** Test method for {@link MusicDAO#remove(Music)}. */
    @Test
    public void testRemove() {
        final Music music = SpringEntitiesUtils.newMusic(objectGenerator);
        entityManager.persist(music);
        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));

        musicDAO.remove(music);

        assertNull(SpringUtils.getMusic(entityManager, music.getId()));
        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
    }

}
