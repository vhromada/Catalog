package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.MOVIES_COUNT;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.impl.MovieDAOImpl;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link MovieDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class MovieDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link MovieDAO} */
	@Autowired
	private MovieDAO movieDAO;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Restarts sequences. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE movies_sq RESTART WITH 4").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE media_sq RESTART WITH 5").executeUpdate();
	}

	/** Test method for {@link MovieDAO#getMovies()}. */
	@Test
	public void testGetMovies() {
		DeepAsserts.assertEquals(SpringEntitiesUtils.getMovies(), movieDAO.getMovies());
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
	}

	/** Test method for {@link MovieDAO#getMovie(Integer)}. */
	@Test
	public void testGetMovie() {
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), movieDAO.getMovie(i));
		}

		assertNull(movieDAO.getMovie(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
	}

	/** Test method for {@link MovieDAO#add(Movie)}. */
	@Test
	public void testAdd() {
		final Movie movie = objectGenerator.generate(Movie.class);
		movie.setId(null);
		movie.setYear(1940 + movie.getYear());
		movie.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));
		for (Medium medium : movie.getMedia()) {
			medium.setId(null);
		}

		movieDAO.add(movie);

		DeepAsserts.assertNotNull(movie.getId());
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, movie.getId());
		DeepAsserts.assertEquals(MOVIES_COUNT, movie.getPosition());
		final Movie addedMovie = SpringUtils.getMovie(entityManager, MOVIES_COUNT + 1);
		DeepAsserts.assertEquals(movie, addedMovie);
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
	}

	/** Test method for {@link MovieDAO#update(Movie)} with no media change. */
	@Test
	public void testUpdateWithNoMediaChange() {
		final Movie movie = SpringEntitiesUtils.updateMovie(SpringUtils.getMovie(entityManager, 1), objectGenerator);

		movieDAO.update(movie);

		final Movie updatedMovie = SpringUtils.getMovie(entityManager, 1);
		DeepAsserts.assertEquals(movie, updatedMovie);
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
	}

	/** Test method for {@link MovieDAO#update(Movie)} with added medium. */
	@Test
	public void testUpdateWithAddedMedium() {
		final Movie movie = SpringEntitiesUtils.updateMovie(SpringUtils.getMovie(entityManager, 1), objectGenerator);
		final List<Medium> media = movie.getMedia();
		final Medium medium = objectGenerator.generate(Medium.class);
		medium.setId(null);
		media.add(medium);
		movie.setMedia(media);

		movieDAO.update(movie);

		final Movie updatedMovie = SpringUtils.getMovie(entityManager, 1);
		DeepAsserts.assertEquals(movie, updatedMovie);
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
	}

	/** Test method for {@link MovieDAO#update(Movie)} with removed medium. */
	@Test
	public void testUpdateWithRemovedMedium() {
		final Movie movie = SpringEntitiesUtils.updateMovie(SpringUtils.getMovie(entityManager, 1), objectGenerator);
		movie.setMedia(new ArrayList<Medium>());

		movieDAO.update(movie);

		final Movie updatedMovie = SpringUtils.getMovie(entityManager, 1);
		DeepAsserts.assertEquals(movie, updatedMovie);
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
	}

	/** Test method for {@link MovieDAO#remove(Movie)}. */
	@Test
	public void testRemove() {
		movieDAO.remove(SpringUtils.getMovie(entityManager, 1));

		assertNull(SpringUtils.getMovie(entityManager, 1));
		DeepAsserts.assertEquals(MOVIES_COUNT - 1, SpringUtils.getMoviesCount(entityManager));
	}

}
