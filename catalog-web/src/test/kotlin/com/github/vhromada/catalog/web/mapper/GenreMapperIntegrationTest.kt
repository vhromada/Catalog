package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.GenreUtils
import com.github.vhromada.catalog.web.fo.GenreFO
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Genre] and [GenreFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class GenreMapperIntegrationTest {

    /**
     * Instance of [GenreMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Genre, GenreFO>

    /**
     * Test method for [GenreMapper.map].
     */
    @Test
    fun map() {
        val genre = GenreUtils.getGenre()

        val genreFO = mapper.map(genre)

        GenreUtils.assertGenreDeepEquals(genreFO, genre)
    }

    /**
     * Test method for [GenreMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val genreFO = GenreUtils.getGenreFO()

        val genre = mapper.mapBack(genreFO)

        GenreUtils.assertGenreDeepEquals(genreFO, genre)
    }

}
