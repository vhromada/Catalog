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
import cz.vhromada.catalog.commons.EpisodeUtils;
import cz.vhromada.catalog.commons.SeasonUtils;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link EpisodeDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class EpisodeDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for episodes
     */
    @Mock
    private TypedQuery<Episode> episodesQuery;

    /**
     * Instance of {@link EpisodeDAO}
     */
    private EpisodeDAO episodeDAO;

    /**
     * Initializes DAO for episodes.
     */
    @Before
    public void setUp() {
        episodeDAO = new EpisodeDAOImpl(entityManager);
    }

    /**
     * Test method for {@link EpisodeDAOImpl#EpisodeDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new EpisodeDAOImpl(null);
    }

    /**
     * Test method for {@link EpisodeDAO#getEpisode(Integer)} with existing episode.
     */
    @Test
    public void testGetEpisode_ExistingEpisode() {
        when(entityManager.find(eq(Episode.class), anyInt())).thenReturn(EpisodeUtils.newEpisode(EpisodeUtils.ID));

        final Episode episode = episodeDAO.getEpisode(EpisodeUtils.ID);

        EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.newEpisode(EpisodeUtils.ID), episode);

        verify(entityManager).find(Episode.class, EpisodeUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link EpisodeDAO#getEpisode(Integer)} with not existing episode.
     */
    @Test
    public void testGetEpisode_NotExistingEpisode() {
        when(entityManager.find(eq(Episode.class), anyInt())).thenReturn(null);

        final Episode episode = episodeDAO.getEpisode(Integer.MAX_VALUE);

        assertNull(episode);

        verify(entityManager).find(Episode.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link EpisodeDAO#getEpisode(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEpisode_NullArgument() {
        episodeDAO.getEpisode(null);
    }

    /**
     * Test method for {@link EpisodeDAO#getEpisode(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetEpisode_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Episode.class), anyInt());

        episodeDAO.getEpisode(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link EpisodeDAO#add(Episode)}.
     */
    @Test
    public void testAdd() {
        final Episode episode = EpisodeUtils.newEpisode(EpisodeUtils.ID);
        doAnswer(setId(EpisodeUtils.ID)).when(entityManager).persist(any(Episode.class));

        episodeDAO.add(episode);

        assertEquals(EpisodeUtils.ID, episode.getId());
        assertEquals(EpisodeUtils.ID - 1, episode.getPosition());

        verify(entityManager).persist(episode);
        verify(entityManager).merge(episode);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link EpisodeDAO#add(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        episodeDAO.add(null);
    }

    /**
     * Test method for {@link EpisodeDAO#add(Episode)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Episode.class));

        episodeDAO.add(EpisodeUtils.newEpisode(EpisodeUtils.ID));
    }

    /**
     * Test method for {@link EpisodeDAO#update(Episode)}.
     */
    @Test
    public void testUpdate() {
        final Episode episode = EpisodeUtils.newEpisode(EpisodeUtils.ID);

        episodeDAO.update(episode);

        verify(entityManager).merge(episode);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link EpisodeDAO#update(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        episodeDAO.update(null);
    }

    /**
     * Test method for {@link EpisodeDAO#update(Episode)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Episode.class));

        episodeDAO.update(EpisodeUtils.newEpisode(EpisodeUtils.ID));
    }

    /**
     * Test method for {@link EpisodeDAO#remove(Episode)} with managed episode.
     */
    @Test
    public void testRemove_ManagedEpisode() {
        final Episode episode = EpisodeUtils.newEpisode(EpisodeUtils.ID);
        when(entityManager.contains(any(Episode.class))).thenReturn(true);

        episodeDAO.remove(episode);

        verify(entityManager).contains(episode);
        verify(entityManager).remove(episode);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link EpisodeDAO#remove(Episode)} with not managed episode.
     */
    @Test
    public void testRemove_NotManagedEpisode() {
        final Episode episode = EpisodeUtils.newEpisode(EpisodeUtils.ID);
        when(entityManager.contains(any(Episode.class))).thenReturn(false);
        when(entityManager.getReference(eq(Episode.class), anyInt())).thenReturn(episode);

        episodeDAO.remove(episode);

        verify(entityManager).contains(episode);
        verify(entityManager).getReference(Episode.class, episode.getId());
        verify(entityManager).remove(episode);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link EpisodeDAO#remove(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        episodeDAO.remove(null);
    }

    /**
     * Test method for {@link EpisodeDAO#remove(Episode)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Episode.class));

        episodeDAO.remove(EpisodeUtils.newEpisode(EpisodeUtils.ID));
    }

    /**
     * Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        final Season season = SeasonUtils.newSeason(SeasonUtils.ID);
        when(entityManager.createNamedQuery(anyString(), eq(Episode.class))).thenReturn(episodesQuery);
        when(episodesQuery.getResultList()).thenReturn(CollectionUtils.newList(EpisodeUtils.newEpisode(EpisodeUtils.ID), EpisodeUtils.newEpisode(2)));

        final List<Episode> episodes = episodeDAO.findEpisodesBySeason(season);

        EpisodeUtils.assertEpisodesDeepEquals(CollectionUtils.newList(EpisodeUtils.newEpisode(EpisodeUtils.ID), EpisodeUtils.newEpisode(2)), episodes);

        verify(entityManager).createNamedQuery(Episode.FIND_BY_SEASON, Episode.class);
        verify(episodesQuery).setParameter("season", SeasonUtils.ID);
        verify(episodesQuery).getResultList();
        verifyNoMoreInteractions(entityManager, episodesQuery);
    }

    /**
     * Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_NullArgument() {
        episodeDAO.findEpisodesBySeason(null);
    }

    /**
     * Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testFindEpisodesBySeason_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Episode.class));

        episodeDAO.findEpisodesBySeason(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Episode) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
