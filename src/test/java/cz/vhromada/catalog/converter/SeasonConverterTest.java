package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.SeasonUtils;

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
     * Instance of {@link SeasonConverter}
     */
    @Autowired
    private SeasonConverter converter;

    /**
     * Test method for {@link SeasonConverter#convert(Season)}.
     */
    @Test
    void convert() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season seasonDomain = converter.convert(season);

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link SeasonConverter#convertBack(cz.vhromada.catalog.domain.Season)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Season seasonDomain = SeasonUtils.newSeasonDomain(1);
        final Season season = converter.convertBack(seasonDomain);

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

}
