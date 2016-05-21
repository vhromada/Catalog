//package cz.vhromada.catalog.facade.converters.spring;
//
//import static org.junit.Assert.assertNull;
//
//import java.util.List;
//
//import cz.vhromada.catalog.dao.entities.Medium;
//import cz.vhromada.catalog.dao.entities.Movie;
//import cz.vhromada.catalog.facade.to.MovieTO;
//import cz.vhromada.converters.Converter;
//import cz.vhromada.generator.ObjectGenerator;
//import cz.vhromada.test.DeepAsserts;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * A class represents test for converter from {@link MovieTO} to {@link Movie}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
//public class MovieTOToMovieConverterTest {
//
//    /**
//     * Instance of {@link Converter}
//     */
//    @Autowired
//    @Qualifier("catalogDozerConverter")
//    private Converter converter;
//
//    /**
//     * Instance of {@link ObjectGenerator}
//     */
//    @Autowired
//    private ObjectGenerator objectGenerator;
//
//    /**
//     * Test method for {@link Converter#convert(Object, Class)}.
//     */
//    @Test
//    public void testConvert() {
//        final MovieTO movieTO = objectGenerator.generate(MovieTO.class);
//        final Movie movie = converter.convert(movieTO, Movie.class);
//        DeepAsserts.assertNotNull(movie);
//        DeepAsserts.assertEquals(movieTO, movie, "media");
//        assertMediaDeepEquals(movieTO.getMedia(), movie.getMedia());
//    }
//
//    /**
//     * Test method for {@link Converter#convert(Object, Class)} with null argument.
//     */
//    @Test
//    public void testConvertWithNullArgument() {
//        assertNull(converter.convert(null, Movie.class));
//    }
//
//    /**
//     * Assert media deep equals.
//     *
//     * @param expected expected media
//     * @param actual   actual media
//     */
//    private static void assertMediaDeepEquals(final List<Integer> expected, final List<Medium> actual) {
//        DeepAsserts.assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < expected.size(); i++) {
//            DeepAsserts.assertEquals(expected.get(i), actual.get(i).getLength());
//            DeepAsserts.assertEquals(i + 1, actual.get(i).getNumber());
//        }
//    }
//
//}
