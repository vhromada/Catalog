package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

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
     * Result for invalid genre
     */
    private static final Result<Void> INVALID_GENRE_RESULT = Result.error("GENRE_INVALID", "Genre must be valid.");

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
     * Instance of {@link CatalogValidator}
     */
    @Mock
    private CatalogValidator<Genre> genreValidator;

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
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGenreService() {
        new GenreFacadeImpl(null, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new GenreFacadeImpl(genreService, null, genreValidator);
    }

    /**
     * Test method for {@link GenreFacadeImpl#GenreFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGenreValidator() {
        new GenreFacadeImpl(genreService, converter, null);
    }

    /**
     * Test method for {@link GenreFacade#newData()}.
     */
    @Test
    public void newData() {
        final Result<Void> result = genreFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).newData();
        verifyNoMoreInteractions(genreService);
        verifyZeroInteractions(converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final List<cz.vhromada.catalog.domain.Genre> genreList = CollectionUtils.newList(GenreUtils.newGenreDomain(1), GenreUtils.newGenreDomain(2));
        final List<Genre> expectedGenres = CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(2));

        when(genreService.getAll()).thenReturn(genreList);
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Genre.class), eq(Genre.class))).thenReturn(expectedGenres);

        final Result<List<Genre>> result = genreFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedGenres));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).getAll();
        verify(converter).convertCollection(genreList, Genre.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#get(Integer)} with existing genre.
     */
    @Test
    public void get_ExistingGenre() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenreDomain(1);
        final Genre expectedGenre = GenreUtils.newGenre(1);

        when(genreService.get(any(Integer.class))).thenReturn(genreEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Genre.class), eq(Genre.class))).thenReturn(expectedGenre);

        final Result<Genre> result = genreFacade.get(1);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedGenre));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).get(1);
        verify(converter).convert(genreEntity, Genre.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#get(Integer)} with not existing genre.
     */
    @Test
    public void get_NotExistingGenre() {
        when(genreService.get(any(Integer.class))).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Genre.class), eq(Genre.class))).thenReturn(null);

        final Result<Genre> result = genreFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Genre.class);
        verifyNoMoreInteractions(genreService, converter);
        verifyZeroInteractions(genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#get(Integer)} with null argument.
     */
    @Test
    public void get_NullArgument() {
        final Result<Genre> result = genreFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        verifyZeroInteractions(genreService, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)}.
     */
    @Test
    public void add() {
        final Genre genre = GenreUtils.newGenre(null);
        final cz.vhromada.catalog.domain.Genre genreDomain = GenreUtils.newGenreDomain(null);

        when(converter.convert(any(Genre.class), eq(cz.vhromada.catalog.domain.Genre.class))).thenReturn(genreDomain);
        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = genreFacade.add(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).add(genreDomain);
        verify(converter).convert(genre, cz.vhromada.catalog.domain.Genre.class);
        verify(genreValidator).validate(genre, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(genreService, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with invalid genre.
     */
    @Test
    public void add_InvalidGenre() {
        final Genre genre = GenreUtils.newGenre(Integer.MAX_VALUE);

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(INVALID_GENRE_RESULT);

        final Result<Void> result = genreFacade.add(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GENRE_RESULT));

        verify(genreValidator).validate(genre, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)}.
     */
    @Test
    public void update() {
        final Genre genre = GenreUtils.newGenre(1);
        final cz.vhromada.catalog.domain.Genre genreDomain = GenreUtils.newGenreDomain(1);

        when(converter.convert(any(Genre.class), eq(cz.vhromada.catalog.domain.Genre.class))).thenReturn(genreDomain);
        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = genreFacade.update(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).update(genreDomain);
        verify(converter).convert(genre, cz.vhromada.catalog.domain.Genre.class);
        verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(genreService, converter, genreValidator);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with invalid genre.
     */
    @Test
    public void update_InvalidGenre() {
        final Genre genre = GenreUtils.newGenre(Integer.MAX_VALUE);

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(INVALID_GENRE_RESULT);

        final Result<Void> result = genreFacade.update(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GENRE_RESULT));

        verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)}.
     */
    @Test
    public void remove() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenreDomain(1);
        final Genre genre = GenreUtils.newGenre(1);

        when(genreService.get(any(Integer.class))).thenReturn(genreEntity);
        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = genreFacade.remove(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).get(1);
        verify(genreService).remove(genreEntity);
        verify(genreValidator).validate(genre, ValidationType.EXISTS);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with invalid genre.
     */
    @Test
    public void remove_InvalidGenre() {
        final Genre genre = GenreUtils.newGenre(Integer.MAX_VALUE);

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(INVALID_GENRE_RESULT);

        final Result<Void> result = genreFacade.remove(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GENRE_RESULT));

        verify(genreValidator).validate(genre, ValidationType.EXISTS);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)}.
     */
    @Test
    public void duplicate() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenreDomain(1);
        final Genre genre = GenreUtils.newGenre(1);

        when(genreService.get(any(Integer.class))).thenReturn(genreEntity);
        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = genreFacade.duplicate(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).get(1);
        verify(genreService).duplicate(genreEntity);
        verify(genreValidator).validate(genre, ValidationType.EXISTS);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with invalid genre.
     */
    @Test
    public void duplicate_InvalidGenre() {
        final Genre genre = GenreUtils.newGenre(Integer.MAX_VALUE);

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(INVALID_GENRE_RESULT);

        final Result<Void> result = genreFacade.duplicate(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GENRE_RESULT));

        verify(genreValidator).validate(genre, ValidationType.EXISTS);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)}.
     */
    @Test
    public void moveUp() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenreDomain(1);
        final Genre genre = GenreUtils.newGenre(1);

        when(genreService.get(any(Integer.class))).thenReturn(genreEntity);
        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = genreFacade.moveUp(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).get(1);
        verify(genreService).moveUp(genreEntity);
        verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with invalid genre.
     */
    @Test
    public void moveUp_InvalidGenre() {
        final Genre genre = GenreUtils.newGenre(Integer.MAX_VALUE);

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(INVALID_GENRE_RESULT);

        final Result<Void> result = genreFacade.moveUp(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GENRE_RESULT));

        verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)}.
     */
    @Test
    public void moveDown() {
        final cz.vhromada.catalog.domain.Genre genreEntity = GenreUtils.newGenreDomain(1);
        final Genre genre = GenreUtils.newGenre(1);

        when(genreService.get(any(Integer.class))).thenReturn(genreEntity);
        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = genreFacade.moveDown(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).get(1);
        verify(genreService).moveDown(genreEntity);
        verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(genreService, genreValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with invalid genre.
     */
    @Test
    public void moveDown_InvalidGenre() {
        final Genre genre = GenreUtils.newGenre(Integer.MAX_VALUE);

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(INVALID_GENRE_RESULT);

        final Result<Void> result = genreFacade.moveDown(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GENRE_RESULT));

        verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link GenreFacade#updatePositions()}.
     */
    @Test
    public void updatePositions() {
        final Result<Void> result = genreFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(genreService).updatePositions();
        verifyNoMoreInteractions(genreService);
        verifyZeroInteractions(converter, genreValidator);
    }

}
