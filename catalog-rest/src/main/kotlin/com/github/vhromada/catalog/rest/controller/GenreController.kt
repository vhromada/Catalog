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
class GenreController(
    private val genreFacade: GenreFacade
) : AbstractController() {

    /**
     * Returns list of genres.
     *
     * @return list of genres
     */
    @GetMapping
    fun getGenres(): List<Genre> {
        return processResult(result = genreFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(result = genreFacade.newData())
    }

    /**
     * Returns genre with ID.
     *
     * @param id ID
     * @return genre with ID
     */
    @GetMapping("/{id}")
    fun getGenre(@PathVariable("id") id: Int): Genre {
        return processResult(result = genreFacade.get(id = id))!!
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
        processResult(result = genreFacade.add(data = genre))
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
        processResult(result = genreFacade.update(data = genre))
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
        processResult(result = genreFacade.remove(id = id))
    }

    /**
     * Duplicates genre.
     * <br></br>
     * Validation errors:
     *
     *  * Genre doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/duplicate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@PathVariable("id") id: Int) {
        processResult(result = genreFacade.duplicate(id = id))
    }

    /**
     * Moves genre in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Genre can't be moved up
     *  * Genre doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveUp/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@PathVariable("id") id: Int) {
        processResult(result = genreFacade.moveUp(id = id))
    }

    /**
     * Moves genre in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Genre can't be moved down
     *  * Genre doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveDown/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@PathVariable("id") id: Int) {
        processResult(result = genreFacade.moveDown(id = id))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(result = genreFacade.updatePositions())
    }

}
