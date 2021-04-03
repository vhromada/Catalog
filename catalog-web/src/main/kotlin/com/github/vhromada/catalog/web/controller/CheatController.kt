package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.facade.CheatFacade
import com.github.vhromada.catalog.facade.GameFacade
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
     */
    @GetMapping
    fun showList(model: Model, @PathVariable("gameId") gameId: Int): String {
        val result = cheatFacade.find(parent = gameId)
        processResults(result)

        model.addAttribute("cheat", result.data?.firstOrNull())
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
     */
    @GetMapping("/add")
    fun showAdd(model: Model, @PathVariable("gameId") gameId: Int): String {
        processResults(gameFacade.get(id = gameId))

        val cheat = CheatFO(
            id = null,
            gameSetting = null,
            cheatSetting = null,
            data = null
        )
        return createAddFormView(model = model, cheat = cheat, gameId = gameId)
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
     */
    @PostMapping("/add")
    fun processAdd(model: Model, @PathVariable("gameId") gameId: Int, @ModelAttribute("cheat") @Valid cheat: CheatFO, errors: Errors, request: HttpServletRequest): String {
        require(cheat.id == null) { "ID must be null." }

        if (request.getParameter("create") != null) {
            if (errors.hasErrors()) {
                return createAddFormView(model = model, cheat = cheat, gameId = gameId)
            }

            processResults(cheatFacade.add(parent = gameId, data = cheatMapper.mapBack(source = cheat)))

            return getCheatsRedirectUrl(gameId = gameId)
        }

        if (request.getParameter("addCheat") != null) {
            val cheatData = if (cheat.data == null) mutableListOf() else cheat.data!!.toMutableList()
            cheatData.add(CheatDataFO())

            return createAddFormView(model = model, cheat = cheat.copy(data = cheatData), gameId = gameId)
        }

        val index = getRemoveIndex(request = request)
        if (index != null) {
            val cheatData = cheat.data!!.toMutableList()
            cheatData.removeAt(index)

            return createAddFormView(model = model, cheat = cheat.copy(data = cheatData), gameId = gameId)
        }

        processResults(gameFacade.get(id = gameId))
        return getCheatsRedirectUrl(gameId = gameId)
    }

    /**
     * Shows page for editing cheat.
     *
     * @param model  model
     * @param gameId game ID
     * @return view for page for editing cheat
     */
    @GetMapping("/edit")
    fun showEdit(model: Model, @PathVariable("gameId") gameId: Int): String {
        val cheatResult = cheatFacade.find(parent = gameId)
        processResults(cheatResult)

        return createEditFormView(model = model, cheat = cheatMapper.map(source = cheatResult.data!!.first()), gameId = gameId)
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
     */
    @PostMapping("/edit")
    fun processEdit(model: Model, @PathVariable("gameId") gameId: Int, @ModelAttribute("cheat") @Valid cheat: CheatFO, errors: Errors, request: HttpServletRequest): String {
        require(cheat.id != null) { "ID mustn't be null." }

        if (request.getParameter("update") != null) {
            if (errors.hasErrors()) {
                return createEditFormView(model = model, cheat = cheat, gameId = gameId)
            }

            processResults(gameFacade.get(id = gameId))
            processResults(cheatFacade.update(data = cheatMapper.mapBack(source = cheat)))
            return getCheatsRedirectUrl(gameId = gameId)
        }

        if (request.getParameter("addCheat") != null) {
            val cheatData = cheat.data!!.toMutableList()
            cheatData.add(CheatDataFO())

            return createEditFormView(model = model, cheat = cheat.copy(data = cheatData), gameId = gameId)
        }

        val index = getRemoveIndex(request)
        if (index != null) {
            val cheatData = cheat.data!!.toMutableList()
            cheatData.removeAt(index)

            return createEditFormView(model = model, cheat = cheat.copy(data = cheatData), gameId = gameId)
        }

        processResults(gameFacade.get(id = gameId))
        return getCheatsRedirectUrl(gameId = gameId)
    }

    /**
     * Process removing cheat.
     *
     * @param gameId game ID
     * @return view for redirect to page with cheats
     */
    @GetMapping("/remove")
    fun processRemove(@PathVariable("gameId") gameId: Int): String {
        val cheatsResult = cheatFacade.find(parent = gameId)
        processResults(cheatsResult)
        processResults(cheatFacade.remove(id = cheatsResult.data!!.first().id!!))

        return "redirect:/games/list"
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
        return createFormView(model = model, cheat = cheat, gameId = gameId, title = "Add cheat", action = "add")
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
        return createFormView(model = model, cheat = cheat, gameId = gameId, title = "Edit cheat", action = "edit")
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

}
