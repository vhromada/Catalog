package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SongUtils;
import cz.vhromada.catalog.dao.entities.Song;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link SongRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testRepositoryContext.xml")
@Transactional
@Rollback
public class SongRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link SongRepository}
     */
    @Autowired
    private SongRepository songRepository;

    /**
     * Test method for get song.
     */
    @Test
    public void testGetSong() {
        for (int i = 1; i <= SongUtils.SONGS_COUNT; i++) {
            final Song song = songRepository.findOne(i);

            SongUtils.assertSongDeepEquals(SongUtils.getSong(i), song);
        }

        assertNull(songRepository.findOne(Integer.MAX_VALUE));

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for update song.
     */
    @Test
    public void testUpdate() {
        final Song song = SongUtils.updateSong(entityManager, 1);

        songRepository.saveAndFlush(song);

        final Song updatedSong = SongUtils.getSong(entityManager, 1);
        final Song expectedUpdatedSong = SongUtils.getSong(1);
        SongUtils.updateSong(expectedUpdatedSong);
        expectedUpdatedSong.setPosition(SongUtils.POSITION);
        SongUtils.assertSongDeepEquals(expectedUpdatedSong, updatedSong);

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for remove song.
     */
    @Test
    public void testRemove() {
        songRepository.delete(SongUtils.getSong(entityManager, 1));

        assertNull(SongUtils.getSong(entityManager, 1));

        assertEquals(SongUtils.SONGS_COUNT - 1, SongUtils.getSongsCount(entityManager));
    }

}
