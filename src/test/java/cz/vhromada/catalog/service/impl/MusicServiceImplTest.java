package cz.vhromada.catalog.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.repository.MusicRepository;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link MusicServiceImpl}.
 *
 * @author Vladimir Hromada
 */
class MusicServiceImplTest extends AbstractServiceTest<Music> {

    /**
     * Instance of {@link MusicRepository}
     */
    @Mock
    private MusicRepository musicRepository;

    /**
     * Test method for {@link MusicServiceImpl#MusicServiceImpl(MusicRepository, Cache)} with null repository for music.
     */
    @Test
    void constructor_NullMusicRepository() {
        assertThatThrownBy(() -> new MusicServiceImpl(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MusicServiceImpl#MusicServiceImpl(MusicRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new MusicServiceImpl(musicRepository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected JpaRepository<Music, Integer> getRepository() {
        return musicRepository;
    }

    @Override
    protected CatalogService<Music> getCatalogService() {
        return new MusicServiceImpl(musicRepository, getCache());
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
        music.setSongs(CollectionUtils.newList(song));

        return music;
    }

}
