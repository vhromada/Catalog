package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Music} and {@link Music}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class MusicConverterTest {

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
    public void testConvertMusicDomain() {
        final cz.vhromada.catalog.domain.Music musicDomain = MusicUtils.newMusicDomain(1);
        final Music music = converter.convert(musicDomain, Music.class);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertMusicDomain_NullArgument() {
        assertNull(converter.convert(null, Music.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertMusic() {
        final Music music = MusicUtils.newMusic(1);
        final cz.vhromada.catalog.domain.Music musicDomain = converter.convert(music, cz.vhromada.catalog.domain.Music.class);

        assertNotNull(music);
        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertMusic_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Music.class));
    }

}
