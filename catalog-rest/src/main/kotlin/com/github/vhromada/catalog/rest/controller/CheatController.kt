package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.CheatFacade
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
 * A class represents controller for cheats.
 *
 * @author Vladimir Hromada
 */
@RestController("cheatController")
@RequestMapping("/catalog/games/{gameId}/cheats")
class CheatController(private val cheatFacade: CheatFacade) : AbstractController() {

    /**
     * Returns cheat with ID or null if there isn't such cheat.
     *
     * @param gameId   game ID
     * @param cheatId  cheat ID
     * @return cheat with ID or null if there isn't such cheat
     */
    @GetMapping("/{cheatId}")
    fun getCheat(@PathVariable("gameId") gameId: Int,
                 @PathVariable("cheatId") cheatId: Int): Cheat? {
        return processResult(cheatFacade.get(cheatId))
    }

    /**
     * Adds cheat. Sets new ID.
     * <br></br>
     * Validation errors:
     *
     *  * Game ID is null
     *  * Game doesn't exist in cheat storage
     *  * Cheat ID isn't null
     *  * Setting for game is null
     *  * Setting for cheat is null
     *  * Cheat's data are null
     *  * Cheat's data contain null value
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *  * Game has already cheat in data storage
     *
     * @param gameId game ID
     * @param cheat  cheat
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@PathVariable("gameId") gameId: Int,
            @RequestBody cheat: Cheat) {
        val game = Game(id = gameId,
                name = null,
                mediaCount = null,
                wikiEn = null,
                wikiCz = null,
                format = null,
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
        processResult(cheatFacade.add(game, cheat))
    }

    /**
     * Updates cheat.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Setting for game is null
     *  * Setting for cheat is null
     *  * Cheat's data are null
     *  * Cheat's data contain null value
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *  * Cheat doesn't exist in data storage
     *
     * @param gameId game ID
     * @param cheat  new value of cheat
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable("gameId") gameId: Int,
               @RequestBody cheat: Cheat) {
        processResult(cheatFacade.update(cheat))
    }

    /**
     * Removes cheat.
     * <br></br>
     * Validation errors:
     *
     *  * Cheat doesn't exist in data storage
     *
     * @param gameId game ID
     * @param id     ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("gameId") gameId: Int,
               @PathVariable("id") id: Int) {
        val cheat = Cheat(id = id, gameSetting = null, cheatSetting = null, position = null, data = null)
        processResult(cheatFacade.remove(cheat))
    }

    /**
     * Returns cheat for specified game.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Game doesn't exist in data storage
     *
     * @param gameId game ID
     * @return cheat for specified game
     */
    @GetMapping
    fun findCheatByGame(@PathVariable("gameId") gameId: Int): Cheat? {
        val game = Game(id = gameId,
                name = null,
                mediaCount = null,
                wikiEn = null,
                wikiCz = null,
                format = null,
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
        return processResult(cheatFacade.find(game))?.firstOrNull()
    }

}
