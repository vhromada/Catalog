package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
public class GenreFacadeImpl implements GenreFacade {

    /**
     * Message for not existing genre
     */
    private static final String NOT_EXISTING_GENRE_MESSAGE = "Genre doesn't exist.";

    /**
     * Message for not movable genre
     */
    private static final String NOT_MOVABLE_GENRE_MESSAGE = "ID isn't valid - genre can't be moved.";

    /**
     * Service for genres
     */
    private CatalogService<cz.vhromada.catalog.domain.Genre> genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for genre
     */
    private GenreValidator genreValidator;

    /**
     * Creates a new instance of GenreFacadeImpl.
     *
     * @param genreService   service for genres
     * @param converter      converter
     * @param genreValidator validator for genre
     * @throws IllegalArgumentException if service for genres is null
     *                                  or converter is null
     *                                  or validator for genre is null
     */
    @Autowired
    public GenreFacadeImpl(@Qualifier("genreService") final CatalogService<cz.vhromada.catalog.domain.Genre> genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final GenreValidator genreValidator) {
        Assert.notNull(genreService, "Service for genres mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(genreValidator, "Validator for genre mustn't be null.");

        this.genreService = genreService;
        this.converter = converter;
        this.genreValidator = genreValidator;
    }

    @Override
    public void newData() {
        genreService.newData();
    }

    @Override
    public List<Genre> getGenres() {
        return converter.convertCollection(genreService.getAll(), Genre.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Genre getGenre(final Integer id) {
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(genreService.get(id), Genre.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     */
    @Override
    public void add(final Genre genre) {
        genreValidator.validateNewGenre(genre);

        genreService.add(converter.convert(genre, cz.vhromada.catalog.domain.Genre.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void update(final Genre genre) {
        genreValidator.validateExistingGenre(genre);
        if (genreService.get(genre.getId()) == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
        }

        genreService.update(converter.convert(genre, cz.vhromada.catalog.domain.Genre.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void remove(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreDomain = genreService.get(genre.getId());
        if (genreDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
        }

        genreService.remove(genreDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void duplicate(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreDomain = genreService.get(genre.getId());
        if (genreDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
        }

        genreService.duplicate(genreDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc
     */
    @Override
    public void moveUp(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreDomain = genreService.get(genre.getId());
        if (genreDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Genre> genres = genreService.getAll();
        if (genres.indexOf(genreDomain) <= 0) {
            throw new IllegalArgumentException(NOT_MOVABLE_GENRE_MESSAGE);
        }

        genreService.moveUp(genreDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreDomain = genreService.get(genre.getId());
        if (genreDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Genre> genres = genreService.getAll();
        if (genres.indexOf(genreDomain) >= genres.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_GENRE_MESSAGE);
        }

        genreService.moveDown(genreDomain);
    }

    @Override
    public void updatePositions() {
        genreService.updatePositions();
    }

}
