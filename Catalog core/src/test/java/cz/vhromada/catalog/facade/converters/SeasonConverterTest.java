package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link Season} and {@link SeasonTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class SeasonConverterTest {

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
    public void testConvertSeason() {
        final Season season = SeasonUtils.newSeason(1);
        final SeasonTO seasonTO = converter.convert(season, SeasonTO.class);

        SeasonUtils.assertSeasonDeepEquals(seasonTO, season);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertSeason_NullArgument() {
        assertNull(converter.convert(null, SeasonTO.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertSeasonTO() {
        final SeasonTO seasonTO = SeasonUtils.newSeasonTO(1);
        final Season season = converter.convert(seasonTO, Season.class);

        assertNotNull(season);
        SeasonUtils.assertSeasonDeepEquals(seasonTO, season);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertSeasonTO_NullArgument() {
        assertNull(converter.convert(null, Season.class));
    }

}
