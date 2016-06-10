package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for programs.
 *
 * @author Vladimir Hromada
 */
@Controller("programController")
@RequestMapping("/programs")
public class ProgramController extends JsonController {

    @Autowired
    @Qualifier("programFacade")
    private ProgramFacade programFacade;

    /**
     * Creates new data.
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public void newData() {
        programFacade.newData();
    }

    /**
     * Returns list of programs.
     *
     * @return list of programs
     */
    @RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
    @ResponseBody
    public String getPrograms() {
        return serialize(programFacade.getPrograms());
    }

    /**
     * Returns program with ID or null if there isn't such program.
     *
     * @param id ID
     * @return program with ID or null if there isn't such program
     * @throws IllegalArgumentException if ID is null
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getProgram(@PathVariable("id") final Integer id) {
        return serialize(programFacade.getProgram(id));
    }

    /**
     * Adds program. Sets new ID and position.
     *
     * @param program program
     * @throws IllegalArgumentException                              if program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about program is null
     *                                                               or URL to czech Wikipedia page about program is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(final String program) {
        programFacade.add(deserialize(program, ProgramTO.class));
    }

    /**
     * Updates program.
     *
     * @param program new value of program
     * @throws IllegalArgumentException                                  if program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or URL to english Wikipedia page about program is null
     *                                                                   or URL to czech Wikipedia page about program is null
     *                                                                   or count of media isn't positive number
     *                                                                   or other data is null
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if program doesn't exist in data storage
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(final String program) {
        programFacade.update(deserialize(program, ProgramTO.class));
    }

    /**
     * Removes program.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if program doesn't exist in data storage
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public void remove(final String program) {
        programFacade.remove(deserialize(program, ProgramTO.class));
    }

    /**
     * Duplicates program.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if program doesn't exist in data storage
     */
    @RequestMapping(value = "/duplicate", method = RequestMethod.POST)
    @ResponseBody
    public void duplicate(final String program) {
        programFacade.duplicate(deserialize(program, ProgramTO.class));
    }

    /**
     * Moves program in list one position up.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or program can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if program doesn't exist in data storage
     */
    @RequestMapping(value = "/moveUp", method = RequestMethod.POST)
    @ResponseBody
    public void moveUp(final String program) {
        programFacade.moveUp(deserialize(program, ProgramTO.class));
    }

    /**
     * Moves program in list one position down.
     *
     * @param program program
     * @throws IllegalArgumentException                                  if program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or program can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if program doesn't exist in data storage
     */
    @RequestMapping(value = "/moveDown", method = RequestMethod.POST)
    @ResponseBody
    public void moveDown(final String program) {
        programFacade.moveDown(deserialize(program, ProgramTO.class));
    }

    /**
     * Returns true if program exists.
     *
     * @param program program
     * @return true if program exists
     * @throws IllegalArgumentException                              if program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String exists(final String program) {
        return null;//serialize(programFacade.exists(deserialize(program, ProgramTO.class)));
    }

    /**
     * Updates positions.
     */
    @RequestMapping(value = "/updatePositions", method = RequestMethod.GET)
    @ResponseBody
    public void updatePositions() {
        programFacade.updatePositions();
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @RequestMapping(value = "/totalMedia", method = RequestMethod.GET)
    @ResponseBody
    public String getTotalMediaCount() {
        return serialize(programFacade.getTotalMediaCount());
    }

}
