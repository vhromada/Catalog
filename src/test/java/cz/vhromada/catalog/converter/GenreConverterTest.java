package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void testConvertGenreDomain() {
        final cz.vhromada.catalog.domain.Genre genreDomain = GenreUtils.newGenreDomain(1);
        final Genre genre = converter.convert(genreDomain, Genre.class);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertGenreDomain_NullArgument() {
        assertNull(converter.convert(null, Genre.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertGenre() {
        final Genre genre = GenreUtils.newGenre(1);
        final cz.vhromada.catalog.domain.Genre genreDomain = converter.convert(genre, cz.vhromada.catalog.domain.Genre.class);

        assertNotNull(genre);
        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertGenre_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Genre.class));
    }

}
