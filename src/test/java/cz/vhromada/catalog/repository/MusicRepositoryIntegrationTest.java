package cz.vhromada.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link MusicRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class MusicRepositoryIntegrationTest {

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
    void getMusic_All() {
        final List<Music> music = musicRepository.findAll(Sort.by("position", "id"));

        MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(), music);

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for get one music.
     */
    @Test
    void getMusic_One() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            final Music music = musicRepository.findById(i).orElse(null);

            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), music);
        }

        assertFalse(musicRepository.findById(Integer.MAX_VALUE).isPresent());

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for add music.
     */
    @Test
    void add() {
        final Music music = MusicUtils.newMusicDomain(null);

        musicRepository.save(music);

        assertEquals(Integer.valueOf(MusicUtils.MUSIC_COUNT + 1), music.getId());

        final Music addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        final Music expectedAddMusic = MusicUtils.newMusicDomain(null);
        expectedAddMusic.setId(MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(expectedAddMusic, addedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT + 1, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for update music with updated data.
     */
    @Test
    void update_Data() {
        final Music music = MusicUtils.updateMusic(entityManager, 1);

        musicRepository.save(music);

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
    void update_AddedSong() {
        final Song song = SongUtils.newSongDomain(null);
        entityManager.persist(song);

        final Music music = MusicUtils.getMusic(entityManager, 1);
        music.getSongs().add(song);

        musicRepository.save(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        final Song expectedSong = SongUtils.newSongDomain(null);
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
    void remove() {
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
    void removeAll() {
        musicRepository.deleteAll();

        assertEquals(0, MusicUtils.getMusicCount(entityManager));
        assertEquals(0, SongUtils.getSongsCount(entityManager));
    }

}
