package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.facade.EpisodeFacade
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
class EpisodeController(private val episodeFacade: EpisodeFacade) : AbstractController() {

    /**
     * Returns episode with ID or null if there isn't such episode.
     *
     * @param showId    show ID
     * @param seasonId  season ID
     * @param episodeId episode ID
     * @return episode with ID or null if there isn't such episode
     */
    @GetMapping("/{episodeId}")
    fun getEpisode(@PathVariable("showId") showId: Int,
                   @PathVariable("seasonId") seasonId: Int,
                   @PathVariable("episodeId") episodeId: Int): Episode? {
        return processResult(episodeFacade.get(episodeId))
    }

    /**
     * Adds episode. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
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
    fun add(@PathVariable("showId") showId: Int,
            @PathVariable("seasonId") seasonId: Int,
            @RequestBody episode: Episode) {
        val season = Season(id = seasonId, number = null, startYear = null, endYear = null, language = null, subtitles = null, note = null, position = null)
        processResult(episodeFacade.add(season, episode))
    }

    /**
     * Updates episode.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Number of episode isn't positive number
     *  * Name is null
     *  * Name is empty string
     *  * Length of episode is negative value
     *  * Note is null
     *  * Season doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  new value of episode
     */
    @PostMapping("/update")
    fun update(@PathVariable("showId") showId: Int,
               @PathVariable("seasonId") seasonId: Int,
               @RequestBody episode: Episode) {
        processResult(episodeFacade.update(episode))
    }

    /**
     * Removes episode.
     * <br></br>
     * Validation errors:
     *
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("showId") showId: Int,
               @PathVariable("seasonId") seasonId: Int,
               @PathVariable("id") id: Int) {
        val episode = Episode(id = id, number = null, name = null, length = null, note = null, position = null)
        processResult(episodeFacade.remove(episode))
    }

    /**
     * Duplicates episode.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  episode
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@PathVariable("showId") showId: Int,
                  @PathVariable("seasonId") seasonId: Int,
                  @RequestBody episode: Episode) {
        processResult(episodeFacade.duplicate(episode))
    }

    /**
     * Moves episode in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Episode can't be moved up
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  episode
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@PathVariable("showId") showId: Int,
               @PathVariable("seasonId") seasonId: Int,
               @RequestBody episode: Episode) {
        processResult(episodeFacade.moveUp(episode))
    }

    /**
     * Moves episode in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Episode can't be moved down
     *  * Episode doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  episode
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@PathVariable("showId") showId: Int,
                 @PathVariable("seasonId") seasonId: Int,
                 @RequestBody episode: Episode) {
        processResult(episodeFacade.moveDown(episode))
    }

    /**
     * Returns list of episodes for specified season.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Season doesn't exist in data storage
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return list of episodes for specified season
     */
    @GetMapping
    fun findEpisodesBySeason(@PathVariable("showId") showId: Int,
                             @PathVariable("seasonId") seasonId: Int): List<Episode> {
        val season = Season(id = seasonId, number = null, startYear = null, endYear = null, language = null, subtitles = null, note = null, position = null)
        return processResult(episodeFacade.find(season))!!
    }

}
