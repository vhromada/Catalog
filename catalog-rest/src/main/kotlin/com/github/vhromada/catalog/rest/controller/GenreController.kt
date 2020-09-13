package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.facade.GenreFacade
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
 * A class represents controller for genres.
 *
 * @author Vladimir Hromada
 */
@RestController("genreController")
@RequestMapping("/catalog/genres")
class GenreController(private val genreFacade: GenreFacade) : AbstractController() {

    /**
     * Returns list of genres.
     *
     * @return list of genres
     */
    @GetMapping
    fun getGenres(): List<Genre> {
        return processResult(genreFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(genreFacade.newData())
    }

    /**
     * Returns genre with ID or null if there isn't such genre.
     *
     * @param id ID
     * @return genre with ID or null if there isn't such genre
     */
    @GetMapping("/{id}")
    fun getGenre(@PathVariable("id") id: Int): Genre? {
        return processResult(genreFacade.get(id))
    }

    /**
     * Adds genre. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *
     * @param genre genre
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody genre: Genre) {
        processResult(genreFacade.add(genre))
    }

    /**
     * Updates genre.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * Genre doesn't exist in data storage
     *
     * @param genre new value of genre
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody genre: Genre) {
        processResult(genreFacade.update(genre))
    }

    /**
     * Removes genre.
     * <br></br>
     * Validation errors:
     *
     *  * Genre doesn't exist in data storage
     *
     * @param id ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("id") id: Int) {
        val genre = Genre(id = id, name = null, position = null)
        processResult(genreFacade.remove(genre))
    }

    /**
     * Duplicates genre.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Genre doesn't exist in data storage
     *
     * @param genre genre
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@RequestBody genre: Genre) {
        processResult(genreFacade.duplicate(genre))
    }

    /**
     * Moves genre in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Genre can't be moved up
     *  * Genre doesn't exist in data storage
     *
     * @param genre genre
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@RequestBody genre: Genre) {
        processResult(genreFacade.moveUp(genre))
    }

    /**
     * Moves genre in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Genre can't be moved down
     *  * Genre doesn't exist in data storage
     *
     * @param genre genre
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@RequestBody genre: Genre) {
        processResult(genreFacade.moveDown(genre))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(genreFacade.updatePositions())
    }

}
