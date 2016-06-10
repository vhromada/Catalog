package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.entities.Episode;
import cz.vhromada.catalog.entities.Genre;
import cz.vhromada.catalog.entities.Season;
import cz.vhromada.catalog.entities.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.ShowTO;
import cz.vhromada.catalog.facade.validators.ShowTOValidator;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showFacade")
public class ShowFacadeImpl implements ShowFacade {

    /**
     * TO for show argument
     */
    private static final String SHOW_TO_ARGUMENT = "TO for show";

    /**
     * TO for genre argument
     */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /**
     * Service for shows
     */
    private CatalogService<Show> showService;

    /**
     * Service for genres
     */
    private CatalogService<Genre> genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for show
     */
    private ShowTOValidator showTOValidator;

    /**
     * Creates a new instance of ShowFacadeImpl.
     *
     * @param showService     service for shows
     * @param genreService    service for genres
     * @param converter       converter
     * @param showTOValidator validator for TO for show
     * @throws IllegalArgumentException if service for shows is null
     *                                  or service for genres is null
     *                                  or converter is null
     *                                  or validator for TO for show is null
     */
    @Autowired
    public ShowFacadeImpl(final CatalogService<Show> showService,
            final CatalogService<Genre> genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ShowTOValidator showTOValidator) {
        Validators.validateArgumentNotNull(showService, "Service for shows");
        Validators.validateArgumentNotNull(genreService, "Service for genres");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(showTOValidator, "Validator for TO for show");

        this.showService = showService;
        this.genreService = genreService;
        this.converter = converter;
        this.showTOValidator = showTOValidator;
    }

    @Override
    public void newData() {
        showService.newData();
    }

    @Override
    public List<ShowTO> getShows() {
        return converter.convertCollection(showService.getAll(), ShowTO.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public ShowTO getShow(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(showService.get(id), ShowTO.class);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void add(final ShowTO show) {
        showTOValidator.validateNewShowTO(show);
        for (final GenreTO genre : show.getGenres()) {
            Validators.validateExists(genreService.get(genre.getId()), GENRE_TO_ARGUMENT);
        }

        showService.add(converter.convert(show, Show.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final ShowTO show) {
        showTOValidator.validateExistingShowTO(show);
        Validators.validateExists(showService.get(show.getId()), SHOW_TO_ARGUMENT);
        for (final GenreTO genre : show.getGenres()) {
            Validators.validateExists(genreService.get(genre.getId()), GENRE_TO_ARGUMENT);
        }

        showService.update(converter.convert(show, Show.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        final Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);

        showService.remove(showEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void duplicate(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        final Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);

        showService.duplicate(showEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        final Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);
        final List<Show> shows = showService.getAll();
        Validators.validateMoveUp(shows, showEntity, SHOW_TO_ARGUMENT);

        showService.moveUp(showEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        final Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);
        final List<Show> shows = showService.getAll();
        Validators.validateMoveDown(shows, showEntity, SHOW_TO_ARGUMENT);

        showService.moveDown(showEntity);
    }

    @Override
    public void updatePositions() {
        showService.updatePositions();
    }

    @Override
    public Time getTotalLength() {
        int totalLength = 0;
        for (final Show show : showService.getAll()) {
            for (final Season season : show.getSeasons()) {
                for (final Episode episode : season.getEpisodes()) {
                    totalLength += episode.getLength();
                }
            }
        }

        return new Time(totalLength);
    }

    @Override
    public int getSeasonsCount() {
        int seasonsCount = 0;
        for (final Show show : showService.getAll()) {
            seasonsCount += show.getSeasons().size();
        }

        return seasonsCount;
    }

    @Override
    public int getEpisodesCount() {
        int episodesCount = 0;
        for (final Show show : showService.getAll()) {
            for (final Season season : show.getSeasons()) {
                episodesCount += season.getEpisodes().size();
            }
        }

        return episodesCount;
    }

}
