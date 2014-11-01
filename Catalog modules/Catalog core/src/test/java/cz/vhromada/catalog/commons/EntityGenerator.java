package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.NAME;
import static cz.vhromada.catalog.commons.TestConstants.NOTE;
import static cz.vhromada.catalog.commons.TestConstants.NUMBER;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;

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
