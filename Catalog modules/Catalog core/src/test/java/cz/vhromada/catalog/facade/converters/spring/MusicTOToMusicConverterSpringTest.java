package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.converters.MusicTOToMusicConverter;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link MusicTOToMusicConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class MusicTOToMusicConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link MusicTOToMusicConverter#convert(MusicTO)}. */
	@Test
	public void testConvert() {
		final MusicTO musicTO = ToGenerator.createMusic(ID);
		final Music music = conversionService.convert(musicTO, Music.class);
		DeepAsserts.assertNotNull(music);
		DeepAsserts.assertEquals(musicTO, music, "songsCount", "totalLength");
	}

	/** Test method for {@link MusicTOToMusicConverter#convert(MusicTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Music.class));
	}

}
