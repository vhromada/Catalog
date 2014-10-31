package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.TestConstants.AUTHOR;
import static cz.vhromada.catalog.commons.TestConstants.CATEGORY;
import static cz.vhromada.catalog.commons.TestConstants.CSFD;
import static cz.vhromada.catalog.commons.TestConstants.CZECH_NAME;
import static cz.vhromada.catalog.commons.TestConstants.END_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.IMDB;
import static cz.vhromada.catalog.commons.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.LANGUAGE;
import static cz.vhromada.catalog.commons.TestConstants.LANGUAGES;
import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.MEDIA_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.NAME;
import static cz.vhromada.catalog.commons.TestConstants.NOTE;
import static cz.vhromada.catalog.commons.TestConstants.NUMBER;
import static cz.vhromada.catalog.commons.TestConstants.ORIGINAL_NAME;
import static cz.vhromada.catalog.commons.TestConstants.PICTURE;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.START_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.SUBTITLES;
import static cz.vhromada.catalog.commons.TestConstants.TITLE;
import static cz.vhromada.catalog.commons.TestConstants.TOTAL_LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_CZ;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_EN;

import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.generator.ObjectGenerator;
import org.joda.time.DateTime;

/**
 * A class represents TO generators.
 *
 * @author Vladimir Hromada
 */
public final class ToGenerator {

	/** Creates a new instance of ToGenerator. */
	private ToGenerator() {
	}

	/**
	 * Returns new TO for movie.
	 *
	 * @param objectGenerator object generator
	 * @return new TO for movie
	 */
	public static MovieTO newMovie(final ObjectGenerator objectGenerator) {
		return newMovie(objectGenerator, false);
	}

	/**
	 * Returns new TO for movie with ID.
	 *
	 * @param objectGenerator object generator
	 * @return new TO for movie with ID
	 */
	public static MovieTO newMovieWithId(final ObjectGenerator objectGenerator) {
		return newMovie(objectGenerator, true);
	}

	/**
	 * Returns new TO for movie.
	 *
	 * @param objectGenerator object generator
	 * @param id              true if id should be generated
	 * @return new TO for movie
	 */
	private static MovieTO newMovie(final ObjectGenerator objectGenerator, final boolean id) {
		final MovieTO movie = objectGenerator.generate(MovieTO.class);
		movie.setYear(objectGenerator.generate(DateTime.class).getYear());
		if (!id) {
			movie.setId(null);
		}

		return movie;
	}

