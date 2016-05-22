package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SeasonUtils;
import cz.vhromada.catalog.commons.ShowUtils;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link SeasonDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SeasonDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for seasons
     */
    @Mock
    private TypedQuery<Season> seasonsQuery;

    /**
     * Instance of {@link SeasonDAO}
     */
    private SeasonDAO seasonDAO;

    /**
     * Initializes DAO for seasons.
     */
    @Before
    public void setUp() {
        seasonDAO = new SeasonDAOImpl(entityManager);
    }

    /**
     * Test method for {@link SeasonDAOImpl#SeasonDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new SeasonDAOImpl(null);
    }

    /**
     * Test method for {@link SeasonDAO#getSeason(Integer)} with existing season.
     */
    @Test
    public void testGetSeason_ExistingSeason() {
        when(entityManager.find(eq(Season.class), anyInt())).thenReturn(SeasonUtils.newSeason(SeasonUtils.ID));

        final Season season = seasonDAO.getSeason(SeasonUtils.ID);

        SeasonUtils.assertSeasonDeepEquals(SeasonUtils.newSeason(SeasonUtils.ID), season);

        verify(entityManager).find(Season.class, SeasonUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SeasonDAO#getSeason(Integer)} with not existing season.
     */
    @Test
    public void testGetSeason_NotExistingSeason() {
        when(entityManager.find(eq(Season.class), anyInt())).thenReturn(null);

        final Season season = seasonDAO.getSeason(Integer.MAX_VALUE);

        assertNull(season);

        verify(entityManager).find(Season.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SeasonDAO#getSeason(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSeason_NullArgument() {
        seasonDAO.getSeason(null);
    }

    /**
     * Test method for {@link SeasonDAO#getSeason(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetSeason_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Season.class), anyInt());

        seasonDAO.getSeason(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link SeasonDAO#add(Season)}.
     */
    @Test
    public void testAdd() {
        final Season season = SeasonUtils.newSeason(SeasonUtils.ID);
        doAnswer(setId(SeasonUtils.ID)).when(entityManager).persist(any(Season.class));

        seasonDAO.add(season);

        assertEquals(SeasonUtils.ID, season.getId());
        assertEquals(SeasonUtils.ID - 1, season.getPosition());

        verify(entityManager).persist(season);
        verify(entityManager).merge(season);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SeasonDAO#add(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        seasonDAO.add(null);
    }

    /**
     * Test method for {@link SeasonDAO#add(Season)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Season.class));

        seasonDAO.add(SeasonUtils.newSeason(SeasonUtils.ID));
    }

    /**
     * Test method for {@link SeasonDAO#update(Season)}.
     */
    @Test
    public void testUpdate() {
        final Season season = SeasonUtils.newSeason(SeasonUtils.ID);

        seasonDAO.update(season);

        verify(entityManager).merge(season);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SeasonDAO#update(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        seasonDAO.update(null);
    }

    /**
     * Test method for {@link SeasonDAO#update(Season)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Season.class));

        seasonDAO.update(SeasonUtils.newSeason(SeasonUtils.ID));
    }

    /**
     * Test method for {@link SeasonDAO#remove(Season)} with managed season.
     */
    @Test
    public void testRemove_ManagedSeason() {
        final Season season = SeasonUtils.newSeason(SeasonUtils.ID);
        when(entityManager.contains(any(Season.class))).thenReturn(true);

        seasonDAO.remove(season);

        verify(entityManager).contains(season);
        verify(entityManager).remove(season);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SeasonDAO#remove(Season)} with not managed season.
     */
    @Test
    public void testRemove_NotManagedSeason() {
        final Season season = SeasonUtils.newSeason(SeasonUtils.ID);
        when(entityManager.contains(any(Season.class))).thenReturn(false);
        when(entityManager.getReference(eq(Season.class), anyInt())).thenReturn(season);

        seasonDAO.remove(season);

        verify(entityManager).contains(season);
        verify(entityManager).getReference(Season.class, season.getId());
        verify(entityManager).remove(season);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SeasonDAO#remove(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        seasonDAO.remove(null);
    }

    /**
     * Test method for {@link SeasonDAO#remove(Season)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Season.class));

        seasonDAO.remove(SeasonUtils.newSeason(SeasonUtils.ID));
    }

    /**
     * Test method for {@link SeasonDAO#findSeasonsByShow(Show)}.
     */
    @Test
    public void testFindSeasonsByShow() {
        final Show show = ShowUtils.newShow(ShowUtils.ID);
        when(entityManager.createNamedQuery(anyString(), eq(Season.class))).thenReturn(seasonsQuery);
        when(seasonsQuery.getResultList()).thenReturn(CollectionUtils.newList(SeasonUtils.newSeason(SeasonUtils.ID), SeasonUtils.newSeason(2)));

        final List<Season> seasons = seasonDAO.findSeasonsByShow(show);

        SeasonUtils.assertSeasonsDeepEquals(CollectionUtils.newList(SeasonUtils.newSeason(SeasonUtils.ID), SeasonUtils.newSeason(2)), seasons);

        verify(entityManager).createNamedQuery(Season.FIND_BY_SHOW, Season.class);
        verify(seasonsQuery).setParameter("show", ShowUtils.ID);
        verify(seasonsQuery).getResultList();
        verifyNoMoreInteractions(entityManager, seasonsQuery);
    }

    /**
     * Test method for {@link SeasonDAO#findSeasonsByShow(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_NullArgument() {
        seasonDAO.findSeasonsByShow(null);
    }

    /**
     * Test method for {@link SeasonDAO#findSeasonsByShow(Show)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testFindSeasonsByShow_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Season.class));

        seasonDAO.findSeasonsByShow(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Season) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
