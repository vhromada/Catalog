package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.facade.GenreFacade
import com.github.vhromada.catalog.facade.MovieFacade
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.catalog.web.mapper.MovieMapper
import com.github.vhromada.common.entity.Language
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
        private val movieMapper: MovieMapper) : AbstractResultController() {

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
     * @param id    ID of editing movie
     * @return view for page with detail of movie
     * @throws IllegalRequestException if movie doesn't exist
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("id") id: Int): String {
        val result = movieFacade.get(id)
        processResults(result)

        val movie = result.data
        if (movie != null) {
            model.addAttribute("movie", movie)
            model.addAttribute("title", "Movie detail")

            return "movie/detail"
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Shows page for adding movie.
     *
     * @param model model
     * @return view for page for adding movie
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        val movie = MovieFO(id = null,
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
                genres = null)
        return createAddFormView(model, movie)
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
                return createAddFormView(model, movie)
            }
            processResults(movieFacade.add(movieMapper.mapBack(movie).copy(genres = getGenres(movie.genres!!))))
        }

        if (request.getParameter("addMedium") != null) {
            val media = if (movie.media == null) mutableListOf() else movie.media!!.toMutableList()
            media.add(TimeFO(hours = null, minutes = null, seconds = null))

            return createAddFormView(model, movie.copy(media = media))
        }

        return processAddMovie(model, movie, request)
    }

    /**
     * Shows page for editing movie.
     *
     * @param model model
     * @param id    ID of editing movie
     * @return view for page for editing movie
     * @throws IllegalRequestException if movie doesn't exist
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = movieFacade.get(id)
        processResults(result)

        val movie = result.data
        return if (movie != null) {
            createEditFormView(model, movieMapper.map(movie))
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
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
     * @throws IllegalRequestException  if movie doesn't exist
     */
    @PostMapping("/edit")
    fun processEdit(model: Model, @ModelAttribute("movie") @Valid movie: MovieFO, errors: Errors, request: HttpServletRequest): String {
        require(movie.id != null) { "ID mustn't be null." }

        if (request.getParameter("update") != null) {
            if (errors.hasErrors()) {
                return createEditFormView(model, movie)
            }

            processResults(movieFacade.update(processMovie(movieMapper.mapBack(movie).copy(genres = getGenres(movie.genres!!)))))
        }

        if (request.getParameter("addMedium") != null) {
            val media = movie.media!!.toMutableList()
            media.add(TimeFO(hours = null, minutes = null, seconds = null))

            return createEditFormView(model, movie.copy(media = media))
        }

        return processEditMovie(model, movie, request)
    }

    /**
     * Process duplicating movie.
     *
     * @param id ID of duplicating movie
     * @return view for redirect to page with list of movies
     * @throws IllegalRequestException if movie doesn't exist
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(movieFacade.duplicate(getMovie(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing movie.
     *
     * @param id ID of removing movie
     * @return view for redirect to page with list of movies
     * @throws IllegalRequestException if movie doesn't exist
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(movieFacade.remove(getMovie(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving movie up.
     *
     * @param id ID of moving movie
     * @return view for redirect to page with list of movies
     * @throws IllegalRequestException if movie doesn't exist
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(movieFacade.moveUp(getMovie(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving movie down.
     *
     * @param id ID of moving movie
     * @return view for redirect to page with list of movies
     * @throws IllegalRequestException if movie doesn't exist
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(movieFacade.moveDown(getMovie(id)))

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
            return createAddFormView(model, movie)
        }

        if (request.getParameter("removePicture") != null) {
            return createAddFormView(model, movie.copy(picture = null))
        }

        val index = getRemoveIndex(request)
        if (index != null) {
            val media = movie.media!!.toMutableList()
            media.removeAt(index)

            return createAddFormView(model, movie.copy(media = media))
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
            return createEditFormView(model, movie)
        }

        if (request.getParameter("removePicture") != null) {
            return createEditFormView(model, movie.copy(picture = null))
        }

        val index = getRemoveIndex(request)
        if (index != null) {
            val media = movie.media!!.toMutableList()
            media.removeAt(index)

            return createEditFormView(model, movie.copy(media = media))
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
        val pictures = pictureFacade.getAll()
        processResults(pictures)
        val genres = genreFacade.getAll()
        processResults(genres)

        model.addAttribute("movie", movie)
        model.addAttribute("title", title)
        model.addAttribute("languages", Language.values())
        model.addAttribute("subtitles", arrayOf(Language.CZ, Language.EN))
        model.addAttribute("pictures", pictures.data!!.map { it.id })
        model.addAttribute("genres", genres.data)
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
        return createFormView(model, movie, "Add movie", "add")
    }

    /**
     * Returns page's view with form for editing movie.
     *
     * @param model model
     * @param movie FO for movie
     * @return page's view with form for editing movie
     */
    private fun createEditFormView(model: Model, movie: MovieFO): String {
        return createFormView(model, movie, "Edit movie", "edit")
    }

    /**
     * Returns movie with ID.
     *
     * @param id ID
     * @return movie with ID
     * @throws IllegalRequestException if movie doesn't exist
     */
    private fun getMovie(id: Int): Movie {
        val movie = Movie(id = id,
                czechName = null,
                originalName = null,
                year = null,
                language = null,
                subtitles = null,
                media = null,
                csfd = null,
                imdbCode = null,
                wikiEn = null,
                wikiCz = null,
                picture = null,
                note = null,
                position = null,
                genres = null)

        return processMovie(movie)
    }

    /**
     * Returns processed movie.
     *
     * @param movie movie for processing
     * @return processed movie
     * @throws IllegalRequestException if movie doesn't exist
     */
    private fun processMovie(movie: Movie): Movie {
        val movieResult = movieFacade.get(movie.id!!)
        processResults(movieResult)

        if (movieResult.data != null) {
            return movie
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

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Movie doesn't exist."
    }

}
