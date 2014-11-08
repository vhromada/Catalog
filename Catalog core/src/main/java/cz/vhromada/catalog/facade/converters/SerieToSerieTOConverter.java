package cz.vhromada.catalog.facade.converters;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity serie to TO for serie.
 *
 * @author Vladimir Hromada
 */
@Component("serieToSerieTOConverter")
public class SerieToSerieTOConverter implements Converter<Serie, SerieTO> {

	/** Converter from entity genre to TO for genre */
	@Autowired
	private GenreToGenreTOConverter converter;

	/**
	 * Returns converter from entity genre to TO for genre.
	 *
	 * @return converter from entity genre to TO for genre
	 */
	public GenreToGenreTOConverter getConverter() {
		return converter;
	}

	/**
	 * Sets a new value to converter from entity genre to TO for genre.
	 *
	 * @param converter new value
	 */
	public void setConverter(final GenreToGenreTOConverter converter) {
		this.converter = converter;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws IllegalStateException    if converter from entity genre to TO for genre isn't set
	 */
	@Override
	public SerieTO convert(final Serie source) {
		Validators.validateFieldNotNull(converter, "Converter");

		if (source == null) {
			return null;
		}

		final SerieTO serie = new SerieTO();
		serie.setId(source.getId());
		serie.setCzechName(source.getCzechName());
		serie.setOriginalName(source.getOriginalName());
		serie.setCsfd(source.getCsfd());
		serie.setImdbCode(source.getImdbCode());
		serie.setWikiEn(source.getWikiEn());
		serie.setWikiCz(source.getWikiCz());
		serie.setPicture(source.getPicture());
		serie.setNote(source.getNote());
		serie.setPosition(source.getPosition());
		final List<GenreTO> genres = new ArrayList<>();
		if (source.getGenres() != null) {
			for (Genre genre : source.getGenres()) {
				genres.add(converter.convert(genre));
			}
		}
		serie.setGenres(genres);
		return serie;
	}

}