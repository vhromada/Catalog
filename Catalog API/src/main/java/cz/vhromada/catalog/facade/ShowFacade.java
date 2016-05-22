package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.ShowTO;

/**
 * An interface represents facade for shows.
 *
 * @author Vladimir Hromada
 */
public interface ShowFacade {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void newData();

    /**
     * Returns list of TO for show.
     *
     * @return list of TO for show
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    List<ShowTO> getShows();

    /**
     * Returns TO for show with ID or null if there isn't such TO for show.
     *
     * @param id ID
     * @return TO for show with ID or null if there isn't such TO for show
     * @throws IllegalArgumentException                                       if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    ShowTO getShow(Integer id);

    /**
     * Adds TO for show. Sets new ID and position.
     *
     * @param show TO for show
     * @throws IllegalArgumentException                                       if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID isn't null
     *                                                                        or czech name is null
     *                                                                        or czech name is empty string
     *                                                                        or original name is null
     *                                                                        or original name is empty string
     *                                                                        or URL to ČSFD page about show is null
     *                                                                        or IMDB code isn't -1 or between 1 and 9999999
     *                                                                        or URL to english Wikipedia page about show is null
     *                                                                        or URL to czech Wikipedia page about show is null
     *                                                                        or path to file with show picture is null
     *                                                                        or note is null
     *                                                                        or genres are null
     *                                                                        or genres contain null value
     *                                                                        or genre ID is null
     *                                                                        or genre name is null
     *                                                                        or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for genre doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void add(ShowTO show);

    /**
     * Updates TO for show.
     *
     * @param show new value of TO for show
     * @throws IllegalArgumentException                                       if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or czech name is null
     *                                                                        or czech name is empty string
     *                                                                        or original name is null
     *                                                                        or original name is empty string
     *                                                                        or URL to ČSFD page about show is null
     *                                                                        or IMDB code isn't -1 or between 1 and 9999999
     *                                                                        or URL to english Wikipedia page about show is null
     *                                                                        or URL to czech Wikipedia page about show is null
     *                                                                        or path to file with show picture is null
     *                                                                        or note is null
     *                                                                        or genres are null
     *                                                                        or genres contain null value
     *                                                                        or genre ID is null
     *                                                                        or genre name is null
     *                                                                        or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for show doesn't exist in data storage
     *                                                                        or TO for genre doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void update(ShowTO show);

    /**
     * Removes TO for show.
     *
     * @param show TO for show
     * @throws IllegalArgumentException                                       if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for show doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void remove(ShowTO show);

    /**
     * Duplicates TO for show.
     *
     * @param show TO for show
     * @throws IllegalArgumentException                                       if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for show doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void duplicate(ShowTO show);

    /**
     * Moves TO for show in list one position up.
     *
     * @param show TO for show
     * @throws IllegalArgumentException                                       if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for show can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for show doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void moveUp(ShowTO show);

    /**
     * Moves TO for show in list one position down.
     *
     * @param show TO for show
     * @throws IllegalArgumentException                                       if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for show can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for show doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void moveDown(ShowTO show);

    /**
     * Returns true if TO for show exists.
     *
     * @param show TO for show
     * @return true if TO for show exists
     * @throws IllegalArgumentException                                       if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    boolean exists(ShowTO show);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void updatePositions();

    /**
     * Returns total length of all shows.
     *
     * @return total length of all shows
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    Time getTotalLength();

    /**
     * Returns count of seasons from all shows.
     *
     * @return count of seasons from all shows
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    int getSeasonsCount();

    /**
     * Returns count of episodes from all shows.
     *
     * @return count of episodes from all shows
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    int getEpisodesCount();

}