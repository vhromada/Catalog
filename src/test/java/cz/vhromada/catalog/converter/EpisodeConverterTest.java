package cz.vhromada.catalog.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.converter.Converter;

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
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertEpisodeDomain() {
        final cz.vhromada.catalog.domain.Episode episodeDomain = EpisodeUtils.newEpisodeDomain(1);
        final Episode episode = converter.convert(episodeDomain, Episode.class);

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null episode.
     */
    @Test
    void convertEpisodeDomain_NullEpisode() {
        assertNull(converter.convert(null, Episode.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertEpisode() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final cz.vhromada.catalog.domain.Episode episodeDomain = converter.convert(episode, cz.vhromada.catalog.domain.Episode.class);

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null episode.
     */
    @Test
    void convertEpisode_NullEpisode() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Episode.class));
    }

}
