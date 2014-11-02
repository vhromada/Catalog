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
	@Autowired
	private SerieToSerieTOConverter converter;

	/**
	 * Returns converter from entity serie to TO for serie.
	 *
	 * @return converter from entity serie to TO for serie
	 */
	public SerieToSerieTOConverter getConverter() {
		return converter;
	}

	/**
	 * Sets a new value to converter from entity serie to TO for serie.
	 *
	 * @param converter new value
	 */
	public void setConverter(final SerieToSerieTOConverter converter) {
		this.converter = converter;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws IllegalStateException    if converter from entity serie to TO for serie isn't set
	 */
	@Override
	public SeasonTO convert(final Season source) {
		Validators.validateFieldNotNull(converter, "Converter");

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
