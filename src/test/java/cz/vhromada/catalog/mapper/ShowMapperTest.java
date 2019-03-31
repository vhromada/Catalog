package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.ShowUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Show} and {@link Show}.
 *
 * @author Vladimir Hromada
 */
class ShowMapperTest {

    private ShowMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ShowMapper.class);
    }

    /**
     * Test method for {@link ShowMapper#map(Show)}.
     */
    @Test
    void map() {
        final Show show = ShowUtils.newShow(1);
        final cz.vhromada.catalog.domain.Show showDomain = mapper.map(show);

        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link ShowMapper#map(Show)} with null show.
     */
    @Test
    void map_NullShow() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link ShowMapper#mapBack(cz.vhromada.catalog.domain.Show)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Show showDomain = ShowUtils.newShowDomain(1);
        final Show show = mapper.mapBack(showDomain);

        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link ShowMapper#mapBack(cz.vhromada.catalog.domain.Show)} with null show.
     */
    @Test
    void mapBack_NullShow() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
