package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.catalog.facade.ShowFacade
import com.github.vhromada.catalog.web.domain.SeasonData
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.SeasonFO
import com.github.vhromada.catalog.web.mapper.SeasonMapper
import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.entity.Time
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
 * A class represents controller for seasons.
 *
 * @author Vladimir Hromada
 */
@Controller("seasonController")
@RequestMapping("/shows/{showId}/seasons")
class SeasonController(
        private val showFacade: ShowFacade,
        private val seasonFacade: SeasonFacade,
        private val episodeFacade: EpisodeFacade,
        private val seasonMapper: SeasonMapper) : AbstractResultController() {

    /**
     * Shows page with list of seasons.
     *
     * @param model  model
     * @param showId show ID
     * @return view for page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     */
    @GetMapping("", "/list")
    fun showList(model: Model, @PathVariable("showId") showId: Int): String {
        val show = getShow(showId)

        val seasonsResult = seasonFacade.find(show)
        processResults(seasonsResult)

        model.addAttribute("seasons", seasonsResult.data)
        model.addAttribute("show", showId)
        model.addAttribute("title", "Seasons")

        return "season/index"
    }

    /**
     * Shows page with detail of season.
     *
     * @param model  model
     * @param showId show ID
     * @param id     ID of editing season
     * @return view for page with detail of season
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        getShow(showId)

        val result = seasonFacade.get(id)
        processResults(result)

        val season = result.data
        if (season != null) {
            val episodesResult = episodeFacade.find(season)
            processResults(episodesResult)

            val length = episodesResult.data!!.sumBy { it.length!! }
            model.addAttribute("season", SeasonData(season = season, episodesCount = episodesResult.data!!.size, totalLength = Time(length)))
            model.addAttribute("show", showId)
            model.addAttribute("title", "Season detail")

            return "season/detail"
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Shows page for adding season.
     *
     * @param model  model
     * @param showId show ID
     * @return view for page for adding season
     * @throws IllegalRequestException  if show doesn't exist
     */
    @GetMapping("/add")
    fun showAdd(model: Model, @PathVariable("showId") showId: Int): String {
        getShow(showId)

        val season = SeasonFO(id = null,
                number = null,
                startYear = null,
                endYear = null,
                language = null,
                subtitles = null,
                note = null,
                position = null)
        return createFormView(model, season, showId, "Add season", "add")
    }

    /**
     * Process adding season.
     *
     * @param model    model
     * @param showId   show ID
     * @param season   FO for season
     * @param errors   errors
     * @return view for redirect to page with list of seasons (no errors) or view for page for adding season (errors)
     * @throws IllegalArgumentException if ID isn't null
     * @throws IllegalRequestException  if show doesn't exist
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @PathVariable("showId") showId: Int, @ModelAttribute("season") @Valid season: SeasonFO, errors: Errors): String {
        require(season.id == null) { "ID must be null." }

        val show = getShow(showId)

        if (errors.hasErrors()) {
            return createFormView(model, season, showId, "Add season", "add")
        }

        processResults(seasonFacade.add(show, seasonMapper.mapBack(season)))

        return getListRedirectUrl(showId)
    }

    /**
     * Cancel adding season.
     *
     * @param showId show ID
     * @return view for redirect to page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(@PathVariable("showId") showId: Int): String {
        return cancel(showId)
    }

    /**
     * Shows page for editing season.
     *
     * @param model  model
     * @param showId show ID
     * @param id     ID of editing season
     * @return view for page for editing season
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        getShow(showId)

        val result = seasonFacade.get(id)
        processResults(result)

        val season = result.data
        return if (season != null) {
            createFormView(model, seasonMapper.map(season), showId, "Edit season", "edit")
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Process editing season.
     *
     * @param model    model
     * @param showId   show ID
     * @param season   FO for season
     * @param errors   errors
     * @return view for redirect to page with list of seasons (no errors) or view for page for editing season (errors)
     * @throws IllegalArgumentException if ID is null
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @PathVariable("showId") showId: Int, @ModelAttribute("season") @Valid season: SeasonFO, errors: Errors): String {
        require(season.id != null) { "ID mustn't be null." }
        getShow(showId)

        if (errors.hasErrors()) {
            return createFormView(model, season, showId, "Edit season", "edit")
        }

        processResults(seasonFacade.update(processSeason(seasonMapper.mapBack(season))))

        return getListRedirectUrl(showId)
    }

    /**
     * Cancel editing season.
     *
     * @param showId show ID
     * @return view for redirect to page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun cancelEdit(@PathVariable("showId") showId: Int): String {
        return cancel(showId)
    }

    /**
     * Process duplicating season.
     *
     * @param showId show ID
     * @param id     ID of duplicating season
     * @return view for redirect to page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.duplicate(getSeason(showId, id)))

        return getListRedirectUrl(showId)
    }

    /**
     * Process removing season.
     *
     * @param showId show ID
     * @param id     ID of removing season
     * @return view for redirect to page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.remove(getSeason(showId, id)))

        return getListRedirectUrl(showId)
    }

    /**
     * Process moving season up.
     *
     * @param showId show ID
     * @param id     ID of moving season
     * @return view for redirect to page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.moveUp(getSeason(showId, id)))

        return getListRedirectUrl(showId)
    }

    /**
     * Process moving season down.
     *
     * @param showId show ID
     * @param id     ID of moving season
     * @return view for redirect to page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.moveDown(getSeason(showId, id)))

        return getListRedirectUrl(showId)
    }

    /**
     * Cancel processing season.
     *
     * @param showId show ID
     * @return view for redirect to page with list of seasons
     * @throws IllegalRequestException  if show doesn't exist
     */
    private fun cancel(showId: Int): String {
        getShow(showId)

        return getListRedirectUrl(showId)
    }

    /**
     * Returns show.
     *
     * @param id show ID
     * @return show
     * @throws IllegalRequestException if show doesn't exist
     */
    private fun getShow(id: Int): Show {
        val showResult = showFacade.get(id)
        processResults(showResult)

        return showResult.data ?: throw IllegalRequestException("Show doesn't exist.")
    }

    /**
     * Returns season with ID.
     *
     * @param showId show ID
     * @param id     ID
     * @return season with ID
     * @throws IllegalRequestException  if show doesn't exist
     * or season doesn't exist
     */
    private fun getSeason(showId: Int, id: Int): Season {
        getShow(showId)

        val season = Season(id = id,
                number = null,
                startYear = null,
                endYear = null,
                language = null,
                subtitles = null,
                note = null,
                position = null)

        return processSeason(season)
    }

    /**
     * Returns processed season.
     *
     * @param season for processing
     * @return processed season
     * @throws IllegalRequestException if season doesn't exist
     */
    private fun processSeason(season: Season): Season {
        val seasonResult = seasonFacade.get(season.id!!)
        processResults(seasonResult)

        if (seasonResult.data != null) {
            return season
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    /**
     * Returns page's view with form.
     *
     * @param model  model
     * @param season FO for season
     * @param showId show ID
     * @param title  page's title
     * @param action action
     * @return page's view with form
     */
    private fun createFormView(model: Model, season: SeasonFO, showId: Int, title: String, action: String): String {
        model.addAttribute("season", season)
        model.addAttribute("show", showId)
        model.addAttribute("languages", Language.values())
        model.addAttribute("subtitles", arrayOf(Language.CZ, Language.EN))
        model.addAttribute("title", title)
        model.addAttribute("action", action)

        return "season/form"
    }

    /**
     * Returns redirect URL to list.
     *
     * @param showId show ID
     * @return redirect URL to list
     */
    private fun getListRedirectUrl(showId: Int): String {
        return "redirect:/shows/$showId/seasons/list"
    }

    companion object {

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Season doesn't exist."

    }

}
