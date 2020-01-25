package cz.vhromada.catalog.facade

import cz.vhromada.catalog.entity.Movie
import cz.vhromada.common.Time
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.result.Result

/**
 * An interface represents facade for movies.
 *
 * @author Vladimir Hromada
 */
interface MovieFacade : MovableParentFacade<Movie> {

    /**
     * Adds movie. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * Year is null
     *  * Year isn't between 1940 and current year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Media are null
     *  * Media contain null value
     *  * Length of medium is null
     *  * Length of medium is negative value
     *  * URL to ČSFD page about movie is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about movie is null
     *  * URL to czech Wikipedia page about movie is null
     *  * Note is null
     *  * Picture doesn't exist
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *
     * @param data movie
     * @return result with validation errors
     */
    override fun add(data: Movie): Result<Unit>

    /**
     * Updates movie.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * Year is null
     *  * Year isn't between 1940 and current year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Media are null
     *  * Media contain null value
     *  * Length of medium is null
     *  * Length of medium is negative value
     *  * URL to ČSFD page about movie is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about movie is null
     *  * URL to czech Wikipedia page about movie is null
     *  * Note is null
     *  * Picture doesn't exist
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *  * Movie doesn't exist in data storage
     *
     * @param data new value of movie
     * @return result with validation errors
     */
    override fun update(data: Movie): Result<Unit>

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    fun getTotalMediaCount(): Result<Int>

    /**
     * Returns total length of all songs.
     *
     * @return result with total length of all songs
     */
    fun getTotalLength(): Result<Time>

}
