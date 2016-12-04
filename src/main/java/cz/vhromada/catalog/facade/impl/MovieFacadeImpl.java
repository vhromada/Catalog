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
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
@Transactional
public class MovieFacadeImpl implements MovieFacade {

    /**
     * Movie argument
     */
    private static final String MOVIE_ARGUMENT = "movie";

    /**
     * Genre argument
     */
    private static final String GENRE_ARGUMENT = "genre";

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
        Validators.validateArgumentNotNull(movieService, "Service for movies");
        Validators.validateArgumentNotNull(genreService, "Service for genres");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(movieValidator, "Validator for movie");

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
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(movieService.get(id), Movie.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final Movie movie) {
        movieValidator.validateNewMovie(movie);
        for (final Genre genre : movie.getGenres()) {
            Validators.validateExists(genreService.get(genre.getId()), GENRE_ARGUMENT);
        }

        movieService.add(converter.convert(movie, cz.vhromada.catalog.domain.Movie.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final Movie movie) {
        movieValidator.validateExistingMovie(movie);
        final cz.vhromada.catalog.domain.Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_ARGUMENT);
        for (final Genre genre : movie.getGenres()) {
            Validators.validateExists(genreService.get(genre.getId()), GENRE_ARGUMENT);
        }

        final cz.vhromada.catalog.domain.Movie updatedMovie = converter.convert(movie, cz.vhromada.catalog.domain.Movie.class);
        updatedMovie.setMedia(getUpdatedMedia(movieEntity.getMedia(), movie.getMedia()));

        movieService.update(updatedMovie);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_ARGUMENT);

        movieService.remove(movieEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_ARGUMENT);

        movieService.duplicate(movieEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Movie> movies = movieService.getAll();
        Validators.validateMoveUp(movies, movieEntity, MOVIE_ARGUMENT);

        movieService.moveUp(movieEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final Movie movie) {
        movieValidator.validateMovieWithId(movie);
        final cz.vhromada.catalog.domain.Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Movie> movies = movieService.getAll();
        Validators.validateMoveDown(movies, movieEntity, MOVIE_ARGUMENT);

        movieService.moveDown(movieEntity);
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
