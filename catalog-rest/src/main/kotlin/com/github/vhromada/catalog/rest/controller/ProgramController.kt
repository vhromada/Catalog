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
class ProgramController(
    private val programFacade: ProgramFacade
) : AbstractController() {

    /**
     * Returns list of programs.
     *
     * @return list of programs
     */
    @GetMapping
    fun getPrograms(): List<Program> {
        return processResult(result = programFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(result = programFacade.newData())
    }

    /**
     * Returns program with ID.
     *
     * @param id ID
     * @return program with ID
     */
    @GetMapping("/{id}")
    fun getProgram(@PathVariable("id") id: Int): Program {
        return processResult(result = programFacade.get(id = id))!!
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
        processResult(result = programFacade.add(data = program))
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
        processResult(result = programFacade.update(data = program))
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
        processResult(result = programFacade.remove(id = id))
    }

    /**
     * Duplicates program.
     * <br></br>
     * Validation errors:
     *
     *  * Program doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/duplicate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@PathVariable("id") id: Int) {
        processResult(result = programFacade.duplicate(id = id))
    }

    /**
     * Moves program in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Program can't be moved up
     *  * Program doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveUp/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@PathVariable("id") id: Int) {
        processResult(result = programFacade.moveUp(id = id))
    }

    /**
     * Moves program in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Program can't be moved down
     *  * Program doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveDown/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@PathVariable("id") id: Int) {
        processResult(result = programFacade.moveDown(id = id))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(result = programFacade.updatePositions())
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @GetMapping("/totalMedia")
    fun totalMediaCount(): Int {
        return processResult(result = programFacade.getTotalMediaCount())!!
    }

}
