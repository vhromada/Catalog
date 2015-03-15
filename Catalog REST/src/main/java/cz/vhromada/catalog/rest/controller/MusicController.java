package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.to.MusicTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for music.
 *
 * @author Vladimir Hromada
 */
@Controller("musicController")
@RequestMapping("/music")
public class MusicController extends JsonController {

    @Autowired
    @Qualifier("musicFacade")
    private MusicFacade musicFacade;

    /**
     * Creates new data.
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public void newData() {
        musicFacade.newData();
    }

    /**
     * Returns list of music.
     *
     * @return list of music
     */
    @RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
    @ResponseBody
    public String getMusic() {
        return serialize(musicFacade.getMusic());
    }

    /**
     * Returns music with ID or null if there isn't such music.
     *
     * @param id ID
     * @return music with ID or null if there isn't such music
     * @throws IllegalArgumentException if ID is null
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getMusic(@PathVariable("id") final Integer id) {
        return serialize(musicFacade.getMusic(id));
    }

    /**
     * Adds music. Sets new ID and position.
     *
     * @param music music
     * @throws IllegalArgumentException                              if music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about music is null
     *                                                               or URL to czech Wikipedia page about music is null
     *                                                               or count of media isn't positive number
     *                                                               or count of songs is negative number
     *                                                               or total length of songs is null
     *                                                               or total length of songs is negative number
     *                                                               or note is null
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(final String music) {
        musicFacade.add(deserialize(music, MusicTO.class));
    }

    /**
     * Updates music.
     *
     * @param music new value of music
     * @throws IllegalArgumentException                                  if music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or URL to english Wikipedia page about music is null
     *                                                                   or URL to czech Wikipedia page about music is null
     *                                                                   or count of media isn't positive number
     *                                                                   or count of songs is negative number
     *                                                                   or total length of songs is null
     *                                                                   or total length of songs is negative number
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if music doesn't exist in data storage
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(final String music) {
        musicFacade.update(deserialize(music, MusicTO.class));
    }

    /**
     * Removes music.
     *
     * @param music music
     * @throws IllegalArgumentException                                  if music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if music doesn't exist in data storage
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public void remove(final String music) {
        musicFacade.remove(deserialize(music, MusicTO.class));
    }

    /**
     * Duplicates music.
     *
     * @param music music
     * @throws IllegalArgumentException                                  if music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if music doesn't exist in data storage
     */
    @RequestMapping(value = "/duplicate", method = RequestMethod.POST)
    @ResponseBody
    public void duplicate(final String music) {
        musicFacade.duplicate(deserialize(music, MusicTO.class));
    }

    /**
     * Moves music in list one position up.
     *
     * @param music music
     * @throws IllegalArgumentException                                  if music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or music can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if music doesn't exist in data storage
     */
    @RequestMapping(value = "/moveUp", method = RequestMethod.POST)
    @ResponseBody
    public void moveUp(final String music) {
        musicFacade.moveUp(deserialize(music, MusicTO.class));
    }

    /**
     * Moves music in list one position down.
     *
     * @param music music
     * @throws IllegalArgumentException                                  if music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or music can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if music doesn't exist in data storage
     */
    @RequestMapping(value = "/moveDown", method = RequestMethod.POST)
    @ResponseBody
    public void moveDown(final String music) {
        musicFacade.moveDown(deserialize(music, MusicTO.class));
    }

    /**
     * Returns true if music exists.
     *
     * @param music music
     * @return true if music exists
     * @throws IllegalArgumentException                              if music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String exists(final String music) {
        return serialize(musicFacade.exists(deserialize(music, MusicTO.class)));
    }

    /**
     * Updates positions.
     */
    @RequestMapping(value = "/updatePositions", method = RequestMethod.GET)
    @ResponseBody
    public void updatePositions() {
        musicFacade.updatePositions();
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @RequestMapping(value = "/totalMedia", method = RequestMethod.GET)
    @ResponseBody
    public String getTotalMediaCount() {
        return serialize(musicFacade.getTotalMediaCount());
    }

    /**
     * Returns total length of all music.
     *
     * @return total length of all music
     */
    @RequestMapping(value = "/totalLength", method = RequestMethod.GET)
    @ResponseBody
    public String getTotalLength() {
        return serialize(musicFacade.getTotalLength());
    }

    /**
     * Returns count of songs from all music.
     *
     * @return count of songs from all music
     */
    @RequestMapping(value = "/songsCount", method = RequestMethod.GET)
    @ResponseBody
    public String getSongsCount() {
        return serialize(musicFacade.getSongsCount());
    }

}
