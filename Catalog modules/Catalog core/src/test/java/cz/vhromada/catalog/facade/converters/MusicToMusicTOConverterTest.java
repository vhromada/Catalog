package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
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
public class MusicToMusicTOConverterTest extends ObjectGeneratorTest {

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
		final Music music = generate(Music.class);
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
