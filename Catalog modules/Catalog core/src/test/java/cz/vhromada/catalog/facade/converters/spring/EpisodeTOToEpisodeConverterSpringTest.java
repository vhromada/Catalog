package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.facade.converters.EpisodeTOToEpisodeConverter;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link EpisodeTOToEpisodeConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class EpisodeTOToEpisodeConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link EpisodeTOToEpisodeConverter#convert(EpisodeTO)}. */
	@Test
	public void testConvert() {
		final EpisodeTO episodeTO = ToGenerator.createEpisode(ID, ToGenerator.createSeason(INNER_ID, ToGenerator.createSerie(INNER_INNER_ID)));
		final Episode episode = conversionService.convert(episodeTO, Episode.class);
		DeepAsserts.assertNotNull(episode);
		DeepAsserts.assertEquals(episodeTO, episode, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
	}

	/** Test method for {@link EpisodeTOToEpisodeConverter#convert(EpisodeTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Episode.class));
	}

}
