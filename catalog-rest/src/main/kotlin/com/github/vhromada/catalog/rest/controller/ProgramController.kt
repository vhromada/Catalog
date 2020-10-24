package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.facade.ProgramFacade
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
 * A class represents controller for programs.
 *
 * @author Vladimir Hromada
 */
@RestController("programController")
@RequestMapping("/catalog/programs")
class ProgramController(private val programFacade: ProgramFacade) : AbstractController() {

    /**
     * Returns list of programs.
     *
     * @return list of programs
     */
    @GetMapping
    fun getPrograms(): List<Program> {
        return processResult(programFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(programFacade.newData())
    }

    /**
     * Returns program with ID or null if there isn't such program.
     *
     * @param id ID
     * @return program with ID or null if there isn't such program
     */
    @GetMapping("/{id}")
    fun getProgram(@PathVariable("id") id: Int): Program? {
        return processResult(programFacade.get(id))
    }

    /**
     * Adds program. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about program is null
     *  * URL to czech Wikipedia page about program is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *
     * @param program program
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody program: Program) {
        processResult(programFacade.add(program))
    }

    /**
     * Updates program.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about program is null
     *  * URL to czech Wikipedia page about program is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *  * Program doesn't exist in data storage
     *
     * @param program new value of program
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody program: Program) {
        processResult(programFacade.update(program))
    }

    /**
     * Removes program.
     * <br></br>
     * Validation errors:
     *
     *  * Program doesn't exist in data storage
     *
     * @param id ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("id") id: Int) {
        val program = Program(id = id,
                name = null,
                mediaCount = null,
                wikiEn = null,
                wikiCz = null,
                format = null,
                crack = null,
                serialKey = null,
                otherData = null,
                note = null,
                position = null)
        processResult(programFacade.remove(program))
    }

    /**
     * Duplicates program.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Program doesn't exist in data storage
     *
     * @param program program
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@RequestBody program: Program) {
        processResult(programFacade.duplicate(program))
    }

    /**
     * Moves program in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Program can't be moved up
     *  * Program doesn't exist in data storage
     *
     * @param program program
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@RequestBody program: Program) {
        processResult(programFacade.moveUp(program))
    }

    /**
     * Moves program in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Program can't be moved down
     *  * Program doesn't exist in data storage
     *
     * @param program program
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@RequestBody program: Program) {
        processResult(programFacade.moveDown(program))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(programFacade.updatePositions())
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @GetMapping("/totalMedia")
    fun totalMediaCount(): Int {
        return processResult(programFacade.getTotalMediaCount())!!
    }

}
