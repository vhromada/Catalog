package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.facade.EpisodeFacade
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
 * A class represents controller for episodes.
 *
 * @author Vladimir Hromada
 */
@RestController("episodeController")
@RequestMapping("/catalog/shows/{showId}/seasons/{seasonId}/episodes")
class EpisodeController(
    private val episodeFacade: EpisodeFacade,
    private val showFacade: ShowFacade,
    private val seasonFacade: SeasonFacade
) : AbstractController() {

    /**
     * Returns episode with ID.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *  * Episode doesn't exist in data storage
     *
     * @param showId    show ID
     * @param seasonId  season ID
     * @param episodeId episode ID
     * @return episode with ID
     */
    @GetMapping("/{episodeId}")
    fun getEpisode(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int,
        @PathVariable("episodeId") episodeId: Int
    ): Episode {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.get(id = seasonId))
        return processResult(result = episodeFacade.get(id = episodeId))!!
    }

    /**
     * Adds episode. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *  * Episode ID isn't null
     *  * Episode position isn't null
     *  * Number of episode isn't positive number
     *  * Name is null
     *  * Name is empty string
     *  * Length of episode is negative value
     *  * Note is null
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  episode
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int,
        @RequestBody episode: Episode
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = episodeFacade.add(parent = seasonId, data = episode))
    }

    /**
     * Updates episode.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *  * ID is null
     *  * Position is null
     *  * Number of episode isn't positive number
     *  * Name is null
     *  * Name is empty string
     *  * Length of episode is negative value
     *  * Note is null
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  new value of episode
     */
    @PostMapping("/update")
    fun update(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int,
        @RequestBody episode: Episode
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.get(id = seasonId))
        processResult(result = episodeFacade.update(data = episode))
    }

    /**
     * Removes episode.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.get(id = seasonId))
        processResult(result = episodeFacade.remove(id = id))
    }

    /**
     * Duplicates episode.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID
     */
    @PostMapping("/duplicate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.get(id = seasonId))
        processResult(result = episodeFacade.duplicate(id = id))
    }

    /**
     * Moves episode in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *  * Episode can't be moved up
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID
     */
    @PostMapping("/moveUp/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.get(id = seasonId))
        processResult(result = episodeFacade.moveUp(id = id))
    }

    /**
     * Moves episode in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *  * Episode can't be moved up
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID
     */
    @PostMapping("/moveDown/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int,
        @PathVariable("id") id: Int
    ) {
        processResult(result = showFacade.get(id = showId))
        processResult(result = seasonFacade.get(id = seasonId))
        processResult(result = episodeFacade.moveDown(id = id))
    }

    /**
     * Returns list of episodes for specified season.
     * <br></br>
     * Validation errors:
     *
     *  * Show doesn't exist in data storage
     *  * Season doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return list of episodes for specified season
     */
    @GetMapping
    fun findEpisodesBySeason(
        @PathVariable("showId") showId: Int,
        @PathVariable("seasonId") seasonId: Int
    ): List<Episode> {
        processResult(result = showFacade.get(id = showId))
        return processResult(result = episodeFacade.find(parent = seasonId))!!
    }

}
