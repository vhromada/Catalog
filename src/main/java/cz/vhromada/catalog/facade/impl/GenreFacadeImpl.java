package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.GenreValidator;
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
        Validators.validateArgumentNotNull(genreService, "Service for genres");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(genreValidator, "Validator for genre");

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
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(genreService.get(id), Genre.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final Genre genre) {
        genreValidator.validateNewGenre(genre);

        genreService.add(converter.convert(genre, cz.vhromada.catalog.domain.Genre.class));
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
            final cz.vhromada.catalog.domain.Genre genreEntity = new cz.vhromada.catalog.domain.Genre();
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
    public void update(final Genre genre) {
        genreValidator.validateExistingGenre(genre);
        Validators.validateExists(genreService.get(genre.getId()), GENRE_ARGUMENT);

        genreService.update(converter.convert(genre, cz.vhromada.catalog.domain.Genre.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_ARGUMENT);

        genreService.remove(genreEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_ARGUMENT);

        genreService.duplicate(genreEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Genre> genres = genreService.getAll();
        Validators.validateMoveUp(genres, genreEntity, GENRE_ARGUMENT);

        genreService.moveUp(genreEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final Genre genre) {
        genreValidator.validateGenreWithId(genre);
        final cz.vhromada.catalog.domain.Genre genreEntity = genreService.get(genre.getId());
        Validators.validateExists(genreEntity, GENRE_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Genre> genres = genreService.getAll();
        Validators.validateMoveDown(genres, genreEntity, GENRE_ARGUMENT);

        genreService.moveDown(genreEntity);
    }

    @Override
    public void updatePositions() {
        genreService.updatePositions();
    }

}
