package cz.vhromada.catalog.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Season} and {@link Season}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class SeasonConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertSeasonDomain() {
        final cz.vhromada.catalog.domain.Season seasonDomain = SeasonUtils.newSeasonDomain(1);
        final Season season = converter.convert(seasonDomain, Season.class);

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null season.
     */
    @Test
    void convertSeasonDomain_NullSeason() {
        assertNull(converter.convert(null, Season.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertSeason() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season seasonDomain = converter.convert(season, cz.vhromada.catalog.domain.Season.class);

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null season.
     */
    @Test
    void convertSeason_NullSeason() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Season.class));
    }

}
