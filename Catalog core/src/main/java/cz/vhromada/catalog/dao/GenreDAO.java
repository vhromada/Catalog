package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;

/**
 * An interface represents DAO for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreDAO {

    /**
     * Returns list of genres.
     *
     * @return list of genres
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    List<Genre> getGenres();

    /**
     * Returns genre with ID or null if there isn't such genre.
     *
     * @param id ID
     * @return genre with ID or null if there isn't such genre
     * @throws IllegalArgumentException                                if ID is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    Genre getGenre(Integer id);

    /**
     * Adds genre. Sets new ID.
     *
     * @param genre genre
     * @throws IllegalArgumentException                                if genre is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void add(Genre genre);

    /**
     * Updates genre.
     *
     * @param genre new value of genre
     * @throws IllegalArgumentException                                if genre is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void update(Genre genre);

    /**
     * Removes genre.
     *
     * @param genre genre
     * @throws IllegalArgumentException                                if genre is null
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException if there was error with working with data storage
     */
    void remove(Genre genre);

}
