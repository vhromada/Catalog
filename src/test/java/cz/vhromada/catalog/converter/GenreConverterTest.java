package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Genre} and {@link Genre}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class GenreConverterTest {

    /**
     * Instance of {@link GenreConverter}
     */
    @Autowired
    private GenreConverter converter;

    /**
     * Test method for {@link GenreConverter#convert(Genre)}.
     */
    @Test
    void convert() {
        final Genre genre = GenreUtils.newGenre(1);
        final cz.vhromada.catalog.domain.Genre genreDomain = converter.convert(genre);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link GenreConverter#convertBack(cz.vhromada.catalog.domain.Genre)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Genre genreDomain = GenreUtils.newGenreDomain(1);
        final Genre genre = converter.convertBack(genreDomain);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

}
