package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
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
public class SongTOToSongConverterTest {

	/** Instance of {@link SongTOToSongConverter} */
	private SongTOToSongConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new SongTOToSongConverter();
		converter.setConverter(new MusicTOToMusicConverter());
	}

	/** Test method for {@link SongTOToSongConverter#getConverter()} and {@link SongTOToSongConverter#setConverter(MusicTOToMusicConverter)}. */
	@Test
	public void testConverter() {
		final MusicTOToMusicConverter musicConverter = new MusicTOToMusicConverter();
		converter.setConverter(musicConverter);
		DeepAsserts.assertEquals(musicConverter, converter.getConverter());
	}

	/** Test method for {@link SongTOToSongConverter#convert(SongTO)}. */
	@Test
	public void testConvert() {
		final SongTO songTO = ToGenerator.createSong(ID, ToGenerator.createMusic(INNER_ID));
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
