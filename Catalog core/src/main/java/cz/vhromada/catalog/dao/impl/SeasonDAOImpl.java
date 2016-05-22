package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonDAO")
public class SeasonDAOImpl extends AbstractDAO<Season> implements SeasonDAO {

    /**
     * Creates a new instance of SeasonDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public SeasonDAOImpl(final EntityManager entityManager) {
        super(entityManager, Season.class, "Season");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Season getSeason(final Integer id) {
        return getItem(id);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Season season) {
        addItem(season);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Season season) {
        updateItem(season);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Season season) {
        removeItem(season);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public List<Season> findSeasonsByShow(final Show show) {
        return null;//getData(show, "Show", Season.FIND_BY_SHOW, "show");
    }

}
