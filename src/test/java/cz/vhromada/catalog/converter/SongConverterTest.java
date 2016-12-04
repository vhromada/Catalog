package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.SongUtils;
import cz.vhromada.catalog.entity.Song;
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
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class SongConverterTest {

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
    public void testConvertSong() {
        final cz.vhromada.catalog.domain.Song song = SongUtils.newSong(1);
        final Song songTO = converter.convert(song, Song.class);

        SongUtils.assertSongDeepEquals(songTO, song);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertSong_NullArgument() {
        assertNull(converter.convert(null, Song.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertSongTO() {
        final Song songTO = SongUtils.newSongTO(1);
        final cz.vhromada.catalog.domain.Song song = converter.convert(songTO, cz.vhromada.catalog.domain.Song.class);

        assertNotNull(song);
        SongUtils.assertSongDeepEquals(songTO, song);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertSongTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Song.class));
    }

}
