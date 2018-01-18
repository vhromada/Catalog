package cz.vhromada.catalog.facade.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class MusicFacadeImplTest extends AbstractParentFacadeTest<Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for music.
     */
    @Test
    void constructor_NullMusicService() {
        assertThrows(IllegalArgumentException.class, () -> new MusicFacadeImpl(null, getConverter(), getCatalogValidator()));
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThrows(IllegalArgumentException.class, () -> new MusicFacadeImpl(getCatalogService(), null, getCatalogValidator()));
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for music.
     */
    @Test
    void constructor_NullMusicValidator() {
        assertThrows(IllegalArgumentException.class, () -> new MusicFacadeImpl(getCatalogService(), getConverter(), null));
    }

    /**
     * Test method for {@link MusicFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.newMusicDomain(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.newMusicDomain(2);
        final int expectedCount = music1.getMediaCount() + music2.getMediaCount();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(music1, music2));

        final Result<Integer> result = ((MusicFacade) getCatalogParentFacade()).getTotalMediaCount();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(Integer.valueOf(expectedCount), result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link MusicFacade#getTotalLength()}.
     */
    @Test
    void getTotalLength() {
        final List<cz.vhromada.catalog.domain.Music> musicList = CollectionUtils.newList(MusicUtils.newMusicWithSongs(1), MusicUtils.newMusicWithSongs(2));
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Music music : musicList) {
            for (final Song song : music.getSongs()) {
                totalLength += song.getLength();
            }
        }
        final int expectedTotalLength = totalLength;

        when(getCatalogService().getAll()).thenReturn(musicList);

        final Result<Time> result = ((MusicFacade) getCatalogParentFacade()).getTotalLength();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(new Time(expectedTotalLength), result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link MusicFacade#getSongsCount()}.
     */
    @Test
    void getSongsCount() {
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.newMusicWithSongs(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.newMusicWithSongs(2);
        final int expectedSongs = music1.getSongs().size() + music2.getSongs().size();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(music1, music2));

        final Result<Integer> result = ((MusicFacade) getCatalogParentFacade()).getSongsCount();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(Integer.valueOf(expectedSongs), result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    @Override
    protected void initUpdateMock(final cz.vhromada.catalog.domain.Music domain) {
        super.initUpdateMock(domain);

        when(getCatalogService().get(any(Integer.class))).thenReturn(domain);
    }

    @Override
    protected void verifyUpdateMock(final Music entity, final cz.vhromada.catalog.domain.Music domain) {
        super.verifyUpdateMock(entity, domain);

        verify(getCatalogService()).get(entity.getId());
    }

    @Override
    protected CatalogParentFacade<Music> getCatalogParentFacade() {
        return new MusicFacadeImpl(getCatalogService(), getConverter(), getCatalogValidator());
    }

    @Override
    protected Music newEntity(final Integer id) {
        return MusicUtils.newMusic(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music newDomain(final Integer id) {
        return MusicUtils.newMusicDomain(id);
    }

    @Override
    protected Class<Music> getEntityClass() {
        return Music.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Music> getDomainClass() {
        return cz.vhromada.catalog.domain.Music.class;
    }

}
