package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.utils.EpisodeUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Episode} and {@link Episode}.
 *
 * @author Vladimir Hromada
 */
class EpisodeMapperTest {

    /**
     * Instance of {@link EpisodeMapper}
     */
    private EpisodeMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EpisodeMapper.class);
    }

    /**
     * Test method for {@link EpisodeMapper#map(Episode)}.
     */
    @Test
    void map() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final cz.vhromada.catalog.domain.Episode episodeDomain = mapper.map(episode);

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

    /**
     * Test method for {@link EpisodeMapper#map(Episode)} with null episode.
     */
    @Test
    void map_NullEpisode() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link EpisodeMapper#mapBack(cz.vhromada.catalog.domain.Episode)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Episode episodeDomain = EpisodeUtils.newEpisodeDomain(1);
        final Episode episode = mapper.mapBack(episodeDomain);

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

    /**
     * Test method for {@link EpisodeMapper#mapBack(cz.vhromada.catalog.domain.Episode)} with null episode.
     */
    @Test
    void mapBack_NullEpisode() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
