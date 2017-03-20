package cz.vhromada.catalog.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.converter.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Genre} and {@link Genre}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class GenreConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void convertGenreDomain() {
        final cz.vhromada.catalog.domain.Genre genreDomain = GenreUtils.newGenreDomain(1);
        final Genre genre = converter.convert(genreDomain, Genre.class);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null genre.
     */
    @Test
    public void convertGenreDomain_NullGenre() {
        assertThat(converter.convert(null, Genre.class), is(nullValue()));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void convertGenre() {
        final Genre genre = GenreUtils.newGenre(1);
        final cz.vhromada.catalog.domain.Genre genreDomain = converter.convert(genre, cz.vhromada.catalog.domain.Genre.class);

        assertThat(genreDomain, is(notNullValue()));
        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null genre.
     */
    @Test
    public void convertGenre_NullGenre() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Genre.class), is(nullValue()));
    }

}
