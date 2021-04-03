package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.facade.GenreFacade
import com.github.vhromada.catalog.facade.MovieFacade
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.entity.Language
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
 * A class represents controller for movies.
 *
 * @author Vladimir Hromada
 */
@Controller("movieController")
@RequestMapping("/movies")
class MovieController(
    private val movieFacade: MovieFacade,
    private val pictureFacade: PictureFacade,
    private val genreFacade: GenreFacade,
    private val movieMapper: Mapper<Movie, MovieFO>
) : AbstractResultController() {

    /**
     * Process new data.
     *
     * @return view for redirect to page with list of movies
     */
    @GetMapping("/new")
    fun processNew(): String {
        movieFacade.newData()

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page with list of movies.
     *
     * @param model model
     * @return view for page with list of movies
     */
    @RequestMapping("", "/list")
    fun showList(model: Model): String {
        val moviesResult = movieFacade.getAll()
        val mediaCountResult = movieFacade.getTotalMediaCount()
        val totalLengthResult = movieFacade.getTotalLength()
        processResults(moviesResult, mediaCountResult, totalLengthResult)

        model.addAttribute("movies", moviesResult.data)
        model.addAttribute("mediaCount", mediaCountResult.data)
        model.addAttribute("totalLength", totalLengthResult.data)
        model.addAttribute("title", "Movies")

        return "movie/index"
    }

    /**
     * Shows page with detail of movie.
     *
     * @param model model
     * @param id    ID of showing movie
     * @return view for page with detail of movie
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("id") id: Int): String {
        val result = movieFacade.get(id = id)
        processResults(result)

        model.addAttribute("movie", result.data)
        model.addAttribute("title", "Movie detail")

        return "movie/detail"
    }

    /**
     * Shows page for adding movie.
     *
     * @param model model
     * @return view for page for adding movie
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        val movie = MovieFO(
            id = null,
            czechName = null,
            originalName = null,
            year = null,
            language = null,
            subtitles = null,
            media = null,
            csfd = null,
            imdb = false,
            imdbCode = null,
            wikiEn = null,
            wikiCz = null,
            picture = null,
            note = null,
            position = null,
            genres = null
        )
        return createAddFormView(model = model, movie = movie)
    }

    /**
     * Process adding movie.
     *
     * @param model   model
     * @param movie   FO for movie
     * @param errors  errors
     * @param request HTTP request
     * @return view for redirect to page with list of movies (no errors) or view for page for adding movie (errors)
     * @throws IllegalArgumentException if ID isn't null
     */
    @PostMapping("/add")
    fun processAdd(model: Model, @ModelAttribute("movie") @Valid movie: MovieFO, errors: Errors, request: HttpServletRequest): String {
        require(movie.id == null) { "ID must be null." }

        if (request.getParameter("create") != null) {
            if (errors.hasErrors()) {
                return createAddFormView(model = model, movie = movie)
            }
            processResults(movieFacade.add(data = movieMapper.mapBack(source = movie).copy(genres = getGenres(source = movie.genres!!))))
        }

        if (request.getParameter("addMedium") != null) {
            val media = if (movie.media == null) mutableListOf() else movie.media!!.toMutableList()
            media.add(TimeFO(hours = null, minutes = null, seconds = null))

            return createAddFormView(model = model, movie = movie.copy(media = media))
        }

        return processAddMovie(model = model, movie = movie, request = request)
    }

    /**
     * Shows page for editing movie.
     *
     * @param model model
     * @param id    ID of editing movie
     * @return view for page for editing movie
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = movieFacade.get(id = id)
        processResults(result)

        return createEditFormView(model = model, movie = movieMapper.map(source = result.data!!))
    }

    /**
     * Process editing movie.
     *
     * @param model   model
     * @param movie   FO for movie
     * @param errors  errors
     * @param request HTTP request
     * @return view for redirect to page with list of movies (no errors) or view for page for editing movie (errors)
     * @throws IllegalArgumentException if ID is null
     */
    @PostMapping("/edit")
    fun processEdit(model: Model, @ModelAttribute("movie") @Valid movie: MovieFO, errors: Errors, request: HttpServletRequest): String {
        require(movie.id != null) { "ID mustn't be null." }

        if (request.getParameter("update") != null) {
            if (errors.hasErrors()) {
                return createEditFormView(model = model, movie = movie)
            }

            processResults(movieFacade.update(data = movieMapper.mapBack(source = movie).copy(genres = getGenres(source = movie.genres!!))))
        }

        if (request.getParameter("addMedium") != null) {
            val media = movie.media!!.toMutableList()
            media.add(TimeFO(hours = null, minutes = null, seconds = null))

            return createEditFormView(model = model, movie = movie.copy(media = media))
        }

        return processEditMovie(model = model, movie = movie, request = request)
    }

    /**
     * Process duplicating movie.
     *
     * @param id ID of duplicating movie
     * @return view for redirect to page with list of movies
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(movieFacade.duplicate(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing movie.
     *
     * @param id ID of removing movie
     * @return view for redirect to page with list of movies
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(movieFacade.remove(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving movie up.
     *
     * @param id ID of moving movie
     * @return view for redirect to page with list of movies
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(movieFacade.moveUp(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving movie down.
     *
     * @param id ID of moving movie
     * @return view for redirect to page with list of movies
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(movieFacade.moveDown(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process updating positions.
     *
     * @return view for redirect to page with list of movies
     */
    @GetMapping("/update")
    fun processUpdatePositions(): String {
        processResults(movieFacade.updatePositions())

        return LIST_REDIRECT_URL
    }

    /**
     * Process adding movie.
     *
     * @param model   model
     * @param movie   FO for movie
     * @param request HTTP request
     * @return view for redirect to page with list of movies (no errors) or view for page for adding movie (errors)
     */
    private fun processAddMovie(model: Model, movie: MovieFO, request: HttpServletRequest): String {
        if (request.getParameter("choosePicture") != null) {
            return createAddFormView(model = model, movie = movie)
        }

        if (request.getParameter("removePicture") != null) {
            return createAddFormView(model = model, movie = movie.copy(picture = null))
        }

        val index = getRemoveIndex(request = request)
        if (index != null) {
            val media = movie.media!!.toMutableList()
            media.removeAt(index)

            return createAddFormView(model = model, movie = movie.copy(media = media))
        }

        return LIST_REDIRECT_URL
    }

    /**
     * Process editing movie.
     *
     * @param model   model
     * @param movie   FO for movie
     * @param request HTTP request
     * @return view for redirect to page with list of movies (no errors) or view for page for editing movie (errors)
     */
    private fun processEditMovie(model: Model, movie: MovieFO, request: HttpServletRequest): String {
        if (request.getParameter("choosePicture") != null) {
            return createEditFormView(model = model, movie = movie)
        }

        if (request.getParameter("removePicture") != null) {
            return createEditFormView(model = model, movie = movie.copy(picture = null))
        }

        val index = getRemoveIndex(request = request)
        if (index != null) {
            val media = movie.media!!.toMutableList()
            media.removeAt(index)

            return createEditFormView(model = model, movie = movie.copy(media = media))
        }

        return LIST_REDIRECT_URL
    }

    /**
     * Returns page's view with form.
     *
     * @param model  model
     * @param movie  FO for movie
     * @param title  page's title
     * @param action action
     * @return page's view with form
     */
    private fun createFormView(model: Model, movie: MovieFO, title: String, action: String): String {
        val picturesResult = pictureFacade.getAll()
        val genresResult = genreFacade.getAll()
        processResults(picturesResult, genresResult)

        model.addAttribute("movie", movie)
        model.addAttribute("title", title)
        model.addAttribute("languages", Language.values())
        model.addAttribute("subtitles", arrayOf(Language.CZ, Language.EN))
        model.addAttribute("pictures", picturesResult.data!!.map { it.id })
        model.addAttribute("genres", genresResult.data)
        model.addAttribute("action", action)

        return "movie/form"
    }

    /**
     * Returns page's view with form for adding movie.
     *
     * @param model model
     * @param movie FO for movie
     * @return page's view with form for adding movie
     */
    private fun createAddFormView(model: Model, movie: MovieFO): String {
        return createFormView(model = model, movie = movie, title = "Add movie", action = "add")
    }

    /**
     * Returns page's view with form for editing movie.
     *
     * @param model model
     * @param movie FO for movie
     * @return page's view with form for editing movie
     */
    private fun createEditFormView(model: Model, movie: MovieFO): String {
        return createFormView(model = model, movie = movie, title = "Edit movie", action = "edit")
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

    /**
     * Returns index of removing media.
     *
     * @param request HTTP request
     * @return index of removing media
     */
    private fun getRemoveIndex(request: HttpServletRequest): Int? {
        var index: Int? = null
        val names = request.parameterNames
        while (names.hasMoreElements() && index == null) {
            val name = names.nextElement()
            if (name.startsWith("removeMedium")) {
                index = (name.substring(12).toInt())
            }
        }

        return index
    }

    companion object {

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/movies/list"

    }

}
