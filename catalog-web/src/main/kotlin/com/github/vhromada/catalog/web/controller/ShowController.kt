package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.catalog.facade.GenreFacade
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.catalog.facade.ShowFacade
import com.github.vhromada.catalog.web.domain.ShowData
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.ShowFO
import com.github.vhromada.catalog.web.mapper.ShowMapper
import com.github.vhromada.common.entity.Time
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.Assert
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
        private val showMapper: ShowMapper) : AbstractResultController() {

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
     * @param id    ID of editing show
     * @return view for page with detail of show
     * @throws IllegalRequestException if show doesn't exist
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("id") id: Int): String {
        val result = showFacade.get(id)
        processResults(result)

        val show = result.data
        if (show != null) {
            val seasonsResult = seasonFacade.find(show)
            processResults(seasonsResult)
            val seasonsCount = seasonsResult.data!!.size
            var episodesCount = 0
            var length = 0
            for (season in seasonsResult.data!!) {
                val episodesResult = episodeFacade.find(season)
                processResults(episodesResult)
                episodesCount += episodesResult.data!!.size
                length += episodesResult.data!!.sumBy { it.length!! }
            }

            model.addAttribute("show", ShowData(show = show, seasonsCount = seasonsCount, episodesCount = episodesCount, totalLength = Time(length)))
            model.addAttribute("title", "Show detail")

            return "show/detail"
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Shows page for adding show.
     *
     * @param model model
     * @return view for page for adding show
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        val show = ShowFO(id = null,
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
                genres = null)
        return createAddFormView(model, show)
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
        Assert.isNull(show.id, "ID must be null.")

        if (request.getParameter("create") != null) {
            if (errors.hasErrors()) {
                return createAddFormView(model, show)
            }
            processResults(showFacade.add(showMapper.mapBack(show).copy(genres = getGenres(show.genres!!))))
        }

        if (request.getParameter("choosePicture") != null) {
            return createAddFormView(model, show)
        }

        if (request.getParameter("removePicture") != null) {
            return createAddFormView(model, show.copy(picture = null))
        }

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page for editing show.
     *
     * @param model model
     * @param id    ID of editing show
     * @return view for page for editing show
     * @throws IllegalRequestException if show doesn't exist
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = showFacade.get(id)
        processResults(result)

        val show = result.data
        return if (show != null) {
            createEditFormView(model, showMapper.map(show))
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
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
     * @throws IllegalRequestException  if show doesn't exist
     */
    @PostMapping("/edit")
    fun processEdit(model: Model, @ModelAttribute("show") @Valid show: ShowFO, errors: Errors, request: HttpServletRequest): String {
        Assert.notNull(show.id, "ID mustn't be null.")

        if (request.getParameter("update") != null) {
            if (errors.hasErrors()) {
                return createEditFormView(model, show)
            }
            processResults(showFacade.update(processShow(showMapper.mapBack(show).copy(genres = getGenres(show.genres!!)))))
        }

        if (request.getParameter("choosePicture") != null) {
            return createEditFormView(model, show)
        }

        if (request.getParameter("removePicture") != null) {
            return createEditFormView(model, show.copy(picture = null))
        }

        return LIST_REDIRECT_URL
    }

    /**
     * Process duplicating show.
     *
     * @param id ID of duplicating show
     * @return view for redirect to page with list of shows
     * @throws IllegalRequestException if show doesn't exist
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(showFacade.duplicate(getShow(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing show.
     *
     * @param id ID of removing show
     * @return view for redirect to page with list of shows
     * @throws IllegalRequestException if show doesn't exist
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(showFacade.remove(getShow(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving show up.
     *
     * @param id ID of moving show
     * @return view for redirect to page with list of shows
     * @throws IllegalRequestException if show doesn't exist
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(showFacade.moveUp(getShow(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving show down.
     *
     * @param id ID of moving show
     * @return view for redirect to page with list of shows
     * @throws IllegalRequestException if show doesn't exist
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(showFacade.moveDown(getShow(id)))

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
        val pictures = pictureFacade.getAll()
        processResults(pictures)
        val genres = genreFacade.getAll()
        processResults(genres)

        model.addAttribute("show", show)
        model.addAttribute("title", title)
        model.addAttribute("pictures", pictures.data!!.map { it.id })
        model.addAttribute("genres", genres.data)
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
        return createFormView(model, show, "Add show", "add")
    }

    /**
     * Returns page's view with form for editing show.
     *
     * @param model model
     * @param show  FO for show
     * @return page's view with form for editing show
     */
    private fun createEditFormView(model: Model, show: ShowFO): String {
        return createFormView(model, show, "Edit show", "edit")
    }

    /**
     * Returns show with ID.
     *
     * @param id ID
     * @return show with ID
     * @throws IllegalRequestException if show doesn't exist
     */
    private fun getShow(id: Int): Show {
        val show = Show(id = id,
                czechName = null,
                originalName = null,
                csfd = null,
                imdbCode = null,
                wikiEn = null,
                wikiCz = null,
                picture = null,
                note = null,
                position = null,
                genres = null)

        return processShow(show)
    }

    /**
     * Returns processed show.
     *
     * @param show for processing
     * @return processed show
     * @throws IllegalRequestException if show doesn't exist
     */
    private fun processShow(show: Show): Show {
        val showResult = showFacade.get(show.id!!)
        processResults(showResult)

        if (showResult.data != null) {
            return show
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    /**
     * Returns genres.
     *
     * @param source list of genres' ID
     * @return genres
     */
    private fun getGenres(source: List<Int?>): List<Genre> {
        return source.map {
            val result = genreFacade.get(it!!)
            processResults(result)

            result.data!!
        }
    }

    companion object {

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/shows/list"

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Show doesn't exist."
    }

}
