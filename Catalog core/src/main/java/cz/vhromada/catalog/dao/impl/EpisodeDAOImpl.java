package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeDAO")
public class EpisodeDAOImpl implements EpisodeDAO {

    /** Entity manager argument */
    private static final String ENTITY_MANAGER_ARGUMENT = "Entity manager";

    /** Season argument */
    private static final String SEASON_ARGUMENT = "Season";

    /** Episode argument */
    private static final String EPISODE_ARGUMENT = "Episode";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link DataStorageException} */
    private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

    /** Entity manager */
    private EntityManager entityManager;

    /**
     * Creates a new instance of EpisodeDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public EpisodeDAOImpl(final EntityManager entityManager) {
        Validators.validateArgumentNotNull(entityManager, ENTITY_MANAGER_ARGUMENT);

        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Episode getEpisode(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Episode.class, id);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            entityManager.persist(episode);
            episode.setPosition(episode.getId() - 1);
            entityManager.merge(episode);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            entityManager.merge(episode);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            if (entityManager.contains(episode)) {
                entityManager.remove(episode);
            } else {
                entityManager.remove(entityManager.getReference(Episode.class, episode.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public List<Episode> findEpisodesBySeason(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            final TypedQuery<Episode> query = entityManager.createNamedQuery(Episode.FIND_BY_SEASON, Episode.class);
            query.setParameter("season", season.getId());
            return query.getResultList();
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
