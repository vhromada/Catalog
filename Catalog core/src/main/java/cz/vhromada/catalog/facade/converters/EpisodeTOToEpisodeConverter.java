package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for episode to entity episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeTOToEpisodeConverter")
public class EpisodeTOToEpisodeConverter implements Converter<EpisodeTO, Episode> {

    /** Converter from TO for season to entity season */
    private SeasonTOToSeasonConverter converter;

    /**
     * Creates a new instance of EpisodeTOToEpisodeConverter.
     *
     * @param converter converter from TO for season to entity season
     * @throws IllegalArgumentException if converter from TO for season to entity season is null
     */
    @Autowired
    public EpisodeTOToEpisodeConverter(final SeasonTOToSeasonConverter converter) {
        Validators.validateArgumentNotNull(converter, "Converter");

        this.converter = converter;
    }

    @Override
    public Episode convert(final EpisodeTO source) {
        if (source == null) {
            return null;
        }

        final Episode episode = new Episode();
        episode.setId(source.getId());
        episode.setNumber(source.getNumber());
        episode.setName(source.getName());
        episode.setLength(source.getLength());
        episode.setNote(source.getNote());
        episode.setPosition(source.getPosition());
        episode.setSeason(converter.convert(source.getSeason()));
        return episode;
    }

}
