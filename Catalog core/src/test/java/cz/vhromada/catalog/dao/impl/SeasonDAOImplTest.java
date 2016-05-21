//package cz.vhromada.catalog.dao.impl;
//
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.fail;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.anyString;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoMoreInteractions;
//import static org.mockito.Mockito.verifyZeroInteractions;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceException;
//import javax.persistence.TypedQuery;
//
//import cz.vhromada.catalog.commons.CollectionUtils;
//import cz.vhromada.catalog.commons.ObjectGeneratorTest;
//import cz.vhromada.catalog.dao.SeasonDAO;
//import cz.vhromada.catalog.dao.entities.Season;
//import cz.vhromada.catalog.dao.entities.Show;
//import cz.vhromada.catalog.dao.exceptions.DataStorageException;
//import cz.vhromada.test.DeepAsserts;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.mockito.stubbing.Answer;
//
///**
// * A class represents test for class {@link SeasonDAOImpl}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(MockitoJUnitRunner.class)
//public class SeasonDAOImplTest extends ObjectGeneratorTest {
//
//    /**
//     * Instance of {@link EntityManager}
//     */
//    @Mock
//    private EntityManager entityManager;
//
//    /**
//     * Query for seasons
//     */
//    @Mock
//    private TypedQuery<Season> seasonsQuery;
//
//    /**
//     * Instance of {@link SeasonDAO}
//     */
//    private SeasonDAO seasonDAO;
//
//    /**
//     * Initializes DAO for seasons.
//     */
//    @Before
//    public void setUp() {
//        seasonDAO = new SeasonDAOImpl(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAOImpl#SeasonDAOImpl(EntityManager)} with null entity manager.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullEntityManager() {
//        new SeasonDAOImpl(null);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#getSeason(Integer)} with existing season.
//     */
//    @Test
//    public void testGetSeasonWithExistingSeason() {
//        final int id = generate(Integer.class);
//        final Season season = mock(Season.class);
//        when(entityManager.find(eq(Season.class), anyInt())).thenReturn(season);
//
//        DeepAsserts.assertEquals(season, seasonDAO.getSeason(id));
//
//        verify(entityManager).find(Season.class, id);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#getSeason(Integer)} with not existing season.
//     */
//    @Test
//    public void testGetSeasonWithNotExistingSeason() {
//        when(entityManager.find(eq(Season.class), anyInt())).thenReturn(null);
//
//        assertNull(seasonDAO.getSeason(Integer.MAX_VALUE));
//
//        verify(entityManager).find(Season.class, Integer.MAX_VALUE);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#getSeason(Integer)} with null argument.
//     */
//    @Test
//    public void testGetSeasonWithNullArgument() {
//        try {
//            seasonDAO.getSeason(null);
//            fail("Can't get season with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#getSeason(Integer)} with exception in persistence.
//     */
//    @Test
//    public void testGetSeasonWithPersistenceException() {
//        doThrow(PersistenceException.class).when(entityManager).find(eq(Season.class), anyInt());
//
//        try {
//            seasonDAO.getSeason(Integer.MAX_VALUE);
//            fail("Can't get season with not thrown DataStorageException for exception in persistence.");
//        } catch (final DataStorageException ex) {
//            // OK
//        }
//
//        verify(entityManager).find(Season.class, Integer.MAX_VALUE);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#add(Season)}.
//     */
//    @Test
//    public void testAdd() {
//        final Season season = generate(Season.class);
//        final int id = generate(Integer.class);
//        doAnswer(setId(id)).when(entityManager).persist(any(Season.class));
//
//        seasonDAO.add(season);
//        DeepAsserts.assertEquals(id, season.getId());
//        DeepAsserts.assertEquals(id - 1, season.getPosition());
//
//        verify(entityManager).persist(season);
//        verify(entityManager).merge(season);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#add(Season)} with null argument.
//     */
//    @Test
//    public void testAddWithNullArgument() {
//        try {
//            seasonDAO.add(null);
//            fail("Can't add season with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#add(Season)} with exception in persistence.
//     */
//    @Test
//    public void testAddWithPersistenceException() {
//        final Season season = generate(Season.class);
//        doThrow(PersistenceException.class).when(entityManager).persist(any(Season.class));
//
//        try {
//            seasonDAO.add(season);
//            fail("Can't add season with not thrown DataStorageException for exception in persistence.");
//        } catch (final DataStorageException ex) {
//            // OK
//        }
//
//        verify(entityManager).persist(season);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#update(Season)}.
//     */
//    @Test
//    public void testUpdate() {
//        final Season season = generate(Season.class);
//
//        seasonDAO.update(season);
//
//        verify(entityManager).merge(season);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#update(Season)} with null argument.
//     */
//    @Test
//    public void testUpdateWithNullArgument() {
//        try {
//            seasonDAO.update(null);
//            fail("Can't update season with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#update(Season)} with exception in persistence.
//     */
//    @Test
//    public void testUpdateWithPersistenceException() {
//        final Season season = generate(Season.class);
//        doThrow(PersistenceException.class).when(entityManager).merge(any(Season.class));
//
//        try {
//            seasonDAO.update(season);
//            fail("Can't update season with not thrown DataStorageException for exception in persistence.");
//        } catch (final DataStorageException ex) {
//            // OK
//        }
//
//        verify(entityManager).merge(season);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#remove(Season)} with managed season.
//     */
//    @Test
//    public void testRemoveWithManagedSeason() {
//        final Season season = generate(Season.class);
//        when(entityManager.contains(any(Season.class))).thenReturn(true);
//
//        seasonDAO.remove(season);
//
//        verify(entityManager).contains(season);
//        verify(entityManager).remove(season);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#remove(Season)} with not managed season.
//     */
//    @Test
//    public void testRemoveWithNotManagedSeason() {
//        final Season season = generate(Season.class);
//        when(entityManager.contains(any(Season.class))).thenReturn(false);
//        when(entityManager.getReference(eq(Season.class), anyInt())).thenReturn(season);
//
//        seasonDAO.remove(season);
//
//        verify(entityManager).contains(season);
//        verify(entityManager).getReference(Season.class, season.getId());
//        verify(entityManager).remove(season);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#remove(Season)} with null argument.
//     */
//    @Test
//    public void testRemoveWithNullArgument() {
//        try {
//            seasonDAO.remove(null);
//            fail("Can't remove season with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#remove(Season)} with exception in persistence.
//     */
//    @Test
//    public void testRemoveWithPersistenceException() {
//        final Season season = generate(Season.class);
//        doThrow(PersistenceException.class).when(entityManager).contains(any(Season.class));
//
//        try {
//            seasonDAO.remove(season);
//            fail("Can't remove season with not thrown DataStorageException for exception in persistence.");
//        } catch (final DataStorageException ex) {
//            // OK
//        }
//
//        verify(entityManager).contains(season);
//        verifyNoMoreInteractions(entityManager);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#findSeasonsByShow(Show)}.
//     */
//    @Test
//    public void testFindSeasonsByShow() {
//        final Show show = generate(Show.class);
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        when(entityManager.createNamedQuery(anyString(), eq(Season.class))).thenReturn(seasonsQuery);
//        when(seasonsQuery.getResultList()).thenReturn(seasons);
//
//        DeepAsserts.assertEquals(seasons, seasonDAO.findSeasonsByShow(show));
//
//        verify(entityManager).createNamedQuery(Season.FIND_BY_SHOW, Season.class);
//        verify(seasonsQuery).getResultList();
//        verify(seasonsQuery).setParameter("show", show.getId());
//        verifyNoMoreInteractions(entityManager, seasonsQuery);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#findSeasonsByShow(Show)} with null argument.
//     */
//    @Test
//    public void testFindSeasonsByShowWithNullArgument() {
//        try {
//            seasonDAO.findSeasonsByShow(null);
//            fail("Can't find seasons by show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(entityManager, seasonsQuery);
//    }
//
//    /**
//     * Test method for {@link SeasonDAO#findSeasonsByShow(Show)} with exception in persistence.
//     */
//    @Test
//    public void testFindSeasonsByShowWithPersistenceException() {
//        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Season.class));
//
//        try {
//            seasonDAO.findSeasonsByShow(generate(Show.class));
//            fail("Can't find seasons by show with not thrown DataStorageException for exception in persistence.");
//        } catch (final DataStorageException ex) {
//            // OK
//        }
//
//        verify(entityManager).createNamedQuery(Season.FIND_BY_SHOW, Season.class);
//        verifyNoMoreInteractions(entityManager);
//        verifyZeroInteractions(seasonsQuery);
//    }
//
//    /**
//     * Sets ID.
//     *
//     * @param id ID
//     * @return mocked answer
//     */
//    private static Answer<Void> setId(final Integer id) {
//        return new Answer<Void>() {
//
//            @Override
//            public Void answer(final InvocationOnMock invocation) {
//                ((Season) invocation.getArguments()[0]).setId(id);
//                return null;
//            }
//
//        };
//    }
//
//}
