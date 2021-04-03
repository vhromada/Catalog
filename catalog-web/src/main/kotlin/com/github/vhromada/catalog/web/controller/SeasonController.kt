package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.catalog.facade.ShowFacade
import com.github.vhromada.catalog.web.domain.SeasonData
import com.github.vhromada.catalog.web.fo.SeasonFO
import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.entity.Time
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
    private val seasonMapper: Mapper<Season, SeasonFO>
) : AbstractResultController() {

    /**
     * Shows page with list of seasons.
     *
     * @param model  model
     * @param showId show ID
     * @return view for page with list of seasons
     */
    @GetMapping("", "/list")
    fun showList(model: Model, @PathVariable("showId") showId: Int): String {
        val result = seasonFacade.find(parent = showId)
        processResults(result)

        model.addAttribute("seasons", result.data)
        model.addAttribute("show", showId)
        model.addAttribute("title", "Seasons")

        return "season/index"
    }

    /**
     * Shows page with detail of season.
     *
     * @param model  model
     * @param showId show ID
     * @param id     ID of showing season
     * @return view for page with detail of season
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = id)
        val episodesResult = episodeFacade.find(parent = id)
        processResults(showResult, seasonResult, episodesResult)

        val length = episodesResult.data!!.sumBy { it.length!! }
        model.addAttribute("season", SeasonData(showId = showId, season = seasonResult.data!!, episodesCount = episodesResult.data!!.size, totalLength = Time(length = length)))
        model.addAttribute("show", showId)
        model.addAttribute("title", "Season detail")

        return "season/detail"
    }

    /**
     * Shows page for adding season.
     *
     * @param model  model
     * @param showId show ID
     * @return view for page for adding season
     */
    @GetMapping("/add")
    fun showAdd(model: Model, @PathVariable("showId") showId: Int): String {
        processResults(showFacade.get(id = showId))

        val season = SeasonFO(
            id = null,
            number = null,
            startYear = null,
            endYear = null,
            language = null,
            subtitles = null,
            note = null,
            position = null
        )
        return createFormView(model = model, season = season, showId = showId, title = "Add season", action = "add")
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
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @PathVariable("showId") showId: Int, @ModelAttribute("season") @Valid season: SeasonFO, errors: Errors): String {
        require(season.id == null) { "ID must be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, season = season, showId = showId, title = "Add season", action = "add")
        }
        processResults(seasonFacade.add(parent = showId, seasonMapper.mapBack(season)))

        return getListRedirectUrl(showId = showId)
    }

    /**
     * Cancel adding season.
     *
     * @param showId show ID
     * @return view for redirect to page with list of seasons
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(@PathVariable("showId") showId: Int): String {
        return cancel(showId = showId)
    }

    /**
     * Shows page for editing season.
     *
     * @param model  model
     * @param showId show ID
     * @param id     ID of editing season
     * @return view for page for editing season
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        val showResult = showFacade.get(id = showId)
        val seasonResult = seasonFacade.get(id = id)
        processResults(showResult, seasonResult)

        return createFormView(model = model, season = seasonMapper.map(source = seasonResult.data!!), showId = showId, title = "Edit season", action = "edit")
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
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @PathVariable("showId") showId: Int, @ModelAttribute("season") @Valid season: SeasonFO, errors: Errors): String {
        require(season.id != null) { "ID mustn't be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, season = season, showId = showId, title = "Edit season", action = "edit")
        }
        processResults(showFacade.get(id = showId))
        processResults(seasonFacade.update(data = seasonMapper.mapBack(source = season)))

        return getListRedirectUrl(showId = showId)
    }

    /**
     * Cancel editing season.
     *
     * @param showId show ID
     * @return view for redirect to page with list of seasons
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun cancelEdit(@PathVariable("showId") showId: Int): String {
        return cancel(showId = showId)
    }

    /**
     * Process duplicating season.
     *
     * @param showId show ID
     * @param id     ID of duplicating season
     * @return view for redirect to page with list of seasons
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.duplicate(id = id))

        return getListRedirectUrl(showId = showId)
    }

    /**
     * Process removing season.
     *
     * @param showId show ID
     * @param id     ID of removing season
     * @return view for redirect to page with list of seasons
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.remove(id = id))

        return getListRedirectUrl(showId = showId)
    }

    /**
     * Process moving season up.
     *
     * @param showId show ID
     * @param id     ID of moving season
     * @return view for redirect to page with list of seasons
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.moveUp(id = id))

        return getListRedirectUrl(showId = showId)
    }

    /**
     * Process moving season down.
     *
     * @param showId show ID
     * @param id     ID of moving season
     * @return view for redirect to page with list of seasons
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("showId") showId: Int, @PathVariable("id") id: Int): String {
        processResults(seasonFacade.moveDown(id = id))

        return getListRedirectUrl(showId = showId)
    }

    /**
     * Cancel processing season.
     *
     * @param showId show ID
     * @return view for redirect to page with list of seasons
     */
    private fun cancel(showId: Int): String {
        processResults(showFacade.get(id = showId))

        return getListRedirectUrl(showId = showId)
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

}
