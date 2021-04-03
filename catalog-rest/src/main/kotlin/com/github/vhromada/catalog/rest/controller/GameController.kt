package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.GameFacade
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
 * A class represents controller for games.
 *
 * @author Vladimir Hromada
 */
@RestController("gameController")
@RequestMapping("/catalog/games")
class GameController(
    private val gameFacade: GameFacade
) : AbstractController() {

    /**
     * Returns list of games.
     *
     * @return list of games
     */
    @GetMapping
    fun getGames(): List<Game> {
        return processResult(result = gameFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(result = gameFacade.newData())
    }

    /**
     * Returns game with ID.
     *
     * @param id ID
     * @return game with ID
     */
    @GetMapping("/{id}")
    fun getGame(@PathVariable("id") id: Int): Game {
        return processResult(result = gameFacade.get(id = id))!!
    }

    /**
     * Adds game. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about game is null
     *  * URL to czech Wikipedia page about game is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *
     * @param game game
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody game: Game) {
        processResult(result = gameFacade.add(data = game))
    }

    /**
     * Updates game.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about game is null
     *  * URL to czech Wikipedia page about game is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *  * Game doesn't exist in data storage
     *
     * @param game new value of game
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody game: Game) {
        processResult(result = gameFacade.update(data = game))
    }

    /**
     * Removes game.
     * <br></br>
     * Validation errors:
     *
     *  * Game doesn't exist in data storage
     *
     * @param id ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("id") id: Int) {
        processResult(result = gameFacade.remove(id = id))
    }

    /**
     * Duplicates game.
     * <br></br>
     * Validation errors:
     *
     *  * Game doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/duplicate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@PathVariable("id") id: Int) {
        processResult(result = gameFacade.duplicate(id = id))
    }

    /**
     * Moves game in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Game can't be moved up
     *  * Game doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveUp/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@PathVariable("id") id: Int) {
        processResult(result = gameFacade.moveUp(id = id))
    }

    /**
     * Moves game in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Game can't be moved down
     *  * Game doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveDown/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@PathVariable("id") id: Int) {
        processResult(result = gameFacade.moveDown(id = id))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(result = gameFacade.updatePositions())
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @GetMapping("/totalMedia")
    fun totalMediaCount(): Int {
        return processResult(result = gameFacade.getTotalMediaCount())!!
    }

}
