package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.converters.MusicToMusicTOConverter;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link MusicToMusicTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class MusicToMusicTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link MusicToMusicTOConverter#convert(Music)}. */
	@Test
	public void testConvert() {
		final Music music = EntityGenerator.createMusic(ID);
		final MusicTO musicTO = conversionService.convert(music, MusicTO.class);
		DeepAsserts.assertNotNull(musicTO, "totalLength");
		DeepAsserts.assertEquals(music, musicTO, "songsCount", "totalLength");
	}

	/** Test method for {@link MusicToMusicTOConverter#convert(Music)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, MusicTO.class));
	}

}
