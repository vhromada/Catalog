package cz.vhromada.catalog.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;

import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.repository.MusicRepository;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.common.repository.MovableRepository;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.service.MovableServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;

/**
 * A class represents test for class {@link MusicService}.
 *
 * @author Vladimir Hromada
 */
class MusicServiceTest extends MovableServiceTest<Music> {

    /**
     * Instance of {@link MusicRepository}
     */
    @Mock
    private MusicRepository repository;

    /**
     * Test method for {@link MusicService#MusicService(MusicRepository, Cache)} with null repository for music.
     */
    @Test
    void constructor_NullMusicRepository() {
        assertThatThrownBy(() -> new MusicService(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MusicService#MusicService(MusicRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new MusicService(repository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableRepository<Music> getRepository() {
        return repository;
    }

    @Override
    protected MovableService<Music> getService() {
        return new MusicService(repository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "music";
    }

    @Override
    protected Music getItem1() {
        return setSongs(MusicUtils.newMusicDomain(1));
    }

    @Override
    protected Music getItem2() {
        return setSongs(MusicUtils.newMusicDomain(2));
    }

    @Override
    protected Music getAddItem() {
        return MusicUtils.newMusicDomain(null);
    }

    @Override
    protected Music getCopyItem() {
        final Music music = MusicUtils.newMusicDomain(null);
        music.setPosition(0);
        setSongs(music);

        return music;
    }

    @Override
    protected Class<Music> getItemClass() {
        return Music.class;
    }

    @Override
    protected void assertDataDeepEquals(final Music expected, final Music actual) {
        MusicUtils.assertMusicDeepEquals(expected, actual);
    }

    /**
     * Sets songs to music.
     *
     * @param music music
     * @return music with songs
     */
    private static Music setSongs(final Music music) {
        final Integer id = music.getId();
        final Song song = SongUtils.newSongDomain(id);
        if (id == null) {
            song.setPosition(0);
        }
        music.setSongs(Collections.singletonList(song));

        return music;
    }

}
