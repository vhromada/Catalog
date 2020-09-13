package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.facade.MovieFacade
import com.github.vhromada.common.web.controller.AbstractController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * A class represents controller for movies.
 *
 * @author Vladimir Hromada
 */
@RestController("movieController")
@RequestMapping("/catalog/movies")
class MovieController(private val movieFacade: MovieFacade) : AbstractController() {

    /**
     * Returns list of movies.
     *
     * @return list of movies
     */
    @GetMapping
    fun getMovies(): List<Movie> {
        return processResult(movieFacade.getAll())!!
    }


    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(movieFacade.newData())
    }

    /**
     * Returns movie with ID or null if there isn't such movie.
     * <br></br>
     *
     * @param id ID
     * @return movie with ID or null if there isn't such movie
     */
    @GetMapping("/{id}")
    fun getMovie(@PathVariable("id") id: Int): Movie? {
        return processResult(movieFacade.get(id))!!
    }

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
     *  * Year isn't between 1940 and current year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Media are null
     *  * Media contain null value
     *  * Length of medium is negative value
     *  * URL to ČSFD page about movie is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about movie is null
     *  * URL to czech Wikipedia page about movie is null
     *  * Path to file with movie's picture is null
     *  * Note is null
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *
     * @param movie movie
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody movie: Movie) {
        processResult(movieFacade.add(movie))
    }

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
     *  * Year isn't between 1940 and current year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Media are null
     *  * Media contain null value
     *  * Length of medium is negative value
     *  * URL to ČSFD page about movie is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about movie is null
     *  * URL to czech Wikipedia page about movie is null
     *  * Path to file with movie's picture is null
     *  * Note is null
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *  * Movie doesn't exist in data storage
     *
     * @param movie new value of movie
     * @return validation errors
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody movie: Movie) {
        processResult(movieFacade.update(movie))
    }

    /**
     * Removes movie.
     * <br></br>
     * Validation errors:
     *
     *  * Movie doesn't exist in data storage
     *
     * @param id ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("id") id: Int) {
        val movie = Movie(id = id,
                czechName = null,
                originalName = null,
                year = null,
                language = null,
                subtitles = null,
                media = null,
                csfd = null,
                imdbCode = null,
                wikiEn = null,
                wikiCz = null,
                picture = null,
                note = null,
                position = null,
                genres = null)
        processResult(movieFacade.remove(movie))
    }

    /**
     * Duplicates movie.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Movie doesn't exist in data storage
     *
     * @param movie movie
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@RequestBody movie: Movie) {
        processResult(movieFacade.duplicate(movie))
    }

    /**
     * Moves movie in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Movie can't be moved up
     *  * Movie doesn't exist in data storage
     *
     * @param movie movie
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@RequestBody movie: Movie) {
        processResult(movieFacade.moveUp(movie))
    }

    /**
     * Moves movie in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Movie can't be moved down
     *  * Movie doesn't exist in data storage
     *
     * @param movie movie
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@RequestBody movie: Movie) {
        processResult(movieFacade.moveDown(movie))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(movieFacade.updatePositions())
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @GetMapping("/totalMedia")
    fun getTotalMediaCount(): Int {
        return processResult(movieFacade.getTotalMediaCount())!!
    }

    /**
     * Returns total length of all movies.
     *
     * @return total length of all movies
     */
    @GetMapping("/totalLength")
    fun getTotalLength(): Int {
        return processResult(movieFacade.getTotalLength())!!.length
    }

}
