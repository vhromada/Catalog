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

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    private CatalogService<cz.vhromada.catalog.domain.Genre> genreService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link GenreValidator}
     */
    @Mock
    private GenreValidator genreValidator;

    /**
     * Instance of {@link GenreFacade}
     */
    private GenreFacade genreFacade;

    /**
     * Initializes facade for genres.
     */
    @Before
    public void setUp() {
        genreFacade = new GenreFacadeImpl(genreService, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, GenreValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreService() {
        new GenreFacadeImpl(null, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, GenreValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new GenreFacadeImpl(genreService, null, genreValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, GenreValidator)} with null validator for TO for genre.
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
        verifyZeroInteractions(converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenres()}.
     */
    @Test
    public void testGetGenres() {
        final List<cz.vhromada.catalog.domain.Genre> genreList = CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(2));
        final List<Genre> expectedGenres = CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(2));

        when(genreService.getAll()).thenReturn(genreList);
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Genre.class), eq(Genre.class))).thenReturn(expectedGenres);

        final List<Genre> genres = genreFacade.getGenres();

        assertNotNull(genres);
        assertEquals(expectedGenres, genres);

        verify(genreService).getAll();
        verify(converter).convertCollection(genreList, Genre.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)} with existing genre.
     */
    @Test
    public void testGetGenre_ExistingGenre() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(1);
        final Genre expectedGenre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(genreEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Genre.class), eq(Genre.class))).thenReturn(expectedGenre);

        final Genre genre = genreFacade.getGenre(1);

        assertNotNull(genre);
        assertEquals(expectedGenre, genre);

        verify(genreService).get(1);
        verify(converter).convert(genreEntity, Genre.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)} with not existing genre.
     */
    @Test
    public void testGetGenre_NotExistingGenre() {
        when(genreService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Genre.class), eq(Genre.class))).thenReturn(null);

        assertNull(genreFacade.getGenre(Integer.MAX_VALUE));

        verify(genreService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Genre.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGenre_NullArgument() {
        genreFacade.getGenre(null);
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)}.
     */
    @Test
    public void testAdd() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(null);
        final Genre genre = GenreUtils.newGenreTO(null);

        when(converter.convert(any(Genre.class), eq(cz.vhromada.catalog.domain.Genre.class))).thenReturn(genreEntity);

        genreFacade.add(genre);

        verify(genreService).add(genreEntity);
        verify(converter).convert(genre, cz.vhromada.catalog.domain.Genre.class);
        verify(genreValidator).validateNewGenre(genre);
        verifyNoMoreInteractions(genreService, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreValidator).validateNewGenre(any(Genre.class));

        genreFacade.add((Genre) null);
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(genreValidator).validateNewGenre(any(Genre.class));

        genreFacade.add(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)}.
     */
    @Test
    public void testUpdate() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(1);
        final Genre genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(genreEntity);
        when(converter.convert(any(Genre.class), eq(cz.vhromada.catalog.domain.Genre.class))).thenReturn(genreEntity);

        genreFacade.update(genre);

        verify(genreService).get(1);
        verify(genreService).update(genreEntity);
        verify(converter).convert(genre, cz.vhromada.catalog.domain.Genre.class);
        verify(genreValidator).validateExistingGenre(genre);
        verifyNoMoreInteractions(genreService, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreValidator).validateExistingGenre(any(Genre.class));

        genreFacade.update(null);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(genreValidator).validateExistingGenre(any(Genre.class));

        genreFacade.update(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.update(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)}.
     */
    @Test
    public void testRemove() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(1);
        final Genre genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(genreEntity);

        genreFacade.remove(genre);

        verify(genreService).get(1);
        verify(genreService).remove(genreEntity);
        verify(genreValidator).validateGenreWithId(genre);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.remove(null);
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.remove(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.remove(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)}.
     */
    @Test
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(1);
        final Genre genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(genreEntity);

        genreFacade.duplicate(genre);

        verify(genreService).get(1);
        verify(genreService).duplicate(genreEntity);
        verify(genreValidator).validateGenreWithId(genre);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.duplicate(null);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.duplicate(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.duplicate(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)}.
     */
    @Test
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(2);
        final List<cz.vhromada.catalog.domain.Genre> genres = CollectionUtils.newList(GenreUtils.newGenre(1), genreEntity);
        final Genre genre = GenreUtils.newGenreTO(2);

        when(genreService.get(anyInt())).thenReturn(genreEntity);
        when(genreService.getAll()).thenReturn(genres);

        genreFacade.moveUp(genre);

        verify(genreService).get(2);
        verify(genreService).getAll();
        verify(genreService).moveUp(genreEntity);
        verify(genreValidator).validateGenreWithId(genre);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.moveUp(null);
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.moveUp(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.moveUp(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Genre> genres = CollectionUtils.newList(genreEntity, GenreUtils.newGenre(1));
        final Genre genre = GenreUtils.newGenreTO(Integer.MAX_VALUE);

        when(genreService.get(anyInt())).thenReturn(genreEntity);
        when(genreService.getAll()).thenReturn(genres);

        genreFacade.moveUp(genre);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)}.
     */
    @Test
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(1);
        final List<cz.vhromada.catalog.domain.Genre> genres = CollectionUtils.newList(genreEntity, GenreUtils.newGenre(2));
        final Genre genre = GenreUtils.newGenreTO(1);

        when(genreService.get(anyInt())).thenReturn(genreEntity);
        when(genreService.getAll()).thenReturn(genres);

        genreFacade.moveDown(genre);

        verify(genreService).get(1);
        verify(genreService).getAll();
        verify(genreService).moveDown(genreEntity);
        verify(genreValidator).validateGenreWithId(genre);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.moveDown(null);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(genreValidator).validateGenreWithId(any(Genre.class));

        genreFacade.moveDown(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(genreService.get(anyInt())).thenReturn(null);

        genreFacade.moveDown(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenre(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Genre> genres = CollectionUtils.newList(GenreUtils.newGenre(1), genreEntity);
        final Genre genre = GenreUtils.newGenreTO(Integer.MAX_VALUE);

        when(genreService.get(anyInt())).thenReturn(genreEntity);
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
        verifyZeroInteractions(converter, genreValidator);
    }

}
