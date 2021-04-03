package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.catalog.facade.GenreFacade
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.catalog.facade.ShowFacade
import com.github.vhromada.catalog.web.domain.ShowData
import com.github.vhromada.catalog.web.fo.ShowFO
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
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * A class represents controller for shows.
 *
 * @author Vladimir Hromada
 */
@Controller("showController")
@RequestMapping("/shows")
class ShowController(
    private val showFacade: ShowFacade,
    private val seasonFacade: SeasonFacade,
    private val episodeFacade: EpisodeFacade,
    private val pictureFacade: PictureFacade,
    private val genreFacade: GenreFacade,
    private val showMapper: Mapper<Show, ShowFO>
) : AbstractResultController() {

    /**
     * Process new data.
     *
     * @return view for redirect to page with list of shows
     */
    @GetMapping("/new")
    fun processNew(): String {
        showFacade.newData()

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page with list of shows.
     *
     * @param model model
     * @return view for page with list of shows
     */
    @RequestMapping("", "/list")
    fun showList(model: Model): String {
        val showsResult = showFacade.getAll()
        val seasonsCountResult = showFacade.getSeasonsCount()
        val episodesCountResult = showFacade.getEpisodesCount()
        val totalLengthResult = showFacade.getTotalLength()
        processResults(showsResult, seasonsCountResult, episodesCountResult, totalLengthResult)

        model.addAttribute("shows", showsResult.data)
        model.addAttribute("seasonsCount", seasonsCountResult.data)
        model.addAttribute("episodesCount", episodesCountResult.data)
        model.addAttribute("totalLength", totalLengthResult.data)
        model.addAttribute("title", "Shows")

        return "show/index"
    }

    /**
     * Shows page with detail of show.
     *
     * @param model model
     * @param id    ID of showing show
     * @return view for page with detail of show
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("id") id: Int): String {
        val showResult = showFacade.get(id = id)
        val seasonsResult = seasonFacade.find(parent = id)
        processResults(showResult, seasonsResult)

        val seasonsCount = seasonsResult.data!!.size
        var episodesCount = 0
        var length = 0
        for (season in seasonsResult.data!!) {
            val episodesResult = episodeFacade.find(parent = season.id!!)
            processResults(episodesResult)
            episodesCount += episodesResult.data!!.size
            length += episodesResult.data!!.sumBy { it.length!! }
        }

        model.addAttribute("show", ShowData(show = showResult.data!!, seasonsCount = seasonsCount, episodesCount = episodesCount, totalLength = Time(length = length)))
        model.addAttribute("title", "Show detail")

        return "show/detail"
    }

    /**
     * Shows page for adding show.
     *
     * @param model model
     * @return view for page for adding show
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        val show = ShowFO(
            id = null,
            czechName = null,
            originalName = null,
            csfd = null,
            imdb = false,
            wikiEn = null,
            imdbCode = null,
            wikiCz = null,
            picture = null,
            note = null,
            position = null,
            genres = null
        )
        return createAddFormView(model = model, show = show)
    }

    /**
     * Process adding show.
     *
     * @param model   model
     * @param show    FO for show
     * @param errors  errors
     * @param request HTTP request
     * @return view for redirect to page with list of shows (no errors) or view for page for adding show (errors)
     * @throws IllegalArgumentException if ID isn't null
     */
    @PostMapping("/add")
    fun processAdd(model: Model, @ModelAttribute("show") @Valid show: ShowFO, errors: Errors, request: HttpServletRequest): String {
        require(show.id == null) { "ID must be null." }

        if (request.getParameter("create") != null) {
            if (errors.hasErrors()) {
                return createAddFormView(model = model, show = show)
            }
            processResults(showFacade.add(data = showMapper.mapBack(source = show).copy(genres = getGenres(source = show.genres!!))))
        }

        if (request.getParameter("choosePicture") != null) {
            return createAddFormView(model = model, show = show)
        }

        if (request.getParameter("removePicture") != null) {
            return createAddFormView(model = model, show = show.copy(picture = null))
        }

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page for editing show.
     *
     * @param model model
     * @param id    ID of editing show
     * @return view for page for editing show
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = showFacade.get(id = id)
        processResults(result)

        return createEditFormView(model = model, show = showMapper.map(source = result.data!!))
    }

    /**
     * Process editing show.
     *
     * @param model   model
     * @param show    FO for show
     * @param errors  errors
     * @param request HTTP request
     * @return view for redirect to page with list of shows (no errors) or view for page for editing show (errors)
     * @throws IllegalArgumentException if ID is null
     */
    @PostMapping("/edit")
    fun processEdit(model: Model, @ModelAttribute("show") @Valid show: ShowFO, errors: Errors, request: HttpServletRequest): String {
        require(show.id != null) { "ID mustn't be null." }

        if (request.getParameter("update") != null) {
            if (errors.hasErrors()) {
                return createEditFormView(model = model, show = show)
            }
            processResults(showFacade.update(data = showMapper.mapBack(source = show).copy(genres = getGenres(source = show.genres!!))))
        }

        if (request.getParameter("choosePicture") != null) {
            return createEditFormView(model = model, show = show)
        }

        if (request.getParameter("removePicture") != null) {
            return createEditFormView(model = model, show = show.copy(picture = null))
        }

        return LIST_REDIRECT_URL
    }

    /**
     * Process duplicating show.
     *
     * @param id ID of duplicating show
     * @return view for redirect to page with list of shows
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(showFacade.duplicate(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing show.
     *
     * @param id ID of removing show
     * @return view for redirect to page with list of shows
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(showFacade.remove(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving show up.
     *
     * @param id ID of moving show
     * @return view for redirect to page with list of shows
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(showFacade.moveUp(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving show down.
     *
     * @param id ID of moving show
     * @return view for redirect to page with list of shows
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(showFacade.moveDown(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process updating positions.
     *
     * @return view for redirect to page with list of shows
     */
    @GetMapping("/update")
    fun processUpdatePositions(): String {
        showFacade.updatePositions()

        return LIST_REDIRECT_URL
    }

    /**
     * Returns page's view with form.
     *
     * @param model  model
     * @param show   FO for show
     * @param title  page's title
     * @param action action
     * @return page's view with form
     */
    private fun createFormView(model: Model, show: ShowFO, title: String, action: String): String {
        val picturesResult = pictureFacade.getAll()
        val genresResult = genreFacade.getAll()
        processResults(picturesResult, genresResult)

        model.addAttribute("show", show)
        model.addAttribute("title", title)
        model.addAttribute("pictures", picturesResult.data!!.map { it.id })
        model.addAttribute("genres", genresResult.data)
        model.addAttribute("action", action)

        return "show/form"
    }

    /**
     * Returns page's view with form for adding show.
     *
     * @param model model
     * @param show  FO for show
     * @return page's view with form for adding show
     */
    private fun createAddFormView(model: Model, show: ShowFO): String {
        return createFormView(model = model, show = show, title = "Add show", action = "add")
    }

    /**
     * Returns page's view with form for editing show.
     *
     * @param model model
     * @param show  FO for show
     * @return page's view with form for editing show
     */
    private fun createEditFormView(model: Model, show: ShowFO): String {
        return createFormView(model = model, show = show, title = "Edit show", action = "edit")
    }

    /**
     * Returns genres.
     *
     * @param source list of genres' ID
     * @return genres
     */
    private fun getGenres(source: List<Int?>): List<Genre> {
        return source.map {
            val result = genreFacade.get(id = it!!)
            processResults(result)

            result.data!!
        }
    }

    companion object {

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/shows/list"

    }

}
