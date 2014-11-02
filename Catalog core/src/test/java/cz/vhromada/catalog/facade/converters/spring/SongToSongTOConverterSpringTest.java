package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.converters.SongToSongTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SongToSongTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link cz.vhromada.catalog.facade.converters.SongToSongTOConverter#convert(Song)}. */
	@Test
	public void testConvert() {
		final Song song = objectGenerator.generate(Song.class);
		final SongTO songTO = conversionService.convert(song, SongTO.class);
		DeepAsserts.assertNotNull(songTO, "totalLength");
		DeepAsserts.assertEquals(song, songTO, "songsCount", "totalLength");
	}

	/** Test method for {@link cz.vhromada.catalog.facade.converters.SongToSongTOConverter#convert(Song)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, SongTO.class));
	}

}
