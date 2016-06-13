package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.entities.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GenreFacadeImplTest {

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<Genre> genreService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link GenreTOValidator}
     */
    @Mock
    private GenreTOValidator genreTOValidator;

    /**
     * Instance of {@link GenreFacade}
     */
    private GenreFacade genreFacade;

    /**
     * Initializes facade for genres.
     */
    @Before
    public void setUp() {
        genreFacade = new GenreFacadeImpl(genreService, converter, genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, GenreTOValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreService() {
        new GenreFacadeImpl(null, converter, genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, GenreTOValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new GenreFacadeImpl(genreService, null, genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, GenreTOValidator)} with null validator for TO for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreTOValidator() {
        new GenreFacadeImpl(genreService, converter, null);
    }

    /**
     * Test method for {@link GenreFacade#newData()}.
     */
    @Test
    public void testNewData() {
        genreFacade.newData();

        verify(genreService).newData();
        verifyNoMoreInteractions(genreService);
        verifyZeroInteractions(converter, genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenres()}.
     */
    @Test
    public void testGetGenres() {
        final List<Genre> genreList = CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(2));
        final List<GenreTO> expectedGenres = CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(2));

        when(genreService.getAll()).thenReturn(genreList);
        when(converter.convertCollection(anyListOf(Genre.class), eq(GenreTO.class))).thenReturn(expectedGenres);

        final List<GenreTO> genres = genreFacade.getGenres();

        assertNotNull(genres);
        assertEquals(expectedGenres, genres);

        verify(genreService).getAll();
        verify(converter).convertCollection(genreList, GenreTO.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)} with existing genre.
     */
    @Test
    public void testGetGenre_ExistingGenre() {
        final Genre entityGenre = GenreUtils.newGenre(1);
        final GenreTO expectedGenre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(entityGenre);
        when(converter.convert(any(Genre.class), eq(GenreTO.class))).thenReturn(expectedGenre);

        final GenreTO genre = genreFacade.getGenre(1);

        assertNotNull(genre);
        assertEquals(expectedGenre, genre);

        verify(genreService).get(1);
        verify(converter).convert(entityGenre, GenreTO.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)} with not existing genre.
     */
    @Test
    public void testGetGenre_NotExistingGenre() {
        when(genreService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(Genre.class), eq(GenreTO.class))).thenReturn(null);

        assertNull(genreFacade.getGenre(Integer.MAX_VALUE));

        verify(genreService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, GenreTO.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGenre_NullArgument() {
        genreFacade.getGenre(null);
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)}.
     */
    @Test
    public void testAdd() {
        final Genre entityGenre = GenreUtils.newGenre(null);
        final GenreTO genre = GenreUtils.newGenreTO(null);

        when(converter.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(entityGenre);

        genreFacade.add(genre);

        verify(genreService).add(entityGenre);
        verify(converter).convert(genre, Genre.class);
        verify(genreTOValidator).validateNewGenreTO(genre);
        verifyNoMoreInteractions(genreService, converter, genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreTOValidator).validateNewGenreTO(any(GenreTO.class));

        genreFacade.add((GenreTO) null);
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(genreTOValidator).validateNewGenreTO(any(GenreTO.class));

        genreFacade.add(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#add(List)}.
     */
    @Test
    public void testAddList() {
        final Genre genre = GenreUtils.newGenre(null);
        final ArgumentCaptor<Genre> genreArgumentCaptor = ArgumentCaptor.forClass(Genre.class);

        genreFacade.add(CollectionUtils.newList(genre.getName()));

        verify(genreService).add(genreArgumentCaptor.capture());
        verifyNoMoreInteractions(genreService);

        final Genre addedGenre = genreArgumentCaptor.getValue();
        GenreUtils.assertGenreDeepEquals(genre, addedGenre);
    }

    /**
     * Test method for {@link GenreFacade#add(List)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddList_NullArgument() {
        genreFacade.add((List<String>) null);
    }

    /**
     * Test method for {@link GenreFacade#add(List)} with bad argument.
     */
    @Test(expected = ValidationException.class)
    public void testAddListWithBadArgument() {
        genreFacade.add(CollectionUtils.newList("Genre", null));
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)}.
     */
    @Test
    public void testUpdate() {
        final Genre entityGenre = GenreUtils.newGenre(1);
        final GenreTO genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(entityGenre);
        when(converter.convert(any(GenreTO.class), eq(Genre.class))).thenReturn(entityGenre);

        genreFacade.update(genre);

        verify(genreService).get(1);
        verify(genreService).update(entityGenre);
        verify(converter).convert(genre, Genre.class);
        verify(genreTOValidator).validateExistingGenreTO(genre);
        verifyNoMoreInteractions(genreService, converter, genreTOValidator);
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreTOValidator).validateExistingGenreTO(any(GenreTO.class));

        genreFacade.update(null);
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(genreTOValidator).validateExistingGenreTO(any(GenreTO.class));

        genreFacade.update(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.update(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)}.
     */
    @Test
    public void testRemove() {
        final Genre entityGenre = GenreUtils.newGenre(1);
        final GenreTO genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(entityGenre);

        genreFacade.remove(genre);

        verify(genreService).get(1);
        verify(genreService).remove(entityGenre);
        verify(genreTOValidator).validateGenreTOWithId(genre);
        verifyNoMoreInteractions(genreService, genreTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.remove(null);
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.remove(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.remove(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)}.
     */
    @Test
    public void testDuplicate() {
        final Genre entityGenre = GenreUtils.newGenre(1);
        final GenreTO genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(entityGenre);

        genreFacade.duplicate(genre);

        verify(genreService).get(1);
        verify(genreService).duplicate(entityGenre);
        verify(genreTOValidator).validateGenreTOWithId(genre);
        verifyNoMoreInteractions(genreService, genreTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.duplicate(null);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.duplicate(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.duplicate(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(GenreTO)}.
     */
    @Test
    public void testMoveUp() {
        final Genre entityGenre = GenreUtils.newGenre(2);
        final List<Genre> genres = CollectionUtils.newList(GenreUtils.newGenre(1), entityGenre);
        final GenreTO genre = GenreUtils.newGenreTO(2);

        when(genreService.get(anyInt())).thenReturn(entityGenre);
        when(genreService.getAll()).thenReturn(genres);

        genreFacade.moveUp(genre);

        verify(genreService).get(2);
        verify(genreService).getAll();
        verify(genreService).moveUp(entityGenre);
        verify(genreTOValidator).validateGenreTOWithId(genre);
        verifyNoMoreInteractions(genreService, genreTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#moveUp(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.moveUp(null);
    }

    /**
     * Test method for {@link GenreFacade#moveUp(GenreTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.moveUp(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(GenreTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.moveUp(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(GenreTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final Genre entityGenre = GenreUtils.newGenre(Integer.MAX_VALUE);
        final List<Genre> genres = CollectionUtils.newList(entityGenre, GenreUtils.newGenre(1));
        final GenreTO genre = GenreUtils.newGenreTO(Integer.MAX_VALUE);

        when(genreService.get(anyInt())).thenReturn(entityGenre);
        when(genreService.getAll()).thenReturn(genres);

        genreFacade.moveUp(genre);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(GenreTO)}.
     */
    @Test
    public void testMoveDown() {
        final Genre entityGenre = GenreUtils.newGenre(1);
        final List<Genre> genres = CollectionUtils.newList(entityGenre, GenreUtils.newGenre(2));
        final GenreTO genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(entityGenre);
        when(genreService.getAll()).thenReturn(genres);

        genreFacade.moveDown(genre);

        verify(genreService).get(1);
        verify(genreService).getAll();
        verify(genreService).moveDown(entityGenre);
        verify(genreTOValidator).validateGenreTOWithId(genre);
        verifyNoMoreInteractions(genreService, genreTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.moveDown(null);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(GenreTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(genreTOValidator).validateGenreTOWithId(any(GenreTO.class));

        genreFacade.moveDown(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(GenreTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.moveDown(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(GenreTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final Genre entityGenre = GenreUtils.newGenre(Integer.MAX_VALUE);
        final List<Genre> genres = CollectionUtils.newList(GenreUtils.newGenre(1), entityGenre);
        final GenreTO genre = GenreUtils.newGenreTO(Integer.MAX_VALUE);

        when(genreService.get(anyInt())).thenReturn(entityGenre);
        when(genreService.getAll()).thenReturn(genres);

        genreFacade.moveDown(genre);
    }

    /**
     * Test method for {@link GenreFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        genreFacade.updatePositions();

        verify(genreService).updatePositions();
        verifyNoMoreInteractions(genreService);
        verifyZeroInteractions(converter, genreTOValidator);
    }

}
