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
import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class MovieFacadeImplTest extends AbstractParentFacadeTest<Movie, cz.vhromada.catalog.domain.Movie> {

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for movies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullMovieService() {
        new MovieFacadeImpl(null, getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new MovieFacadeImpl(getCatalogService(), null, getCatalogValidator());
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullMovieValidator() {
        new MovieFacadeImpl(getCatalogService(), getConverter(), null);
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Movie movie1 = MovieUtils.newMovieDomain(1);
        final cz.vhromada.catalog.domain.Movie movie2 = MovieUtils.newMovieDomain(2);
        final int expectedCount = movie1.getMedia().size() + movie2.getMedia().size();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(movie1, movie2));

        final Result<Integer> result = ((MovieFacade) getParentCatalogFacade()).getTotalMediaCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedCount));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    public void getTotalLength() {
        final List<cz.vhromada.catalog.domain.Movie> movies = CollectionUtils.newList(MovieUtils.newMovieDomain(1), MovieUtils.newMovieDomain(2));
        int expectedTotalLength = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : movies) {
            for (final Medium medium : movie.getMedia()) {
                expectedTotalLength += medium.getLength();
            }
        }

        when(getCatalogService().getAll()).thenReturn(movies);

        final Result<Time> result = ((MovieFacade) getParentCatalogFacade()).getTotalLength();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(new Time(expectedTotalLength)));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    @Override
    protected void initUpdateMock(final cz.vhromada.catalog.domain.Movie domain) {
        super.initUpdateMock(domain);

        when(getCatalogService().get(any(Integer.class))).thenReturn(domain);
    }

    @Override
    protected void verifyUpdateMock(final Movie entity, final cz.vhromada.catalog.domain.Movie domain) {
        super.verifyUpdateMock(entity, domain);

        verify(getCatalogService()).get(entity.getId());
    }

    @Override
    protected AbstractParentCatalogFacade<Movie, cz.vhromada.catalog.domain.Movie> getParentCatalogFacade() {
        return new MovieFacadeImpl(getCatalogService(), getConverter(), getCatalogValidator());
    }

    @Override
    protected Movie newEntity(final Integer id) {
        return MovieUtils.newMovie(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie newDomain(final Integer id) {
        return MovieUtils.newMovieDomain(id);
    }

    @Override
    protected Class<Movie> getEntityClass() {
        return Movie.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Movie> getDomainClass() {
        return cz.vhromada.catalog.domain.Movie.class;
    }

}
