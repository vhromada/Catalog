package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SongToSongTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class SongToSongTOConverterTest {

	/** Instance of {@link SongToSongTOConverter} */
	private SongToSongTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new SongToSongTOConverter();
		converter.setConverter(new MusicToMusicTOConverter());
	}

	/** Test method for {@link SongToSongTOConverter#getConverter()} and {@link SongToSongTOConverter#setConverter(MusicToMusicTOConverter)}. */
	@Test
	public void testConverter() {
		final MusicToMusicTOConverter musicConverter = new MusicToMusicTOConverter();
		converter.setConverter(musicConverter);
		DeepAsserts.assertEquals(musicConverter, converter.getConverter());
	}

	/** Test method for {@link SongToSongTOConverter#convert(Song)}. */
	@Test
	public void testConvert() {
		final Song song = EntityGenerator.createSong(ID, EntityGenerator.createMusic(INNER_ID));
		final SongTO songTO = converter.convert(song);
		DeepAsserts.assertNotNull(songTO, "totalLength");
		DeepAsserts.assertEquals(song, songTO, "songsCount", "totalLength");
	}

	/** Test method for {@link SongToSongTOConverter#convert(Song)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

	/** Test method for {@link SongToSongTOConverter#convert(Song)} with not set converter for music. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetMusicConverter() {
		converter.setConverter(null);
		converter.convert(new Song());
	}

}