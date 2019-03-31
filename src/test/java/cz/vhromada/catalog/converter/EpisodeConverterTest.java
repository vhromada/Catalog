package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.utils.EpisodeUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Episode} and {@link Episode}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class EpisodeConverterTest {

    /**
     * Instance of {@link EpisodeConverter}
     */
    @Autowired
    private EpisodeConverter converter;

    /**
     * Test method for {@link EpisodeConverter#convert(Episode)}.
     */
    @Test
    void convert() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final cz.vhromada.catalog.domain.Episode episodeDomain = converter.convert(episode);

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

    /**
     * Test method for {@link EpisodeConverter#convertBack(cz.vhromada.catalog.domain.Episode)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Episode episodeDomain = EpisodeUtils.newEpisodeDomain(1);
        final Episode episode = converter.convertBack(episodeDomain);

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

}
