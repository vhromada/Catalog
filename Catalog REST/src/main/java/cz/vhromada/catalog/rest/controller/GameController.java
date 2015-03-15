package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for games.
 *
 * @author Vladimir Hromada
 */
@Controller("gameController")
@RequestMapping("/games")
public class GameController extends JsonController {

    @Autowired
    @Qualifier("gameFacade")
    private GameFacade gameFacade;

    /** Creates new data. */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public void newData() {
        gameFacade.newData();
    }

    /**
     * Returns list of games.
     *
     * @return list of games
     */
    @RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
    @ResponseBody
    public String getGames() {
        return serialize(gameFacade.getGames());
    }

    /**
     * Returns game with ID or null if there isn't such game.
     *
     * @param id ID
     * @return game with ID or null if there isn't such game
     * @throws IllegalArgumentException if ID is null
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getGame(@PathVariable("id") final Integer id) {
        return serialize(gameFacade.getGame(id));
    }

    /**
     * Adds game. Sets new ID and position.
     *
     * @param game game
     * @throws IllegalArgumentException                              if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about game is null
     *                                                               or URL to czech Wikipedia page about game is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(final String game) {
        gameFacade.add(deserialize(game, GameTO.class));
    }

    /**
     * Updates game.
     *
     * @param game new value of game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or URL to english Wikipedia page about game is null
     *                                                                   or URL to czech Wikipedia page about game is null
     *                                                                   or count of media isn't positive number
     *                                                                   or other data is null
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(final String game) {
        gameFacade.update(deserialize(game, GameTO.class));
    }

    /**
     * Removes game.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public void remove(final String game) {
        gameFacade.remove(deserialize(game, GameTO.class));
    }

    /**
     * Duplicates game.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    @RequestMapping(value = "/duplicate", method = RequestMethod.POST)
    @ResponseBody
    public void duplicate(final String game) {
        gameFacade.duplicate(deserialize(game, GameTO.class));
    }

    /**
     * Moves game in list one position up.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or game can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    @RequestMapping(value = "/moveUp", method = RequestMethod.POST)
    @ResponseBody
    public void moveUp(final String game) {
        gameFacade.moveUp(deserialize(game, GameTO.class));
    }

    /**
     * Moves game in list one position down.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or game can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    @RequestMapping(value = "/moveDown", method = RequestMethod.POST)
    @ResponseBody
    public void moveDown(final String game) {
        gameFacade.moveDown(deserialize(game, GameTO.class));
    }

    /**
     * Returns true if game exists.
     *
     * @param game game
     * @return true if game exists
     * @throws IllegalArgumentException                              if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String exists(final String game) {
        return serialize(gameFacade.exists(deserialize(game, GameTO.class)));
    }

    /** Updates positions. */
    @RequestMapping(value = "/updatePositions", method = RequestMethod.GET)
    @ResponseBody
    public void updatePositions() {
        gameFacade.updatePositions();
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @RequestMapping(value = "/totalMedia", method = RequestMethod.GET)
    @ResponseBody
    public String getTotalMediaCount() {
        return serialize(gameFacade.getTotalMediaCount());
    }

}
