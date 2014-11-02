package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for songs.
 *
 * @author Vladimir Hromada
 */
@Controller("songController")
@RequestMapping("/music/{musicId}/songs")
public class SongController extends JsonController {

	@Autowired
	@Qualifier("songFacade")
	private SongFacade songFacade;

	/**
	 * Returns song with ID or null if there isn't such song.
	 *
	 * @param musicId music ID
	 * @param songId  song ID
	 * @return song with ID or null if there isn't such song
	 * @throws IllegalArgumentException if ID is null
	 */
	@RequestMapping(value = "/{songId}", method = RequestMethod.GET)
	@ResponseBody
	public String getSong(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, @PathVariable("songId") final Integer songId) {
		return serialize(songFacade.getSong(songId));
	}

	/**
	 * Adds song. Sets new ID and position.
	 *
	 * @param musicId music ID
	 * @param song    song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID isn't null
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of song is negative value
	 *                                  or note is null
	 *                                  or music is null
	 *                                  or music ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if music doesn't exist in data storage
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public void add(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, final String song) {
		songFacade.add(deserialize(song, SongTO.class));
	}

	/**
	 * Updates song.
	 *
	 * @param musicId music ID
	 * @param song    new value of song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of song is negative value
	 *                                  or note is null
	 *                                  or music is null
	 *                                  or music ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if song doesn't exist in data storage
	 *                                  or music doesn't exist in data storage
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void update(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, final String song) {
		songFacade.update(deserialize(song, SongTO.class));
	}

	/**
	 * Removes song.
	 *
	 * @param musicId music ID
	 * @param song    song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if song doesn't exist in data storage
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public void remove(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, final String song) {
		songFacade.remove(deserialize(song, SongTO.class));
	}

	/**
	 * Duplicates song.
	 *
	 * @param musicId music ID
	 * @param song    song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if song doesn't exist in data storage
	 */
	@RequestMapping(value = "/duplicate", method = RequestMethod.POST)
	@ResponseBody
	public void duplicate(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, final String song) {
		songFacade.duplicate(deserialize(song, SongTO.class));
	}

	/**
	 * Moves song in list one position up.
	 *
	 * @param musicId music ID
	 * @param song    song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or song can't be moved up
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if song doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveUp", method = RequestMethod.POST)
	@ResponseBody
	public void moveUp(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, final String song) {
		songFacade.moveUp(deserialize(song, SongTO.class));
	}

	/**
	 * Moves song in list one position down.
	 *
	 * @param musicId music ID
	 * @param song    song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or song can't be moved down
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if song doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveDown", method = RequestMethod.POST)
	@ResponseBody
	public void moveDown(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, final String song) {
		songFacade.moveDown(deserialize(song, SongTO.class));
	}

	/**
	 * Returns true if song exists.
	 *
	 * @param musicId music ID
	 * @param song    song
	 * @return true if song exists
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 */
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public String exists(@PathVariable("musicId") @SuppressWarnings("unused") final Integer musicId, final String song) {
		return serialize(songFacade.exists(deserialize(song, SongTO.class)));
	}

	/**
	 * Returns list of songs for specified music.
	 *
	 * @param musicId music ID
	 * @return list of songs for specified music
	 * @throws IllegalArgumentException if music is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if music doesn't exist in data storage
	 */
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	@ResponseBody
	public String findSongsByMusic(@PathVariable("musicId") final Integer musicId) {
		final MusicTO music = new MusicTO();
		music.setId(musicId);

		return serialize(songFacade.findSongsByMusic(music));
	}

}
