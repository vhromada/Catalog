package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.CatalogValidators;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.catalog.facade.validators.MovieTOValidator;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieTOValidator")
public class MovieTOValidatorImpl implements MovieTOValidator {

	/** Validator for TO for genre */
	@Autowired
	private GenreTOValidator genreTOValidator;

	/**
	 * Returns validator for TO for genre.
	 *
	 * @return validator for TO for genre
	 */
	public GenreTOValidator getGenreTOValidator() {
		return genreTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for genre.
	 *
	 * @param genreTOValidator new value
	 */
	public void setGenreTOValidator(final GenreTOValidator genreTOValidator) {
		this.genreTOValidator = genreTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if validator for TO for genre isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 */
	@Override
	public void validateNewMovieTO(final MovieTO movie) {
		validateMovieTO(movie);
		Validators.validateNull(movie.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if validator for TO for genre isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 */
	@Override
	public void validateExistingMovieTO(final MovieTO movie) {
		validateMovieTO(movie);
		Validators.validateNotNull(movie.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 */
	@Override
	public void validateMovieTOWithId(final MovieTO movie) {
		Validators.validateArgumentNotNull(movie, "TO for movie");
		Validators.validateNotNull(movie.getId(), "ID");
	}

	/**
	 * Validates TO for movie.
	 *
	 * @param movie validating TO for movie
	 * @throws IllegalStateException    if validator for TO for genre isn't set
	 * @throws IllegalArgumentException if TO for movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if czech name is null
	 *                                  or czech name is empty string
	 *                                  or original name is null
	 *                                  or original name is empty string
	 *                                  or year isn't between 1940 and current year
	 *                                  or language is null
	 *                                  or subtitles are null
	 *                                  or subtitles contain null value
	 *                                  or media are null
	 *                                  or media contain null value
	 *                                  or media contain negative value
	 *                                  or URL to ČSFD page about movie is null
	 *                                  or IMDB code isn't -1 or between 1 and 9999999
	 *                                  or URL to english Wikipedia page about movie is null
	 *                                  or URL to czech Wikipedia page about movie is null
	 *                                  or path to file with movie's picture is null
	 *                                  or note is null
	 *                                  or genres contain null value
	 *                                  or genre ID is null
	 *                                  or genre name is null
	 *                                  or genre name is empty string
	 */
	private void validateMovieTO(final MovieTO movie) {
		Validators.validateFieldNotNull(genreTOValidator, "Validator for TO for genre");
		Validators.validateArgumentNotNull(movie, "TO for movie");
		Validators.validateNotNull(movie.getCzechName(), "Czech name");
		Validators.validateNotEmptyString(movie.getCzechName(), "Czech name");
		Validators.validateNotNull(movie.getOriginalName(), "Original name");
		Validators.validateNotEmptyString(movie.getOriginalName(), "Original name");
		CatalogValidators.validateYear(movie.getYear(), "Year");
		Validators.validateNotNull(movie.getLanguage(), "Language");
		Validators.validateNotNull(movie.getSubtitles(), "Subtitles");
		Validators.validateCollectionNotContainNull(movie.getSubtitles(), "Subtitles");
		final String mediaField = "Media";
		Validators.validateNotNull(movie.getMedia(), mediaField);
		Validators.validateCollectionNotContainNull(movie.getMedia(), mediaField);
		for (Integer medium : movie.getMedia()) {
			Validators.validateNotNegativeNumber(medium, mediaField);
		}
		Validators.validateNotNull(movie.getCsfd(), "URL to ČSFD page about movie");
		CatalogValidators.validateImdbCode(movie.getImdbCode(), "IMDB code");
		Validators.validateNotNull(movie.getWikiEn(), "URL to english Wikipedia page about movie");
		Validators.validateNotNull(movie.getWikiCz(), "URL to czech Wikipedia page about movie");
		Validators.validateNotNull(movie.getPicture(), "Path to file with movie's picture");
		Validators.validateNotNull(movie.getNote(), "Note");
		Validators.validateNotNull(movie.getGenres(), "Genres");
		Validators.validateCollectionNotContainNull(movie.getGenres(), "Genres");
		for (GenreTO genre : movie.getGenres()) {
			genreTOValidator.validateExistingGenreTO(genre);
		}
	}

}
