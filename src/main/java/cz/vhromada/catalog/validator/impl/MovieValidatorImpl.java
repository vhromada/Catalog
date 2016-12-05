package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.Constants;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.catalog.validator.MovieValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieValidator")
public class MovieValidatorImpl implements MovieValidator {

    /**
     * Validator for genre
     */
    private GenreValidator genreValidator;

    /**
     * Creates a new instance of MovieValidatorImpl.
     *
     * @param genreValidator validator for genre
     * @throws IllegalArgumentException if validator for genre is null
     */
    @Autowired
    public MovieValidatorImpl(final GenreValidator genreValidator) {
        Assert.notNull(genreValidator, "Validator for genre mustn't be null.");

        this.genreValidator = genreValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewMovie(final Movie movie) {
        validateMovie(movie);
        Assert.isNull(movie.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingMovie(final Movie movie) {
        validateMovie(movie);
        Assert.notNull(movie.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateMovieWithId(final Movie movie) {
        Assert.notNull(movie, "Movie mustn't be null.");
        Assert.notNull(movie.getId(), "ID mustn't be null.");
    }

    /**
     * Validates movie.
     *
     * @param movie validating movie
     * @throws IllegalArgumentException if movie is null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or year isn't between 1940 and current year
     *                                  or language is null
     *                                  or subtitles are null
     *                                  or subtitles contain null value
     *                                  or media are null
     *                                  or media contain null value
     *                                  or length of medium is negative value
     *                                  or URL to ČSFD page about movie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about movie is null
     *                                  or URL to czech Wikipedia page about movie is null
     *                                  or path to file with movie's picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    private void validateMovie(final Movie movie) {
        Assert.notNull(movie, "Movie mustn't be null.");
        Assert.notNull(movie.getCzechName(), "Czech name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(movie.getCzechName()) && !StringUtils.isEmpty(movie.getCzechName().trim()), "Czech name mustn't be empty string.");
        Assert.notNull(movie.getOriginalName(), "Original name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(movie.getOriginalName()) && !StringUtils.isEmpty(movie.getOriginalName().trim()),
                "Original name mustn't be empty string.");
        Assert.isTrue(movie.getYear() >= Constants.MIN_YEAR && movie.getYear() <= Constants.CURRENT_YEAR, "Year must be between " + Constants.MIN_YEAR
                + " and " + Constants.CURRENT_YEAR + ".");
        Assert.notNull(movie.getLanguage(), "Language mustn't be null.");
        Assert.notNull(movie.getSubtitles(), "Subtitles mustn't be null.");
        Assert.isTrue(!movie.getSubtitles().contains(null), "Subtitles mustn't contain null value.");
        Assert.notNull(movie.getMedia(), "Media mustn't be null.");
        Assert.isTrue(!movie.getMedia().contains(null), "Media mustn't contain null value.");
        for (final Medium medium : movie.getMedia()) {
            Assert.isTrue(medium.getLength() > 0, "Length of medium must be positive number.");
        }
        Assert.notNull(movie.getCsfd(), "URL to ČSFD page about movie mustn't be null.");
        Assert.isTrue(movie.getImdbCode() == -1 || (movie.getImdbCode() >= 1 && movie.getImdbCode() <= Constants.MAX_IMDB_CODE),
                "IMDB code must be between 1 and 9999999 or -1.");
        Assert.notNull(movie.getWikiEn(), "URL to english Wikipedia page about movie mustn't be null.");
        Assert.notNull(movie.getWikiCz(), "URL to czech Wikipedia page about movie mustn't be null.");
        Assert.notNull(movie.getPicture(), "Path to file with movie's picture mustn't be null.");
        Assert.notNull(movie.getNote(), "Note mustn't be null.");
        Assert.notNull(movie.getGenres(), "Genres mustn't be null.");
        Assert.isTrue(!movie.getGenres().contains(null), "Genres mustn't contain null value.");
        for (final Genre genre : movie.getGenres()) {
            genreValidator.validateExistingGenre(genre);
        }
    }

}
