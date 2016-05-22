package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.MusicUtils;
import cz.vhromada.catalog.commons.SongUtils;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link SongDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class SongDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link SongDAO}
     */
    @Autowired
    private SongDAO songDAO;

    /**
     * Test method for {@link SongDAO#getSong(Integer)}.
     */
    @Test
    public void testGetSong() {
        for (int i = 0; i < SongUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SongUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SongUtils.SONGS_PER_MUSIC_COUNT + 1;
            final Song song = songDAO.getSong(i + 1);

            SongUtils.assertSongDeepEquals(SongUtils.getSong(musicNumber, songNumber), song);
        }

        assertNull(songDAO.getSong(Integer.MAX_VALUE));

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongDAO#add(Song)}.
     */
    @Test
    public void testAdd() {
        final Song song = SongUtils.newSong(null);
        song.setMusic(1);

        songDAO.add(song);

        assertNotNull(song.getId());
        assertEquals(SongUtils.SONGS_COUNT + 1, song.getId().intValue());
        assertEquals(SongUtils.SONGS_COUNT, song.getPosition());

        final Song addedSong = SongUtils.getSong(entityManager, SongUtils.SONGS_COUNT + 1);
        final Song expectedAddedSong = SongUtils.newSong(SongUtils.SONGS_COUNT + 1);
        expectedAddedSong.setMusic(1);
        SongUtils.assertSongDeepEquals(expectedAddedSong, addedSong);

        assertEquals(SongUtils.SONGS_COUNT + 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongDAO#update(Song)}.
     */
    @Test
    public void testUpdate() {
        final Song song = SongUtils.updateSong(1, entityManager);

        songDAO.update(song);

        final Song updatedSong = SongUtils.getSong(entityManager, 1);
        final Song expectedUpdatedSong = SongUtils.getSong(1, 1);
        SongUtils.updateSong(expectedUpdatedSong);
        expectedUpdatedSong.setPosition(SongUtils.POSITION);
        SongUtils.assertSongDeepEquals(expectedUpdatedSong, updatedSong);

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongDAO#remove(Song)}.
     */
    @Test
    public void testRemove() {
        songDAO.remove(SongUtils.getSong(entityManager, 1));

        assertNull(SongUtils.getSong(entityManager, 1));

        assertEquals(SongUtils.SONGS_COUNT - 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link SongDAO#findSongsByMusic(Music)}.
     */
    @Test
    public void testFindSongsByMusic() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            final Music music = MusicUtils.getMusic(entityManager, i);

            final List<Song> songs = songDAO.findSongsByMusic(music);

            SongUtils.assertSongsDeepEquals(SongUtils.getSongs(i), songs);
        }

        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

}
