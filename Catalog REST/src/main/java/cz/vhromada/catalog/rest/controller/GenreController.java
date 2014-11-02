package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for genres.
 *
 * @author Vladimir Hromada
 */
@Controller("genreController")
@RequestMapping("/genres")
public class GenreController extends JsonController {

	@Autowired
	@Qualifier("genreFacade")
	private GenreFacade genreFacade;

	/** Creates new data. */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	@ResponseBody
	public void newData() {
		genreFacade.newData();
	}

	/**
	 * Returns list of genres.
	 *
	 * @return list of genres
	 */
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	@ResponseBody
	public String getGenres() {
		return serialize(genreFacade.getGenres());
	}

	/**
	 * Returns genre with ID or null if there isn't such genre.
	 *
	 * @param id ID
	 * @return genre with ID or null if there isn't such genre
	 * @throws IllegalArgumentException if ID is null
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getGenre(@PathVariable("id") final Integer id) {
		return serialize(genreFacade.getGenre(id));
	}

	/**
	 * Adds genre. Sets new ID and position.
	 *
	 * @param genre genre
	 * @throws IllegalArgumentException if genre is null
	 * @throws ValidationException      if ID isn't null
	 *                                  or name is null
	 *                                  or name is empty string
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public void add(final String genre) {
		genreFacade.add(deserialize(genre, GenreTO.class));
	}

	/**
	 * Adds list of genre names.
	 *
	 * @param genre genre
	 * @throws IllegalArgumentException if list of genre names is null
	 * @throws ValidationException      if list of genre names contains null value
	 */
	@RequestMapping(value = "/addList", method = RequestMethod.POST)
	@ResponseBody
	public void addList(final String genre) {
		genreFacade.add(this.<String>deserializeList(genre));
	}

	/**
	 * Updates genre.
	 *
	 * @param genre new value of genre
	 * @throws IllegalArgumentException if genre is null
	 * @throws ValidationException      if ID is null
	 *                                  or name is null
	 *                                  or name is empty string
	 * @throws RecordNotFoundException  if genre doesn't exist in data storage
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void update(final String genre) {
		genreFacade.update(deserialize(genre, GenreTO.class));
	}

	/**
	 * Removes genre.
	 *
	 * @param genre genre
	 * @throws IllegalArgumentException if genre is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if genre doesn't exist in data storage
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public void remove(final String genre) {
		genreFacade.remove(deserialize(genre, GenreTO.class));
	}

	/**
	 * Duplicates genre.
	 *
	 * @param genre genre
	 * @throws IllegalArgumentException if genre is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if genre doesn't exist in data storage
	 */
	@RequestMapping(value = "/duplicate", method = RequestMethod.POST)
	@ResponseBody
	public void duplicate(final String genre) {
		genreFacade.duplicate(deserialize(genre, GenreTO.class));
	}

	/**
	 * Returns true if genre exists.
	 *
	 * @param genre genre
	 * @return true if genre exists
	 * @throws IllegalArgumentException if genre is null
	 * @throws ValidationException      if ID is null
	 */
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public String exists(final String genre) {
		return serialize(genreFacade.exists(deserialize(genre, GenreTO.class)));
	}

}
