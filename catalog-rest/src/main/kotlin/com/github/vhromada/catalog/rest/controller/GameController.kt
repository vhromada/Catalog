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
class GameController(private val gameFacade: GameFacade) : AbstractController() {

    /**
     * Returns list of games.
     *
     * @return list of games
     */
    @GetMapping
    fun getGames(): List<Game> {
        return processResult(gameFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(gameFacade.newData())
    }

    /**
     * Returns game with ID or null if there isn't such game.
     *
     * @param id ID
     * @return game with ID or null if there isn't such game
     */
    @GetMapping("/{id}")
    fun getGame(@PathVariable("id") id: Int): Game? {
        return processResult(gameFacade.get(id))
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
        processResult(gameFacade.add(game))
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
        processResult(gameFacade.update(game))
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
        val game = Game(id = id,
                name = null,
                mediaCount = null,
                wikiEn = null,
                wikiCz = null,
                format = null,
                cheat = null,
                crack = null,
                serialKey = null,
                patch = null,
                trainer = null,
                trainerData = null,
                editor = null,
                saves = null,
                otherData = null,
                note = null,
                position = null)
        processResult(gameFacade.remove(game))
    }

    /**
     * Duplicates game.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Game doesn't exist in data storage
     *
     * @param game game
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@RequestBody game: Game) {
        processResult(gameFacade.duplicate(game))
    }

    /**
     * Moves game in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Game can't be moved up
     *  * Game doesn't exist in data storage
     *
     * @param game game
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@RequestBody game: Game) {
        processResult(gameFacade.moveUp(game))
    }

    /**
     * Moves game in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Game can't be moved down
     *  * Game doesn't exist in data storage
     *
     * @param game game
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@RequestBody game: Game) {
        processResult(gameFacade.moveDown(game))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(gameFacade.updatePositions())
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @GetMapping("/totalMedia")
    fun totalMediaCount(): Int {
        return processResult(gameFacade.getTotalMediaCount())!!
    }

}
