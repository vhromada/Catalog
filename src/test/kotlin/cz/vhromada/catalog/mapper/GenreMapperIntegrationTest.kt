package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Genre
import cz.vhromada.catalog.utils.GenreUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [cz.vhromada.catalog.domain.Genre] and [Genre].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class GenreMapperIntegrationTest {

    /**
     * Instance of [GenreMapper]
     */
    @Autowired
    private lateinit var mapper: GenreMapper

    /**
     * Test method for [GenreMapper.map].
     */
    @Test
    fun map() {
        val genre = GenreUtils.newGenre(1)
        val genreDomain = mapper.map(genre)

        GenreUtils.assertGenreDeepEquals(genre, genreDomain)
    }

    /**
     * Test method for [GenreMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val genreDomain = GenreUtils.newGenreDomain(1)
        val genre = mapper.mapBack(genreDomain)

        GenreUtils.assertGenreDeepEquals(genre, genreDomain)
    }

}
