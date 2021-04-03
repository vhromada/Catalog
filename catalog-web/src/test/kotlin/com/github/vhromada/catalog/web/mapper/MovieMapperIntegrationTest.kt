package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.utils.MovieUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Movie] and [MovieFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class MovieMapperIntegrationTest {

    /**
     * Instance of [MovieMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Movie, MovieFO>

    /**
     * Test method for [MovieMapper.map].
     */
    @Test
    fun map() {
        val movie = MovieUtils.getMovie()
        val movieFO = mapper.map(source = movie)

        MovieUtils.assertMovieDeepEquals(expected = movie, actual = movieFO)
    }

    /**
     * Test method for [MovieMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val movieFO = MovieUtils.getMovieFO()
        val movie = mapper.mapBack(source = movieFO)

        MovieUtils.assertMovieDeepEquals(expected = movieFO, actual = movie)
    }

}
