package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.SeasonUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Season} and {@link Season}.
 *
 * @author Vladimir Hromada
 */
class SeasonMapperTest {

    private SeasonMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SeasonMapper.class);
    }

    /**
     * Test method for {@link SeasonMapper#map(Season)}.
     */
    @Test
    void map() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season seasonDomain = mapper.map(season);

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link SeasonMapper#map(Season)} with null season.
     */
    @Test
    void map_NullSeason() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link SeasonMapper#mapBack(cz.vhromada.catalog.domain.Season)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Season seasonDomain = SeasonUtils.newSeasonDomain(1);
        final Season season = mapper.mapBack(seasonDomain);

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain);
    }

    /**
     * Test method for {@link SeasonMapper#mapBack(cz.vhromada.catalog.domain.Season)} with null season.
     */
    @Test
    void mapBack_NullSeason() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
