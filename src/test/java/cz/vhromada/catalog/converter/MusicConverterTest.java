package cz.vhromada.catalog.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void convertMusicDomain() {
        final cz.vhromada.catalog.domain.Music musicDomain = MusicUtils.newMusicDomain(1);
        final Music music = converter.convert(musicDomain, Music.class);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void convertMusicDomain_NullArgument() {
        assertThat(converter.convert(null, Music.class), is(nullValue()));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void convertMusic() {
        final Music music = MusicUtils.newMusic(1);
        final cz.vhromada.catalog.domain.Music musicDomain = converter.convert(music, cz.vhromada.catalog.domain.Music.class);

        assertThat(musicDomain, is(notNullValue()));
        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void convertMusic_NullArgument() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Music.class), is(nullValue()));
    }

}
