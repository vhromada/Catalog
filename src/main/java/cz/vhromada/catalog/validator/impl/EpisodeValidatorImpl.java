package cz.vhromada.catalog.validator.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeValidator")
public class EpisodeValidatorImpl extends AbstractCatalogValidator<Episode, Show> {

    /**
     * Creates a new instance of EpisodeValidatorImpl.
     *
     * @param showService service for show
     * @throws IllegalArgumentException if service for show is null
     */
    @Autowired
    public EpisodeValidatorImpl(final CatalogService<Show> showService) {
        super("Episode", showService);
    }

    @Override
    protected cz.vhromada.catalog.domain.Episode getData(final Episode data) {
        for (final Show show : getCatalogService().getAll()) {
            for (final Season season : show.getSeasons()) {
                for (final cz.vhromada.catalog.domain.Episode episode : season.getEpisodes()) {
                    if (data.getId().equals(episode.getId())) {
                        return episode;
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Episode> getList(final Episode data) {
        for (final Show show : getCatalogService().getAll()) {
            for (final Season season : show.getSeasons()) {
                for (final cz.vhromada.catalog.domain.Episode episode : season.getEpisodes()) {
                    if (data.getId().equals(episode.getId())) {
                        return CollectionUtils.getSortedData(season.getEpisodes());
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Validates episode deeply.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>Number of episode isn't positive number</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of episode is negative value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data   validating episode
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Episode data, final Result<Void> result) {
        if (data.getNumber() <= 0) {
            result.addEvent(new Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number."));
        }
        if (data.getName() == null) {
            result.addEvent(new Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string."));
        }
        if (data.getLength() < 0) {
            result.addEvent(new Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number."));
        }
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null."));
        }
    }

}
