package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.common.SongUtils;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.repository.MusicRepository;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CollectionUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link MusicServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicServiceImplTest extends AbstractServiceTest<Music> {

    /**
     * Instance of {@link MusicRepository}
     */
    @Mock
    private MusicRepository musicRepository;

    /**
     * Test method for {@link MusicServiceImpl#MusicServiceImpl(MusicRepository, Cache)} with null repository for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicRepository() {
        new MusicServiceImpl(null, getCache());
    }

    /**
     * Test method for {@link MusicServiceImpl#MusicServiceImpl(MusicRepository, Cache)} with null cache.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullCache() {
        new MusicServiceImpl(musicRepository, null);
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
        return setSongs(MusicUtils.newMusic(1));
    }

    @Override
    protected Music getItem2() {
        return setSongs(MusicUtils.newMusic(2));
    }

    @Override
    protected Music getAddItem() {
        return MusicUtils.newMusic(null);
    }

    @Override
    protected Music getCopyItem() {
        final Music music = MusicUtils.newMusic(null);
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
        final Song song = SongUtils.newSong(id);
        if (id == null) {
            song.setPosition(0);
        }
        music.setSongs(CollectionUtils.newList(song));

        return music;
    }

}
