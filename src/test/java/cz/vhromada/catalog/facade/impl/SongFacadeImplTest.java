package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.MovableChildFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableChildFacadeTest;
import cz.vhromada.common.validator.MovableValidator;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link SongFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class SongFacadeImplTest extends MovableChildFacadeTest<Song, cz.vhromada.catalog.domain.Song, Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(MovableService, MovableConverter, MovableValidator, MovableValidator)} with null service for music.
     */
    @Test
    void constructor_NullMusicService() {
        assertThatThrownBy(() -> new SongFacadeImpl(null, getConverter(), getParentMovableValidator(), getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(MovableService, MovableConverter, MovableValidator, MovableValidator)}
     * with null converter for songs.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new SongFacadeImpl(getService(), null, getParentMovableValidator(), getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(MovableService, MovableConverter, MovableValidator, MovableValidator)}
     * with null validator for music.
     */
    @Test
    void constructor_NullMusicValidator() {
        assertThatThrownBy(() -> new SongFacadeImpl(getService(), getConverter(), null, getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(MovableService, MovableConverter, MovableValidator, MovableValidator)} with null validator for song.
     */
    @Test
    void constructor_NullSongValidator() {
        assertThatThrownBy(() -> new SongFacadeImpl(getService(), getConverter(), getParentMovableValidator(), null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableChildFacade<Song, Music> getFacade() {
        return new SongFacadeImpl(getService(), getConverter(), getParentMovableValidator(), getChildMovableValidator());
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
