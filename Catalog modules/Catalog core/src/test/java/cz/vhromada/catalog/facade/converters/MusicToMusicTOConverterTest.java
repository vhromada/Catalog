package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MusicToMusicTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class MusicToMusicTOConverterTest {

	/** Instance of {@link MusicToMusicTOConverter} */
	private MusicToMusicTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new MusicToMusicTOConverter();
	}

	/** Test method for {@link MusicToMusicTOConverter#convert(Music)}. */
	@Test
	public void testConvert() {
		final Music music = EntityGenerator.createMusic(ID);
		final MusicTO musicTO = converter.convert(music);
		DeepAsserts.assertNotNull(musicTO, "totalLength");
		DeepAsserts.assertEquals(music, musicTO, "songsCount", "totalLength");
	}

	/** Test method for {@link MusicToMusicTOConverter#convert(Music)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
