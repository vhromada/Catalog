package cz.vhromada.catalog.repository

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.catalog.utils.MediumUtils
import cz.vhromada.catalog.utils.MovieUtils
import cz.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [MovieRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class MovieRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MovieRepository]
     */
    @Autowired
    private lateinit var movieRepository: MovieRepository

    /**
     * Test method for get movies.
     */
    @Test
    fun getMovies() {
        val movies = movieRepository.findAll(Sort.by("position", "id"))

        MovieUtils.assertMoviesDeepEquals(MovieUtils.getMovies(), movies)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

    /**
     * Test method for get movie.
     */
    @Test
    fun getMovie() {
        for (i in 1..MovieUtils.MOVIES_COUNT) {
            val movie = movieRepository.findById(i).orElse(null)

            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), movie)
        }

        assertThat(movieRepository.findById(Integer.MAX_VALUE).isPresent).isFalse()

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

    /**
     * Test method for add movie.
     */
    @Test
    fun add() {
        val movie = MovieUtils.newMovieDomain(null)
                .copy(media = listOf(MediumUtils.newMediumDomain(null)),
                        position = MovieUtils.MOVIES_COUNT,
                        genres = listOf(GenreUtils.getGenre(entityManager, 1)!!))

        movieRepository.save(movie)

        assertThat(movie.id).isEqualTo(MovieUtils.MOVIES_COUNT + 1)

        val addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1)!!
        val expectedAddedMovie = MovieUtils.newMovieDomain(null)
                .copy(id = MovieUtils.MOVIES_COUNT + 1,
                        media = listOf(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1)),
                        position = MovieUtils.MOVIES_COUNT,
                        genres = listOf(GenreUtils.getGenreDomain(1)))
        MovieUtils.assertMovieDeepEquals(expectedAddedMovie, addedMovie)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT + 1)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1)
        }
    }

    /**
     * Test method for update movie with no media change.
     */
    @Test
    fun updateNoMediaChange() {
        val movie = MovieUtils.updateMovie(entityManager, 1)

        movieRepository.save(movie)

        val updatedMovie = MovieUtils.getMovie(entityManager, 1)!!
        val expectedUpdatedMovie = MovieUtils.getMovie(1)
                .updated()
                .copy(position = MovieUtils.POSITION)
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

    /**
     * Test method for update movie with added medium.
     */
    @Test
    @DirtiesContext
    fun updateAddedMedium() {
        var movie = MovieUtils.updateMovie(entityManager, 1)
        val media = movie.media.toMutableList()
        media.add(MediumUtils.newMediumDomain(null))
        movie = movie.copy(media = media)

        movieRepository.save(movie)

        val updatedMovie = MovieUtils.getMovie(entityManager, 1)!!
        var expectedUpdatedMovie = MovieUtils.getMovie(1)
                .updated()
        val expectedMedia = expectedUpdatedMovie.media.toMutableList()
        expectedMedia.add(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1))
        expectedUpdatedMovie = expectedUpdatedMovie.copy(media = expectedMedia, position = MovieUtils.POSITION)
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1)
        }
    }

    /**
     * Test method for update movie with removed medium.
     */
    @Test
    fun updateRemovedMedium() {
        val mediaCount = MovieUtils.getMovie(1).media.size
        val movie = MovieUtils.updateMovie(entityManager, 1)
                .copy(media = emptyList())

        movieRepository.save(movie)

        val updatedMovie = MovieUtils.getMovie(entityManager, 1)!!
        val expectedUpdatedMovie = MovieUtils.getMovie(1)
                .updated()
                .copy(media = emptyList(), position = MovieUtils.POSITION)
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - mediaCount)
        }
    }

    /**
     * Test method for remove movie.
     */
    @Test
    fun remove() {
        val mediaCount = MovieUtils.getMovie(1).media.size

        movieRepository.delete(MovieUtils.getMovie(entityManager, 1)!!)

        assertThat(MovieUtils.getMovie(entityManager, 1)).isNull()

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT - 1)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - mediaCount)
        }
    }

    /**
     * Test method for remove all movies.
     */
    @Test
    fun removeAll() {
        movieRepository.deleteAll()

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(0)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(0)
        }
    }

}
