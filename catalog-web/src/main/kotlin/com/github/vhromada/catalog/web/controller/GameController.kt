package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.GameFacade
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.GameFO
import com.github.vhromada.catalog.web.mapper.GameMapper
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
 * A class represents controller for games.
 *
 * @author Vladimir Hromada
 */
@Controller("gameController")
@RequestMapping("/games")
class GameController(
        private val gameFacade: GameFacade,
        private val gameMapper: GameMapper) : AbstractResultController() {

    /**
     * Process new data.
     *
     * @return view for redirect to page with list of games
     */
    @GetMapping("/new")
    fun processNew(): String {
        processResults(gameFacade.newData())

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page with list of games.
     *
     * @param model model
     * @return view for page with list of games
     */
    @GetMapping("", "/list")
    fun showList(model: Model): String {
        val gamesResult = gameFacade.getAll()
        val mediaCountResult = gameFacade.getTotalMediaCount()
        processResults(gamesResult, mediaCountResult)

        model.addAttribute("games", gamesResult.data)
        model.addAttribute("mediaCount", mediaCountResult.data)
        model.addAttribute("title", "Games")

        return "game/index"
    }

    /**
     * Shows page with detail of game.
     *
     * @param model model
     * @param id    ID of editing game
     * @return view for page with detail of game
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("id") id: Int?): String {
        val result = gameFacade.get(id!!)
        processResults(result)

        val game = result.data
        if (game != null) {
            model.addAttribute("game", game)
            model.addAttribute("title", "Game detail")

            return "game/detail"
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Shows page for adding game.
     *
     * @param model model
     * @return view for page for adding game
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        val game = GameFO(id = null,
                name = null,
                wikiEn = null,
                wikiCz = null,
                mediaCount = null,
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
        return createFormView(model, game, "Add game", "add")
    }

    /**
     * Process adding game.
     *
     * @param model  model
     * @param game   FO for game
     * @param errors errors
     * @return view for redirect to page with list of games (no errors) or view for page for adding game (errors)
     * @throws IllegalArgumentException if ID isn't null
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @ModelAttribute("game") @Valid game: GameFO, errors: Errors): String {
        require(game.id == null) { "ID must be null." }

        if (errors.hasErrors()) {
            return createFormView(model, game, "Add game", "add")
        }
        processResults(gameFacade.add(gameMapper.mapBack(game)))

        return LIST_REDIRECT_URL
    }

    /**
     * Cancel adding game.
     *
     * @return view for redirect to page with list of games
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Shows page for editing game.
     *
     * @param model model
     * @param id    ID of editing game
     * @return view for page for editing game
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = gameFacade.get(id)
        processResults(result)

        val game = result.data
        return if (game != null) {
            createFormView(model, gameMapper.map(game), "Edit game", "edit")
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Process editing game.
     *
     * @param model  model
     * @param game   FO for game
     * @param errors errors
     * @return view for redirect to page with list of games (no errors) or view for page for editing game (errors)
     * @throws IllegalArgumentException if ID is null
     * @throws IllegalRequestException  if game doesn't exist
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @ModelAttribute("game") @Valid game: GameFO, errors: Errors): String {
        require(game.id != null) { "ID mustn't be null." }

        if (errors.hasErrors()) {
            return createFormView(model, game, "Edit game", "edit")
        }
        processResults(gameFacade.update(processGame(gameMapper.mapBack(game))))

        return LIST_REDIRECT_URL
    }

    /**
     * Cancel editing game.
     *
     * @return view for redirect to page with list of games
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun processEdit(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Process duplicating game.
     *
     * @param id ID of duplicating game
     * @return view for redirect to page with list of games
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(gameFacade.duplicate(getGame(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing game.
     *
     * @param id ID of removing game
     * @return view for redirect to page with list of games
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(gameFacade.remove(getGame(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving game up.
     *
     * @param id ID of moving game
     * @return view for redirect to page with list of games
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(gameFacade.moveUp(getGame(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving game down.
     *
     * @param id ID of moving game
     * @return view for redirect to page with list of games
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(gameFacade.moveDown(getGame(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process updating positions.
     *
     * @return view for redirect to page with list of games
     */
    @GetMapping("/update")
    fun processUpdatePositions(): String {
        processResults(gameFacade.updatePositions())

        return LIST_REDIRECT_URL
    }

    /**
     * Returns game with ID.
     *
     * @param id ID
     * @return game with ID
     * @throws IllegalRequestException if game doesn't exist
     */
    private fun getGame(id: Int): Game {
        val game = Game(id = id,
                name = null,
                wikiEn = null,
                wikiCz = null,
                mediaCount = null,
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

        return processGame(game)
    }

    /**
     * Returns processed game.
     *
     * @param game game for processing
     * @return processed game
     * @throws IllegalRequestException if game doesn't exist
     */
    private fun processGame(game: Game): Game {
        val gameResult = gameFacade.get(game.id!!)
        processResults(gameResult)

        if (gameResult.data != null) {
            return game
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    /**
     * Returns page's view with form.
     *
     * @param model  model
     * @param game   FO for game
     * @param title  page's title
     * @param action action
     * @return page's view with form
     */
    private fun createFormView(model: Model, game: GameFO, title: String, action: String): String {
        model.addAttribute("game", game)
        model.addAttribute("title", title)
        model.addAttribute("action", action)

        return "game/form"
    }

    companion object {

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/games/list"

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Game doesn't exist."

    }

}
