package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieDAO")
public class MovieDAOImpl extends AbstractDAO<Movie> implements MovieDAO {

    /**
     * Creates a new instance of MovieDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public MovieDAOImpl(final EntityManager entityManager) {
        super(entityManager, Movie.class, "Movie");
    }

    /**
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Movie> getMovies() {
        return getData(Movie.SELECT_MOVIES);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Movie getMovie(final Integer id) {
        return getItem(id);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Movie movie) {
        addItem(movie);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Movie movie) {
        updateItem(movie);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Movie movie) {
        removeItem(movie);
    }

}
