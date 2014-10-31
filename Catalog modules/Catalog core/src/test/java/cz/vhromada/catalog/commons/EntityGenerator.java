package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.TestConstants.AUTHOR;
import static cz.vhromada.catalog.commons.TestConstants.CATEGORY;
import static cz.vhromada.catalog.commons.TestConstants.END_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.LANGUAGE;
import static cz.vhromada.catalog.commons.TestConstants.LANGUAGES;
import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.NAME;
import static cz.vhromada.catalog.commons.TestConstants.NOTE;
import static cz.vhromada.catalog.commons.TestConstants.NUMBER;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.START_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.SUBTITLES;
import static cz.vhromada.catalog.commons.TestConstants.TITLE;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.entities.Song;

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

	/**
	 * Returns new song with specified ID and music.
	 *
	 * @param id    ID
	 * @param music music
	 * @return new song with specified ID and music
	 */
	public static Song createSong(final Integer id, final Music music) {
		final Song song = new Song();
		song.setName(NAME);
		song.setLength(LENGTH);
		song.setNote(NOTE);
		song.setPosition(POSITION);
		song.setMusic(music);
		song.setId(id);
		return song;
	}

	/**
	 * Returns new book with specified ID and book category.
	 *
	 * @param id           ID
	 * @param bookCategory bookCategory
	 * @return new book with specified ID and book category
	 */
	public static Book createBook(final Integer id, final BookCategory bookCategory) {
		final Book book = new Book();
		book.setAuthor(AUTHOR);
		book.setTitle(TITLE);
		book.setLanguages(LANGUAGES);
		book.setCategory(CATEGORY);
		book.setNote(NOTE);
		book.setPosition(POSITION);
		book.setBookCategory(bookCategory);
		book.setId(id);
		return book;
	}

}
