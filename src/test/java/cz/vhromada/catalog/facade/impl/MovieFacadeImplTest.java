package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.common.Time;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableParentFacadeTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class MovieFacadeImplTest extends MovableParentFacadeTest<Movie, cz.vhromada.catalog.domain.Movie> {

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(MovableService, MovableConverter, MovableValidator)} with null service for movies.
     */
    @Test
    void constructor_NullMovieService() {
        assertThatThrownBy(() -> new MovieFacadeImpl(null, getConverter(), getValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(MovableService, MovableConverter, MovableValidator)} with null converter for movies.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new MovieFacadeImpl(getService(), null, getValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(MovableService, MovableConverter, MovableValidator)} with null validator for movie.
     */
    @Test
    void constructor_NullMovieValidator() {
        assertThatThrownBy(() -> new MovieFacadeImpl(getService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Movie movie1 = MovieUtils.newMovieDomain(1);
        final cz.vhromada.catalog.domain.Movie movie2 = MovieUtils.newMovieDomain(2);
        final int expectedCount = movie1.getMedia().size() + movie2.getMedia().size();

        when(getService().getAll()).thenReturn(List.of(movie1, movie2));

        final Result<Integer> result = ((MovieFacade) getFacade()).getTotalMediaCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(expectedCount);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getService()).getAll();
        verifyNoMoreInteractions(getService());
        verifyZeroInteractions(getConverter(), getValidator());
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    void getTotalLength() {
        final List<cz.vhromada.catalog.domain.Movie> movies = List.of(MovieUtils.newMovieDomain(1), MovieUtils.newMovieDomain(2));
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : movies) {
            for (final Medium medium : movie.getMedia()) {
                totalLength += medium.getLength();
            }
        }
        final int expectedTotalLength = totalLength;

        when(getService().getAll()).thenReturn(movies);

        final Result<Time> result = ((MovieFacade) getFacade()).getTotalLength();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(new Time(expectedTotalLength));
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getService()).getAll();
        verifyNoMoreInteractions(getService());
        verifyZeroInteractions(getConverter(), getValidator());
    }

    @Override
    protected void initUpdateMock(final cz.vhromada.catalog.domain.Movie domain) {
        super.initUpdateMock(domain);

        when(getService().get(any(Integer.class))).thenReturn(domain);
    }

    @Override
    protected void verifyUpdateMock(final Movie entity, final cz.vhromada.catalog.domain.Movie domain) {
        super.verifyUpdateMock(entity, domain);

        verify(getService()).get(entity.getId());
    }

    @Override
    protected MovableParentFacade<Movie> getFacade() {
        return new MovieFacadeImpl(getService(), getConverter(), getValidator());
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