	/**
	 * Returns new TO for serie.
	 *
	 * @return new TO for serie
	 */
	@Deprecated
	public static SerieTO createSerie() {
		final SerieTO serie = new SerieTO();
		serie.setCzechName(CZECH_NAME);
		serie.setOriginalName(ORIGINAL_NAME);
		serie.setCsfd(CSFD);
		serie.setImdbCode(IMDB);
		serie.setWikiEn(WIKIPEDIA_EN);
		serie.setWikiCz(WIKIPEDIA_CZ);
		serie.setPicture(PICTURE);
		serie.setSeasonsCount(INNER_COUNT);
		serie.setEpisodesCount(INNER_INNER_COUNT);
		serie.setTotalLength(TOTAL_LENGTH);
		serie.setNote(NOTE);
		serie.setPosition(POSITION);
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre(SECONDARY_INNER_ID)));
		return serie;
	}

	/**
	 * Returns new TO for genre with specified ID.
	 *
	 * @param id ID
	 * @return new TO for genre with specified ID
	 */
	@Deprecated
	private static GenreTO createGenre(final Integer id) {
		final GenreTO genre = new GenreTO();
		genre.setId(id);
		return genre;
	}

	/**
	 * Returns new TO for serie with specified ID.
	 *
	 * @param id ID
	 * @return new TO for serie with specified ID
	 */
	@Deprecated
	public static SerieTO createSerie(final Integer id) {
		final SerieTO serie = createSerie();
		serie.setId(id);
		return serie;
	}

	/**
	 * Returns new TO for season.
	 *
	 * @return new TO for season
	 */
	@Deprecated
	public static SeasonTO createSeason() {
		final SeasonTO season = new SeasonTO();
		season.setNumber(NUMBER);
		season.setStartYear(START_YEAR);
		season.setEndYear(END_YEAR);
		season.setLanguage(LANGUAGE);
		season.setSubtitles(SUBTITLES);
		season.setEpisodesCount(INNER_COUNT);
		season.setTotalLength(TOTAL_LENGTH);
		season.setNote(NOTE);
		season.setPosition(POSITION);

		return season;
	}

	/**
	 * Returns new TO for season with specified ID.
	 *
	 * @param id ID
	 * @return new TO for season with specified ID
	 */
	@Deprecated
	public static SeasonTO createSeason(final Integer id) {
		final SeasonTO season = createSeason();
		season.setId(id);
		return season;
	}

	/**
	 * Returns new TO for season with specified serie.
	 *
	 * @param serie TO for serie
	 * @return new TO for season with specified serie
	 */
	@Deprecated
	public static SeasonTO createSeason(final SerieTO serie) {
		final SeasonTO season = createSeason();
		season.setSerie(serie);
		return season;
	}

	/**
	 * Returns new TO for season with specified ID and serie.
	 *
	 * @param id    ID
	 * @param serie TO for serie
	 * @return new TO for season with specified ID and serie
	 */
	@Deprecated
	public static SeasonTO createSeason(final Integer id, final SerieTO serie) {
		final SeasonTO season = createSeason(serie);
		season.setId(id);
		return season;
	}

	/**
	 * Creates and returns new TO for episode.
	 *
	 * @return new TO for episode
	 */
	@Deprecated
	public static EpisodeTO createEpisode() {
		final EpisodeTO episode = new EpisodeTO();
		episode.setNumber(NUMBER);
		episode.setName(NAME);
		episode.setLength(LENGTH);
		episode.setNote(NOTE);
		episode.setPosition(POSITION);

		return episode;
	}

	/**
	 * Creates and returns new TO for episode with specified ID.
	 *
	 * @param id ID
	 * @return new TO for episode with specified ID
	 */
	@Deprecated
	public static EpisodeTO createEpisode(final Integer id) {
		final EpisodeTO episode = createEpisode();
		episode.setId(id);
		return episode;
	}

	/**
	 * Creates and returns new TO for episode with specified season.
	 *
	 * @param season TO for season
	 * @return new TO for episode with specified season
	 */
	@Deprecated
	public static EpisodeTO createEpisode(final SeasonTO season) {
		final EpisodeTO episode = createEpisode();
		episode.setSeason(season);
		return episode;
	}

	/**
	 * Creates and returns new TO for episode with specified ID and season.
	 *
	 * @param id     ID
	 * @param season TO for season
	 * @return new TO for episode with specified ID and season
	 */
	@Deprecated
	public static EpisodeTO createEpisode(final Integer id, final SeasonTO season) {
		final EpisodeTO episode = createEpisode(season);
		episode.setId(id);
		return episode;
	}

	/**
	 * Returns new TO for music.
	 *
	 * @return new TO for music
	 */
	@Deprecated
	public static MusicTO createMusic() {
		final MusicTO music = new MusicTO();
		music.setName(NAME);
		music.setWikiEn(WIKIPEDIA_EN);
		music.setWikiCz(WIKIPEDIA_CZ);
		music.setMediaCount(MEDIA_COUNT);
		music.setSongsCount(INNER_COUNT);
		music.setTotalLength(TOTAL_LENGTH);
		music.setNote(NOTE);
		music.setPosition(POSITION);

		return music;
	}

	/**
	 * Returns new TO for music with specified ID.
	 *
	 * @param id ID
	 * @return new TO for music with specified ID
	 */
	@Deprecated
	public static MusicTO createMusic(final Integer id) {
		final MusicTO music = createMusic();
		music.setId(id);
		return music;
	}

	/**
	 * Returns new TO for song.
	 *
	 * @return new TO for song
	 */
	@Deprecated
	public static SongTO createSong() {
		final SongTO song = new SongTO();
		song.setName(NAME);
		song.setLength(LENGTH);
		song.setNote(NOTE);
		song.setPosition(POSITION);

		return song;
	}

	/**
	 * Returns new TO for song with specified ID.
	 *
	 * @param id ID
	 * @return new TO for song with specified ID
	 */
	@Deprecated
	public static SongTO createSong(final Integer id) {
		final SongTO song = createSong();
		song.setId(id);
		return song;
	}

	/**
	 * Returns new TO for song with specified music.
	 *
	 * @param music TO for music
	 * @return new TO for song with specified music
	 */
	@Deprecated
	public static SongTO createSong(final MusicTO music) {
		final SongTO song = createSong();
		song.setMusic(music);
		return song;
	}

	/**
	 * Returns new TO for song with specified ID and music.
	 *
	 * @param id    ID
	 * @param music TO for music
	 * @return new TO for song with specified ID and music
	 */
	@Deprecated
	public static SongTO createSong(final Integer id, final MusicTO music) {
		final SongTO song = createSong(music);
		song.setId(id);
		return song;
	}

	/**
	 * Returns new TO for book category.
	 *
	 * @return new TO for book category
	 */
	@Deprecated
	public static BookCategoryTO createBookCategory() {
		final BookCategoryTO bookCategory = new BookCategoryTO();
		bookCategory.setName(NAME);
		bookCategory.setBooksCount(INNER_COUNT);
		bookCategory.setNote(NOTE);
		bookCategory.setPosition(POSITION);

		return bookCategory;
	}

	/**
	 * Returns new TO for book category with specified ID.
	 *
	 * @return new TO for book category with specified ID
	 */
	@Deprecated
	public static BookCategoryTO createBookCategory(final Integer id) {
		final BookCategoryTO bookCategory = createBookCategory();
		bookCategory.setId(id);
		return bookCategory;
	}

	/**
	 * Returns new TO for book.
	 *
	 * @return new TO for book
	 */
	@Deprecated
	public static BookTO createBook() {
		final BookTO book = new BookTO();
		book.setAuthor(AUTHOR);
		book.setTitle(TITLE);
		book.setLanguages(LANGUAGES);
		book.setCategory(CATEGORY);
		book.setNote(NOTE);
		book.setPosition(POSITION);

		return book;
	}

	/**
	 * Returns new TO for book with specified ID.
	 *
	 * @param id ID
	 * @return new TO for book with specified ID
	 */
	@Deprecated
	public static BookTO createBook(final Integer id) {
		final BookTO book = createBook();
		book.setId(id);
		return book;
	}

	/**
	 * Returns new TO for book with specified book category.
	 *
	 * @param bookCategory TO for book category
	 * @return new TO for book with specified book category
	 */
	@Deprecated
	public static BookTO createBook(final BookCategoryTO bookCategory) {
		final BookTO book = createBook();
		book.setBookCategory(bookCategory);
		return book;
	}

	/**
	 * Returns new TO for book with specified ID and book category.
	 *
	 * @param id           ID
	 * @param bookCategory TO for book category
	 * @return new TO for book with specified ID and book category
	 */
	@Deprecated
	public static BookTO createBook(final Integer id, final BookCategoryTO bookCategory) {
		final BookTO book = createBook(bookCategory);
		book.setId(id);
		return book;
	}

	/**
	 * Returns new TO for genre.
	 *
	 * @param objectGenerator object generator
	 * @return new TO for genre
	 */
	public static GenreTO newGenre(final ObjectGenerator objectGenerator) {
		return newGenre(objectGenerator, false);
	}

	/**
	 * Returns new TO for genre with ID.
	 *
	 * @param objectGenerator object generator
	 * @return new TO for genre with ID
	 */
	public static GenreTO newGenreWithId(final ObjectGenerator objectGenerator) {
		return newGenre(objectGenerator, true);
	}

	/**
	 * Returns new TO for genre.
	 *
	 * @param objectGenerator object generator
	 * @param id              true if id should be generated
	 * @return new TO for genre
	 */
	private static GenreTO newGenre(final ObjectGenerator objectGenerator, final boolean id) {
		final GenreTO genre = objectGenerator.generate(GenreTO.class);
		if (!id) {
			genre.setId(null);
		}

		return genre;
	}

}
