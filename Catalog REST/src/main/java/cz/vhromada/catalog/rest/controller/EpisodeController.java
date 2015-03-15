package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for episodes.
 *
 * @author Vladimir Hromada
 */
@Controller("episodeController")
@RequestMapping("/series/{serieId}/seasons/{seasonId}/episodes")
public class EpisodeController extends JsonController {

    @Autowired
    @Qualifier("episodeFacade")
    private EpisodeFacade episodeFacade;

    /**
     * Returns episode with ID or null if there isn't such episode.
     *
     * @param serieId   serie ID
     * @param seasonId  season ID
     * @param episodeId episode ID
     * @return episode with ID or null if there isn't such episode
     * @throws IllegalArgumentException if ID is null
     */
    @RequestMapping(value = "/{episodeId}", method = RequestMethod.GET)
    @ResponseBody
    public String getEpisode(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, @PathVariable("episodeId") final Integer episodeId) {
        return serialize(episodeFacade.getEpisode(episodeId));
    }

    /**
     * Adds episode. Sets new ID and position.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @param episode  episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID isn't null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of episode is negative value
     *                                                                   or note is null
     *                                                                   or season is null
     *                                                                   or season ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if season doesn't exist in data storage
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, final String episode) {
        episodeFacade.add(deserialize(episode, EpisodeTO.class));
    }

    /**
     * Updates episode.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @param episode  new value of episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of episode is negative value
     *                                                                   or note is null
     *                                                                   or season is null
     *                                                                   or season ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     *                                                                   or season doesn't exist in data storage
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, final String episode) {
        episodeFacade.update(deserialize(episode, EpisodeTO.class));
    }

    /**
     * Removes episode.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @param episode  episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public void remove(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, final String episode) {
        episodeFacade.remove(deserialize(episode, EpisodeTO.class));
    }

    /**
     * Duplicates episode.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @param episode  episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    @RequestMapping(value = "/duplicate", method = RequestMethod.POST)
    @ResponseBody
    public void duplicate(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, final String episode) {
        episodeFacade.duplicate(deserialize(episode, EpisodeTO.class));
    }

    /**
     * Moves episode in list one position up.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @param episode  episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or episode can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    @RequestMapping(value = "/moveUp", method = RequestMethod.POST)
    @ResponseBody
    public void moveUp(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, final String episode) {
        episodeFacade.moveUp(deserialize(episode, EpisodeTO.class));
    }

    /**
     * Moves episode in list one position down.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @param episode  episode
     * @throws IllegalArgumentException                                  if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or episode can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if episode doesn't exist in data storage
     */
    @RequestMapping(value = "/moveDown", method = RequestMethod.POST)
    @ResponseBody
    public void moveDown(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, final String episode) {
        episodeFacade.moveDown(deserialize(episode, EpisodeTO.class));
    }

    /**
     * Returns true if episode exists.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @param episode  episode
     * @return true if episode exists
     * @throws IllegalArgumentException                              if episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String exists(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") @SuppressWarnings("unused") final Integer seasonId, final String episode) {
        return serialize(episodeFacade.exists(deserialize(episode, EpisodeTO.class)));
    }

    /**
     * Returns list of episodes for specified season.
     *
     * @param serieId  serie ID
     * @param seasonId season ID
     * @return list of episodes for specified season
     * @throws IllegalArgumentException                                  if season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if season doesn't exist in data storage
     */
    @RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
    @ResponseBody
    public String findEpisodesBySeason(@PathVariable("serieId") @SuppressWarnings("unused") final Integer serieId,
            @PathVariable("seasonId") final Integer seasonId) {
        final SeasonTO season = new SeasonTO();
        season.setId(seasonId);

        return serialize(episodeFacade.findEpisodesBySeason(season));
    }

}
