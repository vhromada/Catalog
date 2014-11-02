package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.converters.SongTOToSongConverter;
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
 * A class represents test for class {@link SongTOToSongConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SongTOToSongConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link SongTOToSongConverter#convert(SongTO)}. */
	@Test
	public void testConvert() {
		final SongTO songTO = objectGenerator.generate(SongTO.class);
		final Song song = conversionService.convert(songTO, Song.class);
		DeepAsserts.assertNotNull(song);
		DeepAsserts.assertEquals(songTO, song, "songsCount", "totalLength");
	}

	/** Test method for {@link SongTOToSongConverter#convert(SongTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Song.class));
	}

}
