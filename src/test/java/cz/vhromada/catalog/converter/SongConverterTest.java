package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.CatalogConfiguration;
import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Song} and {@link Song}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CatalogConfiguration.class, CatalogTestConfiguration.class })
public class SongConverterTest {

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
    public void testConvertSongDomain() {
        final cz.vhromada.catalog.domain.Song songDomain = SongUtils.newSongDomain(1);
        final Song song = converter.convert(songDomain, Song.class);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertSongDomain_NullArgument() {
        assertNull(converter.convert(null, Song.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertSong() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song songDomain = converter.convert(song, cz.vhromada.catalog.domain.Song.class);

        assertNotNull(song);
        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertSong_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Song.class));
    }

}
