package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.facade.GenreFacade
import com.github.vhromada.catalog.web.fo.GenreFO
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
 * A class represents controller for genres.
 *
 * @author Vladimir Hromada
 */
@Controller("genreController")
@RequestMapping("/genres")
class GenreController(
    private val genreFacade: GenreFacade,
    private val genreMapper: Mapper<Genre, GenreFO>
) : AbstractResultController() {

    /**
     * Process new data.
     *
     * @return view for redirect to page with list of genres
     */
    @GetMapping("/new")
    fun processNew(): String {
        processResults(genreFacade.newData())

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page with list of genres.
     *
     * @param model model
     * @return view for page with list of genres
     */
    @GetMapping("", "/list")
    fun showList(model: Model): String {
        val result = genreFacade.getAll()
        processResults(result)

        model.addAttribute("genres", result.data)
        model.addAttribute("title", "Genres")

        return "genre/index"
    }

    /**
     * Shows page for adding genre.
     *
     * @param model model
     * @return view for page for adding genre
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        return createFormView(model = model, genre = GenreFO(id = null, name = null, position = null), title = "Add genre", action = "add")
    }

    /**
     * Process adding genre.
     *
     * @param model  model
     * @param genre  FO for genre
     * @param errors errors
     * @return view for redirect to page with list of genres (no errors) or view for page for adding genre (errors)
     * @throws IllegalArgumentException if ID isn't null
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @ModelAttribute("genre") @Valid genre: GenreFO, errors: Errors): String {
        require(genre.id == null) { "ID must be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, genre = genre, title = "Add genre", action = "add")
        }
        processResults(genreFacade.add(data = genreMapper.mapBack(source = genre)))

        return LIST_REDIRECT_URL
    }

    /**
     * Cancel adding genre.
     *
     * @return view for redirect to page with list of genres
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Shows page for editing genre.
     *
     * @param model model
     * @param id    ID of editing genre
     * @return view for page for editing genre
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = genreFacade.get(id = id)
        processResults(result)

        return createFormView(model = model, genre = genreMapper.map(source = result.data!!), title = "Edit genre", action = "edit")
    }

    /**
     * Process editing genre.
     *
     * @param model  model
     * @param genre  FO for genre
     * @param errors errors
     * @return view for redirect to page with list of genres (no errors) or view for page for editing genre (errors)
     * @throws IllegalArgumentException if ID is null
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @ModelAttribute("genre") @Valid genre: GenreFO, errors: Errors): String {
        require(genre.id != null) { "ID mustn't be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, genre = genre, title = "Edit genre", action = "edit")
        }
        processResults(genreFacade.update(data = genreMapper.mapBack(source = genre)))

        return LIST_REDIRECT_URL
    }

    /**
     * Cancel editing genre.
     *
     * @return view for redirect to page with list of genres
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun cancelEdit(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Process duplicating genre.
     *
     * @param id ID of duplicating genre
     * @return view for redirect to page with list of genres
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(genreFacade.duplicate(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing genre.
     *
     * @param id ID of removing genre
     * @return view for redirect to page with list of genres
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(genreFacade.remove(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving genre up.
     *
     * @param id ID of moving genre
     * @return view for redirect to page with list of genres
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(genreFacade.moveUp(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving genre down.
     *
     * @param id ID of moving genre
     * @return view for redirect to page with list of genres
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(genreFacade.moveDown(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process updating positions.
     *
     * @return view for redirect to page with list of genres
     */
    @GetMapping("/update")
    fun processUpdatePositions(): String {
        processResults(genreFacade.updatePositions())

        return LIST_REDIRECT_URL
    }

    /**
     * Returns page's view with form.
     *
     * @param model  model
     * @param genre  FO for genre
     * @param title  page's title
     * @param action action
     * @return page's view with form
     */
    private fun createFormView(model: Model, genre: GenreFO, title: String, action: String): String {
        model.addAttribute("genre", genre)
        model.addAttribute("title", title)
        model.addAttribute("action", action)

        return "genre/form"
    }

    companion object {

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/genres/list"

    }

}
