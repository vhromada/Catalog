package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.SerieTO;

/**
 * An interface represents facade for series.
 *
 * @author Vladimir Hromada
 */
public interface SerieFacade {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *          if there was error in working with service tier
     */
    void newData();

    /**
     * Returns list of TO for serie.
     *
     * @return list of TO for serie
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *          if there was error in working with service tier
     */
    List<SerieTO> getSeries();

    /**
     * Returns TO for serie with ID or null if there isn't such TO for serie.
     *
     * @param id ID
     * @return TO for serie with ID or null if there isn't such TO for serie
     * @throws IllegalArgumentException if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    SerieTO getSerie(Integer id);

    /**
     * Adds TO for serie. Sets new ID and position.
     *
     * @param serie TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID isn't null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about serie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about serie is null
     *                                  or URL to czech Wikipedia page about serie is null
     *                                  or path to file with serie's picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if TO for genre doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    void add(SerieTO serie);

    /**
     * Updates TO for serie.
     *
     * @param serie new value of TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about serie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about serie is null
     *                                  or URL to czech Wikipedia page about serie is null
     *                                  or path to file with serie's picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if TO for serie doesn't exist in data storage
     *                                  or TO for genre doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    void update(SerieTO serie);

    /**
     * Removes TO for serie.
     *
     * @param serie TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if TO for serie doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    void remove(SerieTO serie);

    /**
     * Duplicates TO for serie.
     *
     * @param serie TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if TO for serie doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    void duplicate(SerieTO serie);

    /**
     * Moves TO for serie in list one position up.
     *
     * @param serie TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or TO for serie can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if TO for serie doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    void moveUp(SerieTO serie);

    /**
     * Moves TO for serie in list one position down.
     *
     * @param serie TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or TO for serie can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if TO for serie doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    void moveDown(SerieTO serie);

    /**
     * Returns true if TO for serie exists.
     *
     * @param serie TO for serie
     * @return true if TO for serie exists
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *                                  if there was error in working with service tier
     */
    boolean exists(SerieTO serie);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *          if there was error in working with service tier
     */
    void updatePositions();

    /**
     * Returns total length of all series.
     *
     * @return total length of all series
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *          if there was error in working with service tier
     */
    Time getTotalLength();

    /**
     * Returns count of seasons from all series.
     *
     * @return count of seasons from all series
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *          if there was error in working with service tier
     */
    int getSeasonsCount();

    /**
     * Returns count of episodes from all series.
     *
     * @return count of episodes from all series
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
     *          if there was error in working with service tier
     */
    int getEpisodesCount();

}
