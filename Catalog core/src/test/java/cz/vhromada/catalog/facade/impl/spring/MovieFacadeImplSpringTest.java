package cz.vhromada.catalog.facade.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.commons.TestConstants;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class MovieFacadeImplSpringTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link PlatformTransactionManager}
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * Instance of {@link MovieFacade}
     */
    @Autowired
    private MovieFacade movieFacade;

    /**
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Initializes database.
     */
    @Before
    public void setUp() {
        SpringUtils.remove(transactionManager, entityManager, Movie.class);
        SpringUtils.remove(transactionManager, entityManager, Medium.class);
        SpringUtils.updateSequence(transactionManager, entityManager, "movies_sq");
        SpringUtils.updateSequence(transactionManager, entityManager, "media_sq");
        for (final Movie movie : SpringEntitiesUtils.getMovies()) {
            movie.setId(null);
            for (final Medium medium : movie.getMedia()) {
                medium.setId(null);
            }
            SpringUtils.persist(transactionManager, entityManager, movie);
        }
    }

    /**
     * Test method for {@link MovieFacade#newData()}.
     */
    @Test
    public void testNewData() {
        movieFacade.newData();

        DeepAsserts.assertEquals(0, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getMovies()}.
     */
    @Test
    public void testGetMovies() {
        DeepAsserts.assertEquals(SpringToUtils.getMovies(), movieFacade.getMovies());
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)}.
     */
    @Test
    public void testGetMovie() {
        for (int i = 1; i <= SpringUtils.MOVIES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringToUtils.getMovie(i), movieFacade.getMovie(i));
        }

        assertNull(movieFacade.getMovie(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMovieWithNullArgument() {
        movieFacade.getMovie(null);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)}.
     */
    @Test
    public void testAdd() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);

        movieFacade.add(movie);

        DeepAsserts.assertNotNull(movie.getId());
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT + 1, movie.getId());
        final Movie addedMovie = SpringUtils.getMovie(entityManager, SpringUtils.MOVIES_COUNT + 1);
        DeepAsserts.assertEquals(movie, addedMovie, "subtitlesAsString", "media", "totalLength", "genresAsString");
        DeepAsserts.assertEquals(movie.getMedia().size(), addedMovie.getMedia().size());
        for (int i = 0; i < movie.getMedia().size(); i++) {
            DeepAsserts.assertEquals(movie.getMedia().get(i), addedMovie.getMedia().get(i).getLength());
        }
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullArgument() {
        movieFacade.add(null);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithMovieWithNotNullId() {
        movieFacade.add(SpringToUtils.newMovieWithId(objectGenerator));
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullCzechName() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setCzechName(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithEmptyCzechName() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setCzechName("");

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullOriginalName() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setOriginalName(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithEmptyOriginalName() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setOriginalName("");

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMinimumYear() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMaximumYear() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullLanguage() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setLanguage(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullSubtitles() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setSubtitles(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadSubtitles() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setSubtitles(CollectionUtils.newList(objectGenerator.generate(Language.class), null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullMedia() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setMedia(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMedia() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setMedia(CollectionUtils.newList(objectGenerator.generate(Integer.class), null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithMediaWithBadMedium() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setMedia(CollectionUtils.newList(objectGenerator.generate(Integer.class), -1));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullCsfd() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setCsfd(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMinimalImdb() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadDividerImdb() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setImdbCode(0);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadMaximalImdb() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullWikiEn() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setWikiEn(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullWikiCz() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setWikiCz(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullPicture() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setPicture(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullNote() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setNote(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithNullGenres() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setGenres(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBadGenres() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithGenresWithGenreWithNullId() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        movie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), SpringToUtils.newGenre(objectGenerator)));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithGenresWithGenreWithNullName() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator);
        final GenreTO badGenre = SpringToUtils.newGenreWithId(objectGenerator);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), badGenre));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)}.
     */
    @Test
    public void testUpdate() {
        final MovieTO movie = SpringToUtils.newMovie(objectGenerator, 1);

        movieFacade.update(movie);

        final Movie updatedMovie = SpringUtils.getMovie(entityManager, 1);
        DeepAsserts.assertEquals(movie, updatedMovie, "subtitlesAsString", "media", "totalLength", "genresAsString");
        DeepAsserts.assertEquals(movie.getMedia().size(), updatedMovie.getMedia().size());
        for (int i = 0; i < movie.getMedia().size(); i++) {
            DeepAsserts.assertEquals(movie.getMedia().get(i), updatedMovie.getMedia().get(i).getLength());
        }
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullArgument() {
        movieFacade.update(null);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithMovieWithNullId() {
        movieFacade.update(SpringToUtils.newMovie(objectGenerator));
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullCzechName() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setCzechName(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithEmptyCzechName() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setCzechName("");

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullOriginalName() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setOriginalName(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithEmptyOriginalName() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setOriginalName("");

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMinimumYear() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMaximumYear() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullLanguage() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setLanguage(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullSubtitles() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setSubtitles(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadSubtitles() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setSubtitles(CollectionUtils.newList(objectGenerator.generate(Language.class), null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullMedia() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setMedia(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMedia() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setMedia(CollectionUtils.newList(objectGenerator.generate(Integer.class), null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithMediaWithBadMedium() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setMedia(CollectionUtils.newList(objectGenerator.generate(Integer.class), -1));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullCsfd() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setCsfd(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMinimalImdb() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadDividerImdb() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setImdbCode(0);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadMaximalImdb() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullWikiEn() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setWikiEn(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullWikiCz() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setWikiCz(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullPicture() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setPicture(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullNote() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setNote(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithNullGenres() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setGenres(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBadGenres() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithGenresWithGenreWithNullId() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        movie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), SpringToUtils.newGenre(objectGenerator)));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithGenresWithGenreWithNullName() {
        final MovieTO movie = SpringToUtils.newMovieWithId(objectGenerator);
        final GenreTO badGenre = SpringToUtils.newGenreWithId(objectGenerator);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(SpringToUtils.newGenreWithId(objectGenerator), badGenre));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithMovieWithBadId() {
        movieFacade.update(SpringToUtils.newMovie(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)}.
     */
    @Test
    public void testRemove() {
        movieFacade.remove(SpringToUtils.newMovie(objectGenerator, 1));

        assertNull(SpringUtils.getMovie(entityManager, 1));
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT - 1, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullArgument() {
        movieFacade.remove(null);
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemoveWithMovieWithNullId() {
        movieFacade.remove(SpringToUtils.newMovie(objectGenerator));
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with movie with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemoveWithMovieWithBadId() {
        movieFacade.remove(SpringToUtils.newMovie(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)}.
     */
    @Test
    public void testDuplicate() {
        final Movie movie = SpringEntitiesUtils.getMovie(SpringUtils.MOVIES_COUNT);
        movie.setId(SpringUtils.MOVIES_COUNT + 1);
        for (final Medium medium : movie.getMedia()) {
            medium.setId(SpringUtils.MEDIA_COUNT + movie.getMedia().indexOf(medium) + 1);
        }

        movieFacade.duplicate(SpringToUtils.newMovie(objectGenerator, SpringUtils.MOVIES_COUNT));

        final Movie duplicatedMovie = SpringUtils.getMovie(entityManager, SpringUtils.MOVIES_COUNT + 1);
        DeepAsserts.assertEquals(movie, duplicatedMovie);
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateWithNullArgument() {
        movieFacade.duplicate(null);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicateWithMovieWithNullId() {
        movieFacade.duplicate(SpringToUtils.newMovie(objectGenerator));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with movie with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicateWithMovieWithBadId() {
        movieFacade.duplicate(SpringToUtils.newMovie(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)}.
     */
    @Test
    public void testMoveUp() {
        final Movie movie1 = SpringEntitiesUtils.getMovie(1);
        movie1.setPosition(1);
        final Movie movie2 = SpringEntitiesUtils.getMovie(2);
        movie2.setPosition(0);

        movieFacade.moveUp(SpringToUtils.newMovie(objectGenerator, 2));
        DeepAsserts.assertEquals(movie1, SpringUtils.getMovie(entityManager, 1));
        DeepAsserts.assertEquals(movie2, SpringUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= SpringUtils.MOVIES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUpWithNullArgument() {
        movieFacade.moveUp(null);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithMovieWithNullId() {
        movieFacade.moveUp(SpringToUtils.newMovie(objectGenerator));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithNotMovableArgument() {
        movieFacade.moveUp(SpringToUtils.newMovie(objectGenerator, 1));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUpWithBadId() {
        movieFacade.moveUp(SpringToUtils.newMovie(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)}.
     */
    @Test
    public void testMoveDown() {
        final Movie movie1 = SpringEntitiesUtils.getMovie(1);
        movie1.setPosition(1);
        final Movie movie2 = SpringEntitiesUtils.getMovie(2);
        movie2.setPosition(0);

        movieFacade.moveDown(SpringToUtils.newMovie(objectGenerator, 1));
        DeepAsserts.assertEquals(movie1, SpringUtils.getMovie(entityManager, 1));
        DeepAsserts.assertEquals(movie2, SpringUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= SpringUtils.MOVIES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDownWithNullArgument() {
        movieFacade.moveDown(null);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithMovieWithNullId() {
        movieFacade.moveDown(SpringToUtils.newMovie(objectGenerator));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithNotMovableArgument() {
        movieFacade.moveDown(SpringToUtils.newMovie(objectGenerator, SpringUtils.MOVIES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDownWithBadId() {
        movieFacade.moveDown(SpringToUtils.newMovie(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with existing movie.
     */
    @Test
    public void testExists() {
        for (int i = 1; i <= SpringUtils.MOVIES_COUNT; i++) {
            assertTrue(movieFacade.exists(SpringToUtils.newMovie(objectGenerator, i)));
        }

        assertFalse(movieFacade.exists(SpringToUtils.newMovie(objectGenerator, Integer.MAX_VALUE)));

        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsWithNullArgument() {
        movieFacade.exists(null);
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testExistsWithMovieWithNullId() {
        movieFacade.exists(SpringToUtils.newMovie(objectGenerator));
    }

    /**
     * Test method for {@link MovieFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        movieFacade.updatePositions();

        for (int i = 1; i <= SpringUtils.MOVIES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        DeepAsserts.assertEquals(SpringUtils.MEDIA_COUNT, movieFacade.getTotalMediaCount());
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        DeepAsserts.assertEquals(new Time(1000), movieFacade.getTotalLength());
        DeepAsserts.assertEquals(SpringUtils.MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
    }

}
