package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.facade.SeasonFacade
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
 * A class represents controller for seasons.
 *
 * @author Vladimir Hromada
 */
@RestController("seasonController")
@RequestMapping("/catalog/shows/{showId}/seasons")
class SeasonController(
    private val seasonFacade: SeasonFacade,
    private val showFacade: ShowFacade
) : AbstractController() {

    /**
     * Returns season with ID.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return season with ID
     */
    @GetMapping("/{seasonId}")
    fun getSeason(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int
    ): Season {
        processResult(result = showFacade.get(id = showId))
        return processResult(result = seasonFacade.get(id = seasonId))!!
    }

    /**
     * Adds season. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in season storage
     *  * Season ID isn't null
     *  * Season position isn't null
     *  * Number of season isn't positive number
     *  * Starting year isn't between 1940 and current year
     *  * Ending year isn't between 1940 and current year
     *  * Starting year is greater than ending year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Note is null
     *
     * @param showId show ID
     * @param season season
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(
        @PathVariable("showId") showId: Int,
        @RequestBody season: Season
    ) {
        processResult(result = seasonFacade.add(parent = showId, data = season))
    }

    /**
     * Updates season.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * ID is null
     *  * Position is null
     *  * Number of season isn't positive number
     *  * Starting year isn't between 1940 and current year
     *  * Ending year isn't between 1940 and current year
     *  * Starting year is greater than ending year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Note is null
     *  * Season doesn't exist in data storage
     *
     * @param showId show ID
     * @param season new value of season
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable("showId") showId: Int,
        @RequestBody season: Season
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.update(data = season))
    }

    /**
     * Removes season.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *
     * @param showId show ID
     * @param id     ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(
        @PathVariable("showId") showId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.remove(id = id))
    }

    /**
     * Duplicates season.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *
     * @param showId show ID
     * @param id     ID
     */
    @PostMapping("/duplicate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(
        @PathVariable("showId") showId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.duplicate(id = id))
    }

    /**
     * Moves season in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season can't be moved up
     *  * Season doesn't exist in data storage
     *
     * @param showId show ID
     * @param id     ID
     */
    @PostMapping("/moveUp/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(
        @PathVariable("showId") showId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.moveUp(id = id))
    }

    /**
     * Moves season in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season can't be moved down
     *  * Season doesn't exist in data storage
     *
     * @param showId show ID
     * @param id     ID
     */
    @PostMapping("/moveDown/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(
        @PathVariable("showId") showId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.moveDown(id = id))
    }

    /**
     * Returns list of seasons for specified show.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *
     * @param showId show ID
     * @return list of seasons for specified show
     */
    @GetMapping
    fun findSeasonsByShow(@PathVariable("showId") showId: Int): List<Season> {
        return processResult(result = seasonFacade.find(parent = showId))!!
    }

}
