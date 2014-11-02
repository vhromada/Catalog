package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for seasons.
 *
 * @author Vladimir Hromada
 */
@Controller("seasonController")
@RequestMapping("/series/{serieId}/seasons")
public class SeasonController extends JsonController {

	@Autowired
	@Qualifier("seasonFacade")
	private SeasonFacade seasonFacade;

	/**
	 * Returns season with ID or null if there isn't such season.
	 *
	 * @param serieId  serie ID
	 * @param seasonId season ID
	 * @return season with ID or null if there isn't such season
	 * @throws IllegalArgumentException if ID is null
	 */
	@RequestMapping(value = "/{seasonId}", method = RequestMethod.GET)
	@ResponseBody
	public String getSeason(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, @PathVariable("seasonId") final Integer seasonId) {
		return serialize(seasonFacade.getSeason(seasonId));
	}

	/**
	 * Adds season. Sets new ID and position.
	 *
	 * @param serieId serie ID
	 * @param season  season
	 * @throws IllegalArgumentException if season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID isn't null
	 *                                  or number of season isn't positive number
	 *                                  or starting year isn't between 1940 and current year
	 *                                  or ending year isn't between 1940 and current year
	 *                                  or starting year is greater than ending year
	 *                                  or language is null
	 *                                  or subtitles are null
	 *                                  or subtitles contain null value
	 *                                  or count of episodes is negative number
	 *                                  or total length of episodes is null
	 *                                  or total length of episodes is negative number
	 *                                  or note is null
	 *                                  or serie is null
	 *                                  or serie ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if serie doesn't exist in data storage
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public void add(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, final String season) {
		seasonFacade.add(deserialize(season, SeasonTO.class));
	}

	/**
	 * Updates season.
	 *
	 * @param serieId serie ID
	 * @param season  new value of season
	 * @throws IllegalArgumentException if season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or number of season isn't positive number
	 *                                  or starting year isn't between 1940 and current year
	 *                                  or ending year isn't between 1940 and current year
	 *                                  or starting year is greater than ending year
	 *                                  or language is null
	 *                                  or subtitles are null
	 *                                  or subtitles contain null value
	 *                                  or count of episodes is negative number
	 *                                  or total length of episodes is null
	 *                                  or total length of episodes is negative number
	 *                                  or note is null
	 *                                  or serie is null
	 *                                  or serie ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if season doesn't exist in data storage
	 *                                  or serie doesn't exist in data storage
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void update(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, final String season) {
		seasonFacade.update(deserialize(season, SeasonTO.class));
	}

	/**
	 * Removes season.
	 *
	 * @param serieId serie ID
	 * @param season  season
	 * @throws IllegalArgumentException if season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if season doesn't exist in data storage
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public void remove(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, final String season) {
		seasonFacade.remove(deserialize(season, SeasonTO.class));
	}

	/**
	 * Duplicates season.
	 *
	 * @param serieId serie ID
	 * @param season  season
	 * @throws IllegalArgumentException if season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if season doesn't exist in data storage
	 */
	@RequestMapping(value = "/duplicate", method = RequestMethod.POST)
	@ResponseBody
	public void duplicate(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, final String season) {
		seasonFacade.duplicate(deserialize(season, SeasonTO.class));
	}

	/**
	 * Moves season in list one position up.
	 *
	 * @param serieId serie ID
	 * @param season  season
	 * @throws IllegalArgumentException if season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or season can't be moved up
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if season doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveUp", method = RequestMethod.POST)
	@ResponseBody
	public void moveUp(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, final String season) {
		seasonFacade.moveUp(deserialize(season, SeasonTO.class));
	}

	/**
	 * Moves season in list one position down.
	 *
	 * @param serieId serie ID
	 * @param season  season
	 * @throws IllegalArgumentException if season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or season can't be moved down
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if season doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveDown", method = RequestMethod.POST)
	@ResponseBody
	public void moveDown(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, final String season) {
		seasonFacade.moveDown(deserialize(season, SeasonTO.class));
	}

	/**
	 * Returns true if season exists.
	 *
	 * @param serieId serie ID
	 * @param season  season
	 * @return true if season exists
	 * @throws IllegalArgumentException if season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 */
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public String exists(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId, final String season) {
		return serialize(seasonFacade.exists(deserialize(season, SeasonTO.class)));
	}

	/**
	 * Returns list of seasons for specified serie.
	 *
	 * @param serieId serie ID
	 * @return list of seasons for specified serie
	 * @throws IllegalArgumentException if serie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if serie doesn't exist in data storage
	 */
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	@ResponseBody
	public String findSeasonsBySerie(@PathVariable("serieId") final Integer serieId) {
		final SerieTO serie = new SerieTO();
		serie.setId(serieId);

		return serialize(seasonFacade.findSeasonsBySerie(serie));
	}

}
