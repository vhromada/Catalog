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
class MovieController(
    private val movieFacade: MovieFacade
) : AbstractController() {

    /**
     * Returns list of movies.
     *
     * @return list of movies
     */
    @GetMapping
    fun getMovies(): List<Movie> {
        return processResult(result = movieFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(result = movieFacade.newData())
    }

    /**
     * Returns movie with ID.
     * <br></br>
     *
     * @param id ID
     * @return movie with ID
     */
    @GetMapping("/{id}")
    fun getMovie(@PathVariable("id") id: Int): Movie {
        return processResult(result = movieFacade.get(id = id))!!
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
        processResult(result = movieFacade.add(data = movie))
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
        processResult(result = movieFacade.update(data = movie))
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
        processResult(result = movieFacade.remove(id = id))
    }

    /**
     * Duplicates movie.
     * <br></br>
     * Validation errors:
     *
     *  * Movie doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/duplicate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@PathVariable("id") id: Int) {
        processResult(result = movieFacade.duplicate(id = id))
    }

    /**
     * Moves movie in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Movie can't be moved up
     *  * Movie doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveUp/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@PathVariable("id") id: Int) {
        processResult(result = movieFacade.moveUp(id = id))
    }

    /**
     * Moves movie in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Movie can't be moved down
     *  * Movie doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveDown/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@PathVariable("id") id: Int) {
        processResult(result = movieFacade.moveDown(id = id))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(result = movieFacade.updatePositions())
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @GetMapping("/totalMedia")
    fun getTotalMediaCount(): Int {
        return processResult(result = movieFacade.getTotalMediaCount())!!
    }

    /**
     * Returns total length of all movies.
     *
     * @return total length of all movies
     */
    @GetMapping("/totalLength")
    fun getTotalLength(): Int {
        return processResult(result = movieFacade.getTotalLength())!!.length
    }

}
