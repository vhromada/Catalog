package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link ShowFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class ShowFacadeImplTest extends AbstractParentFacadeTest<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for shows.
     */
    @Test
    void constructor_NullShowService() {
        assertThatThrownBy(() -> new ShowFacadeImpl(null, getConverter(), getCatalogValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new ShowFacadeImpl(getCatalogService(), null, getCatalogValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for show.
     */
    @Test
    void constructor_NullShowValidator() {
        assertThatThrownBy(() -> new ShowFacadeImpl(getCatalogService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ShowFacade#getTotalLength()}.
     */
    @Test
    void getTotalLength() {
        final List<cz.vhromada.catalog.domain.Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Show show : showList) {
            for (final Season season : show.getSeasons()) {
                for (final Episode episode : season.getEpisodes()) {
                    totalLength += episode.getLength();
                }
            }
        }
        final int expectedTotalLength = totalLength;

        when(getCatalogService().getAll()).thenReturn(showList);

        final Result<Time> result = ((ShowFacade) getCatalogParentFacade()).getTotalLength();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(new Time(expectedTotalLength));
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link ShowFacade#getSeasonsCount()}.
     */
    @Test
    void getSeasonsCount() {
        final cz.vhromada.catalog.domain.Show show1 = ShowUtils.newShowWithSeasons(1);
        final cz.vhromada.catalog.domain.Show show2 = ShowUtils.newShowWithSeasons(2);
        final int expectedSeasons = show1.getSeasons().size() + show2.getSeasons().size();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(show1, show2));

        final Result<Integer> result = ((ShowFacade) getCatalogParentFacade()).getSeasonsCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(expectedSeasons);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link ShowFacade#getEpisodesCount()}.
     */
    @Test
    void getEpisodesCount() {
        final List<cz.vhromada.catalog.domain.Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int episodesCount = 0;
        for (final cz.vhromada.catalog.domain.Show show : showList) {
            for (final Season season : show.getSeasons()) {
                episodesCount += season.getEpisodes().size();
            }
        }
        final int expectedEpisodes = episodesCount;

        when(getCatalogService().getAll()).thenReturn(showList);

        final Result<Integer> result = ((ShowFacade) getCatalogParentFacade()).getEpisodesCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(expectedEpisodes);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    @Override
    protected void initUpdateMock(final cz.vhromada.catalog.domain.Show domain) {
        super.initUpdateMock(domain);

        when(getCatalogService().get(any(Integer.class))).thenReturn(domain);
    }

    @Override
    protected void verifyUpdateMock(final Show entity, final cz.vhromada.catalog.domain.Show domain) {
        super.verifyUpdateMock(entity, domain);

        verify(getCatalogService()).get(entity.getId());
    }

    @Override
    protected CatalogParentFacade<Show> getCatalogParentFacade() {
        return new ShowFacadeImpl(getCatalogService(), getConverter(), getCatalogValidator());
    }

    @Override
    protected Show newEntity(final Integer id) {
        return ShowUtils.newShow(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Show newDomain(final Integer id) {
        return ShowUtils.newShowDomain(id);
    }

    @Override
    protected Class<Show> getEntityClass() {
        return Show.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Show> getDomainClass() {
        return cz.vhromada.catalog.domain.Show.class;
    }

}
