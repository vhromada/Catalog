package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.entity.MediumTO;
import cz.vhromada.catalog.entity.MovieTO;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validators.MovieTOValidator;
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
     * TO for movie argument
     */
    private static final String MOVIE_TO_ARGUMENT = "TO for movie";

    /**
     * TO for genre argument
     */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /**
     * Service for movies
     */
    private CatalogService<Movie> movieService;

    /**
     * Service for genres
     */
    private CatalogService<Genre> genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for movie
     */
    private MovieTOValidator movieTOValidator;

    /**
     * Creates a new instance of MovieFacadeImpl.
     *
     * @param movieService     service for movies
     * @param genreService     service for genres
     * @param converter        converter
     * @param movieTOValidator validator for TO for movie
     * @throws IllegalArgumentException if service for movies is null
     *                                  or service for genres is null
     *                                  or converter is null
     *                                  or validator for TO for movie is null
     */
    @Autowired
    public MovieFacadeImpl(final CatalogService<Movie> movieService,
            final CatalogService<Genre> genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MovieTOValidator movieTOValidator) {
        Validators.validateArgumentNotNull(movieService, "Service for movies");
        Validators.validateArgumentNotNull(genreService, "Service for genres");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(movieTOValidator, "Validator for TO for movie");

        this.movieService = movieService;
        this.genreService = genreService;
        this.converter = converter;
        this.movieTOValidator = movieTOValidator;
    }

    @Override
    public void newData() {
        movieService.newData();
    }

    @Override
    public List<MovieTO> getMovies() {
        return converter.convertCollection(movieService.getAll(), MovieTO.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public MovieTO getMovie(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(movieService.get(id), MovieTO.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final MovieTO movie) {
        movieTOValidator.validateNewMovieTO(movie);
        for (final GenreTO genre : movie.getGenres()) {
            Validators.validateExists(genreService.get(genre.getId()), GENRE_TO_ARGUMENT);
        }

        movieService.add(converter.convert(movie, Movie.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final MovieTO movie) {
        movieTOValidator.validateExistingMovieTO(movie);
        final Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);
        for (final GenreTO genre : movie.getGenres()) {
            Validators.validateExists(genreService.get(genre.getId()), GENRE_TO_ARGUMENT);
        }

        final Movie updatedMovie = converter.convert(movie, Movie.class);
        updatedMovie.setMedia(getUpdatedMedia(movieEntity.getMedia(), movie.getMedia()));

        movieService.update(updatedMovie);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        final Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);

        movieService.remove(movieEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        final Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);

        movieService.duplicate(movieEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        final Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);
        final List<Movie> movies = movieService.getAll();
        Validators.validateMoveUp(movies, movieEntity, MOVIE_TO_ARGUMENT);

        movieService.moveUp(movieEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        final Movie movieEntity = movieService.get(movie.getId());
        Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);
        final List<Movie> movies = movieService.getAll();
        Validators.validateMoveDown(movies, movieEntity, MOVIE_TO_ARGUMENT);

        movieService.moveDown(movieEntity);
    }

    @Override
    public void updatePositions() {
        movieService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMediaCount = 0;
        for (final Movie movie : movieService.getAll()) {
            totalMediaCount += movie.getMedia().size();
        }

        return totalMediaCount;
    }

    @Override
    public Time getTotalLength() {
        int totalLength = 0;
        for (final Movie movie : movieService.getAll()) {
            for (final Medium medium : movie.getMedia()) {
                totalLength += medium.getLength();
            }
        }

        return new Time(totalLength);
    }

    /**
     * Updates media.
     *
     * @param originalMedia original list of media
     * @param updatedMedia  updated list of TO for medium
     * @return updated media
     */
    private static List<Medium> getUpdatedMedia(final List<Medium> originalMedia, final List<MediumTO> updatedMedia) {
        final List<Medium> result = new ArrayList<>();

        int index = 0;
        final int max = Math.min(originalMedia.size(), updatedMedia.size());
        while (index < max) {
            final Medium medium = new Medium();
            medium.setId(originalMedia.get(index).getId());
            medium.setNumber(index + 1);
            medium.setLength(updatedMedia.get(index).getLength());
            result.add(medium);
            index++;
        }
        while (index < updatedMedia.size()) {
            final Medium medium = new Medium();
            medium.setNumber(index + 1);
            medium.setLength(updatedMedia.get(index).getLength());
            result.add(medium);
            index++;
        }

        return result;
    }

}
