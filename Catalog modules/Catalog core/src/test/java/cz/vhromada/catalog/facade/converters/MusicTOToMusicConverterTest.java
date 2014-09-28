package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MusicTOToMusicConverter}.
 *
 * @author Vladimir Hromada
 */
public class MusicTOToMusicConverterTest {

	/** Instance of {@link MusicTOToMusicConverter} */
	private MusicTOToMusicConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new MusicTOToMusicConverter();
	}

	/** Test method for {@link MusicTOToMusicConverter#convert(MusicTO)}. */
	@Test
	public void testConvert() {
		final MusicTO musicTO = ToGenerator.createMusic(ID);
		final Music music = converter.convert(musicTO);
		DeepAsserts.assertNotNull(music);
		DeepAsserts.assertEquals(musicTO, music, "songsCount", "totalLength");
	}

	/** Test method for {@link MusicTOToMusicConverter#convert(MusicTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
