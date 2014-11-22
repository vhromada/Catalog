package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;

/**
 * An interface represents service for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreService {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    void newData();

    /**
     * Returns list of genre.
     *
     * @return list of genre
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *          if there was error in working with DAO tier
     */
    List<Genre> getGenres();

    /**
     * Returns genre with ID or null if there isn't such genre.
     *
     * @param id ID
     * @return genre with ID or null if there isn't such genre
     * @throws IllegalArgumentException if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    Genre getGenre(Integer id);

    /**
     * Adds genre. Sets new ID.
     *
     * @param genre genre
     * @throws IllegalArgumentException if genre is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void add(Genre genre);

    /**
     * Adds list of genre names.
     *
     * @param genres list of genre names
     * @throws IllegalArgumentException if list of genre names is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void add(List<String> genres);

    /**
     * Updates genre.
     *
     * @param genre new value of genre
     * @throws IllegalArgumentException if genre is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void update(Genre genre);

    /**
     * Removes genre.
     *
     * @param genre genre
     * @throws IllegalArgumentException if genre is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void remove(Genre genre);

    /**
     * Returns true if genre exists.
     *
     * @param genre genre
     * @return true if genre exists
     * @throws IllegalArgumentException if genre is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    boolean exists(Genre genre);

}
