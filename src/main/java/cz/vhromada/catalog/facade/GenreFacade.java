package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.GenreTO;

/**
 * An interface represents facade for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreFacade {

    /**
     * Creates new data.
     */
    void newData();

    /**
     * Returns list of TO for genre.
     *
     * @return list of TO for genre
     */
    List<GenreTO> getGenres();

    /**
     * Returns TO for genre with ID or null if there isn't such TO for genre.
     *
     * @param id ID
     * @return TO for genre with ID or null if there isn't such TO for genre
     * @throws IllegalArgumentException if ID is null
     */
    GenreTO getGenre(Integer id);

    /**
     * Adds TO for genre. Sets new ID and position.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException                              if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about genre is null
     *                                                               or URL to czech Wikipedia page about genre is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    void add(GenreTO genre);

    /**
     * Adds list of genre names.
     *
     * @param genres list of genre names
     * @throws IllegalArgumentException                              if list of genre names is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if list of genre names contains null value
     */
    void add(List<String> genres);

    /**
     * Updates TO for genre.
     *
     * @param genre new value of TO for genre
     * @throws IllegalArgumentException                                  if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for genre doesn't exist in data storage
     */
    void update(GenreTO genre);

    /**
     * Removes TO for genre.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException                                  if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for genre doesn't exist in data storage
     */
    void remove(GenreTO genre);

    /**
     * Duplicates TO for genre.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException                                  if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for genre doesn't exist in data storage
     */
    void duplicate(GenreTO genre);

    /**
     * Moves TO for genre in list one position up.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException                                  if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for genre can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for genre doesn't exist in data storage
     */
    void moveUp(GenreTO genre);

    /**
     * Moves TO for genre in list one position down.
     *
     * @param genre TO for genre
     * @throws IllegalArgumentException                                  if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for genre can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for genre doesn't exist in data storage
     */
    void moveDown(GenreTO genre);

    /**
     * Updates positions.
     */
    void updatePositions();
}
