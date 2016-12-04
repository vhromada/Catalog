package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.entity.Genre;
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
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class GenreConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO.
     */
    @Test
    public void testConvertGenre() {
        final cz.vhromada.catalog.domain.Genre genre = GenreUtils.newGenre(1);
        final Genre genreTO = converter.convert(genre, Genre.class);

        GenreUtils.assertGenreDeepEquals(genreTO, genre);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertGenre_NullArgument() {
        assertNull(converter.convert(null, Genre.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertGenreTO() {
        final Genre genreTO = GenreUtils.newGenreTO(1);
        final cz.vhromada.catalog.domain.Genre genre = converter.convert(genreTO, cz.vhromada.catalog.domain.Genre.class);

        assertNotNull(genre);
        GenreUtils.assertGenreDeepEquals(genreTO, genre);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertGenreTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Genre.class));
    }

}
