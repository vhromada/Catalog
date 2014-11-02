package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.MovieTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for movies.
 *
 * @author Vladimir Hromada
 */
@Controller("movieController")
@RequestMapping("/movies")
public class MovieController extends JsonController {

	@Autowired
	@Qualifier("movieFacade")
	private MovieFacade movieFacade;

	/** Creates new data. */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	@ResponseBody
	public void newData() {
		movieFacade.newData();
	}

	/**
	 * Returns list of movies.
	 *
	 * @return list of movies
	 */
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	@ResponseBody
	public String getMovies() {
		return serialize(movieFacade.getMovies());
	}

	/**
	 * Returns movie with ID or null if there isn't such movie.
	 *
	 * @param id ID
	 * @return movie with ID or null if there isn't such movie
	 * @throws IllegalArgumentException if ID is null
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getMovie(@PathVariable("id") final Integer id) {
		return serialize(movieFacade.getMovie(id));
	}

	/**
	 * Adds movie. Sets new ID and position.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID isn't null
	 *                                  or czech name is null
	 *                                  or czech name is empty string
	 *                                  or original name is null
	 *                                  or original name is empty string
	 *                                  or year isn't between 1940 and current year
	 *                                  or language is null
	 *                                  or subtitles are null
	 *                                  or subtitles contain null value
	 *                                  or media are null
	 *                                  or media contain null value
	 *                                  or media contain negative value
	 *                                  or URL to ČSFD page about movie is null
	 *                                  or IMDB code isn't -1 or between 1 and 9999999
	 *                                  or URL to english Wikipedia page about movie is null
	 *                                  or URL to czech Wikipedia page about movie is null
	 *                                  or path to file with movie's picture is null
	 *                                  or note is null
	 *                                  or genres are value
	 *                                  or genres contain null value
	 *                                  or genre ID is null
	 *                                  or genre name is null
	 *                                  or genre name is empty string
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if genre doesn't exist in data storage
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public void add(final String movie) {
		movieFacade.add(deserialize(movie, MovieTO.class));
	}

	/**
	 * Updates movie.
	 *
	 * @param movie new value of movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or czech name is null
	 *                                  or czech name is empty string
	 *                                  or original name is null
	 *                                  or original name is empty string
	 *                                  or year isn't between 1940 and current year
	 *                                  or language is null
	 *                                  or subtitles are null
	 *                                  or subtitles contain null value
	 *                                  or media are null
	 *                                  or media contain null value
	 *                                  or media contain negative value
	 *                                  or URL to ČSFD page about movie is null
	 *                                  or IMDB code isn't -1 or between 1 and 9999999
	 *                                  or URL to english Wikipedia page about movie is null
	 *                                  or URL to czech Wikipedia page about movie is null
	 *                                  or path to file with movie's picture is null
	 *                                  or note is null
	 *                                  or genres are value
	 *                                  or genres contain null value
	 *                                  or genre ID is null
	 *                                  or genre name is null
	 *                                  or genre name is empty string
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if movie doesn't exist in data storage
	 *                                  or genre doesn't exist in data storage
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void update(final String movie) {
		movieFacade.update(deserialize(movie, MovieTO.class));
	}

	/**
	 * Removes movie.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if movie doesn't exist in data storage
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public void remove(final String movie) {
		movieFacade.remove(deserialize(movie, MovieTO.class));
	}

	/**
	 * Duplicates movie.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if movie doesn't exist in data storage
	 */
	@RequestMapping(value = "/duplicate", method = RequestMethod.POST)
	@ResponseBody
	public void duplicate(final String movie) {
		movieFacade.duplicate(deserialize(movie, MovieTO.class));
	}

	/**
	 * Moves movie in list one position up.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or movie can't be moved up
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if movie doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveUp", method = RequestMethod.POST)
	@ResponseBody
	public void moveUp(final String movie) {
		movieFacade.moveUp(deserialize(movie, MovieTO.class));
	}

	/**
	 * Moves movie in list one position down.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or movie can't be moved down
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if movie doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveDown", method = RequestMethod.POST)
	@ResponseBody
	public void moveDown(final String movie) {
		movieFacade.moveDown(deserialize(movie, MovieTO.class));
	}

	/**
	 * Returns true if movie exists.
	 *
	 * @param movie movie
	 * @return true if movie exists
	 * @throws IllegalArgumentException if movie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 */
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public String exists(final String movie) {
		return serialize(movieFacade.exists(deserialize(movie, MovieTO.class)));
	}

	/** Updates positions. */
	@RequestMapping(value = "/updatePositions", method = RequestMethod.GET)
	@ResponseBody
	public void updatePositions() {
		movieFacade.updatePositions();
	}

	/**
	 * Returns total count of media.
	 *
	 * @return total count of media
	 */
	@RequestMapping(value = "/totalMedia", method = RequestMethod.GET)
	@ResponseBody
	public String getTotalMediaCount() {
		return serialize(movieFacade.getTotalMediaCount());
	}

	/**
	 * Returns total length of all movies.
	 *
	 * @return total length of all movies
	 */
	@RequestMapping(value = "/totalLength", method = RequestMethod.GET)
	@ResponseBody
	public String getTotalLength() {
		return serialize(movieFacade.getTotalLength());
	}

}
