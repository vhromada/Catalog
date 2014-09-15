package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.facade.converters.EpisodeToEpisodeTOConverter;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link EpisodeToEpisodeTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class EpisodeToEpisodeTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link EpisodeToEpisodeTOConverter#convert(Episode)}. */
	@Test
	public void testConvert() {
		final Episode episode = EntityGenerator.createEpisode(ID, EntityGenerator.createSeason(INNER_ID, EntityGenerator.createSerie(INNER_INNER_ID)));
		final EpisodeTO episodeTO = conversionService.convert(episode, EpisodeTO.class);
		DeepAsserts.assertNotNull(episodeTO, "totalLength");
		DeepAsserts.assertEquals(episode, episodeTO, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
	}

	/** Test method for {@link EpisodeToEpisodeTOConverter#convert(Episode)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, EpisodeTO.class));
	}

}
