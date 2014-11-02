package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity episode to TO for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeToEpisodeTOConverter")
public class EpisodeToEpisodeTOConverter implements Converter<Episode, EpisodeTO> {

	/** Converter from entity season to TO for season */
	@Autowired
	private SeasonToSeasonTOConverter converter;

	/**
	 * Returns converter from entity season to TO for season.
	 *
	 * @return converter from entity season to TO for season
	 */
	public SeasonToSeasonTOConverter getConverter() {
		return converter;
	}

	/**
	 * Sets a new value to converter from entity season to TO for season.
	 *
	 * @param converter new value
	 */
	public void setConverter(final SeasonToSeasonTOConverter converter) {
		this.converter = converter;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws IllegalStateException    if converter from entity season to TO for season isn't set
	 */
	@Override
	public EpisodeTO convert(final Episode source) {
		Validators.validateFieldNotNull(converter, "Converter");

		if (source == null) {
			return null;
		}

		final EpisodeTO episode = new EpisodeTO();
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
