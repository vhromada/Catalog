package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SongTOToSongConverter}.
 *
 * @author Vladimir Hromada
 */
public class SongTOToSongConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link SongTOToSongConverter} */
    private SongTOToSongConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new SongTOToSongConverter();
        converter.setConverter(new MusicTOToMusicConverter());
    }

    /** Test method for {@link SongTOToSongConverter#convert(SongTO)}. */
    @Test
    public void testConvert() {
        final SongTO songTO = generate(SongTO.class);
        final Song song = converter.convert(songTO);
        DeepAsserts.assertNotNull(song);
        DeepAsserts.assertEquals(songTO, song, "songsCount", "totalLength");
    }

    /** Test method for {@link SongTOToSongConverter#convert(SongTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

    /** Test method for {@link SongTOToSongConverter#convert(SongTO)} with not set converter for music. */
    @Test(expected = IllegalStateException.class)
    public void testConvertWithNotSetMusicConverter() {
        converter.setConverter(null);
        converter.convert(new SongTO());
    }

}
