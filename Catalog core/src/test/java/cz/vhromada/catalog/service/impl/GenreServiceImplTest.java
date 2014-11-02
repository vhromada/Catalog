package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * A class represents test for class {@link GenreServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GenreServiceImplTest extends ObjectGeneratorTest {

	/** Cache key for list of genres */
	private static final String GENRES_CACHE_KEY = "genres";

	/** Cache key for genre */
	private static final String GENRE_CACHE_KEY = "genre";

	/** Instance of {@link GenreDAO} */
	@Mock
	private GenreDAO genreDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache genreCache;

	/** Instance of {@link List} of {@link String}. */
	@Mock
	private List<String> mockNamesList;

	/** Instance of {@link GenreService} */
	@InjectMocks
	private GenreService genreService = new GenreServiceImpl();

	/** Test method for {@link GenreService#newData()} with cached genres. */
	@Test
	public void testNewDataWithCachedGenres() {
		final List<Genre> genres = CollectionUtils.newList(mock(Genre.class), mock(Genre.class));
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(genres));

		genreService.newData();

		for (Genre genre : genres) {
			verify(genreDAO).remove(genre);
		}
		verify(genreCache).get(GENRES_CACHE_KEY);
		verify(genreCache).clear();
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#newData()} with not cached genres. */
	@Test
	public void testNewDataWithNotCachedGenres() {
		final List<Genre> genres = CollectionUtils.newList(mock(Genre.class), mock(Genre.class));
		when(genreDAO.getGenres()).thenReturn(genres);
		when(genreCache.get(anyString())).thenReturn(null);

		genreService.newData();

		verify(genreDAO).getGenres();
		for (Genre genre : genres) {
			verify(genreDAO).remove(genre);
		}
		verify(genreCache).get(GENRES_CACHE_KEY);
		verify(genreCache).clear();
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#newData()} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.newData();
	}

	/** Test method for {@link GenreService#newData()} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.newData();
	}

	/** Test method for {@link GenreService#newData()} with exception in DAO tier. */
	@Test
	public void testNewDataWithDAOTierException() {
		doThrow(DataStorageException.class).when(genreDAO).getGenres();
		when(genreCache.get(anyString())).thenReturn(null);

		try {
			genreService.newData();
			fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).getGenres();
		verify(genreCache).get(GENRES_CACHE_KEY);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#getGenres()} with cached genres. */
	@Test
	public void testGetGenresWithCachedGenres() {
		final List<Genre> genres = CollectionUtils.newList(mock(Genre.class), mock(Genre.class));
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(genres));

		DeepAsserts.assertEquals(genres, genreService.getGenres());

		verify(genreCache).get(GENRES_CACHE_KEY);
		verifyNoMoreInteractions(genreCache);
		verifyZeroInteractions(genreDAO);
	}

	/** Test method for {@link GenreService#getGenres()} with not cached genres. */
	@Test
	public void testGetGenresWithNotCachedGenres() {
		final List<Genre> genres = CollectionUtils.newList(mock(Genre.class), mock(Genre.class));
		when(genreDAO.getGenres()).thenReturn(genres);
		when(genreCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(genres, genreService.getGenres());

		verify(genreDAO).getGenres();
		verify(genreCache).get(GENRES_CACHE_KEY);
		verify(genreCache).put(GENRES_CACHE_KEY, genres);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#getGenres()} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenresWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.getGenres();
	}

	/** Test method for {@link GenreService#getGenres()} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenresWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.getGenres();
	}

	/** Test method for {@link GenreService#getGenres()} with exception in DAO tier. */
	@Test
	public void testGetGenresWithDAOTierException() {
		doThrow(DataStorageException.class).when(genreDAO).getGenres();
		when(genreCache.get(anyString())).thenReturn(null);

		try {
			genreService.getGenres();
			fail("Can't get genres with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).getGenres();
		verify(genreCache).get(GENRES_CACHE_KEY);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with cached existing genre. */
	@Test
	public void testGetGenreWithCachedExistingGenre() {
		final Genre genre = generate(Genre.class);
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(genre));

		DeepAsserts.assertEquals(genre, genreService.getGenre(genre.getId()));

		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verifyNoMoreInteractions(genreCache);
		verifyZeroInteractions(genreDAO);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with cached not existing genre. */
	@Test
	public void testGetGenreWithCachedNotExistingGenre() {
		final int id = generate(Integer.class);
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(genreService.getGenre(id));

		verify(genreCache).get(GENRE_CACHE_KEY + id);
		verifyNoMoreInteractions(genreCache);
		verifyZeroInteractions(genreDAO);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with not cached existing genre. */
	@Test
	public void testGetGenreWithNotCachedExistingGenre() {
		final Genre genre = generate(Genre.class);
		when(genreDAO.getGenre(anyInt())).thenReturn(genre);
		when(genreCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(genre, genreService.getGenre(genre.getId()));

		verify(genreDAO).getGenre(genre.getId());
		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verify(genreCache).put(GENRE_CACHE_KEY + genre.getId(), genre);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with not cached not existing genre. */
	@Test
	public void testGetGenreWithNotCachedNotExistingGenre() {
		final int id = generate(Integer.class);
		when(genreDAO.getGenre(anyInt())).thenReturn(null);
		when(genreCache.get(anyString())).thenReturn(null);

		assertNull(genreService.getGenre(id));

		verify(genreDAO).getGenre(id);
		verify(genreCache).get(GENRE_CACHE_KEY + id);
		verify(genreCache).put(GENRE_CACHE_KEY + id, null);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenreWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.getGenre(Integer.MAX_VALUE);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenreWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.getGenre(Integer.MAX_VALUE);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with null argument. */
	@Test
	public void testGetGenreWithNullArgument() {
		try {
			genreService.getGenre(null);
			fail("Can't get genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with exception in DAO tier. */
	@Test
	public void testGetGenreWithDAOTierException() {
		doThrow(DataStorageException.class).when(genreDAO).getGenre(anyInt());
		when(genreCache.get(anyString())).thenReturn(null);

		try {
			genreService.getGenre(Integer.MAX_VALUE);
			fail("Can't get genre with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).getGenre(Integer.MAX_VALUE);
		verify(genreCache).get(GENRE_CACHE_KEY + Integer.MAX_VALUE);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#add(Genre)} with cached genres. */
	@Test
	public void testAddWithCachedGenres() {
		final Genre genre = generate(Genre.class);
		final List<Genre> genres = CollectionUtils.newList(mock(Genre.class), mock(Genre.class));
		final List<Genre> genresList = new ArrayList<>(genres);
		genresList.add(genre);
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(genres));

		genreService.add(genre);

		verify(genreDAO).add(genre);
		verify(genreCache).get(GENRES_CACHE_KEY);
		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verify(genreCache).put(GENRES_CACHE_KEY, genresList);
		verify(genreCache).put(GENRE_CACHE_KEY + genre.getId(), genre);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#add(Genre)} with not cached genres. */
	@Test
	public void testAddWithNotCachedGenres() {
		final Genre genre = generate(Genre.class);
		when(genreCache.get(anyString())).thenReturn(null);

		genreService.add(genre);

		verify(genreDAO).add(genre);
		verify(genreCache).get(GENRES_CACHE_KEY);
		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#add(Genre)} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.add(mock(Genre.class));
	}

	/** Test method for {@link GenreService#add(Genre)} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.add(mock(Genre.class));
	}

	/** Test method for {@link GenreService#add(Genre)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			genreService.add((Genre) null);
			fail("Can't add genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#add(Genre)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final Genre genre = generate(Genre.class);
		doThrow(DataStorageException.class).when(genreDAO).add(any(Genre.class));

		try {
			genreService.add(genre);
			fail("Can't add genre with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).add(genre);
		verifyNoMoreInteractions(genreDAO);
		verifyZeroInteractions(genreCache);
	}

	/** Test method for {@link GenreService#add(List)}. */
	@Test
	public void testAddList() {
		final List<String> names = CollectionUtils.newList(generate(String.class), generate(String.class));

		genreService.add(names);

		verify(genreDAO, times(names.size())).add(any(Genre.class));
		verify(genreCache).clear();
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#add(List)} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testAddListWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.add(mockNamesList);
	}

	/** Test method for {@link GenreService#add(List)} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testAddListWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.add(mockNamesList);
	}

	/** Test method for {@link GenreService#add(List)} with null argument. */
	@Test
	public void testAddListWithNullArgument() {
		try {
			final List<String> names = null;
			genreService.add(names);
			fail("Can't add genres with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#add(List)} with exception in DAO tier. */
	@Test
	public void testAddListWithDAOTierException() {
		doThrow(DataStorageException.class).when(genreDAO).add(any(Genre.class));

		try {
			genreService.add(CollectionUtils.newList(generate(String.class), generate(String.class)));
			fail("Can't add genres with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).add(any(Genre.class));
		verifyNoMoreInteractions(genreDAO);
		verifyZeroInteractions(genreCache);
	}

	/** Test method for {@link GenreService#update(Genre)}. */
	@Test
	public void testUpdate() {
		final Genre genre = generate(Genre.class);

		genreService.update(genre);

		verify(genreDAO).update(genre);
		verify(genreCache).clear();
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#update(Genre)} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.update(mock(Genre.class));
	}

	/** Test method for {@link GenreService#update(Genre)} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.update(mock(Genre.class));
	}

	/** Test method for {@link GenreService#update(Genre)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			genreService.update(null);
			fail("Can't update genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#update(Genre)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final Genre genre = generate(Genre.class);
		doThrow(DataStorageException.class).when(genreDAO).update(any(Genre.class));

		try {
			genreService.update(genre);
			fail("Can't update genre with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).update(genre);
		verifyNoMoreInteractions(genreDAO);
		verifyZeroInteractions(genreCache);
	}

	/** Test method for {@link GenreService#remove(Genre)} with cached genres. */
	@Test
	public void testRemoveWithCachedGenres() {
		final Genre genre = generate(Genre.class);
		final List<Genre> genres = CollectionUtils.newList(mock(Genre.class), mock(Genre.class));
		final List<Genre> genresList = new ArrayList<>(genres);
		genresList.add(genre);
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(genresList));

		genreService.remove(genre);

		verify(genreDAO).remove(genre);
		verify(genreCache).get(GENRES_CACHE_KEY);
		verify(genreCache).put(GENRES_CACHE_KEY, genres);
		verify(genreCache).evict(genre.getId());
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#remove(Genre)} with not cached genres. */
	@Test
	public void testRemoveWithNotCachedGenres() {
		final Genre genre = generate(Genre.class);
		when(genreCache.get(anyString())).thenReturn(null);

		genreService.remove(genre);

		verify(genreDAO).remove(genre);
		verify(genreCache).get(GENRES_CACHE_KEY);
		verify(genreCache).evict(genre.getId());
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#remove(Genre)} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.remove(mock(Genre.class));
	}

	/** Test method for {@link GenreService#remove(Genre)} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.remove(mock(Genre.class));
	}

	/** Test method for {@link GenreService#remove(Genre)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			genreService.remove(null);
			fail("Can't remove genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#remove(Genre)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final Genre genre = generate(Genre.class);
		doThrow(DataStorageException.class).when(genreDAO).remove(any(Genre.class));

		try {
			genreService.remove(genre);
			fail("Can't remove genre with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).remove(genre);
		verifyNoMoreInteractions(genreDAO);
		verifyZeroInteractions(genreCache);
	}

	/** Test method for {@link GenreService#exists(Genre)} with cached existing genre. */
	@Test
	public void testExistsWithCachedExistingGenre() {
		final Genre genre = generate(Genre.class);
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(genre));

		assertTrue(genreService.exists(genre));

		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verifyNoMoreInteractions(genreCache);
		verifyZeroInteractions(genreDAO);
	}

	/** Test method for {@link GenreService#exists(Genre)} with cached not existing genre. */
	@Test
	public void testExistsWithCachedNotExistingGenre() {
		final Genre genre = generate(Genre.class);
		when(genreCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(genreService.exists(genre));

		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verifyNoMoreInteractions(genreCache);
		verifyZeroInteractions(genreDAO);
	}

	/** Test method for {@link GenreService#exists(Genre)} with not cached existing genre. */
	@Test
	public void testExistsWithNotCachedExistingGenre() {
		final Genre genre = generate(Genre.class);
		when(genreDAO.getGenre(anyInt())).thenReturn(genre);
		when(genreCache.get(anyString())).thenReturn(null);

		assertTrue(genreService.exists(genre));

		verify(genreDAO).getGenre(genre.getId());
		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verify(genreCache).put(GENRE_CACHE_KEY + genre.getId(), genre);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#exists(Genre)} with not cached not existing genre. */
	@Test
	public void testExistsWithNotCachedNotExistingGenre() {
		final Genre genre = generate(Genre.class);
		when(genreDAO.getGenre(anyInt())).thenReturn(null);
		when(genreCache.get(anyString())).thenReturn(null);

		assertFalse(genreService.exists(genre));

		verify(genreDAO).getGenre(genre.getId());
		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verify(genreCache).put(GENRE_CACHE_KEY + genre.getId(), null);
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#exists(Genre)} with not set DAO for genres. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGenreDAO() {
		((GenreServiceImpl) genreService).setGenreDAO(null);
		genreService.exists(mock(Genre.class));
	}

	/** Test method for {@link GenreService#exists(Genre)} with not set cache for genres. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGenreCache() {
		((GenreServiceImpl) genreService).setGenreCache(null);
		genreService.exists(mock(Genre.class));
	}

	/** Test method for {@link GenreService#exists(Genre)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		try {
			genreService.exists(null);
			fail("Can't exists genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreDAO, genreCache);
	}

	/** Test method for {@link GenreService#exists(Genre)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		final Genre genre = generate(Genre.class);
		doThrow(DataStorageException.class).when(genreDAO).getGenre(anyInt());
		when(genreCache.get(anyString())).thenReturn(null);

		try {
			genreService.exists(genre);
			fail("Can't exists genre with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(genreDAO).getGenre(genre.getId());
		verify(genreCache).get(GENRE_CACHE_KEY + genre.getId());
		verifyNoMoreInteractions(genreDAO, genreCache);
	}

}
