package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.entity.Episode;
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
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class EpisodeConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO.
     */
    @Test
    public void testConvertEpisode() {
        final cz.vhromada.catalog.domain.Episode episode = EpisodeUtils.newEpisode(1);
        final Episode episodeTO = converter.convert(episode, Episode.class);

        EpisodeUtils.assertEpisodeDeepEquals(episodeTO, episode);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertEpisode_NullArgument() {
        assertNull(converter.convert(null, Episode.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertEpisodeTO() {
        final Episode episodeTO = EpisodeUtils.newEpisodeTO(1);
        final cz.vhromada.catalog.domain.Episode episode = converter.convert(episodeTO, cz.vhromada.catalog.domain.Episode.class);

        assertNotNull(episode);
        EpisodeUtils.assertEpisodeDeepEquals(episodeTO, episode);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertEpisodeTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Episode.class));
    }

}
