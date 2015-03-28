package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieDAO")
public class MovieDAOImpl implements MovieDAO {

    /**
     * Entity manager argument
     */
    private static final String ENTITY_MANAGER_ARGUMENT = "Entity manager";

    /**
     * Movie argument
     */
    private static final String MOVIE_ARGUMENT = "Movie";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link DataStorageException}
     */
    private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

    /**
     * Entity manager
     */
    private EntityManager entityManager;

    /**
     * Creates a new instance of MovieDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public MovieDAOImpl(final EntityManager entityManager) {
        Validators.validateArgumentNotNull(entityManager, ENTITY_MANAGER_ARGUMENT);

        this.entityManager = entityManager;
    }

    /**
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Movie> getMovies() {
        try {
            return new ArrayList<>(entityManager.createNamedQuery(Movie.SELECT_MOVIES, Movie.class).getResultList());
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Movie getMovie(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Movie.class, id);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            entityManager.persist(movie);
            movie.setPosition(movie.getId() - 1);
            entityManager.merge(movie);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            entityManager.merge(movie);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            if (entityManager.contains(movie)) {
                entityManager.remove(movie);
            } else {
                entityManager.remove(entityManager.getReference(Movie.class, movie.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
