package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Episode} and {@link Episode}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class EpisodeConverterTest {

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
    public void testConvertEpisodeDomain() {
        final cz.vhromada.catalog.domain.Episode episodeDomain = EpisodeUtils.newEpisodeDomain(1);
        final Episode episode = converter.convert(episodeDomain, Episode.class);

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertEpisodeDomain_NullArgument() {
        assertNull(converter.convert(null, Episode.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertEpisode() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final cz.vhromada.catalog.domain.Episode episodeDomain = converter.convert(episode, cz.vhromada.catalog.domain.Episode.class);

        assertNotNull(episodeDomain);
        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertEpisode_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Episode.class));
    }

}
