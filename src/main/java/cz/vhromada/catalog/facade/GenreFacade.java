package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Genre;

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
     * Returns genres.
     *
     * @return genres
     */
    List<Genre> getGenres();

    /**
     * Returns genre with ID or null if there isn't such genre.
     *
     * @param id ID
     * @return genre with ID or null if there isn't such genre
     * @throws IllegalArgumentException if ID is null
     */
    Genre getGenre(Integer id);

    /**
     * Adds genre. Sets new ID and position.
     *
     * @param genre genre
     * @throws IllegalArgumentException                              if genre is null
     *                                                               or ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about genre is null
     *                                                               or URL to czech Wikipedia page about genre is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    void add(Genre genre);

    /**
     * Updates genre.
     *
     * @param genre new value of genre
     * @throws IllegalArgumentException                                  if genre is null
     *                                                                   or ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or genre doesn't exist in data storage
     */
    void update(Genre genre);

    /**
     * Removes genre.
     *
     * @param genre genre
     * @throws IllegalArgumentException                                  if genre is null
     *                                                                   or ID is null
     *                                                                   or genre doesn't exist in data storage
     */
    void remove(Genre genre);

    /**
     * Duplicates genre.
     *
     * @param genre genre
     * @throws IllegalArgumentException                                  if genre is null
     *                                                                   or ID is null
     *                                                                   or genre doesn't exist in data storage
     */
    void duplicate(Genre genre);

    /**
     * Moves genre in list one position up.
     *
     * @param genre genre
     * @throws IllegalArgumentException                                  if genre is null
     *                                                                   or ID is null
     *                                                                   or genre can't be moved up
     *                                                                   or genre doesn't exist in data storage
     */
    void moveUp(Genre genre);

    /**
     * Moves genre in list one position down.
     *
     * @param genre genre
     * @throws IllegalArgumentException                                  if genre is null
     *                                                                   or ID is null
     *                                                                   or genre can't be moved down
     *                                                                   or genre doesn't exist in data storage
     */
    void moveDown(Genre genre);

    /**
     * Updates positions.
     */
    void updatePositions();
}
