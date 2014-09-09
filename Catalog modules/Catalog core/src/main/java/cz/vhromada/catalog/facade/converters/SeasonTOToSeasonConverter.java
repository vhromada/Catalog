package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for season to entity season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonTOToSeasonConverter")
public class SeasonTOToSeasonConverter implements Converter<SeasonTO, Season> {

	/** Converter from TO for serie to entity serie */
	@Autowired
	private SerieTOToSerieConverter converter;

	/**
	 * Returns converter from TO for serie to entity serie.
	 *
	 * @return converter from TO for serie to entity serie
	 */
	public SerieTOToSerieConverter getConverter() {
		return converter;
	}

	/**
	 * Sets a new value to converter from TO for serie to entity serie.
	 *
	 * @param converter new value
	 */
	public void setConverter(final SerieTOToSerieConverter converter) {
		this.converter = converter;
	}

	@Override
	public Season convert(final SeasonTO source) {
		Validators.validateFieldNotNull(converter, "Converter");

		if (source == null) {
			return null;
		}

		final Season season = new Season();
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
