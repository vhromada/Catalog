//package cz.vhromada.catalog.facade.converters.spring;
//
//import static org.junit.Assert.assertNull;
//
//import cz.vhromada.catalog.entities.Episode;
//import cz.vhromada.catalog.facade.to.EpisodeTO;
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
// * A class represents test for converter from {@link EpisodeTO} to {@link Episode}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
//public class EpisodeTOToEpisodeConverterTest {
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
//        final EpisodeTO episodeTO = objectGenerator.generate(EpisodeTO.class);
//        final Episode episode = converter.convert(episodeTO, Episode.class);
//        DeepAsserts.assertNotNull(episode);
//        DeepAsserts.assertEquals(episodeTO, episode);
//    }
//
//    /**
//     * Test method for {@link Converter#convert(Object, Class)} with null argument.
//     */
//    @Test
//    public void testConvertWithNullArgument() {
//        assertNull(converter.convert(null, Episode.class));
//    }
//
//}
