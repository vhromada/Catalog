package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.convert.ConversionService;

/**
 * A class represents test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GenreFacadeImplTest extends ObjectGeneratorTest {

	/** Instance of {@link GenreService} */
	@Mock
	private GenreService genreService;

	/** Instance of {@link ConversionService} */
	@Mock
	private ConversionService conversionService;

	/** Instance of {@link GenreTOValidator} */
	@Mock
	private GenreTOValidator genreTOValidator;

	/** Genre names */
	@Mock
	private List<String> genreNames;

	/** Instance of {@link GenreFacade} */
	@InjectMocks
	private GenreFacade genreFacade = new GenreFacadeImpl();

	/** Test method for {@link GenreFacade#newData()}. */
	@Test
	public void testNewData() {
		genreFacade.newData();

		verify(genreService).newData();
		verifyNoMoreInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#newData()} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.newData();
	}

	/** Test method for {@link GenreFacade#newData()} with exception in service tier. */
	@Test
	public void testNewDataWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(genreService).newData();

		try {
			genreFacade.newData();
			fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).newData();
		verifyNoMoreInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#getGenres()}. */
	@Test
	public void testGetGenres() {
		final List<Genre> genres = CollectionUtils.newList(generate(Genre.class), generate(Genre.class));
		final List<GenreTO> genresList = CollectionUtils.newList(generate(GenreTO.class), generate(GenreTO.class));
		when(genreService.getGenres()).thenReturn(genres);
		for (int i = 0; i < genres.size(); i++) {
			final Genre genre = genres.get(i);
			when(conversionService.convert(genre, GenreTO.class)).thenReturn(genresList.get(i));
		}

		DeepAsserts.assertEquals(genresList, genreFacade.getGenres());

		verify(genreService).getGenres();
		for (Genre genre : genres) {
			verify(conversionService).convert(genre, GenreTO.class);
		}
		verifyNoMoreInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#getGenres()} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenresWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.getGenres();
	}

	/** Test method for {@link GenreFacade#getGenres()} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenresWithNotSetConversionService() {
		((GenreFacadeImpl) genreFacade).setConversionService(null);
		genreFacade.getGenres();
	}

	/** Test method for {@link GenreFacade#getGenres()} with exception in service tier. */
	@Test
	public void testGetGenresWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(genreService).getGenres();

		try {
			genreFacade.getGenres();
			fail("Can't get genres with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).getGenres();
		verifyNoMoreInteractions(genreService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)} with existing genre. */
	@Test
	public void testGetGenreWithExistingGenre() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		when(genreService.getGenre(anyInt())).thenReturn(genre);
		when(conversionService.convert(any(Genre.class), eq(GenreTO.class))).thenReturn(genreTO);

		DeepAsserts.assertEquals(genreTO, genreFacade.getGenre(genreTO.getId()));

		verify(genreService).getGenre(genreTO.getId());
		verify(conversionService).convert(genre, GenreTO.class);
		verifyNoMoreInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)} with not existing genre. */
	@Test
	public void testGetGenreWithNotExistingGenre() {
		when(genreService.getGenre(anyInt())).thenReturn(null);
		when(conversionService.convert(any(Genre.class), eq(GenreTO.class))).thenReturn(null);

		assertNull(genreFacade.getGenre(Integer.MAX_VALUE));

		verify(genreService).getGenre(Integer.MAX_VALUE);
		verify(conversionService).convert(null, GenreTO.class);
		verifyNoMoreInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenreWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.getGenre(Integer.MAX_VALUE);
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenreWithNotSetConversionService() {
		((GenreFacadeImpl) genreFacade).setConversionService(null);
		genreFacade.getGenre(Integer.MAX_VALUE);
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)} with null argument. */
	@Test
	public void testGetGenreWithNullArgument() {
		try {
			genreFacade.getGenre(null);
			fail("Can't get genre with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)} with exception in service tier. */
	@Test
	public void testGetGenreWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(genreService).getGenre(anyInt());

		try {
			genreFacade.getGenre(Integer.MAX_VALUE);
			fail("Can't get genre with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).getGenre(Integer.MAX_VALUE);
		verifyNoMoreInteractions(genreService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)}. */
	@Test
	public void testAdd() {
		final Genre genre = generate(Genre.class);
		genre.setId(null);
		final GenreTO genreTO = generate(GenreTO.class);
		genreTO.setId(null);
		final int id = generate(Integer.class);
		doAnswer(setGenreId(id)).when(genreService).add(any(Genre.class));
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		genreFacade.add(genreTO);
		DeepAsserts.assertEquals(id, genre.getId());

		verify(genreService).add(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateNewGenreTO(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.add(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((GenreFacadeImpl) genreFacade).setConversionService(null);
		genreFacade.add(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGenreTOValidator() {
		((GenreFacadeImpl) genreFacade).setGenreTOValidator(null);
		genreFacade.add(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(genreTOValidator).validateNewGenreTO(any(GenreTO.class));

		try {
			genreFacade.add((GenreTO) null);
			fail("Can't add genre with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(genreTOValidator).validateNewGenreTO(null);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final GenreTO genre = generate(GenreTO.class);
		genre.setId(null);
		doThrow(ValidationException.class).when(genreTOValidator).validateNewGenreTO(any(GenreTO.class));

		try {
			genreFacade.add(genre);
			fail("Can't add genre with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(genreTOValidator).validateNewGenreTO(genre);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final Genre genre = generate(Genre.class);
		genre.setId(null);
		final GenreTO genreTO = generate(GenreTO.class);
		genreTO.setId(null);
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		try {
			genreFacade.add(genreTO);
			fail("Can't add genre with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).add(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateNewGenreTO(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final Genre genre = generate(Genre.class);
		genre.setId(null);
		final GenreTO genreTO = generate(GenreTO.class);
		genreTO.setId(null);
		doThrow(ServiceOperationException.class).when(genreService).add(any(Genre.class));
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		try {
			genreFacade.add(genreTO);
			fail("Can't add genre with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).add(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateNewGenreTO(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#add(List)}. */
	@Test
	public void testAddList() {
		final List<String> genres = CollectionUtils.newList(generate(String.class), generate(String.class));

		genreFacade.add(genres);

		verify(genreService).add(genres);
		verifyNoMoreInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#add(List)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testAddListWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.add(genreNames);
	}

	/** Test method for {@link GenreFacade#add(List)} with null argument. */
	@Test
	public void testAddListWithNullArgument() {
		try {
			genreFacade.add((List<String>) null);
			fail("Can't add genre with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#add(List)} with bad argument. */
	@Test
	public void testAddListWithBadArgument() {
		try {
			genreFacade.add(CollectionUtils.newList(generate(String.class), null));
			fail("Can't add genre with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verifyZeroInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#add(List)} with exception in service tier. */
	@Test
	public void testAddListWithServiceTierException() {
		final List<String> genres = CollectionUtils.newList(generate(String.class), generate(String.class));
		doThrow(ServiceOperationException.class).when(genreService).add(anyListOf(String.class));

		try {
			genreFacade.add(genres);
			fail("Can't add list of genre names with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).add(genres);
		verifyNoMoreInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)}. */
	@Test
	public void testUpdate() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		when(genreService.exists(any(Genre.class))).thenReturn(true);
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		genreFacade.update(genreTO);

		verify(genreService).exists(genre);
		verify(genreService).update(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateExistingGenreTO(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.update(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((GenreFacadeImpl) genreFacade).setConversionService(null);
		genreFacade.update(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGenreTOValidator() {
		((GenreFacadeImpl) genreFacade).setGenreTOValidator(null);
		genreFacade.update(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(genreTOValidator).validateExistingGenreTO(any(GenreTO.class));

		try {
			genreFacade.update(null);
			fail("Can't update genre with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(genreTOValidator).validateExistingGenreTO(null);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final GenreTO genre = generate(GenreTO.class);
		doThrow(ValidationException.class).when(genreTOValidator).validateExistingGenreTO(any(GenreTO.class));

		try {
			genreFacade.update(genre);
			fail("Can't update genre with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(genreTOValidator).validateExistingGenreTO(genre);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		when(genreService.exists(any(Genre.class))).thenReturn(false);
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		try {
			genreFacade.update(genreTO);
			fail("Can't update genre with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(genreService).exists(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateExistingGenreTO(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		doThrow(ServiceOperationException.class).when(genreService).exists(any(Genre.class));
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		try {
			genreFacade.update(genreTO);
			fail("Can't update genre with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).exists(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateExistingGenreTO(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)}. */
	@Test
	public void testRemove() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		when(genreService.getGenre(anyInt())).thenReturn(genre);

		genreFacade.remove(genreTO);

		verify(genreService).getGenre(genreTO.getId());
		verify(genreService).remove(genre);
		verify(genreTOValidator).validateGenreTOWithId(genreTO);
		verifyNoMoreInteractions(genreService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.remove(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGenreTOValidator() {
		((GenreFacadeImpl) genreFacade).setGenreTOValidator(null);
		genreFacade.remove(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

		try {
			genreFacade.remove(null);
			fail("Can't remove genre with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(genreTOValidator).validateGenreTOWithId(null);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final GenreTO genre = generate(GenreTO.class);
		doThrow(ValidationException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

		try {
			genreFacade.remove(genre);
			fail("Can't remove genre with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(genreTOValidator).validateGenreTOWithId(genre);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final GenreTO genre = generate(GenreTO.class);
		when(genreService.getGenre(anyInt())).thenReturn(null);

		try {
			genreFacade.remove(genre);
			fail("Can't remove genre with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(genreService).getGenre(genre.getId());
		verify(genreTOValidator).validateGenreTOWithId(genre);
		verifyNoMoreInteractions(genreService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final GenreTO genre = generate(GenreTO.class);
		doThrow(ServiceOperationException.class).when(genreService).getGenre(anyInt());

		try {
			genreFacade.remove(genre);
			fail("Can't remove genre with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).getGenre(genre.getId());
		verify(genreTOValidator).validateGenreTOWithId(genre);
		verifyNoMoreInteractions(genreService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)}. */
	@Test
	public void testDuplicate() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		when(genreService.getGenre(anyInt())).thenReturn(genre);

		genreFacade.duplicate(genreTO);

		verify(genreService).getGenre(genreTO.getId());
		verify(genreService).add(any(Genre.class));
		verify(genreTOValidator).validateGenreTOWithId(genreTO);
		verifyNoMoreInteractions(genreService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.duplicate(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetGenreTOValidator() {
		((GenreFacadeImpl) genreFacade).setGenreTOValidator(null);
		genreFacade.duplicate(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

		try {
			genreFacade.duplicate(null);
			fail("Can't duplicate genre with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(genreTOValidator).validateGenreTOWithId(null);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final GenreTO genre = generate(GenreTO.class);
		doThrow(ValidationException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

		try {
			genreFacade.duplicate(genre);
			fail("Can't duplicate genre with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(genreTOValidator).validateGenreTOWithId(genre);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService);
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final GenreTO genre = generate(GenreTO.class);
		when(genreService.getGenre(anyInt())).thenReturn(null);

		try {
			genreFacade.duplicate(genre);
			fail("Can't duplicate genre with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(genreService).getGenre(genre.getId());
		verify(genreTOValidator).validateGenreTOWithId(genre);
		verifyNoMoreInteractions(genreService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final GenreTO genre = generate(GenreTO.class);
		doThrow(ServiceOperationException.class).when(genreService).getGenre(anyInt());

		try {
			genreFacade.duplicate(genre);
			fail("Can't duplicate genre with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).getGenre(genre.getId());
		verify(genreTOValidator).validateGenreTOWithId(genre);
		verifyNoMoreInteractions(genreService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with existing genre. */
	@Test
	public void testExistsWithExistingGenre() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		when(genreService.exists(any(Genre.class))).thenReturn(true);
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		assertTrue(genreFacade.exists(genreTO));

		verify(genreService).exists(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateGenreTOWithId(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with not existing genre. */
	@Test
	public void testExistsWithNotExistingGenre() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		when(genreService.exists(any(Genre.class))).thenReturn(false);
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		assertFalse(genreFacade.exists(genreTO));

		verify(genreService).exists(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateGenreTOWithId(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGenreService() {
		((GenreFacadeImpl) genreFacade).setGenreService(null);
		genreFacade.exists(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((GenreFacadeImpl) genreFacade).setConversionService(null);
		genreFacade.exists(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGenreTOValidator() {
		((GenreFacadeImpl) genreFacade).setGenreTOValidator(null);
		genreFacade.exists(mock(GenreTO.class));
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

		try {
			genreFacade.exists(null);
			fail("Can't exists genre with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(genreTOValidator).validateGenreTOWithId(null);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final GenreTO genre = generate(GenreTO.class);
		doThrow(ValidationException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

		try {
			genreFacade.exists(genre);
			fail("Can't exists genre with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(genreTOValidator).validateGenreTOWithId(genre);
		verifyNoMoreInteractions(genreTOValidator);
		verifyZeroInteractions(genreService, conversionService);
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final Genre genre = generate(Genre.class);
		final GenreTO genreTO = generate(GenreTO.class);
		doThrow(ServiceOperationException.class).when(genreService).exists(any(Genre.class));
		when(conversionService.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(genre);

		try {
			genreFacade.exists(genreTO);
			fail("Can't exists genre with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).exists(genre);
		verify(conversionService).convert(genreTO, Genre.class);
		verify(genreTOValidator).validateGenreTOWithId(genreTO);
		verifyNoMoreInteractions(genreService, conversionService, genreTOValidator);
	}

	/**
	 * Sets genre's ID.
	 *
	 * @param id ID
	 * @return mocked answer
	 */
	private Answer<Void> setGenreId(final Integer id) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Genre genre = (Genre) invocation.getArguments()[0];
				genre.setId(id);
				return null;
			}

		};

	}

}
