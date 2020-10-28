package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.CheatFacade
import com.github.vhromada.catalog.facade.GameFacade
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.CheatDataFO
import com.github.vhromada.catalog.web.fo.CheatFO
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
 * A class represents controller for cheats.
 *
 * @author Vladimir Hromada
 */
@Controller("cheatController")
@RequestMapping("/games/{gameId}/cheats")
class CheatController(
        private val gameFacade: GameFacade,
        private val cheatFacade: CheatFacade,
        private val cheatMapper: Mapper<Cheat, CheatFO>
) : AbstractResultController() {

    /**
     * Shows page with cheat.
     *
     * @param model  model
     * @param gameId game ID
     * @return view for page with cheat
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping
    fun showList(model: Model, @PathVariable("gameId") gameId: Int): String {
        val game = getGame(gameId)

        val cheatResult = cheatFacade.find(game)
        processResults(cheatResult)

        model.addAttribute("cheat", cheatResult.data?.firstOrNull())
        model.addAttribute("game", gameId)
        model.addAttribute("title", "Cheats")

        return "cheat/index"
    }

    /**
     * Shows page for adding cheat.
     *
     * @param model  model
     * @param gameId game ID
     * @return view for page for adding cheat
     * @throws IllegalRequestException if game doesn't exist
     */
    @GetMapping("/add")
    fun showAdd(model: Model, @PathVariable("gameId") gameId: Int): String {
        getGame(gameId)

        val cheat = CheatFO(id = null,
                gameSetting = null,
                cheatSetting = null,
                data = null,
                position = null)
        return createAddFormView(model, cheat, gameId)
    }

    /**
     * Process adding cheat.
     *
     * @param model    model
     * @param gameId   game ID
     * @param cheat    FO for cheat
     * @param errors   errors
     * @param request  HTTP request
     * @return view for redirect to page with cheats (no errors) or view for page for adding cheat (errors)
     * @throws IllegalArgumentException if ID isn't null
     * @throws IllegalRequestException  if game doesn't exist
     */
    @PostMapping("/add")
    fun processAdd(model: Model, @PathVariable("gameId") gameId: Int, @ModelAttribute("cheat") @Valid cheat: CheatFO, errors: Errors, request: HttpServletRequest): String {
        require(cheat.id == null) { "ID must be null." }

        if (request.getParameter("create") != null) {
            if (errors.hasErrors()) {
                return createAddFormView(model, cheat, gameId)
            }

            val game = getGame(gameId)
            processResults(cheatFacade.add(game, cheatMapper.mapBack(cheat)))

            return getCheatsRedirectUrl(gameId)
        }

        if (request.getParameter("addCheat") != null) {
            val cheatData = if (cheat.data == null) mutableListOf() else cheat.data!!.toMutableList()
            cheatData.add(CheatDataFO())

            return createAddFormView(model, cheat.copy(data = cheatData), gameId)
        }

        val index = getRemoveIndex(request)
        if (index != null) {
            val cheatData = cheat.data!!.toMutableList()
            cheatData.removeAt(index)

            return createAddFormView(model, cheat.copy(data = cheatData), gameId)
        }

        getGame(gameId)
        return getCheatsRedirectUrl(gameId)
    }

    /**
     * Shows page for editing cheat.
     *
     * @param model  model
     * @param gameId game ID
     * @return view for page for editing cheat
     * @throws IllegalRequestException if game doesn't exist
     * or cheat doesn't exist
     */
    @GetMapping("/edit")
    fun showEdit(model: Model, @PathVariable("gameId") gameId: Int): String {
        return createEditFormView(model, cheatMapper.map(getCheat(gameId)), gameId)
    }

    /**
     * Process editing cheat.
     *
     * @param model   model
     * @param gameId  game ID
     * @param cheat   FO for cheat
     * @param errors  errors
     * @param request HTTP request
     * @return view for redirect to page with cheats (no errors) or view for page for editing cheat (errors)
     * @throws IllegalArgumentException if ID is null
     * @throws IllegalRequestException if game doesn't exist
     * or cheat doesn't exist
     */
    @PostMapping("/edit")
    fun processEdit(model: Model, @PathVariable("gameId") gameId: Int, @ModelAttribute("cheat") @Valid cheat: CheatFO, errors: Errors, request: HttpServletRequest): String {
        require(cheat.id != null) { "ID mustn't be null." }

        if (request.getParameter("update") != null) {
            if (errors.hasErrors()) {
                return createEditFormView(model, cheat, gameId)
            }

            getGame(gameId)
            processResults(cheatFacade.update(processCheat(cheatMapper.mapBack(cheat))))
            return getCheatsRedirectUrl(gameId)
        }

        if (request.getParameter("addCheat") != null) {
            val cheatData = cheat.data!!.toMutableList()
            cheatData.add(CheatDataFO())

            return createEditFormView(model, cheat.copy(data = cheatData), gameId)
        }

        val index = getRemoveIndex(request)
        if (index != null) {
            val cheatData = cheat.data!!.toMutableList()
            cheatData.removeAt(index)

            return createEditFormView(model, cheat.copy(data = cheatData), gameId)
        }

        getGame(gameId)
        return getCheatsRedirectUrl(gameId)
    }

    /**
     * Process removing cheat.
     *
     * @param gameId game ID
     * @return view for redirect to page with cheats
     * @throws IllegalRequestException if game doesn't exist
     * or cheat doesn't exist
     */
    @GetMapping("/remove")
    fun processRemove(@PathVariable("gameId") gameId: Int): String {
        cheatFacade.remove(getCheat(gameId))

        return getCheatsRedirectUrl(gameId)
    }

    /**
     * Returns game.
     *
     * @param id game ID
     * @return game
     * @throws IllegalRequestException if game doesn't exist
     */
    private fun getGame(id: Int): Game {
        val gameResult = gameFacade.get(id)
        processResults(gameResult)

        return gameResult.data ?: throw IllegalRequestException("Game doesn't exist.")
    }

    /**
     * Returns cheat.
     *
     * @param id game ID
     * @return cheat
     * @throws IllegalRequestException if game doesn't exist
     * or cheat doesn't exist
     */
    private fun getCheat(id: Int): Cheat {
        val cheatResult = cheatFacade.find(getGame(id))
        processResults(cheatResult)

        return cheatResult.data?.firstOrNull() ?: throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    /**
     * Returns processed cheat.
     *
     * @param cheat cheat for processing
     * @return processed cheat
     * @throws IllegalRequestException if cheat doesn't exist
     */
    private fun processCheat(cheat: Cheat): Cheat {
        val cheatResult = cheatFacade.get(cheat.id!!)
        processResults(cheatResult)

        if (cheatResult.data != null) {
            return cheat
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    /**
     * Returns page's view with form for adding cheat.
     *
     * @param model  model
     * @param cheat  FO for cheat
     * @param gameId game ID
     * @return page's view with form for adding cheat
     */
    private fun createAddFormView(model: Model, cheat: CheatFO, gameId: Int): String {
        return createFormView(model, cheat, gameId, "Add cheat", "add")
    }

    /**
     * Returns page's view with form for editing cheat.
     *
     * @param model  model
     * @param cheat  FO for cheat
     * @param gameId game ID
     * @return page's view with form for editing cheat
     */
    private fun createEditFormView(model: Model, cheat: CheatFO, gameId: Int): String {
        return createFormView(model, cheat, gameId, "Edit cheat", "edit")
    }

    /**
     * Returns page's view with form.
     *
     * @param model  model
     * @param cheat FO for cheat
     * @param gameId game ID
     * @param title  page's title
     * @param action action
     * @return page's view with form
     */
    private fun createFormView(model: Model, cheat: CheatFO, gameId: Int, title: String, action: String): String {
        model.addAttribute("cheat", cheat)
        model.addAttribute("game", gameId)
        model.addAttribute("title", title)
        model.addAttribute("action", action)

        return "cheat/form"
    }

    /**
     * Returns redirect URL to cheats.
     *
     * @param gameId game ID
     * @return redirect URL to cheats
     */
    private fun getCheatsRedirectUrl(gameId: Int): String {
        return "redirect:/games/$gameId/cheats"
    }

    /**
     * Returns index of removing cheat.
     *
     * @param request HTTP request
     * @return index of removing cheat
     */
    private fun getRemoveIndex(request: HttpServletRequest): Int? {
        var index: Int? = null
        val names = request.parameterNames
        while (names.hasMoreElements() && index == null) {
            val name = names.nextElement()
            if (name.startsWith("removeCheat")) {
                index = (name.substring(11).toInt())
            }
        }

        return index
    }

    companion object {

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Cheat doesn't exist."

    }

}
