package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.to.SerieTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for series.
 *
 * @author Vladimir Hromada
 */
@Controller("serieController")
@RequestMapping("/series")
public class SerieController extends JsonController {

    @Autowired
    @Qualifier("serieFacade")
    private SerieFacade serieFacade;

    /**
     * Creates new data.
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public void newData() {
        serieFacade.newData();
    }

    /**
     * Returns list of series.
     *
     * @return list of series
     */
    @RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
    @ResponseBody
    public String getSeries() {
        return serialize(serieFacade.getSeries());
    }

    /**
     * Returns serie with ID or null if there isn't such serie.
     *
     * @param id ID
     * @return serie with ID or null if there isn't such serie
     * @throws IllegalArgumentException if ID is null
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getSerie(@PathVariable("id") final Integer id) {
        return serialize(serieFacade.getSerie(id));
    }

    /**
     * Adds serie. Sets new ID and position.
     *
     * @param serie serie
     * @throws IllegalArgumentException                                  if serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID isn't null
     *                                                                   or czech name is null
     *                                                                   or czech name is empty string
     *                                                                   or original name is null
     *                                                                   or original name is empty string
     *                                                                   or URL to ČSFD page about serie is null
     *                                                                   or IMDB code isn't -1 or between 1 and 9999999
     *                                                                   or URL to english Wikipedia page about serie is null
     *                                                                   or URL to czech Wikipedia page about serie is null
     *                                                                   or path to file with serie's picture is null
     *                                                                   or count of seasons is negative number
     *                                                                   or count of episodes is negative number
     *                                                                   or total length of seasons is null
     *                                                                   or total length of seasons is negative number
     *                                                                   or note is null
     *                                                                   or genres are null
     *                                                                   or genres contain null value
     *                                                                   or genre ID is null
     *                                                                   or genre name is null
     *                                                                   or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if genre doesn't exist in data storage
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(final String serie) {
        serieFacade.add(deserialize(serie, SerieTO.class));
    }

    /**
     * Updates serie.
     *
     * @param serie new value of serie
     * @throws IllegalArgumentException                                  if serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or czech name is null
     *                                                                   or czech name is empty string
     *                                                                   or original name is null
     *                                                                   or original name is empty string
     *                                                                   or URL to ČSFD page about serie is null
     *                                                                   or IMDB code isn't -1 or between 1 and 9999999
     *                                                                   or URL to english Wikipedia page about serie is null
     *                                                                   or URL to czech Wikipedia page about serie is null
     *                                                                   or path to file with serie's picture is null
     *                                                                   or count of seasons is negative number
     *                                                                   or count of episodes is negative number
     *                                                                   or total length of seasons is null
     *                                                                   or total length of seasons is negative number
     *                                                                   or note is null
     *                                                                   or genres are null
     *                                                                   or genres contain null value
     *                                                                   or genre ID is null
     *                                                                   or genre name is null
     *                                                                   or genre name is empty string
     *                                                                   or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if serie doesn't exist in data storage
     *                                                                   or genre doesn't exist in data storage
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(final String serie) {
        serieFacade.update(deserialize(serie, SerieTO.class));
    }

    /**
     * Removes serie.
     *
     * @param serie serie
     * @throws IllegalArgumentException                                  if serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if serie doesn't exist in data storage
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public void remove(final String serie) {
        serieFacade.remove(deserialize(serie, SerieTO.class));
    }

    /**
     * Duplicates serie.
     *
     * @param serie serie
     * @throws IllegalArgumentException                                  if serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if serie doesn't exist in data storage
     */
    @RequestMapping(value = "/duplicate", method = RequestMethod.POST)
    @ResponseBody
    public void duplicate(final String serie) {
        serieFacade.duplicate(deserialize(serie, SerieTO.class));
    }

    /**
     * Moves serie in list one position up.
     *
     * @param serie serie
     * @throws IllegalArgumentException                                  if serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or serie can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if serie doesn't exist in data storage
     */
    @RequestMapping(value = "/moveUp", method = RequestMethod.POST)
    @ResponseBody
    public void moveUp(final String serie) {
        serieFacade.moveUp(deserialize(serie, SerieTO.class));
    }

    /**
     * Moves serie in list one position down.
     *
     * @param serie serie
     * @throws IllegalArgumentException                                  if serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or serie can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if serie doesn't exist in data storage
     */
    @RequestMapping(value = "/moveDown", method = RequestMethod.POST)
    @ResponseBody
    public void moveDown(final String serie) {
        serieFacade.moveDown(deserialize(serie, SerieTO.class));
    }

    /**
     * Returns true if serie exists.
     *
     * @param serie serie
     * @return true if serie exists
     * @throws IllegalArgumentException                              if serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String exists(final String serie) {
        return serialize(serieFacade.exists(deserialize(serie, SerieTO.class)));
    }

    /**
     * Updates positions.
     */
    @RequestMapping(value = "/updatePositions", method = RequestMethod.GET)
    @ResponseBody
    public void updatePositions() {
        serieFacade.updatePositions();
    }

    /**
     * Returns total length of all series.
     *
     * @return total length of all series
     */
    @RequestMapping(value = "/totalLength", method = RequestMethod.GET)
    @ResponseBody
    public String getTotalLength() {
        return serialize(serieFacade.getTotalLength());
    }

    /**
     * Returns count of seasons from all series.
     *
     * @return count of seasons from all series
     */
    @RequestMapping(value = "/seasonsCount", method = RequestMethod.GET)
    @ResponseBody
    public String getSeasonsCount() {
        return serialize(serieFacade.getSeasonsCount());
    }

    /**
     * Returns count of episodes from all series.
     *
     * @return count of episodes from all series
     */
    @RequestMapping(value = "/episodesCount", method = RequestMethod.GET)
    @ResponseBody
    public String getEpisodesCount() {
        return serialize(serieFacade.getEpisodesCount());
    }

}
