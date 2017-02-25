package cz.vhromada.catalog.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
        assertThat(converter.convert(null, Season.class), is(nullValue()));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertSeason() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season seasonDomain = converter.convert(season, cz.vhromada.catalog.domain.Season.class);

        assertThat(seasonDomain, is(notNullValue()));
        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertSeason_NullArgument() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Season.class), is(nullValue()));
    }

}
