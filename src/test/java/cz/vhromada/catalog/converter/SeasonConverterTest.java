package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.SeasonUtils;
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
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class SeasonConverterTest {

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
    public void testConvertSeasonDomain() {
        final cz.vhromada.catalog.domain.Season seasonDomain = SeasonUtils.newSeasonDomain(1);
        final Season season = converter.convert(seasonDomain, Season.class);

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertSeasonDomain_NullArgument() {
        assertNull(converter.convert(null, Season.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertSeason() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season seasonDomain = converter.convert(season, cz.vhromada.catalog.domain.Season.class);

        assertNotNull(season);
        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertSeason_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Season.class));
    }

}
