package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.MusicUtils;
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.entities.Music;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link MusicDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class MusicDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link MusicDAO}
     */
    @Autowired
    private MusicDAO musicDAO;

    /**
     * Test method for {@link MusicDAO#getMusic()}.
     */
    @Test
    public void testGetMusic() {
        final List<Music> music = musicDAO.getMusic();

        MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(), music);

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
    }

    /**
     * Test method for {@link MusicDAO#getMusic(Integer)}.
     */
    @Test
    public void testGetMusicItem() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            final Music music = musicDAO.getMusic(i);

            assertNotNull(music);
            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), music);
        }

        assertNull(musicDAO.getMusic(Integer.MAX_VALUE));

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
    }

    /**
     * Test method for {@link MusicDAO#add(Music)}.
     */
    @Test
    public void testAdd() {
        final Music music = MusicUtils.newMusic(null);

        musicDAO.add(music);

        assertNotNull(music.getId());
        assertEquals(MusicUtils.MUSIC_COUNT + 1, music.getId().intValue());
        assertEquals(MusicUtils.MUSIC_COUNT, music.getPosition());

        final Music addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusic(MusicUtils.MUSIC_COUNT + 1), addedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT + 1, MusicUtils.getMusicCount(entityManager));
    }

    /**
     * Test method for {@link MusicDAO#update(Music)}.
     */
    @Test
    public void testUpdate() {
        final Music music = MusicUtils.updateMusic(1, entityManager);

        musicDAO.update(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        final Music expectedUpdatedMusic = MusicUtils.getMusic(1);
        MusicUtils.updateMusic(expectedUpdatedMusic);
        expectedUpdatedMusic.setPosition(MusicUtils.POSITION);
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
    }

    /**
     * Test method for {@link MusicDAO#remove(Music)}.
     */
    @Test
    public void testRemove() {
        final Music music = MusicUtils.newMusic(null);
        entityManager.persist(music);
        assertEquals(MusicUtils.MUSIC_COUNT + 1, MusicUtils.getMusicCount(entityManager));

        musicDAO.remove(music);

        assertNull(MusicUtils.getMusic(entityManager, music.getId()));
        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
    }

}
