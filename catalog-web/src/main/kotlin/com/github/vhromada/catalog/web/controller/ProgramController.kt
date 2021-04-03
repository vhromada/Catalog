package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.common.Format
import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.facade.ProgramFacade
import com.github.vhromada.catalog.web.fo.ProgramFO
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
 * A class represents controller for programs.
 *
 * @author Vladimir Hromada
 */
@Controller("programController")
@RequestMapping("/programs")
class ProgramController(
    private val programFacade: ProgramFacade,
    private val programMapper: Mapper<Program, ProgramFO>
) : AbstractResultController() {

    /**
     * Process new data.
     *
     * @return view for redirect to page with list of programs
     */
    @GetMapping("/new")
    fun processNew(): String {
        processResults(programFacade.newData())

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page with list of programs.
     *
     * @param model model
     * @return view for page with list of programs
     */
    @GetMapping("", "/list")
    fun showList(model: Model): String {
        val programsResult = programFacade.getAll()
        val mediaCountResult = programFacade.getTotalMediaCount()
        processResults(programsResult, mediaCountResult)

        model.addAttribute("programs", programsResult.data)
        model.addAttribute("mediaCount", mediaCountResult.data)
        model.addAttribute("title", "Programs")

        return "program/index"
    }

    /**
     * Shows page with detail of program.
     *
     * @param model model
     * @param id    ID of showing program
     * @return view for page with detail of program
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("id") id: Int): String {
        val result = programFacade.get(id = id)
        processResults(result)

        model.addAttribute("program", result.data)
        model.addAttribute("title", "Program detail")

        return "program/detail"
    }

    /**
     * Shows page for adding program.
     *
     * @param model model
     * @return view for page for adding program
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        val program = ProgramFO(
            id = null,
            name = null,
            wikiEn = null,
            wikiCz = null,
            mediaCount = null,
            format = null,
            crack = null,
            serialKey = null,
            otherData = null,
            note = null,
            position = null
        )
        return createFormView(model = model, program = program, title = "Add program", action = "add")
    }

    /**
     * Process adding program.
     *
     * @param model   model
     * @param program FO for program
     * @param errors  errors
     * @return view for redirect to page with list of programs (no errors) or view for page for adding program (errors)
     * @throws IllegalArgumentException if ID isn't null
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @ModelAttribute("program") @Valid program: ProgramFO, errors: Errors): String {
        require(program.id == null) { "ID must be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, program = program, title = "Add program", action = "add")
        }
        processResults(programFacade.add(data = programMapper.mapBack(source = program)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process adding program.
     *
     * @return view for redirect to page with list of programs
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Shows page for editing program.
     *
     * @param model model
     * @param id    ID of editing program
     * @return view for page for editing program
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = programFacade.get(id = id)
        processResults(result)

        return createFormView(model = model, program = programMapper.map(source = result.data!!), title = "Edit program", action = "edit")
    }

    /**
     * Process editing program.
     *
     * @param model   model
     * @param program FO for program
     * @param errors  errors
     * @return view for redirect to page with list of programs (no errors) or view for page for editing program (errors)
     * @throws IllegalArgumentException if ID is null
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @ModelAttribute("program") @Valid program: ProgramFO, errors: Errors): String {
        require(program.id != null) { "ID mustn't be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, program = program, title = "Edit program", action = "edit")
        }
        processResults(programFacade.update(data = programMapper.mapBack(source = program)))

        return LIST_REDIRECT_URL
    }

    /**
     * Cancel editing program.
     *
     * @return view for redirect to page with list of programs
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun cancelEdit(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Process duplicating program.
     *
     * @param id ID of duplicating program
     * @return view for redirect to page with list of programs
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(programFacade.duplicate(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing program.
     *
     * @param id ID of removing program
     * @return view for redirect to page with list of programs
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(programFacade.remove(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving program up.
     *
     * @param id ID of moving program
     * @return view for redirect to page with list of programs
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(programFacade.moveUp(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving program down.
     *
     * @param id ID of moving program
     * @return view for redirect to page with list of programs
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(programFacade.moveDown(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process updating positions.
     *
     * @return view for redirect to page with list of programs
     */
    @GetMapping("/update")
    fun processUpdatePositions(): String {
        processResults(programFacade.updatePositions())

        return LIST_REDIRECT_URL
    }

    /**
     * Returns page's view with form.
     *
     * @param model   model
     * @param program FO for program
     * @param title   page's title
     * @param action  action
     * @return page's view with form
     */
    private fun createFormView(model: Model, program: ProgramFO, title: String, action: String): String {
        model.addAttribute("program", program)
        model.addAttribute("title", title)
        model.addAttribute("formats", Format.values())
        model.addAttribute("action", action)

        return "program/form"
    }

    companion object {

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/programs/list"

    }

}
