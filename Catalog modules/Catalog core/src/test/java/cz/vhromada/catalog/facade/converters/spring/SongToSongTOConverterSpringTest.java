package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.converters.SongToSongTOConverter;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link SongToSongTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SongToSongTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link SongToSongTOConverter#convert(Song)}. */
	@Test
	public void testConvert() {
		final Song song = EntityGenerator.createSong(ID, EntityGenerator.createMusic(INNER_ID));
		final SongTO songTO = conversionService.convert(song, SongTO.class);
		DeepAsserts.assertNotNull(songTO, "totalLength");
		DeepAsserts.assertEquals(song, songTO, "songsCount", "totalLength");
	}

	/** Test method for {@link SongToSongTOConverter#convert(Song)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, SongTO.class));
	}

}
