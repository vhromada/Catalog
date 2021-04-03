package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.catalog.facade.ShowFacade
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
    private val episodeMapper: Mapper<Episode, EpisodeFO>
) : AbstractResultController() {

    /**
     * Shows page with list of episodes.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for page with list of episodes
     */
    @GetMapping("", "/list")
    fun showList(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        val showResult = showFacade.get(id = showId)
        val episodesResult = episodeFacade.find(parent = seasonId)
        processResults(showResult, episodesResult)

        model.addAttribute("episodes", episodesResult.data)
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
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = seasonId)
        val episodeResult = episodeFacade.get(id = id)
        processResults(showResult, seasonResult, episodeResult)

        model.addAttribute("episode", episodeResult.data)
        model.addAttribute("show", showId)
        model.addAttribute("season", seasonId)
        model.addAttribute("title", "Episode detail")

        return "episode/detail"
    }

    /**
     * Shows page for adding episode.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for page for adding episode
     */
    @GetMapping("add")
    fun showAdd(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = seasonId)
        processResults(showResult, seasonResult)

        val episode = EpisodeFO(
            id = null,
            number = null,
            length = null,
            name = null,
            note = null,
            position = null
        )
        return createFormView(model = model, episode = episode, showId = showId, seasonId = seasonId, title = "Add episode", action = "add")
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
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @ModelAttribute("episode") @Valid episode: EpisodeFO, errors: Errors): String {
        require(episode.id == null) { "ID must be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, episode = episode, showId = showId, seasonId = seasonId, title = "Add episode", action = "add")
        }
        processResults(showFacade.get(id = showId))
        processResults(episodeFacade.add(parent = seasonId, data = episodeMapper.mapBack(source = episode)))

        return getListRedirectUrl(showId = showId, seasonId = seasonId)
    }

    /**
     * Cancel adding episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for redirect to page with list of episodes
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        return cancel(showId = showId, seasonId = seasonId)
    }

    /**
     * Shows page for editing episode.
     *
     * @param model    model
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of editing episode
     * @return view for page for editing episode
     */
    @GetMapping("edit/{id}")
    fun showEdit(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = seasonId)
        val episodeResult = episodeFacade.get(id = id)
        processResults(showResult, seasonResult, episodeResult)

        return createFormView(model = model, episode = episodeMapper.map(source = episodeResult.data!!), showId = showId, seasonId = seasonId, title = "Edit episode", action = "edit")
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
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @ModelAttribute("episode") @Valid episode: EpisodeFO, errors: Errors): String {
        require(episode.id != null) { "ID mustn't be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, episode = episode, showId = showId, seasonId = seasonId, title = "Edit episode", action = "edit")
        }
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = seasonId)
        processResults(showResult, seasonResult)
        processResults(episodeFacade.update(data = episodeMapper.mapBack(source = episode)))

        return getListRedirectUrl(showId = showId, seasonId = seasonId)
    }

    /**
     * Cancel editing episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for redirect to page with list of episodes
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun cancelEdit(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int): String {
        return cancel(showId = showId, seasonId = seasonId)
    }

    /**
     * Process duplicating episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of duplicating episode
     * @return view for redirect to page with list of episodes
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = seasonId)
        processResults(showResult, seasonResult)

        processResults(episodeFacade.duplicate(id = id))

        return getListRedirectUrl(showId = showId, seasonId = seasonId)
    }

    /**
     * Process removing episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of removing episode
     * @return view for redirect to page with list of episodes
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        processResults(episodeFacade.remove(id = id))

        return getListRedirectUrl(showId = showId, seasonId = seasonId)
    }

    /**
     * Process moving episode up.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of moving episode
     * @return view for redirect to page with list of episodes
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        processResults(episodeFacade.moveUp(id = id))

        return getListRedirectUrl(showId = showId, seasonId = seasonId)
    }

    /**
     * Process moving episode down.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @param id       ID of moving episode
     * @return view for redirect to page with list of episodes
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("showId") showId: Int, @PathVariable("seasonId") seasonId: Int, @PathVariable("id") id: Int): String {
        processResults(episodeFacade.moveDown(id = id))

        return getListRedirectUrl(showId = showId, seasonId = seasonId)
    }

    /**
     * Cancel processing episode.
     *
     * @param showId   show ID
     * @param seasonId season ID
     * @return view for redirect to page with list of episodes
     */
    private fun cancel(showId: Int, seasonId: Int): String {
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = seasonId)
        processResults(showResult, seasonResult)

        return getListRedirectUrl(showId = showId, seasonId = seasonId)
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

}
