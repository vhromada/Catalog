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

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CollectionUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents AbstractServiceTest.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractServiceTest<T extends Movable> {

    /**
     * ID
     */
    private static final Integer ID = 5;

    /**
     * Instance of {@link Cache}
     */
    @Mock
    private Cache cache;

    /**
     * Instance of {@link CatalogService}
     */
    private CatalogService<T> catalogService;

    /**
     * Instance of {@link JpaRepository}
     */
    private JpaRepository<T, Integer> repository;

    /**
     * Data list
     */
    private List<T> dataList;

    /**
     * Initializes data.
     */
    @Before
    public void setUp() {
        repository = getRepository();
        catalogService = getCatalogService();
        dataList = CollectionUtils.newList(getItem1(), getItem2());
        when(repository.findAll()).thenReturn(dataList);
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

        final List<T> data = catalogService.getAll();

        assertNotNull(data);
        assertEquals(dataList, data);

        verify(cache).get(getCacheKey());
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#getAll()} with not cached data.
     */
    @Test
    public void testGets_NotCachedData() {
        when(cache.get(anyString())).thenReturn(null);

        final List<T> data = catalogService.getAll();

        assertNotNull(data);
        assertEquals(dataList, data);

        verify(repository).findAll();
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with cached existing data.
     */
    @Test
    public void testGet_CachedExistingData() {
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        final T data = catalogService.get(dataList.get(0).getId());

        assertNotNull(data);
        assertEquals(dataList.get(0), data);

        verify(cache).get(getCacheKey());
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with cached not existing data.
     */
    @Test
    public void testGet_CachedNotExistingData() {
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        final T data = catalogService.get(Integer.MAX_VALUE);

        assertNull(data);

        verify(cache).get(getCacheKey());
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with not cached existing data.
     */
    @Test
    public void testGet_NotCachedExistingData() {
        when(cache.get(anyString())).thenReturn(null);

        final T data = catalogService.get(dataList.get(0).getId());

        assertNotNull(data);
        assertEquals(dataList.get(0), data);

        verify(repository).findAll();
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with not cached not existing data.
     */
    @Test
    public void testGet_NotCachedNotExistingData() {
        when(cache.get(anyString())).thenReturn(null);

        final T data = catalogService.get(Integer.MAX_VALUE);

        assertNull(data);

        verify(repository).findAll();
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
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
     * Test method for {@link CatalogService#add(T)} with cached data.
     */
    @Test
    public void testAdd_CachedData() {
        final T data = getAddItem();

        when(repository.save(any(getItemClass()))).thenAnswer(setId(ID));
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.add(data);

        assertEquals(ID, data.getId());
        assertEquals(ID - 1, data.getPosition());
        assertEquals(3, dataList.size());
        assertEquals(data, dataList.get(2));

        verify(repository, times(2)).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#add(T)} with not cached data.
     */
    @Test
    public void testAdd_NotCachedData() {
        final T data = getAddItem();

        when(repository.save(any(getItemClass()))).thenAnswer(setId(ID));
        when(cache.get(anyString())).thenReturn(null);

        catalogService.add(data);

        assertEquals(ID, data.getId());
        assertEquals(ID - 1, data.getPosition());
        assertEquals(3, dataList.size());
        assertEquals(data, dataList.get(2));

        verify(repository).findAll();
        verify(repository, times(2)).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#add(T)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        catalogService.add(null);
    }

    /**
     * Test method for {@link CatalogService#update(T)} with cached data.
     */
    @Test
    public void testUpdate_CachedData() {
        final T data = dataList.get(0);
        data.setPosition(10);

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.update(data);

        assertEquals(2, dataList.size());
        assertEquals(data, dataList.get(0));

        verify(repository).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#update(T)} with not cached data.
     */
    @Test
    public void testUpdate_NotCachedData() {
        final T data = dataList.get(0);
        data.setPosition(10);

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.update(data);

        assertEquals(2, dataList.size());
        assertEquals(data, dataList.get(0));

        verify(repository).findAll();
        verify(repository).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#update(T)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        catalogService.update(null);
    }

    /**
     * Test method for {@link CatalogService#remove(T)} with cached data.
     */
    @Test
    public void testRemove_CachedData() {
        final T data = dataList.get(0);

        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.remove(data);

        assertEquals(1, dataList.size());
        assertFalse(dataList.contains(data));

        verify(repository).delete(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#remove(T)} with not cached data.
     */
    @Test
    public void testRemove_NotCachedData() {
        final T data = dataList.get(0);

        when(cache.get(anyString())).thenReturn(null);

        catalogService.remove(data);

        assertEquals(1, dataList.size());
        assertFalse(dataList.contains(data));

        verify(repository).findAll();
        verify(repository).delete(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#remove(T)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        catalogService.remove(null);
    }

    /**
     * Test method for {@link CatalogService#duplicate(T)} with cached data.
     */
    @Test
    public void testDuplicate_CachedData() {
        final T copy = getCopyItem();
        final ArgumentCaptor<T> copyArgumentCaptor = ArgumentCaptor.forClass(getItemClass());

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.duplicate(dataList.get(0));

        assertEquals(3, dataList.size());
        assertDataDeepEquals(copy, dataList.get(2));

        verify(repository).save(copyArgumentCaptor.capture());
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);

        final T copyArgument = copyArgumentCaptor.getValue();
        assertDataDeepEquals(copy, copyArgument);
    }

    /**
     * Test method for {@link CatalogService#duplicate(T)} with not cached data.
     */
    @Test
    public void testDuplicate_NotCachedData() {
        final T copy = getCopyItem();
        final ArgumentCaptor<T> copyArgumentCaptor = ArgumentCaptor.forClass(getItemClass());

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.duplicate(dataList.get(0));

        assertEquals(3, dataList.size());
        assertDataDeepEquals(copy, dataList.get(2));

        verify(repository).findAll();
        verify(repository).save(copyArgumentCaptor.capture());
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);

        final T copyArgument = copyArgumentCaptor.getValue();
        assertDataDeepEquals(copy, copyArgument);
    }

    /**
     * Test method for {@link CatalogService#duplicate(T)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        catalogService.duplicate(null);
    }

    /**
     * Test method for {@link CatalogService#moveUp(T)} with cached data.
     */
    @Test
    public void testMoveUp_CachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.moveUp(data2);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveUp(T)} with not cached data.
     */
    @Test
    public void testMoveUp_NotCachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.moveUp(data2);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).findAll();
        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveUp(T)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        catalogService.moveUp(null);
    }

    /**
     * Test method for {@link CatalogService#moveDown(T)} with cached data.
     */
    @Test
    public void testMoveDown_CachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.moveDown(data1);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveDown(T)} with not cached data.
     */
    @Test
    public void testMoveDown_NotCachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.moveDown(data1);

        assertEquals(position2, data1.getPosition());
        assertEquals(position1, data2.getPosition());

        verify(repository).findAll();
        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveDown(T)} with null argument.
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
        when(repository.save(anyListOf(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.updatePositions();

        for (int i = 0; i < dataList.size(); i++) {
            final T data = dataList.get(i);
            assertEquals(i, data.getPosition());
        }

        verify(repository).save(dataList);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#updatePositions()} with not cached data.
     */
    @Test
    public void testUpdatePositions_NotCachedData() {
        when(repository.save(anyListOf(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(anyString())).thenReturn(null);

        catalogService.updatePositions();

        for (int i = 0; i < dataList.size(); i++) {
            final T data = dataList.get(i);
            assertEquals(i, data.getPosition());
        }

        verify(repository).findAll();
        verify(repository).save(dataList);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Returns instance of {@link Cache}.
     *
     * @return instance of {@link Cache}
     */
    protected Cache getCache() {
        return cache;
    }

    /**
     * Returns instance of {@link JpaRepository}.
     *
     * @return instance of {@link JpaRepository}
     */
    protected abstract JpaRepository<T, Integer> getRepository();

    /**
     * Returns instance of {@link CatalogService}.
     *
     * @return instance of {@link CatalogService}
     */
    protected abstract CatalogService<T> getCatalogService();

    /**
     * Returns cache key.
     *
     * @return cache key
     */
    protected abstract String getCacheKey();

    /**
     * Returns 1st item in data list.
     *
     * @return 1st item in data list
     */
    protected abstract T getItem1();

    /**
     * Returns 2nd item in data list.
     *
     * @return 2nd item in data list
     */
    protected abstract T getItem2();

    /**
     * Returns add item
     *
     * @return add item
     */
    protected abstract T getAddItem();

    /**
     * Returns copy item.
     *
     * @return copy item
     */
    protected abstract T getCopyItem();

    /**
     * Returns item class.
     *
     * @return item class
     */
    protected abstract Class<T> getItemClass();

    /**
     * Asserts data deep equals.
     *
     * @param expected expected data
     * @param actual   actual data
     */
    protected abstract void assertDataDeepEquals(final T expected, final T actual);

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private Answer<T> setId(final Integer id) {
        return invocation -> {
            @SuppressWarnings("unchecked")
            final T movable = (T) invocation.getArguments()[0];
            movable.setId(id);

            return movable;
        };
    }

}

