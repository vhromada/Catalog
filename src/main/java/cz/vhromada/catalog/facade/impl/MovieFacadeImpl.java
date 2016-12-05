package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.MovieValidator;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
@Transactional
public class MovieFacadeImpl implements MovieFacade {

    /**
     * Message for not existing movie
     */
    private static final String NOT_EXISTING_MOVIE_MESSAGE = "Movie doesn't exist.";

    /**
     * Message for not movable movie
     */
    private static final String NOT_MOVABLE_MOVIE_MESSAGE = "ID isn't valid - movie can't be moved.";

    /**
     * Message for not existing genre
     */
    private static final String NOT_EXISTING_GENRE_MESSAGE = "Genre doesn't exist.";

    /**
     * Service for movies
     */
    private CatalogService<cz.vhromada.catalog.domain.Movie> movieService;

    /**
     * Service for genres
     */
    private CatalogService<cz.vhromada.catalog.domain.Genre> genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for movie
     */
    private MovieValidator movieValidator;

    /**
     * Creates a new instance of MovieFacadeImpl.
     *
     * @param movieService   service for movies
     * @param genreService   service for genres
     * @param converter      converter
     * @param movieValidator validator for movie
     * @throws IllegalArgumentException if service for movies is null
     *                                  or service for genres is null
     *                                  or converter is null
     *                                  or validator for movie is null
     */
    @Autowired
    public MovieFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Movie> movieService,
            final CatalogService<cz.vhromada.catalog.domain.Genre> genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MovieValidator movieValidator) {
        Assert.notNull(movieService, "Service for movies mustn't be null.");
        Assert.notNull(genreService, "Service for genres mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(movieValidator, "Validator for movie mustn't be null.");

        this.movieService = movieService;
        this.genreService = genreService;
        this.converter = converter;
        this.movieValidator = movieValidator;
    }

    @Override
    public void newData() {
        movieService.newData();
    }

    @Override
    public List<Movie> getMovies() {
        return converter.convertCollection(movieService.getAll(), Movie.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Movie getMovie(final Integer id) {
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(movieService.get(id), Movie.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void add(final Movie movie) {
        movieValidator.validateNewMovie(movie);
        for (final Genre genre : movie.getGenres()) {
            if (genreService.get(genre.getId()) == null) {
                throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
            }
        }

        movieService.add(converter.convert(movie, cz.vhromada.catalog.domain.Movie.class));
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void update(final Movie movie) {
        movieValidator.validateExistingMovie(movie);
        final cz.vhromada.catalog.domain.Movie movieDomain = movieService.get(movie.getId());
        if (movieDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MOVIE_MESSAGE);
        }
        for (final Genre genre : movie.getGenres()) {
            if (genreService.get(genre.getId()) == null) {
                throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
            }
        }

        final cz.vhromada.catalog.domain.Movie updatedMovie = converter.convert(movie, cz.vhromada.catalog.domain.Movie.class);
        updatedMovie.setMedia(getUpdatedMedia(movieDomain.getMedia(), movie.getMedia()));

        movieService.update(updatedMovie);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void remove(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieDomain = movieService.get(movie.getId());
        if (movieDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MOVIE_MESSAGE);
        }

        movieService.remove(movieDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void duplicate(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieDomain = movieService.get(movie.getId());
        if (movieDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MOVIE_MESSAGE);
        }

        movieService.duplicate(movieDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveUp(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieDomain = movieService.get(movie.getId());
        if (movieDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MOVIE_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Movie> movies = movieService.getAll();
        if (movies.indexOf(movieDomain) <= 0) {
            throw new IllegalArgumentException(NOT_MOVABLE_MOVIE_MESSAGE);
        }

        movieService.moveUp(movieDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveDown(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieDomain = movieService.get(movie.getId());
        if (movieDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MOVIE_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Movie> movies = movieService.getAll();
        if (movies.indexOf(movieDomain) >= movies.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_MOVIE_MESSAGE);
        }

        movieService.moveDown(movieDomain);
    }

    @Override
    public void updatePositions() {
        movieService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMediaCount = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : movieService.getAll()) {
            totalMediaCount += movie.getMedia().size();
        }

        return totalMediaCount;
    }

    @Override
    public Time getTotalLength() {
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : movieService.getAll()) {
            for (final cz.vhromada.catalog.domain.Medium medium : movie.getMedia()) {
                totalLength += medium.getLength();
            }
        }

        return new Time(totalLength);
    }

    /**
     * Updates media.
     *
     * @param originalMedia original media
     * @param updatedMedia  updated media
     * @return updated media
     */
    private static List<cz.vhromada.catalog.domain.Medium> getUpdatedMedia(final List<cz.vhromada.catalog.domain.Medium> originalMedia,
            final List<Medium> updatedMedia) {
        final List<cz.vhromada.catalog.domain.Medium> result = new ArrayList<>();

        int index = 0;
        final int max = Math.min(originalMedia.size(), updatedMedia.size());
        while (index < max) {
            final cz.vhromada.catalog.domain.Medium medium = new cz.vhromada.catalog.domain.Medium();
            medium.setId(originalMedia.get(index).getId());
            medium.setNumber(index + 1);
            medium.setLength(updatedMedia.get(index).getLength());
            result.add(medium);
            index++;
        }
        while (index < updatedMedia.size()) {
            final cz.vhromada.catalog.domain.Medium medium = new cz.vhromada.catalog.domain.Medium();
            medium.setNumber(index + 1);
            medium.setLength(updatedMedia.get(index).getLength());
            result.add(medium);
            index++;
        }

        return result;
    }

}
