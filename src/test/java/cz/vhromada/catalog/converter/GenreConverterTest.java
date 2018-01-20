package cz.vhromada.catalog.converter;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.converter.Converter;

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
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertGenreDomain() {
        final cz.vhromada.catalog.domain.Genre genreDomain = GenreUtils.newGenreDomain(1);
        final Genre genre = converter.convert(genreDomain, Genre.class);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null genre.
     */
    @Test
    void convertGenreDomain_NullGenre() {
        assertThat(converter.convert(null, Genre.class)).isNull();
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertGenre() {
        final Genre genre = GenreUtils.newGenre(1);
        final cz.vhromada.catalog.domain.Genre genreDomain = converter.convert(genre, cz.vhromada.catalog.domain.Genre.class);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null genre.
     */
    @Test
    void convertGenre_NullGenre() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Genre.class)).isNull();
    }

}
