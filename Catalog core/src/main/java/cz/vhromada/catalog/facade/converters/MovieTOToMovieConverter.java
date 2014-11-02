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
 * A class represents converter from TO for genre to entity movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieTOToMovieConverter")
public class MovieTOToMovieConverter implements Converter<MovieTO, Movie> {

	/** Converter from integer to entity medium */
	@Autowired
	private IntegerToMediumConverter mediumConverter;

	/** Converter from TO for genre to entity genre */
	@Autowired
	private GenreTOToGenreConverter genreConverter;

	/**
	 * Returns converter from integer to entity medium.
	 *
	 * @return converter from integer to entity medium
	 */
	public IntegerToMediumConverter getMediumConverter() {
		return mediumConverter;
	}

	/**
	 * Sets a new value to converter from integer to entity medium.
	 *
	 * @param mediumConverter new value
	 */
	public void setMediumConverter(final IntegerToMediumConverter mediumConverter) {
		this.mediumConverter = mediumConverter;
	}

	/**
	 * Returns converter from TO for genre to entity genre.
	 *
	 * @return converter from TO for genre to entity genre
	 */
	public GenreTOToGenreConverter getGenreConverter() {
		return genreConverter;
	}

	/**
	 * Sets a new value to converter from TO for genre to entity genre.
	 *
	 * @param genreConverter new value
	 */
	public void setGenreConverter(final GenreTOToGenreConverter genreConverter) {
		this.genreConverter = genreConverter;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws IllegalStateException    if converter from integer to medium isn't set
	 *                                  or converter from TO for genre to entity genre isn't set
	 */
	@Override
	public Movie convert(final MovieTO source) {
		Validators.validateFieldNotNull(mediumConverter, "Medium converter");
		Validators.validateFieldNotNull(genreConverter, "Genre converter");

		if (source == null) {
			return null;
		}

		final Movie movie = new Movie();
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
		movie.setMedia(convertMedia(source.getMedia()));
		final List<Genre> genres = new ArrayList<>();
		if (source.getGenres() != null) {
			for (GenreTO genre : source.getGenres()) {
				genres.add(genreConverter.convert(genre));
			}
		}
		movie.setGenres(genres);
		return movie;
	}

	/**
	 * Returns converted media.
	 *
	 * @param source converting media
	 * @return converted media
	 */
	private List<Medium> convertMedia(final List<Integer> source) {
		final List<Medium> media = new ArrayList<>();
		if (source != null) {
			for (int i = 0; i < source.size(); i++) {
				final int length = source.get(i);
				final Medium medium = mediumConverter.convert(length);
				if (medium != null) {
					medium.setNumber(i + 1);
				}
				media.add(medium);
			}
		}

		return media;
	}

}
