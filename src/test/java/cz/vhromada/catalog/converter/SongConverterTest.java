package cz.vhromada.catalog.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Song} and {@link Song}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class SongConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void convertSongDomain() {
        final cz.vhromada.catalog.domain.Song songDomain = SongUtils.newSongDomain(1);
        final Song song = converter.convert(songDomain, Song.class);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void convertSongDomain_NullArgument() {
        assertThat(converter.convert(null, Song.class), is(nullValue()));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void convertSong() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song songDomain = converter.convert(song, cz.vhromada.catalog.domain.Song.class);

        assertThat(songDomain, is(notNullValue()));
        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void convertSong_NullArgument() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Song.class), is(nullValue()));
    }

}
