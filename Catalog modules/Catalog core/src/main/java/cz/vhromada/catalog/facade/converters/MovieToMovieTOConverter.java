package cz.vhromada.catalog.facade.converters;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity movie to TO for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieToMovieTOConverter")
public class MovieToMovieTOConverter implements Converter<Movie, MovieTO> {

	/** Converter from entity medium to integer */
	@Autowired
	private MediumToIntegerConverter mediumConverter;

	/** Converter from entity genre to TO for genre */
	@Autowired
	private GenreToGenreTOConverter genreConverter;

	/**
	 * Returns converter from entity medium to integer.
	 *
	 * @return converter from entity medium to integer
	 */
	public MediumToIntegerConverter getMediumConverter() {
		return mediumConverter;
	}

	/**
	 * Sets a new value to converter from entity medium to integer.
	 *
	 * @param mediumConverter new value
	 */
	public void setMediumConverter(final MediumToIntegerConverter mediumConverter) {
		this.mediumConverter = mediumConverter;
	}

	/**
	 * Returns converter from entity genre to TO for genre.
	 *
	 * @return converter from entity genre to TO for genre
	 */
	public GenreToGenreTOConverter getGenreConverter() {
		return genreConverter;
	}

	/**
	 * Sets a new value to converter from entity genre to TO for genre.
	 *
	 * @param genreConverter new value
	 */
	public void setGenreConverter(final GenreToGenreTOConverter genreConverter) {
		this.genreConverter = genreConverter;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws IllegalStateException    if converter from entity medium to integer isn't set
	 *                                  or converter from entity genre to TO for genre isn't set
	 */
	@Override
	public MovieTO convert(final Movie source) {
		Validators.validateFieldNotNull(mediumConverter, "Medium converter");
		Validators.validateFieldNotNull(genreConverter, "Genre converter");

		if (source == null) {
			return null;
		}

		final MovieTO movie = new MovieTO();
		movie.setId(source.getId());
		movie.setCzechName(source.getCzechName());
		movie.setOriginalName(source.getOriginalName());
		movie.setYear(source.getYear());
		movie.setLanguage(source.getLanguage());
		movie.setSubtitles(source.getSubtitles());
		movie.setCsfd(source.getCsfd());
		movie.setImdbCode(source.getImdbCode());
		movie.setWikiEn(source.getWikiEn());
		movie.setWikiCz(source.getWikiCz());
		movie.setPicture(source.getPicture());
		movie.setNote(source.getNote());
		movie.setPosition(source.getPosition());
		final List<Integer> media = new ArrayList<>();
		if (source.getMedia() != null) {
			for (Medium medium : source.getMedia()) {
				media.add(mediumConverter.convert(medium));
			}
		}
		movie.setMedia(media);
		final List<GenreTO> genres = new ArrayList<>();
		if (source.getGenres() != null) {
			for (Genre genre : source.getGenres()) {
				genres.add(genreConverter.convert(genre));
			}
		}
		movie.setGenres(genres);
		return movie;
	}

}
