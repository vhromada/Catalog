package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
public class GenreFacadeImpl implements GenreFacade {

    /**
     * Genre argument
     */
    private static final String GENRE_ARGUMENT = "genre";

    /**
     * TO for genre argument
     */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /**
     * Service for genres
     */
    private CatalogService<Genre> genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for genre
     */
    private GenreTOValidator genreTOValidator;

    /**
     * Creates a new instance of GenreFacadeImpl.
     *
     * @param genreService     service for genres
     * @param converter        converter
     * @param genreTOValidator validator for TO for genre
     * @throws IllegalArgumentException if service for genres is null
     *                                  or converter is null
     *                                  or validator for TO for genre is null
     */
    @Autowired
    public GenreFacadeImpl(@Qualifier("genreService") final CatalogService<Genre> genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final GenreTOValidator genreTOValidator) {
        Validators.validateArgumentNotNull(genreService, "Service for genres");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(genreTOValidator, "Validator for TO for genre");

        this.genreService = genreService;
        this.converter = converter;
        this.genreTOValidator = genreTOValidator;
    }

    @Override
    public void newData() {
        genreService.newData();
    }

    @Override
    public List<GenreTO> getGenres() {
        return converter.convertCollection(genreService.getAll(), GenreTO.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public GenreTO getGenre(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(genreService.get(id), GenreTO.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final GenreTO genre) {
        genreTOValidator.validateNewGenreTO(genre);

        genreService.add(converter.convert(genre, Genre.class));
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final List<String> genres) {
        Validators.validateArgumentNotNull(genres, "List of genre names");
        Validators.validateCollectionNotContainNull(genres, "List of genre names");

        for (final String genre : genres) {
            final Genre genreEntity = new Genre();
            genreEntity.setName(genre);
            genreService.add(genreEntity);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final GenreTO genre) {
        genreTOValidator.validateExistingGenreTO(genre);
        Validators.validateExists(genreService.get(genre.getId()), GENRE_TO_ARGUMENT);

        genreService.update(converter.convert(genre, Genre.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final GenreTO genre) {
        genreTOValidator.validateGenreTOWithId(genre);
        final Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_TO_ARGUMENT);

        genreService.remove(genreEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final GenreTO genre) {
        genreTOValidator.validateGenreTOWithId(genre);
        final Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_TO_ARGUMENT);

        genreService.duplicate(genreEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final GenreTO genre) {
        genreTOValidator.validateGenreTOWithId(genre);
        final Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_TO_ARGUMENT);
        final List<Genre> genres = genreService.getAll();
        Validators.validateMoveUp(genres, genreEntity, GENRE_ARGUMENT);

        genreService.moveUp(genreEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final GenreTO genre) {
        genreTOValidator.validateGenreTOWithId(genre);
        final Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_TO_ARGUMENT);
        final List<Genre> genres = genreService.getAll();
        Validators.validateMoveDown(genres, genreEntity, GENRE_ARGUMENT);

        genreService.moveDown(genreEntity);
    }

    @Override
    public void updatePositions() {
        genreService.updatePositions();
    }

}
