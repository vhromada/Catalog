//package cz.vhromada.catalog.facade.converters.spring;
//
//import static org.junit.Assert.assertNull;
//
//import cz.vhromada.catalog.dao.entities.Season;
//import cz.vhromada.catalog.facade.to.SeasonTO;
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
// * A class represents test for converter from {@link SeasonTO} to {@link Season}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
//public class SeasonTOToSeasonConverterTest {
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
//        final SeasonTO seasonTO = objectGenerator.generate(SeasonTO.class);
//        final Season season = converter.convert(seasonTO, Season.class);
//        DeepAsserts.assertNotNull(season);
//        DeepAsserts.assertEquals(seasonTO, season);
//    }
//
//    /**
//     * Test method for {@link Converter#convert(Object, Class)} with null argument.
//     */
//    @Test
//    public void testConvertWithNullArgument() {
//        assertNull(converter.convert(null, Season.class));
//    }
//
//}
