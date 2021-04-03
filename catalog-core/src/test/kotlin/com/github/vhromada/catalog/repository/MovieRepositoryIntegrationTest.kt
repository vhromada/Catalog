package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.MediumUtils
import com.github.vhromada.catalog.utils.MovieUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

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
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MovieRepository]
     */
    @Autowired
    private lateinit var repository: MovieRepository

    /**
     * Test method for get movies.
     */
    @Test
    fun getMovies() {
        val movies = repository.findAll()

        MovieUtils.assertDomainMoviesDeepEquals(expected = MovieUtils.getMovies(), actual = movies)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

    /**
     * Test method for get movie.
     */
    @Test
    fun getMovie() {
        for (i in 1..MovieUtils.MOVIES_COUNT) {
            val movie = repository.findById(i).orElse(null)

            MovieUtils.assertMovieDeepEquals(expected = MovieUtils.getMovieDomain(index = i), actual = movie)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

    /**
     * Test method for add movie.
     */
    @Test
    fun add() {
        val movie = MovieUtils.newMovieDomain(id = null)
            .copy(position = MovieUtils.MOVIES_COUNT, genres = listOf(GenreUtils.getGenre(entityManager = entityManager, id = 1)!!))
        val expectedMovie = MovieUtils.newMovieDomain(id = MovieUtils.MOVIES_COUNT + 1)
            .copy(
                media = listOf(MediumUtils.newMediumDomain(id = MediumUtils.MEDIA_COUNT + 1).fillAudit(audit = AuditUtils.newAudit())),
                picture = null,
                genres = listOf(GenreUtils.getGenreDomain(index = 1))
            ).fillAudit(audit = AuditUtils.newAudit())

        repository.save(movie)

        assertSoftly {
            it.assertThat(movie.id).isEqualTo(MovieUtils.MOVIES_COUNT + 1)
            it.assertThat(movie.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(movie.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(movie.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(movie.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1)!!
        assertThat(addedMovie).isNotNull
        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = addedMovie)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT + 1)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1)
        }
    }

    /**
     * Test method for update movie.
     */
    @Test
    fun update() {
        val movie = MovieUtils.updateMovie(entityManager = entityManager, id = 1)
        val expectedMovie = MovieUtils.getMovieDomain(index = 1)
            .updated()
            .copy(position = MovieUtils.POSITION)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(movie)

        val updatedMovie = MovieUtils.getMovie(entityManager = entityManager, id = 1)
        assertThat(updatedMovie).isNotNull
        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = updatedMovie!!)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

    /**
     * Test method for update movie with added medium.
     */
    @Test
    @DirtiesContext
    fun updateAddedMedium() {
        var movie = MovieUtils.updateMovie(entityManager = entityManager, id = 1)
        val media = movie.media.toMutableList()
        media.add(MediumUtils.newMediumDomain(id = null))
        movie = movie.copy(media = media)
            .fillAudit(audit = movie)
        var expectedMovie = MovieUtils.getMovieDomain(index = 1)
            .updated()
        val expectedMedia = expectedMovie.media.toMutableList()
        expectedMedia.add(MediumUtils.newMediumDomain(id = MediumUtils.MEDIA_COUNT + 1).fillAudit(audit = AuditUtils.newAudit()))
        expectedMovie = expectedMovie.copy(media = expectedMedia)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(movie)

        val updatedMovie = MovieUtils.getMovie(entityManager, 1)
        assertThat(updatedMovie).isNotNull
        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = updatedMovie!!)

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
        var movie = MovieUtils.updateMovie(entityManager = entityManager, id = 1)
        movie = movie.copy(media = emptyList())
            .fillAudit(audit = movie)
        val expectedMovie = MovieUtils.getMovieDomain(index = 1)
            .updated()
            .copy(media = emptyList())
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(movie)

        val updatedMovie = MovieUtils.getMovie(entityManager, 1)
        assertThat(updatedMovie).isNotNull
        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = updatedMovie!!)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - MovieUtils.getMovieDomain(index = 1).media.size)
        }
    }

    /**
     * Test method for remove movie.
     */
    @Test
    fun remove() {
        repository.delete(MovieUtils.getMovie(entityManager = entityManager, id = 1)!!)

        assertThat(MovieUtils.getMovie(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT - 1)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - MovieUtils.getMovieDomain(index = 1).media.size)
        }
    }

    /**
     * Test method for remove all movies.
     */
    @Test
    fun removeAll() {
        repository.deleteAll()

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for get movies for user.
     */
    @Test
    fun findByCreatedUser() {
        val movies = repository.findByCreatedUser(user = AuditUtils.getAudit().createdUser!!)

        MovieUtils.assertDomainMoviesDeepEquals(expected = MovieUtils.getMovies(), actual = movies)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

    /**
     * Test method for get movie by id for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..MovieUtils.MOVIES_COUNT) {
            val author = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            MovieUtils.assertMovieDeepEquals(expected = MovieUtils.getMovieDomain(index = i), actual = author)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
        }
    }

}
