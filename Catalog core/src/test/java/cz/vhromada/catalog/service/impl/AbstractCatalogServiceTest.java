package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.entities.Movable;
import cz.vhromada.catalog.service.CatalogService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link AbstractCatalogService}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractCatalogServiceTest {

    /**
     * ID
     */
    private static final Integer ID = 5;

    /**
     * Cache key
     */
    private static final String KEY = "data";

    /**
     * Instance of {@link JpaRepository}
     */
    @Mock
    private JpaRepository<Movable, Integer> repository;

    /**
     * Instance of {@link Cache}
     */
    @Mock
    private Cache cache;

    /**
     * Instance of {@link AbstractCatalogService}
     */
    private CatalogService<Movable> catalogService;

    /**
     * Data list
     */
    private List<Movable> dataList;

    /**
     * Initializes data.
     */
    @Before
    public void setUp() {
        catalogService = new AbstractCatalogServiceStub(repository, cache, KEY);
        dataList = CollectionUtils.newList(new MovableStub(1, 0), new MovableStub(2, 1));
        when(repository.findAll()).thenReturn(dataList);
    }

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null repository.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMovableRepository() {
        new AbstractCatalogServiceStub(null, cache, KEY);
    }

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null cache for datas.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGCache() {
        new AbstractCatalogServiceStub(repository, null, KEY);
    }

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null cache key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullCacheKey() {
        new AbstractCatalogServiceStub(repository, cache, null);
    }

    /**
     * Test method for {@link CatalogService#newData()}.
     */
    @Test
    public void testNewData_CachedData() {
        catalogService.newData();

        verify(repository).deleteAll();
        verify(cache).clear();
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#getAll()} with cached data.
     */
    @Test
    public void testGetAll_CachedData() {
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        final List<Movable> data = catalogService.getAll();

        assertNotNull(data);
        assertEquals(dataList, data);

        verify(cache).get(KEY);
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#getAll()} with not cached data.
     */
    @Test
    public void testGets_NotCachedData() {
        when(cache.get(anyString())).thenReturn(null);

        final List<Movable> data = catalogService.getAll();

        assertNotNull(data);
        assertEquals(dataList, data);

        verify(repository).findAll();
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with cached existing data.
     */
    @Test
    public void testGet_CachedExistingData() {
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        final Movable data = catalogService.get(dataList.get(0).getId());

        assertNotNull(data);
        assertEquals(dataList.get(0), data);

        verify(cache).get(KEY);
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with cached not existing data.
     */
    @Test
    public void testGet_CachedNotExistingData() {
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        final Movable data = catalogService.get(Integer.MAX_VALUE);

        assertNull(data);

        verify(cache).get(KEY);
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with not cached existing data.
     */
    @Test
    public void testGet_NotCachedExistingData() {
        when(cache.get(anyString())).thenReturn(null);

        final Movable data = catalogService.get(dataList.get(0).getId());

        assertNotNull(data);
        assertEquals(dataList.get(0), data);

        verify(repository).findAll();
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with not cached not existing data.
     */
    @Test
    public void testGet_NotCachedNotExistingData() {
        when(cache.get(anyString())).thenReturn(null);

        final Movable data = catalogService.get(Integer.MAX_VALUE);

        assertNull(data);

        verify(repository).findAll();
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_NullArgument() {
        catalogService.get(null);
    }

    /**
     * Test method for {@link CatalogService#add(Movable)} with cached data.
     */
    @Test
    public void testAdd_CachedData() {
        final Movable data = new MovableStub(null, 0);
        when(repository.save(any(Movable.class))).thenAnswer(setId(ID));
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.add(data);

        assertEquals(ID, data.getId());
        assertEquals(ID - 1, data.getPosition());
        assertEquals(3, dataList.size());
        assertEquals(data, dataList.get(2));

        verify(repository, times(2)).save(data);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#add(Movable)} with not cached data.
     */
    @Test
    public void testAdd_NotCachedData() {
        final Movable data = new MovableStub(null, 0);
        when(repository.save(any(Movable.class))).thenAnswer(setId(ID));
        when(cache.get(anyString())).thenReturn(null);

        catalogService.add(data);

        assertEquals(ID, data.getId());
        assertEquals(ID - 1, data.getPosition());
        assertEquals(3, dataList.size());
        assertEquals(data, dataList.get(2));

        verify(repository).findAll();
        verify(repository, times(2)).save(data);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#add(Movable)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        catalogService.add(null);
    }

    /**
     * Test method for {@link CatalogService#update(Movable)} with cached data.
     */
    @Test
    public void testUpdate_CachedData() {
        final Movable data = dataList.get(0);
        data.setPosition(10);
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.update(data);

        assertEquals(2, dataList.size());
        assertEquals(data, dataList.get(0));

        verify(repository).save(data);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#update(Movable)} with not cached data.
     */
    @Test
    public void testUpdate_NotCachedData() {
        final Movable data = dataList.get(0);
        data.setPosition(10);
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.update(data);

        assertEquals(2, dataList.size());
        assertEquals(data, dataList.get(0));

        verify(repository).findAll();
        verify(repository).save(data);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#update(Movable)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        catalogService.update(null);
    }

    /**
     * Test method for {@link CatalogService#remove(Movable)} with cached data.
     */
    @Test
    public void testRemove_CachedData() {
        final Movable data = dataList.get(0);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.remove(data);

        assertEquals(1, dataList.size());
        assertFalse(dataList.contains(data));

        verify(repository).delete(data);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#remove(Movable)} with not cached data.
     */
    @Test
    public void testRemove_NotCachedData() {
        final Movable data = dataList.get(0);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.remove(data);

        assertEquals(1, dataList.size());
        assertFalse(dataList.contains(data));

        verify(repository).findAll();
        verify(repository).delete(data);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#remove(Movable)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        catalogService.remove(null);
    }

    /**
     * Test method for {@link CatalogService#duplicate(Movable)} with cached data.
     */
    @Test
    public void testDuplicate_CachedData() {
        final Movable copy = ((AbstractCatalogServiceStub) catalogService).getCopy();
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.duplicate(dataList.get(0));

        assertEquals(3, dataList.size());
        assertEquals(copy, dataList.get(2));

        verify(repository).save(copy);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#duplicate(Movable)} with not cached data.
     */
    @Test
    public void testDuplicate_NotCachedData() {
        final Movable copy = ((AbstractCatalogServiceStub) catalogService).getCopy();
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.duplicate(dataList.get(0));

        assertEquals(3, dataList.size());
        assertEquals(copy, dataList.get(2));

        verify(repository).findAll();
        verify(repository).save(copy);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#duplicate(Movable)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        catalogService.duplicate(null);
    }

    /**
     * Test method for {@link CatalogService#moveUp(Movable)} with cached data.
     */
    @Test
    public void testMoveUp_CachedData() {
        final Movable data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final Movable data2 = dataList.get(1);
        final int position2 = data2.getPosition();
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.moveUp(data2);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveUp(Movable)} with not cached data.
     */
    @Test
    public void testMoveUp_NotCachedData() {
        final Movable data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final Movable data2 = dataList.get(1);
        final int position2 = data2.getPosition();
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.moveUp(data2);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).findAll();
        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveUp(Movable)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        catalogService.moveUp(null);
    }

    /**
     * Test method for {@link CatalogService#moveDown(Movable)} with cached data.
     */
    @Test
    public void testMoveDown_CachedData() {
        final Movable data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final Movable data2 = dataList.get(1);
        final int position2 = data2.getPosition();
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.moveDown(data1);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveDown(Movable)} with not cached data.
     */
    @Test
    public void testMoveDown_NotCachedData() {
        final Movable data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final Movable data2 = dataList.get(1);
        final int position2 = data2.getPosition();
        when(repository.save(any(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.moveDown(data1);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).findAll();
        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveDown(Movable)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        catalogService.moveDown(null);
    }

    /**
     * Test method for {@link CatalogService#updatePositions()} with cached data.
     */
    @Test
    public void testUpdatePositions_CachedData() {
        when(repository.save(anyListOf(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.updatePositions();

        for (int i = 0; i < dataList.size(); i++) {
            final Movable data = dataList.get(i);
            assertEquals(i, data.getPosition());
        }

        verify(repository).save(dataList);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#updatePositions()} with not cached data.
     */
    @Test
    public void testUpdatePositions_NotCachedData() {
        when(repository.save(anyListOf(Movable.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.updatePositions();

        for (int i = 0; i < dataList.size(); i++) {
            final Movable data = dataList.get(i);
            assertEquals(i, data.getPosition());
        }

        verify(repository).findAll();
        verify(repository).save(dataList);
        verify(cache).get(KEY);
        verify(cache).put(KEY, dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Movable> setId(final Integer id) {
        return invocation -> {
            final Movable movable = (Movable) invocation.getArguments()[0];
            movable.setId(id);

            return movable;
        };
    }

    /**
     * A class represents movable stub.
     */
    private static final class MovableStub implements Movable {

        /**
         * SerialVersionUID
         */
        private static final long serialVersionUID = 1L;

        /**
         * ID
         */
        private Integer id;

        /**
         * Position
         */
        private int position;

        /**
         * Creates a new instance of MovableStub.
         *
         * @param id       ID
         * @param position position
         */
        MovableStub(final Integer id, final int position) {
            this.id = id;
            this.position = position;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public void setId(final Integer id) {
            this.id = id;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public void setPosition(final int position) {
            this.position = position;
        }

    }

    /**
     * A class represents abstract catalog service stub.
     */
    private static final class AbstractCatalogServiceStub extends AbstractCatalogService<Movable> {

        private Movable copy;

        /**
         * Creates a new instance of AbstractCatalogServiceStub.
         *
         * @param repository repository for data
         * @param cache      cache for data
         * @param key        cache key
         * @throws IllegalArgumentException if repository for data is null
         *                                  or cache for data is null
         *                                  or cache key is null
         */
        AbstractCatalogServiceStub(final JpaRepository<Movable, Integer> repository, final Cache cache, final String key) {
            super(repository, cache, key);

            this.copy = new MovableStub(10, 10);
        }

        Movable getCopy() {
            return copy;
        }

        @Override
        protected Movable getCopy(final Movable data) {
            return copy;
        }

    }

}
