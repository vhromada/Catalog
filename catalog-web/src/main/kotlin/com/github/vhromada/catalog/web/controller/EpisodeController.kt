package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.catalog.facade.ShowFacade
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.EpisodeFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

/**
 * A class represents controller for episodes.
 *
 * @author Vladimir Hromada
 */
@Controller("episodeController")
@RequestMapping("/shows/{showId}/seasons/{seasonId}/episodes")
class EpisodeController(
        private val showFacade: ShowFacade,
        private val seasonFacade: SeasonFacade,
        private val episodeFacade: EpisodeFacade,
        private val episodeMapper: Mapper<Episode, EpisodeFO>) : AbstractResultController() {

    /**
     * Shows page with list of episodes.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("", "/list")
    fun showList(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        getShow(showId)

        val season = getSeason(seasonId)

        val result = episodeFacade.find(season)
        processResults(result)

        model.addAttribute("episodes", result.data)
        model.addAttribute("show", showId)
        model.addAttribute("season", seasonId)
        model.addAttribute("title", "Episodes")

        return "episode/index"
    }

    /**
     * Shows page with detail of episode.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of showing episode
     * @return view for page with detail of episode
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        getShow(showId)
        getSeason(seasonId)

        val result = episodeFacade.get(id)
        processResults(result)

        val episode = result.data
        if (episode != null) {
            model.addAttribute("episode", episode)
            model.addAttribute("show", showId)
            model.addAttribute("season", seasonId)
            model.addAttribute("title", "Episode detail")

            return "episode/detail"
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Shows page for adding episode.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for page for adding episode
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("add")
    fun showAdd(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        getShow(showId)
        getSeason(seasonId)

        val episode = EpisodeFO(id = null,
                number = null,
                length = null,
                name = null,
                note = null,
                position = null)
        return createFormView(model, episode, showId, seasonId, "Add episode", "add")
    }

    /**
     * Process adding episode.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  FO for episode
     * @param errors   errors
     * @return view for redirect to page with list of episodes (no errors) or view for page for adding episode (errors)
     * @throws IllegalArgumentException if ID isn't null
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @ModelAttribute("episode") @Valid episode: EpisodeFO, errors: Errors): String {
        require(episode.id == null) { "ID must be null." }
        getShow(showId)

        val season = getSeason(seasonId)

        if (errors.hasErrors()) {
            return createFormView(model, episode, showId, seasonId, "Add episode", "add")
        }
        processResults(episodeFacade.add(season, episodeMapper.mapBack(episode)))

        return getListRedirectUrl(showId, seasonId)
    }

    /**
     * Cancel adding episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for redirect to page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        return cancel(showId, seasonId)
    }

    /**
     * Shows page for editing episode.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of editing episode
     * @return view for page for editing episode
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    @GetMapping("edit/{id}")
    fun showEdit(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        getShow(showId)
        getSeason(seasonId)

        val result = episodeFacade.get(id)
        processResults(result)

        val episode = result.data
        return if (episode != null) {
            createFormView(model, episodeMapper.map(episode), showId, seasonId, "Edit episode", "edit")
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Process editing episode.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @param episode  FO for episode
     * @param errors   errors
     * @return view for redirect to page with list of episodes (no errors) or view for page for editing episode (errors)
     * @throws IllegalArgumentException if ID is null
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @ModelAttribute("episode") @Valid episode: EpisodeFO, errors: Errors): String {
        require(episode.id != null) { "ID mustn't be null." }
        getShow(showId)
        getSeason(seasonId)

        if (errors.hasErrors()) {
            return createFormView(model, episode, showId, seasonId, "Edit episode", "edit")
        }
        processResults(episodeFacade.update(processEpisode(episodeMapper.mapBack(episode))))

        return getListRedirectUrl(showId, seasonId)
    }

    /**
     * Cancel editing episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for redirect to page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun cancelEdit(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        return cancel(showId, seasonId)
    }

    /**
     * Process duplicating episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of duplicating episode
     * @return view for redirect to page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        getShow(showId)
        getSeason(seasonId)

        processResults(episodeFacade.duplicate(getEpisode(showId, seasonId, id)))

        return getListRedirectUrl(showId, seasonId)
    }

    /**
     * Process removing episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of removing episode
     * @return view for redirect to page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        processResults(episodeFacade.remove(getEpisode(showId, seasonId, id)))

        return getListRedirectUrl(showId, seasonId)
    }

    /**
     * Process moving episode up.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of moving episode
     * @return view for redirect to page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        processResults(episodeFacade.moveUp(getEpisode(showId, seasonId, id)))

        return getListRedirectUrl(showId, seasonId)
    }

    /**
     * Process moving episode down.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of moving episode
     * @return view for redirect to page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        processResults(episodeFacade.moveDown(getEpisode(showId, seasonId, id)))

        return getListRedirectUrl(showId, seasonId)
    }

    /**
     * Cancel processing episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for redirect to page with list of episodes
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     */
    private fun cancel(showId: Int, seasonId: Int): String {
        getShow(showId)
        getSeason(seasonId)

        return getListRedirectUrl(showId, seasonId)
    }

    /**
     * Returns show.
     *
     * @param id show ID
     * @throws IllegalRequestException if show doesn't exist
     */
    private fun getShow(id: Int) {
        val showResult = showFacade.get(id)
        processResults(showResult)

        if (showResult.data == null) {
            throw IllegalRequestException("Show doesn't exist.")
        }
    }

    /**
     * Returns season.
     *
     * @param id season ID
     * @return season
     * @throws IllegalRequestException if season doesn't exist
     */
    private fun getSeason(id: Int): Season {
        val seasonResult = seasonFacade.get(id)
        processResults(seasonResult)

        return seasonResult.data ?: throw IllegalRequestException("Season doesn't exist.")
    }

    /**
     * Returns episode with ID.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID
     * @return episode with ID
     * @throws IllegalRequestException if show doesn't exist
     * or season doesn't exist
     * or episode doesn't exist
     */
    private fun getEpisode(showId: Int, seasonId: Int, id: Int): Episode {
        getShow(showId)
        getSeason(seasonId)

        val episode = Episode(id = id,
                number = null,
                length = null,
                name = null,
                note = null,
                position = null)

        return processEpisode(episode)
    }

    /**
     * Returns processed episode.
     *
     * @param episode episode for processing
     * @return processed episode
     * @throws IllegalRequestException if episode doesn't exist
     */
    private fun processEpisode(episode: Episode): Episode {
        val episodeResult = episodeFacade.get(episode.id!!)
        processResults(episodeResult)

        if (episodeResult.data != null) {
            return episode
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    /**
     * Returns page's view with form.
     *
     * @param model    model
     * @param episode  FO for episode
     * @param showId   show ID
     * @param seasonId season ID
     * @param title    page's title
     * @param action   action
     * @return page's view with form
     */
    private fun createFormView(model: Model, episode: EpisodeFO, showId: Int, seasonId: Int, title: String, action: String): String {
        model.addAttribute("episode", episode)
        model.addAttribute("show", showId)
        model.addAttribute("season", seasonId)
        model.addAttribute("title", title)
        model.addAttribute("action", action)

        return "episode/form"
    }

    /**
     * Returns redirect URL to list.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return redirect URL to list
     */
    private fun getListRedirectUrl(showId: Int, seasonId: Int): String {
        return "redirect:/shows/$showId/seasons/$seasonId/episodes/list"
    }

    companion object {

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Episode doesn't exist."

    }

}
