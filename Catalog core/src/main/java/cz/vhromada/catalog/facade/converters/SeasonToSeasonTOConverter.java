package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity season to TO for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonToSeasonTOConverter")
public class SeasonToSeasonTOConverter implements Converter<Season, SeasonTO> {

    /** Converter from entity serie to TO for serie */
    private SerieToSerieTOConverter converter;

    /**
     * Creates a new instance of SeasonToSeasonTOConverter.
     *
     * @param converter converter from entity serie to TO for serie
     * @throws IllegalArgumentException if converter from entity serie to TO for serie is null
     */
    @Autowired
    public SeasonToSeasonTOConverter(final SerieToSerieTOConverter converter) {
        Validators.validateArgumentNotNull(converter, "Converter");

        this.converter = converter;
    }

    @Override
    public SeasonTO convert(final Season source) {
        if (source == null) {
            return null;
        }

        final SeasonTO season = new SeasonTO();
        season.setId(source.getId());
        season.setNumber(source.getNumber());
        season.setStartYear(source.getStartYear());
        season.setEndYear(source.getEndYear());
        season.setLanguage(source.getLanguage());
        season.setSubtitles(source.getSubtitles());
        season.setNote(source.getNote());
        season.setPosition(source.getPosition());
        season.setSerie(converter.convert(source.getSerie()));
        return season;
    }

}
