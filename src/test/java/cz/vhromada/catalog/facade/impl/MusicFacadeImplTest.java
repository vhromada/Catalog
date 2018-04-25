package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.common.Time;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableParentFacadeTest;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class MusicFacadeImplTest extends MovableParentFacadeTest<Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(MovableService, Converter, MovableValidator)} with null service for music.
     */
    @Test
    void constructor_NullMusicService() {
        assertThatThrownBy(() -> new MusicFacadeImpl(null, getConverter(), getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(MovableService, Converter, MovableValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new MusicFacadeImpl(getMovableService(), null, getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(MovableService, Converter, MovableValidator)} with null validator for music.
     */
    @Test
    void constructor_NullMusicValidator() {
        assertThatThrownBy(() -> new MusicFacadeImpl(getMovableService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MusicFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.newMusicDomain(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.newMusicDomain(2);
        final int expectedCount = music1.getMediaCount() + music2.getMediaCount();

        when(getMovableService().getAll()).thenReturn(CollectionUtils.newList(music1, music2));

        final Result<Integer> result = ((MusicFacade) getMovableParentFacade()).getTotalMediaCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(expectedCount);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getMovableService()).getAll();
        verifyNoMoreInteractions(getMovableService());
        verifyZeroInteractions(getConverter(), getMovableValidator());
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

        when(getMovableService().getAll()).thenReturn(musicList);

        final Result<Time> result = ((MusicFacade) getMovableParentFacade()).getTotalLength();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(new Time(expectedTotalLength));
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getMovableService()).getAll();
        verifyNoMoreInteractions(getMovableService());
        verifyZeroInteractions(getConverter(), getMovableValidator());
    }

    /**
     * Test method for {@link MusicFacade#getSongsCount()}.
     */
    @Test
    void getSongsCount() {
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.newMusicWithSongs(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.newMusicWithSongs(2);
        final int expectedSongs = music1.getSongs().size() + music2.getSongs().size();

        when(getMovableService().getAll()).thenReturn(CollectionUtils.newList(music1, music2));

        final Result<Integer> result = ((MusicFacade) getMovableParentFacade()).getSongsCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(expectedSongs);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getMovableService()).getAll();
        verifyNoMoreInteractions(getMovableService());
        verifyZeroInteractions(getConverter(), getMovableValidator());
    }

    @Override
    protected void initUpdateMock(final cz.vhromada.catalog.domain.Music domain) {
        super.initUpdateMock(domain);

        when(getMovableService().get(any(Integer.class))).thenReturn(domain);
    }

    @Override
    protected void verifyUpdateMock(final Music entity, final cz.vhromada.catalog.domain.Music domain) {
        super.verifyUpdateMock(entity, domain);

        verify(getMovableService()).get(entity.getId());
    }

    @Override
    protected MovableParentFacade<Music> getMovableParentFacade() {
        return new MusicFacadeImpl(getMovableService(), getConverter(), getMovableValidator());
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
