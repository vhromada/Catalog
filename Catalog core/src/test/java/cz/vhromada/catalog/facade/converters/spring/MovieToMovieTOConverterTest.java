package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.converters.Converter;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter from {@link Movie} to {@link MovieTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class MovieToMovieTOConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Test method for {@link Converter#convert(Object, Class)}.
     */
    @Test
    public void testConvert() {
        final Movie movie = objectGenerator.generate(Movie.class);
        final MovieTO movieTO = converter.convert(movie, MovieTO.class);
        DeepAsserts.assertNotNull(movieTO);
        DeepAsserts.assertEquals(movie, movieTO, "media");
        assertMediaDeepEquals(movie.getMedia(), movieTO.getMedia());
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} with null argument.
     */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null, MovieTO.class));
    }

    /**
     * Assert media deep equals.
     *
     * @param expected expected media
     * @param actual   actual media
     */
    private static void assertMediaDeepEquals(final List<Medium> expected, final List<Integer> actual) {
        DeepAsserts.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            DeepAsserts.assertEquals(expected.get(i).getLength(), actual.get(i));
        }
    }

}
