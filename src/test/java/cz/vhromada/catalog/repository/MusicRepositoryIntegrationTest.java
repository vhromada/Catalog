package cz.vhromada.catalog.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;

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
@ContextConfiguration(classes = CatalogTestConfiguration.class)
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
    public void getMusic_All() {
        final List<Music> music = musicRepository.findAll(new Sort("position", "id"));

        MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(), music);

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for get one music.
     */
    @Test
    public void getMusic_One() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            final Music music = musicRepository.findOne(i);

            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), music);
        }

        assertThat(musicRepository.findOne(Integer.MAX_VALUE), is(nullValue()));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for add music.
     */
    @Test
    public void add() {
        final Music music = MusicUtils.newMusicDomain(null);

        musicRepository.saveAndFlush(music);

        assertThat(music.getId(), is(notNullValue()));
        assertThat(music.getId(), is(MusicUtils.MUSIC_COUNT + 1));

        final Music addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        final Music expectedAddMusic = MusicUtils.newMusicDomain(null);
        expectedAddMusic.setId(MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(expectedAddMusic, addedMusic);

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT + 1));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for update music with updated data.
     */
    @Test
    public void update_Data() {
        final Music music = MusicUtils.updateMusic(entityManager, 1);

        musicRepository.saveAndFlush(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        final Music expectedUpdatedMusic = MusicUtils.getMusic(1);
        MusicUtils.updateMusic(expectedUpdatedMusic);
        expectedUpdatedMusic.setPosition(MusicUtils.POSITION);
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic);

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT));
    }

    /**
     * Test method for update music with added song.
     */
    @Test
    public void update_AddedSong() {
        final Song song = SongUtils.newSongDomain(null);
        entityManager.persist(song);

        final Music music = MusicUtils.getMusic(entityManager, 1);
        music.getSongs().add(song);

        musicRepository.saveAndFlush(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        final Song expectedSong = SongUtils.newSongDomain(null);
        expectedSong.setId(SongUtils.SONGS_COUNT + 1);
        final Music expectedUpdatedMusic = MusicUtils.getMusic(1);
        expectedUpdatedMusic.getSongs().add(expectedSong);
        MusicUtils.assertMusicDeepEquals(expectedUpdatedMusic, updatedMusic);

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT + 1));
    }

    /**
     * Test method for remove music.
     */
    @Test
    public void remove() {
        final int songsCount = MusicUtils.getMusic(1).getSongs().size();

        musicRepository.delete(MusicUtils.getMusic(entityManager, 1));

        assertThat(MusicUtils.getMusic(entityManager, 1), is(nullValue()));

        assertThat(MusicUtils.getMusicCount(entityManager), is(MusicUtils.MUSIC_COUNT - 1));
        assertThat(SongUtils.getSongsCount(entityManager), is(SongUtils.SONGS_COUNT - songsCount));
    }

    /**
     * Test method for remove all music.
     */
    @Test
    public void removeAll() {
        musicRepository.deleteAll();

        assertThat(MusicUtils.getMusicCount(entityManager), is(0));
        assertThat(SongUtils.getSongsCount(entityManager), is(0));
    }

}
