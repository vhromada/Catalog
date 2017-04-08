package cz.vhromada.catalog.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;

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
 * An abstract class represents test for service.
 *
 * @param <T> type of data
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
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void newData_CachedData() {
        catalogService.newData();

        verify(repository).deleteAll();
        verify(cache).clear();
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#getAll()} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void getAll_CachedData() {
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        final List<T> data = catalogService.getAll();

        assertThat(data, is(notNullValue()));
        assertThat(data, is(dataList));

        verify(cache).get(getCacheKey());
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#getAll()} with not cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void getAll_NotCachedData() {
        when(cache.get(any(String.class))).thenReturn(null);

        final List<T> data = catalogService.getAll();

        assertThat(data, is(notNullValue()));
        assertThat(data, is(dataList));

        verify(repository).findAll();
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with cached existing data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void get_CachedExistingData() {
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        final T data = catalogService.get(dataList.get(0).getId());

        assertThat(data, is(notNullValue()));
        assertThat(data, is(dataList.get(0)));

        verify(cache).get(getCacheKey());
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with cached not existing data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void get_CachedNotExistingData() {
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        final T data = catalogService.get(Integer.MAX_VALUE);

        assertThat(data, is(nullValue()));

        verify(cache).get(getCacheKey());
        verifyNoMoreInteractions(cache);
        verifyZeroInteractions(repository);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with not cached existing data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void get_NotCachedExistingData() {
        when(cache.get(any(String.class))).thenReturn(null);

        final T data = catalogService.get(dataList.get(0).getId());

        assertThat(data, is(notNullValue()));
        assertThat(data, is(dataList.get(0)));

        verify(repository).findAll();
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with not cached not existing data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void get_NotCachedNotExistingData() {
        when(cache.get(any(String.class))).thenReturn(null);

        final T data = catalogService.get(Integer.MAX_VALUE);

        assertThat(data, is(nullValue()));

        verify(repository).findAll();
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#get(Integer)} with null data.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void get_NullData() {
        catalogService.get(null);
    }

    /**
     * Test method for {@link CatalogService#add(T)} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void add_CachedData() {
        final T data = getAddItem();

        when(repository.save(any(getItemClass()))).thenAnswer(setId(ID, getItemClass()));
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.add(data);

        assertThat(data.getId(), is(ID));
        assertThat(data.getPosition(), is(ID - 1));
        assertThat(dataList.size(), is(3));
        assertThat(dataList.get(2), is(data));

        verify(repository, times(2)).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#add(T)} with not cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void add_NotCachedData() {
        final T data = getAddItem();

        when(repository.save(any(getItemClass()))).thenAnswer(setId(ID, getItemClass()));
        when(cache.get(any(String.class))).thenReturn(null);

        catalogService.add(data);

        assertThat(data.getId(), is(ID));
        assertThat(data.getPosition(), is(ID - 1));
        assertThat(dataList.size(), is(3));
        assertThat(dataList.get(2), is(data));

        verify(repository).findAll();
        verify(repository, times(2)).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#add(T)} with null data.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void add_NullData() {
        catalogService.add(null);
    }

    /**
     * Test method for {@link CatalogService#update(T)} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void update_CachedData() {
        final T data = dataList.get(0);
        data.setPosition(10);

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.update(data);

        assertThat(dataList.size(), is(2));
        assertThat(dataList.get(0), is(data));

        verify(repository).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#update(T)} with not cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void update_NotCachedData() {
        final T data = dataList.get(0);
        data.setPosition(10);

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(null);

        catalogService.update(data);

        assertThat(dataList.size(), is(2));
        assertThat(dataList.get(0), is(data));

        verify(repository).findAll();
        verify(repository).save(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#update(T)} with null data.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void update_NullData() {
        catalogService.update(null);
    }

    /**
     * Test method for {@link CatalogService#remove(T)} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void remove_CachedData() {
        final T data = dataList.get(0);

        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.remove(data);

        assertThat(dataList.size(), is(1));
        assertThat(dataList.contains(data), is(false));

        verify(repository).delete(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#remove(T)} with not cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void remove_NotCachedData() {
        final T data = dataList.get(0);

        when(cache.get(any(String.class))).thenReturn(null);

        catalogService.remove(data);

        assertThat(dataList.size(), is(1));
        assertThat(dataList.contains(data), is(false));

        verify(repository).findAll();
        verify(repository).delete(data);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#remove(T)} with null data.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void remove_NullData() {
        catalogService.remove(null);
    }

    /**
     * Test method for {@link CatalogService#duplicate(T)} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void duplicate_CachedData() {
        final T copy = getCopyItem();
        final ArgumentCaptor<T> copyArgumentCaptor = ArgumentCaptor.forClass(getItemClass());

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.duplicate(dataList.get(0));

        assertThat(dataList.size(), is(3));
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
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void duplicate_NotCachedData() {
        final T copy = getCopyItem();
        final ArgumentCaptor<T> copyArgumentCaptor = ArgumentCaptor.forClass(getItemClass());

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(null);

        catalogService.duplicate(dataList.get(0));

        assertThat(dataList.size(), is(3));
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
     * Test method for {@link CatalogService#duplicate(T)} with null data.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void duplicate_NullData() {
        catalogService.duplicate(null);
    }

    /**
     * Test method for {@link CatalogService#moveUp(T)} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void moveUp_CachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.moveUp(data2);

        assertThat(data1.getPosition(), is(position2));
        assertThat(data2.getPosition(), is(position1));

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
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void moveUp_NotCachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(null);

        catalogService.moveUp(data2);

        assertThat(data1.getPosition(), is(position2));
        assertThat(data2.getPosition(), is(position1));

        verify(repository).findAll();
        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveUp(T)} with null data.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void moveUp_NullData() {
        catalogService.moveUp(null);
    }

    /**
     * Test method for {@link CatalogService#moveDown(T)} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void moveDown_CachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.moveDown(data1);

        assertThat(data1.getPosition(), is(position2));
        assertThat(data2.getPosition(), is(position1));

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
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void moveDown_NotCachedData() {
        final T data1 = dataList.get(0);
        final int position1 = data1.getPosition();
        final T data2 = dataList.get(1);
        final int position2 = data2.getPosition();

        when(repository.save(any(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(null);

        catalogService.moveDown(data1);

        assertThat(data1.getPosition(), is(position2));
        assertThat(data2.getPosition(), is(position1));

        verify(repository).findAll();
        verify(repository).save(data1);
        verify(repository).save(data2);
        verify(cache).get(getCacheKey());
        verify(cache).put(getCacheKey(), dataList);
        verifyNoMoreInteractions(repository, cache);
    }

    /**
     * Test method for {@link CatalogService#moveDown(T)} with null data.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void moveDown_NullData() {
        catalogService.moveDown(null);
    }

    /**
     * Test method for {@link CatalogService#updatePositions()} with cached data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void updatePositions_CachedData() {
        when(repository.save(anyListOf(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(new SimpleValueWrapper(dataList));

        catalogService.updatePositions();

        for (int i = 0; i < dataList.size(); i++) {
            final T data = dataList.get(i);
            assertThat(data.getPosition(), is(i));
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
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void updatePositions_NotCachedData() {
        when(repository.save(anyListOf(getItemClass()))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(cache.get(any(String.class))).thenReturn(null);

        catalogService.updatePositions();

        for (int i = 0; i < dataList.size(); i++) {
            final T data = dataList.get(i);
            assertThat(data.getPosition(), is(i));
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
    protected abstract void assertDataDeepEquals(T expected, T actual);

    /**
     * Sets ID.
     *
     * @param id    ID
     * @param clazz class of mocked answer
     * @return mocked answer
     */
    private Answer<T> setId(final Integer id, final Class<T> clazz) {
        return invocation -> {
            final T movable = invocation.getArgumentAt(0, clazz);
            movable.setId(id);

            return movable;
        };
    }

}
