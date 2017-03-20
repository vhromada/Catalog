package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.facade.CatalogChildFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;

import org.junit.Test;

/**
 * A class represents test for class {@link SongFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class SongFacadeImplTest extends AbstractChildFacadeTest<Song, cz.vhromada.catalog.domain.Song, Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null service for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullMusicService() {
        new SongFacadeImpl(null, getConverter(), getParentCatalogValidator(), getChildCatalogValidator());
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new SongFacadeImpl(getCatalogService(), null, getParentCatalogValidator(), getChildCatalogValidator());
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullMusicValidator() {
        new SongFacadeImpl(getCatalogService(), getConverter(), null, getChildCatalogValidator());
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullSongValidator() {
        new SongFacadeImpl(getCatalogService(), getConverter(), getParentCatalogValidator(), null);
    }

    @Override
    protected CatalogChildFacade<Song, Music> getCatalogChildFacade() {
        return new SongFacadeImpl(getCatalogService(), getConverter(), getParentCatalogValidator(), getChildCatalogValidator());
    }

    @Override
    protected Music newParentEntity(final Integer id) {
        return MusicUtils.newMusic(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music newParentDomain(final Integer id) {
        return MusicUtils.newMusicWithSongs(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music newParentDomainWithChildren(final Integer id, final List<cz.vhromada.catalog.domain.Song> children) {
        final cz.vhromada.catalog.domain.Music music = newParentDomain(id);
        music.setSongs(children);

        return music;
    }

    @Override
    protected Song newChildEntity(final Integer id) {
        return SongUtils.newSong(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Song newChildDomain(final Integer id) {
        return SongUtils.newSongDomain(id);
    }

    @Override
    protected Class<Music> getParentEntityClass() {
        return Music.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Music> getParentDomainClass() {
        return cz.vhromada.catalog.domain.Music.class;
    }

    @Override
    protected Class<Song> getChildEntityClass() {
        return Song.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Song> getChildDomainClass() {
        return cz.vhromada.catalog.domain.Song.class;
    }

    @Override
    protected void assertParentDeepEquals(final cz.vhromada.catalog.domain.Music expected, final cz.vhromada.catalog.domain.Music actual) {
        MusicUtils.assertMusicDeepEquals(expected, actual);
    }

}
