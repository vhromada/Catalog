package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.ShowValidator;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of facade for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showFacade")
public class ShowFacadeImpl implements ShowFacade {

    /**
     * Message for not existing show
     */
    private static final String NOT_EXISTING_SHOW_MESSAGE = "Show doesn't exist.";

    /**
     * Message for not movable show
     */
    private static final String NOT_MOVABLE_SHOW_MESSAGE = "ID isn't valid - show can't be moved.";

    /**
     * Message for not existing genre
     */
    private static final String NOT_EXISTING_GENRE_MESSAGE = "Genre doesn't exist.";

    /**
     * Service for shows
     */
    private CatalogService<cz.vhromada.catalog.domain.Show> showService;

    /**
     * Service for genres
     */
    private CatalogService<cz.vhromada.catalog.domain.Genre> genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for show
     */
    private ShowValidator showValidator;

    /**
     * Creates a new instance of ShowFacadeImpl.
     *
     * @param showService   service for shows
     * @param genreService  service for genres
     * @param converter     converter
     * @param showValidator validator for show
     * @throws IllegalArgumentException if service for shows is null
     *                                  or service for genres is null
     *                                  or converter is null
     *                                  or validator for show is null
     */
    @Autowired
    public ShowFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Show> showService,
            final CatalogService<cz.vhromada.catalog.domain.Genre> genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ShowValidator showValidator) {
        Assert.notNull(showService, "Service for shows mustn't be null.");
        Assert.notNull(genreService, "Service for genres mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(showValidator, "Validator for show mustn't be null.");

        this.showService = showService;
        this.genreService = genreService;
        this.converter = converter;
        this.showValidator = showValidator;
    }

    @Override
    public void newData() {
        showService.newData();
    }

    @Override
    public List<Show> getShows() {
        return converter.convertCollection(showService.getAll(), Show.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Show getShow(final Integer id) {
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(showService.get(id), Show.class);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void add(final Show show) {
        showValidator.validateNewShow(show);
        for (final Genre genre : show.getGenres()) {
            if (genreService.get(genre.getId()) == null) {
                throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
            }
        }

        final cz.vhromada.catalog.domain.Show showDomain = converter.convert(show, cz.vhromada.catalog.domain.Show.class);
        showDomain.setSeasons(new ArrayList<>());

        showService.add(showDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void update(final Show show) {
        showValidator.validateExistingShow(show);
        final cz.vhromada.catalog.domain.Show showDomain = showService.get(show.getId());
        if (showDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SHOW_MESSAGE);
        }
        showDomain.getGenres().clear();
        for (final Genre genre : show.getGenres()) {
            final cz.vhromada.catalog.domain.Genre genreDomain = genreService.get(genre.getId());
            if (genreDomain == null) {
                throw new IllegalArgumentException(NOT_EXISTING_GENRE_MESSAGE);
            }
            showDomain.getGenres().add(genreDomain);
        }

        showDomain.setCzechName(show.getCzechName());
        showDomain.setOriginalName(show.getOriginalName());
        showDomain.setCsfd(show.getCsfd());
        showDomain.setImdbCode(show.getImdbCode());
        showDomain.setWikiEn(show.getWikiEn());
        showDomain.setWikiCz(show.getWikiCz());
        showDomain.setPicture(show.getPicture());
        showDomain.setNote(show.getNote());
        showDomain.setPosition(show.getPosition());

        showService.update(showDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void remove(final Show show) {
        showValidator.validateShowWithId(show);
        final cz.vhromada.catalog.domain.Show showDomain = showService.get(show.getId());
        if (showDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SHOW_MESSAGE);
        }

        showService.remove(showDomain);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     */
    @Override
    public void duplicate(final Show show) {
        showValidator.validateShowWithId(show);
        final cz.vhromada.catalog.domain.Show showDomain = showService.get(show.getId());
        if (showDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SHOW_MESSAGE);
        }

        showService.duplicate(showDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final Show show) {
        showValidator.validateShowWithId(show);
        final cz.vhromada.catalog.domain.Show showDomain = showService.get(show.getId());
        if (showDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SHOW_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Show> shows = showService.getAll();
        if (shows.indexOf(showDomain) <= 0) {
            throw new IllegalArgumentException(NOT_MOVABLE_SHOW_MESSAGE);
        }

        showService.moveUp(showDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final Show show) {
        showValidator.validateShowWithId(show);
        final cz.vhromada.catalog.domain.Show showDomain = showService.get(show.getId());
        if (showDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SHOW_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Show> shows = showService.getAll();
        if (shows.indexOf(showDomain) >= shows.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_SHOW_MESSAGE);
        }

        showService.moveDown(showDomain);
    }

    @Override
    public void updatePositions() {
        showService.updatePositions();
    }

    @Override
    public Time getTotalLength() {
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Show show : showService.getAll()) {
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
        for (final cz.vhromada.catalog.domain.Show show : showService.getAll()) {
            seasonsCount += show.getSeasons().size();
        }

        return seasonsCount;
    }

    @Override
    public int getEpisodesCount() {
        int episodesCount = 0;
        for (final cz.vhromada.catalog.domain.Show show : showService.getAll()) {
            for (final Season season : show.getSeasons()) {
                episodesCount += season.getEpisodes().size();
            }
        }

        return episodesCount;
    }

}
