package cz.vhromada.catalog.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Music} and {@link Music}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class MusicConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertMusicDomain() {
        final cz.vhromada.catalog.domain.Music musicDomain = MusicUtils.newMusicDomain(1);
        final Music music = converter.convert(musicDomain, Music.class);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null music.
     */
    @Test
    void convertMusicDomain_NullMusic() {
        assertNull(converter.convert(null, Music.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertMusic() {
        final Music music = MusicUtils.newMusic(1);
        final cz.vhromada.catalog.domain.Music musicDomain = converter.convert(music, cz.vhromada.catalog.domain.Music.class);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null music.
     */
    @Test
    void convertMusic_NullMusic() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Music.class));
    }

}
