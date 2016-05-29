package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.MusicUtils;
import cz.vhromada.catalog.commons.SongUtils;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link MusicRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testRepositoryContext.xml")
@Transactional
@Rollback
public class MusicRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link MusicRepository}
     */
    @Autowired
    private MusicRepository musicRepository;

    /**
     * Test method for get all music.
     */
    @Test
    public void testGetMusic_All() {
        final List<Music> music = musicRepository.findAll(new Sort("position", "id"));

        MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(), music);

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for get one music.
     */
    @Test
    public void testGetMusic_One() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            final Music music = musicRepository.findOne(i);

            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), music);
        }

        assertNull(musicRepository.findOne(Integer.MAX_VALUE));

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for add music.
     */
    @Test
    public void testAdd() {
        final Music music = MusicUtils.newMusic(null);

        musicRepository.saveAndFlush(music);

        assertNotNull(music.getId());
        assertEquals(MusicUtils.MUSIC_COUNT + 1, music.getId().intValue());

        final Music addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        final Music expectedAddMusic = MusicUtils.newMusic(null);
        expectedAddMusic.setId(MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(expectedAddMusic, addedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT + 1, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for update music with updated data.
     */
    @Test
    public void testUpdate_Data() {
        final Music music = MusicUtils.updateMusic(entityManager, 1);

        musicRepository.saveAndFlush(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        final Music expectedUpdatedMusic = MusicUtils.getMusic(1);
        MusicUtils.updateMusic(expectedUpdatedMusic);
        expectedUpdatedMusic.setPosition(MusicUtils.POSITION);
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for update music with added song.
     */
    @Test
    public void testUpdate_Song() {
        final Song song = SongUtils.newSong(null);
        entityManager.persist(song);

        final Music music = MusicUtils.getMusic(entityManager, 1);
        music.getSongs().add(song);

        musicRepository.saveAndFlush(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        final Song expectedSong = SongUtils.newSong(null);
        expectedSong.setId(SongUtils.SONGS_COUNT + 1);
        final Music expectedUpdatedMusic = MusicUtils.getMusic(1);
        expectedUpdatedMusic.getSongs().add(expectedSong);
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT + 1, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for remove music.
     */
    @Test
    public void testRemove() {
        final int songsCount = MusicUtils.getMusic(1).getSongs().size();

        musicRepository.delete(MusicUtils.getMusic(entityManager, 1));

        assertNull(MusicUtils.getMusic(entityManager, 1));

        assertEquals(MusicUtils.MUSIC_COUNT - 1, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT - songsCount, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for remove all music.
     */
    @Test
    public void testRemoveAll() {
        musicRepository.deleteAll();

        assertEquals(0, MusicUtils.getMusicCount(entityManager));
        assertEquals(0, SongUtils.getSongsCount(entityManager));
    }

}
