package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.TestConstants.END_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.LANGUAGE;
import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.NAME;
import static cz.vhromada.catalog.commons.TestConstants.NOTE;
import static cz.vhromada.catalog.commons.TestConstants.NUMBER;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.START_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.SUBTITLES;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;

/**
 * A class represents entity generators.
 *
 * @author Vladimir Hromada
 */
@Deprecated
public final class EntityGenerator {

	/** Creates a new instance of EntityGenerator. */
	private EntityGenerator() {
	}

	/**
	 * Returns new season with specified ID and serie.
	 *
	 * @param id    ID
	 * @param serie serie
	 * @return new season with specified ID and serie
	 */
	public static Season createSeason(final Integer id, final Serie serie) {
		final Season season = new Season();
		season.setNumber(NUMBER);
		season.setStartYear(START_YEAR);
		season.setEndYear(END_YEAR);
		season.setLanguage(LANGUAGE);
		season.setSubtitles(SUBTITLES);
		season.setNote(NOTE);
		season.setPosition(POSITION);
		season.setSerie(serie);
		season.setId(id);
		return season;
	}

	/**
	 * Creates and returns new episode with specified ID and season.
	 *
	 * @param id     ID
	 * @param season season
	 * @return new episode with specified ID and season
	 */
	public static Episode createEpisode(final Integer id, final Season season) {
		final Episode episode = new Episode();
		episode.setNumber(NUMBER);
		episode.setName(NAME);
		episode.setLength(LENGTH);
		episode.setNote(NOTE);
		episode.setPosition(POSITION);
		episode.setSeason(season);
		episode.setId(id);
		return episode;
	}

}
