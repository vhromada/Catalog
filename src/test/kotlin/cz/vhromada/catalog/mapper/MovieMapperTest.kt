package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Movie
import cz.vhromada.catalog.utils.MovieUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [cz.vhromada.catalog.domain.Movie] and [Movie].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class MovieMapperTest {

    /**
     * Instance of [MovieMapper]
     */
    @Autowired
    private lateinit var mapper: MovieMapper

    /**
     * Test method for [MovieMapper.map].
     */
    @Test
    fun map() {
        val movie = MovieUtils.newMovie(1)
        val movieDomain = mapper.map(movie)

        MovieUtils.assertMovieDeepEquals(movie, movieDomain)
    }

    /**
     * Test method for [MovieMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val movieDomain = MovieUtils.newMovieDomain(1)
        val movie = mapper.mapBack(movieDomain)

        MovieUtils.assertMovieDeepEquals(movie, movieDomain)
    }

}
