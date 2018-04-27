package cz.vhromada.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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

        assertSoftly(softly -> {
            softly.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT);
            softly.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT);
        });
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

        assertThat(musicRepository.findById(Integer.MAX_VALUE).isPresent()).isFalse();

        assertSoftly(softly -> {
            softly.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT);
            softly.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT);
        });
    }

    /**
     * Test method for add music.
     */
    @Test
    void add() {
        final Music music = MusicUtils.newMusicDomain(null);
        music.setPosition(MusicUtils.MUSIC_COUNT);

        musicRepository.save(music);

        assertThat(music.getId()).isEqualTo(MusicUtils.MUSIC_COUNT + 1);

        final Music addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        final Music expectedAddMusic = MusicUtils.newMusicDomain(null);
        expectedAddMusic.setId(MusicUtils.MUSIC_COUNT + 1);
        expectedAddMusic.setPosition(MusicUtils.MUSIC_COUNT);
        MusicUtils.assertMusicDeepEquals(expectedAddMusic, addedMusic);

        assertSoftly(softly -> {
            softly.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT + 1);
            softly.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT);
        });
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

        assertSoftly(softly -> {
            softly.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT);
            softly.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT);
        });
    }

    /**
     * Test method for update music with added song.
     */
    @Test
    void update_AddedSong() {
        final Song song = SongUtils.newSongDomain(null);
        song.setPosition(SongUtils.SONGS_COUNT);
        entityManager.persist(song);

        final Music music = MusicUtils.getMusic(entityManager, 1);
        music.getSongs().add(song);

        musicRepository.save(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        final Song expectedSong = SongUtils.newSongDomain(null);
        expectedSong.setId(SongUtils.SONGS_COUNT + 1);
        expectedSong.setPosition(SongUtils.SONGS_COUNT);
        final Music expectedUpdatedMusic = MusicUtils.getMusic(1);
        expectedUpdatedMusic.getSongs().add(expectedSong);
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic);

        assertSoftly(softly -> {
            softly.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT);
            softly.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT + 1);
        });
    }

    /**
     * Test method for remove music.
     */
    @Test
    void remove() {
        final int songsCount = MusicUtils.getMusic(1).getSongs().size();

        musicRepository.delete(MusicUtils.getMusic(entityManager, 1));

        assertThat(MusicUtils.getMusic(entityManager, 1)).isNull();

        assertSoftly(softly -> {
            softly.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(MusicUtils.MUSIC_COUNT - 1);
            softly.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT - songsCount);
        });
    }

    /**
     * Test method for remove all music.
     */
    @Test
    void removeAll() {
        musicRepository.deleteAll();

        assertSoftly(softly -> {
            softly.assertThat(MusicUtils.getMusicCount(entityManager)).isEqualTo(0);
            softly.assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(0);
        });
    }

}
