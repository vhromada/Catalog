package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link ShowFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class ShowFacadeImplTest extends AbstractParentFacadeTest<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullShowService() {
        new ShowFacadeImpl(null, getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new ShowFacadeImpl(getCatalogService(), null, getCatalogValidator());
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullShowValidator() {
        new ShowFacadeImpl(getCatalogService(), getConverter(), null);
    }

    /**
     * Test method for {@link ShowFacade#getTotalLength()}.
     */
    @Test
    public void getTotalLength() {
        final List<cz.vhromada.catalog.domain.Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int expectedTotalLength = 0;
        for (final cz.vhromada.catalog.domain.Show show : showList) {
            for (final Season season : show.getSeasons()) {
                for (final Episode episode : season.getEpisodes()) {
                    expectedTotalLength += episode.getLength();
                }
            }
        }

        when(getCatalogService().getAll()).thenReturn(showList);

        final Result<Time> result = ((ShowFacade) getParentCatalogFacade()).getTotalLength();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(new Time(expectedTotalLength)));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link ShowFacade#getSeasonsCount()}.
     */
    @Test
    public void getSeasonsCount() {
        final cz.vhromada.catalog.domain.Show show1 = ShowUtils.newShowWithSeasons(1);
        final cz.vhromada.catalog.domain.Show show2 = ShowUtils.newShowWithSeasons(2);
        final int expectedSeasons = show1.getSeasons().size() + show2.getSeasons().size();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(show1, show2));

        final Result<Integer> result = ((ShowFacade) getParentCatalogFacade()).getSeasonsCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedSeasons));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link ShowFacade#getEpisodesCount()}.
     */
    @Test
    public void getEpisodesCount() {
        final List<cz.vhromada.catalog.domain.Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int expectedEpisodes = 0;
        for (final cz.vhromada.catalog.domain.Show show : showList) {
            for (final Season season : show.getSeasons()) {
                expectedEpisodes += season.getEpisodes().size();
            }
        }

        when(getCatalogService().getAll()).thenReturn(showList);

        final Result<Integer> result = ((ShowFacade) getParentCatalogFacade()).getEpisodesCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedEpisodes));
        assertThat(result.getEvents().isEmpty(), is(true));

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
    protected AbstractParentCatalogFacade<Show, cz.vhromada.catalog.domain.Show> getParentCatalogFacade() {
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
