package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.ShowFacade
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
 * A class represents controller for shows.
 *
 * @author Vladimir Hromada
 */
@RestController("showController")
@RequestMapping("/catalog/shows")
class ShowController(private val showFacade: ShowFacade) : AbstractController() {

    /**
     * Returns list of shows.
     *
     * @return list of shows
     */
    @GetMapping
    fun getShows(): List<Show> {
        return processResult(showFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(showFacade.newData())
    }

    /**
     * Returns show with ID or null if there isn't such show.
     *
     * @param id ID
     * @return show with ID or null if there isn't such sho
     */
    @GetMapping("/{id}")
    fun getShow(@PathVariable("id") id: Int): Show? {
        return processResult(showFacade.get(id))
    }

    /**
     * Adds show. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * URL to ČSFD page about show is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about show is null
     *  * URL to czech Wikipedia page about show is null
     *  * Path to file with show's picture is null
     *  * Note is null
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *
     * @param show show
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody show: Show) {
        processResult(showFacade.add(show))
    }

    /**
     * Updates show.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * URL to ČSFD page about show is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about show is null
     *  * URL to czech Wikipedia page about show is null
     *  * Path to file with show's picture is null
     *  * Note is null
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *  * Show doesn't exist in data storage
     *
     * @param show new value of show
     */
    @PostMapping("/update")
    fun update(@RequestBody show: Show) {
        processResult(showFacade.update(show))
    }

    /**
     * Removes show.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *
     * @param id ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("id") id: Int) {
        val show = Show(id = id, czechName = null, originalName = null, csfd = null, imdbCode = null, wikiEn = null, wikiCz = null, picture = null, note = null, position = null, genres = null)
        processResult(showFacade.remove(show))
    }

    /**
     * Duplicates show.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Show doesn't exist in data storage
     *
     * @param show show
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@RequestBody show: Show) {
        processResult(showFacade.duplicate(show))
    }

    /**
     * Moves show in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Show can't be moved up
     *  * Show doesn't exist in data storage
     *
     * @param show show
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@RequestBody show: Show) {
        processResult(showFacade.moveUp(show))
    }

    /**
     * Moves show in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Show can't be moved down
     *  * Show doesn't exist in data storage
     *
     * @param show show
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@RequestBody show: Show) {
        processResult(showFacade.moveDown(show))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(showFacade.updatePositions())
    }


    /**
     * Returns total length of all shows.
     *
     * @return total length of all shows
     */
    @GetMapping("/totalLength")
    fun getTotalLength(): Int {
        return processResult(showFacade.getTotalLength())!!.length
    }

    /**
     * Returns count of seasons from all shows.
     *
     * @return count of seasons from all shows
     */
    @GetMapping("/seasonsCount")
    fun getSeasonsCount(): Int {
        return processResult(showFacade.getSeasonsCount())!!
    }

    /**
     * Returns count of episodes from all shows.
     *
     * @return  count of episodes from all shows
     */
    @GetMapping("/episodesCount")
    fun getEpisodesCount(): Int {
        return processResult(showFacade.getEpisodesCount())!!
    }

}
