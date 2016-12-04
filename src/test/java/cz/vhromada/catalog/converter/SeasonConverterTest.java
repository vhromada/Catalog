package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Season} and {@link Season}.
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
        final cz.vhromada.catalog.domain.Season season = SeasonUtils.newSeason(1);
        final Season seasonTO = converter.convert(season, Season.class);

        SeasonUtils.assertSeasonDeepEquals(seasonTO, season);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertSeason_NullArgument() {
        assertNull(converter.convert(null, Season.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertSeasonTO() {
        final Season seasonTO = SeasonUtils.newSeasonTO(1);
        final cz.vhromada.catalog.domain.Season season = converter.convert(seasonTO, cz.vhromada.catalog.domain.Season.class);

        assertNotNull(season);
        SeasonUtils.assertSeasonDeepEquals(seasonTO, season);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertSeasonTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Season.class));
    }

}
