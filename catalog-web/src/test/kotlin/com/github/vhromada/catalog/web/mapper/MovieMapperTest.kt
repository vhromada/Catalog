package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.MovieUtils
import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.mapper.impl.MovieMapperImpl
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
class MovieMapperTest {

    /**
     * Mapper for movies
     */
    @Autowired
    private lateinit var mapper: MovieMapper

    /**
     * Test method for [MovieMapperImpl.map].
     */
    @Test
    fun map() {
        val movie = MovieUtils.getMovie()

        val movieFO = mapper.map(movie)

        MovieUtils.assertMovieDeepEquals(movie, movieFO)
    }

    /**
     * Test method for [MovieMapperImpl.mapBack].
     */
    @Test
    fun mapBack() {
        val movieFO = MovieUtils.getMovieFO()

        val movie = mapper.mapBack(movieFO)

        MovieUtils.assertMovieDeepEquals(movieFO, movie)
    }

}
