package cz.vhromada.catalog.common;

import static cz.vhromada.catalog.common.SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.BOOK_CATEGORIES_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.EPISODES_PER_SEASON_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.EPISODES_PER_SERIE_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.GAMES_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.GENRES_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.LENGTH_MULTIPLIERS;
import static cz.vhromada.catalog.common.SpringUtils.MOVIES_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.MUSIC_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.PROGRAMS_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.SEASONS_PER_SERIE_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.SERIES_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.SONGS_PER_MUSIC_COUNT;
import static cz.vhromada.catalog.common.TestConstants.AUTHOR;
import static cz.vhromada.catalog.common.TestConstants.CATEGORY;
import static cz.vhromada.catalog.common.TestConstants.CRACK;
import static cz.vhromada.catalog.common.TestConstants.CSFD;
import static cz.vhromada.catalog.common.TestConstants.CZECH_NAME;
import static cz.vhromada.catalog.common.TestConstants.EDITOR;
import static cz.vhromada.catalog.common.TestConstants.END_YEAR;
import static cz.vhromada.catalog.common.TestConstants.IMDB;
import static cz.vhromada.catalog.common.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.common.TestConstants.INNER_INNER_COUNT;
import static cz.vhromada.catalog.common.TestConstants.LANGUAGE;
import static cz.vhromada.catalog.common.TestConstants.LANGUAGES;
import static cz.vhromada.catalog.common.TestConstants.LENGTH;
import static cz.vhromada.catalog.common.TestConstants.MEDIA;
import static cz.vhromada.catalog.common.TestConstants.MEDIA_COUNT;
import static cz.vhromada.catalog.common.TestConstants.NAME;
import static cz.vhromada.catalog.common.TestConstants.NOTE;
import static cz.vhromada.catalog.common.TestConstants.NUMBER;
import static cz.vhromada.catalog.common.TestConstants.ORIGINAL_NAME;
import static cz.vhromada.catalog.common.TestConstants.OTHER_DATA;
import static cz.vhromada.catalog.common.TestConstants.PATCH;
import static cz.vhromada.catalog.common.TestConstants.PICTURE;
import static cz.vhromada.catalog.common.TestConstants.POSITION;
import static cz.vhromada.catalog.common.TestConstants.SAVES;
import static cz.vhromada.catalog.common.TestConstants.SERIAL_KEY;
import static cz.vhromada.catalog.common.TestConstants.START_YEAR;
import static cz.vhromada.catalog.common.TestConstants.SUBTITLES;
import static cz.vhromada.catalog.common.TestConstants.TITLE;
import static cz.vhromada.catalog.common.TestConstants.TOTAL_LENGTH;
import static cz.vhromada.catalog.common.TestConstants.TRAINER;
import static cz.vhromada.catalog.common.TestConstants.TRAINER_DATA;
import static cz.vhromada.catalog.common.TestConstants.WIKIPEDIA_CZ;
import static cz.vhromada.catalog.common.TestConstants.WIKIPEDIA_EN;
import static cz.vhromada.catalog.common.TestConstants.YEAR;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.to.SongTO;

/**
 * A class represents utility class for TO for Spring framework.
 *
 * @author Vladimir Hromada
 */
public final class SpringToUtils {

	/** Creates a new instance of SpringToUtils. */
	private SpringToUtils() {
	}

	/**
	 * Returns movies.
	 *
	 * @return movies
	 */
	public static List<MovieTO> getMovies() {
		final List<MovieTO> movies = new ArrayList<>();
		for (int i = 0; i < MOVIES_COUNT; i++) {
			movies.add(getMovie(i + 1));
		}
		return movies;
	}

	/**
	 * Returns TO for movie for index.
	 *
	 * @param index index
	 * @return TO for movie for index
	 */
	public static MovieTO getMovie(final int index) {
		final MovieTO movie = new MovieTO();
		movie.setId(index);
		movie.setCzechName("Movie " + index + " czech name");
		movie.setOriginalName("Movie " + index + " original name");
		movie.setYear(index + 2000);
		movie.setCsfd("Movie " + index + " CSFD");
		movie.setImdbCode(index);
		movie.setWikiEn("Movie " + index + " English Wikipedia");
		movie.setWikiCz("Movie " + index + " Czech Wikipedia");
		movie.setPicture("Movie " + index + " pc");
		movie.setNote(index == 3 ? "Movie 3 note" : "");
		movie.setPosition(index - 1);
		final List<Language> subtitles = new ArrayList<>();
		final List<Integer> media = new ArrayList<>();
		final List<GenreTO> genres = new ArrayList<>();
		final Language language;
		media.add(index * 100);
		genres.add(getGenre(index));
		switch (index) {
			case 1:
				language = Language.CZ;
				break;
			case 2:
				language = Language.JP;
				subtitles.add(Language.EN);
				break;
			case 3:
				language = Language.FR;
				subtitles.add(Language.CZ);
				subtitles.add(Language.EN);
				media.add(400);
				genres.add(getGenre(4));
				break;
			default:
				throw new IllegalArgumentException("Bad index");
		}
		movie.setLanguage(language);
		movie.setSubtitles(subtitles);
		movie.setMedia(media);
		movie.setGenres(genres);

		return movie;
	}

	/**
	 * Returns TO for movie with updated fields.
	 *
	 * @param movie TO for movie
	 * @return TO for movie with updated fields
	 */
	public static MovieTO updateMovie(final MovieTO movie) {
		movie.setCzechName(CZECH_NAME);
		movie.setOriginalName(ORIGINAL_NAME);
		movie.setYear(YEAR);
		movie.setLanguage(LANGUAGE);
		movie.setSubtitles(SUBTITLES);
		movie.setMedia(MEDIA);
		movie.setCsfd(CSFD);
		movie.setImdbCode(IMDB);
		movie.setWikiEn(WIKIPEDIA_EN);
		movie.setWikiCz(WIKIPEDIA_CZ);
		movie.setPicture(PICTURE);
		movie.setNote(NOTE);
		movie.setPosition(POSITION);
		return movie;
	}

	/**
	 * Returns series.
	 *
	 * @return series
	 */
	public static List<SerieTO> getSeries() {
		final List<SerieTO> series = new ArrayList<>();
		for (int i = 0; i < SERIES_COUNT; i++) {
			series.add(getSerie(i + 1));
		}
		return series;
	}

	/**
	 * Returns TO for serie for index.
	 *
	 * @param index index
	 * @return TO for serie for index
	 */
	public static SerieTO getSerie(final int index) {
		final SerieTO serie = new SerieTO();
		serie.setId(index);
		serie.setCzechName("Serie " + index + " czech name");
		serie.setOriginalName("Serie " + index + " original name");
		serie.setCsfd("Serie " + index + " CSFD");
		serie.setImdbCode(index * 100);
		serie.setWikiEn("Serie " + index + " English Wikipedia");
		serie.setWikiCz("Serie " + index + " Czech Wikipedia");
		serie.setPicture("Serie " + index + " pc");
		serie.setSeasonsCount(SEASONS_PER_SERIE_COUNT);
		serie.setEpisodesCount(EPISODES_PER_SERIE_COUNT);
		serie.setTotalLength(new Time(666));
		serie.setNote(index == 2 ? "Serie 2 note" : "");
		serie.setPosition(index - 1);
		final List<GenreTO> genres = new ArrayList<>();
		genres.add(getGenre(index));
		if (index == 3) {
			genres.add(getGenre(4));
		}
		serie.setGenres(genres);

		return serie;
	}

	/**
	 * Returns TO for serie with updated fields.
	 *
	 * @param serie TO for serie
	 * @return TO for serie with updated fields
	 */
	public static SerieTO updateSerie(final SerieTO serie) {
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
		return serie;
	}

	/**
	 * Returns seasons.
	 *
	 * @param serie index of TO for serie
	 * @return seasons
	 */
	public static List<SeasonTO> getSeasons(final int serie) {
		final List<SeasonTO> seasons = new ArrayList<>();
		for (int i = 0; i < SEASONS_PER_SERIE_COUNT; i++) {
			seasons.add(getSeason(serie, i + 1));
		}
		return seasons;
	}

	/**
	 * Returns TO for season for indexes.
	 *
	 * @param serieIndex  TO for serie index
	 * @param seasonIndex TO for season index
	 * @return TO for season for indexes
	 */
	public static SeasonTO getSeason(final int serieIndex, final int seasonIndex) {
		final SeasonTO season = new SeasonTO();
		season.setId((serieIndex - 1) * SEASONS_PER_SERIE_COUNT + seasonIndex);
		season.setNumber(seasonIndex);
		season.setStartYear(1980 + seasonIndex);
		season.setEndYear(seasonIndex == 3 ? 1984 : 1982);
		season.setEpisodesCount(EPISODES_PER_SEASON_COUNT);
		season.setTotalLength(new Time(6 * LENGTH_MULTIPLIERS[seasonIndex - 1]));
		season.setNote(seasonIndex == 2 ? "Serie " + serieIndex + " Season 2 note" : "");
		season.setPosition(seasonIndex - 1);
		season.setSerie(getSerie(serieIndex));
		final List<Language> subtitles = new ArrayList<>();
		final Language language;
		switch (seasonIndex) {
			case 1:
				language = Language.EN;
				subtitles.add(Language.CZ);
				subtitles.add(Language.EN);
				break;
			case 2:
				language = Language.FR;
				break;
			case 3:
				language = Language.JP;
				subtitles.add(Language.EN);
				break;
			default:
				throw new IllegalArgumentException("Bad season index");
		}
		season.setLanguage(language);
		season.setSubtitles(subtitles);

		return season;
	}

	/**
	 * Returns TO for season with updated fields.
	 *
	 * @param season TO for season
	 * @return TO for season with updated fields
	 */
	public static SeasonTO updateSeason(final SeasonTO season) {
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
	 * Returns episodes.
	 *
	 * @param serie  index of TO for serie
	 * @param season index of TO for season
	 * @return episodes
	 */
	public static List<EpisodeTO> getEpisodes(final int serie, final int season) {
		final List<EpisodeTO> episodes = new ArrayList<>();
		for (int i = 0; i < EPISODES_PER_SEASON_COUNT; i++) {
			episodes.add(getEpisode(serie, season, i + 1));
		}
		return episodes;
	}

	/**
	 * Returns TO for episode for indexes.
	 *
	 * @param serieIndex   TO for serie index
	 * @param seasonIndex  TO for season index
	 * @param episodeIndex TO for episode index
	 * @return TO for episode for indexes
	 */
	public static EpisodeTO getEpisode(final int serieIndex, final int seasonIndex, final int episodeIndex) {
		final EpisodeTO episode = new EpisodeTO();
		episode.setId((serieIndex - 1) * EPISODES_PER_SERIE_COUNT + (seasonIndex - 1) * EPISODES_PER_SEASON_COUNT + episodeIndex);
		episode.setNumber(episodeIndex);
		episode.setName("Serie " + serieIndex + " Season " + seasonIndex + " Episode " + episodeIndex);
		episode.setLength(episodeIndex * LENGTH_MULTIPLIERS[seasonIndex - 1]);
		episode.setNote(episodeIndex == 2 ? "Serie " + serieIndex + " Season " + seasonIndex + " Episode 2 note" : "");
		episode.setPosition(episodeIndex - 1);
		episode.setSeason(getSeason(serieIndex, seasonIndex));

		return episode;
	}

	/**
	 * Returns TO for episode with updated fields.
	 *
	 * @param episode TO for episode
	 * @return TO for episode with updated fields
	 */
	public static EpisodeTO updateEpisode(final EpisodeTO episode) {
		episode.setNumber(NUMBER);
		episode.setName(NAME);
		episode.setLength(LENGTH);
		episode.setNote(NOTE);
		episode.setPosition(POSITION);
		return episode;
	}

	/**
	 * Returns games.
	 *
	 * @return games
	 */
	public static List<GameTO> getGames() {
		final List<GameTO> games = new ArrayList<>();
		for (int i = 0; i < GAMES_COUNT; i++) {
			games.add(getGame(i + 1));
		}
		return games;
	}

	/**
	 * Returns TO for game for index.
	 *
	 * @param index index
	 * @return TO for game for index
	 */
	public static GameTO getGame(final int index) {
		final GameTO game = new GameTO();
		game.setId(index);
		game.setName("Game " + index + " name");
		game.setWikiEn("Game " + index + " English Wikipedia");
		game.setWikiCz("Game " + index + " Czech Wikipedia");
		game.setMediaCount(index);
		game.setCrack(index != 1);
		game.setSerialKey(index != 1);
		game.setPatch(index != 1);
		game.setTrainer(index != 1);
		game.setTrainerData(index == 3);
		game.setEditor(index == 3);
		game.setSaves(index == 3);
		game.setOtherData(index == 3 ? "Game 3 other data" : "");
		game.setNote(index == 3 ? "Game 3 note" : "");
		game.setPosition(index - 1);

		return game;
	}

	/**
	 * Returns TO for game with updated fields.
	 *
	 * @param game TO for game
	 * @return TO for game with updated fields
	 */
	public static GameTO updateGame(final GameTO game) {
		game.setName(NAME);
		game.setWikiEn(WIKIPEDIA_EN);
		game.setWikiCz(WIKIPEDIA_CZ);
		game.setMediaCount(MEDIA_COUNT);
		game.setCrack(CRACK);
		game.setSerialKey(SERIAL_KEY);
		game.setPatch(PATCH);
		game.setTrainer(TRAINER);
		game.setTrainerData(TRAINER_DATA);
		game.setEditor(EDITOR);
		game.setSaves(SAVES);
		game.setOtherData(OTHER_DATA);
		game.setNote(NOTE);
		game.setPosition(POSITION);
		return game;
	}

	/**
	 * Returns music.
	 *
	 * @return music
	 */
	public static List<MusicTO> getMusic() {
		final List<MusicTO> musics = new ArrayList<>();
		for (int i = 0; i < MUSIC_COUNT; i++) {
			musics.add(getMusic(i + 1));
		}
		return musics;
	}

	/**
	 * Returns TO for music for index.
	 *
	 * @param index index
	 * @return TO for music for index
	 */
	public static MusicTO getMusic(final int index) {
		final MusicTO music = new MusicTO();
		music.setId(index);
		music.setName("Music " + index + " name");
		music.setWikiEn("Music " + index + " English Wikipedia");
		music.setWikiCz("Music " + index + " Czech Wikipedia");
		music.setMediaCount(index * 10);
		music.setSongsCount(SONGS_PER_MUSIC_COUNT);
		music.setTotalLength(new Time(6 * LENGTH_MULTIPLIERS[index - 1]));
		music.setNote(index == 2 ? "Music 2 note" : "");
		music.setPosition(index - 1);

		return music;
	}

	/**
	 * Returns TO for music with updated fields.
	 *
	 * @param music TO for music
	 * @return TO for music with updated fields
	 */
	public static MusicTO updateMusic(final MusicTO music) {
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
	 * Returns songs.
	 *
	 * @param music index of TO for music
	 * @return songs
	 */
	public static List<SongTO> getSongs(final int music) {
		final List<SongTO> songs = new ArrayList<>();
		for (int i = 0; i < SONGS_PER_MUSIC_COUNT; i++) {
			songs.add(getSong(music, i + 1));
		}
		return songs;
	}

	/**
	 * Returns TO for song for indexes.
	 *
	 * @param musicIndex TO for music index
	 * @param songIndex  TO for song index
	 * @return TO for song for indexes
	 */
	public static SongTO getSong(final int musicIndex, final int songIndex) {
		final SongTO song = new SongTO();
		song.setId((musicIndex - 1) * SONGS_PER_MUSIC_COUNT + songIndex);
		song.setName("Music " + musicIndex + " Song " + songIndex);
		song.setLength(songIndex * LENGTH_MULTIPLIERS[musicIndex - 1]);
		song.setNote(songIndex == 2 ? "Music " + musicIndex + " Song 2 note" : "");
		song.setPosition(songIndex - 1);
		song.setMusic(getMusic(musicIndex));

		return song;
	}

	/**
	 * Returns TO for song with updated fields.
	 *
	 * @param song TO for song
	 * @return TO for song with updated fields
	 */
	public static SongTO updateSong(final SongTO song) {
		song.setName(NAME);
		song.setLength(LENGTH);
		song.setNote(NOTE);
		song.setPosition(POSITION);
		return song;
	}

	/**
	 * Returns programs.
	 *
	 * @return programs
	 */
	public static List<ProgramTO> getPrograms() {
		final List<ProgramTO> programs = new ArrayList<>();
		for (int i = 0; i < PROGRAMS_COUNT; i++) {
			programs.add(getProgram(i + 1));
		}
		return programs;
	}

	/**
	 * Returns TO for program for index.
	 *
	 * @param index index
	 * @return TO for program for index
	 */
	public static ProgramTO getProgram(final int index) {
		final ProgramTO program = new ProgramTO();
		program.setId(index);
		program.setName("Program " + index + " name");
		program.setWikiEn("Program " + index + " English Wikipedia");
		program.setWikiCz("Program " + index + " Czech Wikipedia");
		program.setMediaCount(index * 100);
		program.setCrack(index == 3);
		program.setSerialKey(index != 1);
		program.setOtherData(index == 3 ? "Program 3 other data" : "");
		program.setNote(index == 3 ? "Program 3 note" : "");
		program.setPosition(index - 1);

		return program;
	}

	/**
	 * Returns TO for program with updated fields.
	 *
	 * @param program TO for program
	 * @return TO for program with updated fields
	 */
	public static ProgramTO updateProgram(final ProgramTO program) {
		program.setName(NAME);
		program.setWikiEn(WIKIPEDIA_EN);
		program.setWikiCz(WIKIPEDIA_CZ);
		program.setMediaCount(MEDIA_COUNT);
		program.setCrack(CRACK);
		program.setSerialKey(SERIAL_KEY);
		program.setOtherData(OTHER_DATA);
		program.setNote(NOTE);
		program.setPosition(POSITION);
		return program;
	}

	/**
	 * Returns book categories.
	 *
	 * @return book categories
	 */
	public static List<BookCategoryTO> getBookCategories() {
		final List<BookCategoryTO> programs = new ArrayList<>();
		for (int i = 0; i < BOOK_CATEGORIES_COUNT; i++) {
			programs.add(getBookCategory(i + 1));
		}
		return programs;
	}

	/**
	 * Returns TO for book category for index.
	 *
	 * @param index index
	 * @return TO for book category for index
	 */
	public static BookCategoryTO getBookCategory(final int index) {
		final BookCategoryTO bookCategory = new BookCategoryTO();
		bookCategory.setId(index);
		bookCategory.setName("Book category " + index + " name");
		bookCategory.setBooksCount(BOOKS_PER_BOOK_CATEGORY_COUNT);
		bookCategory.setNote(index == 1 ? "Book category 1 note" : "");
		bookCategory.setPosition(index - 1);

		return bookCategory;
	}

	/**
	 * Returns TO for book category with updated fields.
	 *
	 * @param bookCategory TO for book category
	 * @return TO for book category with updated fields
	 */
	public static BookCategoryTO updateBookCategory(final BookCategoryTO bookCategory) {
		bookCategory.setName(NAME);
		bookCategory.setBooksCount(INNER_COUNT);
		bookCategory.setNote(NOTE);
		bookCategory.setPosition(POSITION);
		return bookCategory;
	}

	/**
	 * Returns books.
	 *
	 * @param bookCategory index of book category
	 * @return books
	 */
	public static List<BookTO> getBooks(final int bookCategory) {
		final List<BookTO> books = new ArrayList<>();
		for (int i = 0; i < BOOKS_PER_BOOK_CATEGORY_COUNT; i++) {
			books.add(getBook(bookCategory, i + 1));
		}
		return books;
	}

	/**
	 * Returns TO for book for indexes.
	 *
	 * @param bookCategoryIndex TO for book category index
	 * @param bookIndex         TO for book index
	 * @return TO for book for indexes
	 */
	public static BookTO getBook(final int bookCategoryIndex, final int bookIndex) {
		final BookTO book = new BookTO();
		book.setId((bookCategoryIndex - 1) * BOOKS_PER_BOOK_CATEGORY_COUNT + bookIndex);
		book.setAuthor("Book category " + bookCategoryIndex + " Book " + bookIndex + " author");
		book.setTitle("Book category " + bookCategoryIndex + " Book " + bookIndex + " title");
		book.setCategory("Book category " + bookCategoryIndex + " Book " + bookIndex + " category");
		book.setNote(bookIndex == 3 ? "Book category " + bookCategoryIndex + " Book 3 note" : "");
		book.setPosition(bookIndex - 1);
		book.setBookCategory(getBookCategory(bookCategoryIndex));
		final List<Language> languages = new ArrayList<>();
		switch (bookIndex) {
			case 1:
				languages.add(Language.CZ);
				break;
			case 2:
				languages.add(Language.EN);
				languages.add(Language.JP);
				break;
			case 3:
				languages.add(Language.FR);
				break;
			default:
				throw new IllegalArgumentException("Bad book index");
		}
		book.setLanguages(languages);

		return book;
	}

	/**
	 * Returns TO for book with updated fields.
	 *
	 * @param book TO for book
	 * @return TO for book with updated fields
	 */
	public static BookTO updateBook(final BookTO book) {
		book.setAuthor(AUTHOR);
		book.setTitle(TITLE);
		book.setLanguages(LANGUAGES);
		book.setCategory(CATEGORY);
		book.setNote(NOTE);
		book.setPosition(POSITION);
		return book;
	}

	/**
	 * Returns genres.
	 *
	 * @return genres
	 */
	public static List<GenreTO> getGenres() {
		final List<GenreTO> genres = new ArrayList<>();
		for (int i = 0; i < GENRES_COUNT; i++) {
			genres.add(getGenre(i + 1));
		}
		return genres;
	}

	/**
	 * Returns TO for genre for index.
	 *
	 * @param index index
	 * @return TO for genre for index
	 */
	public static GenreTO getGenre(final int index) {
		final GenreTO genre = new GenreTO();
		genre.setId(index);
		genre.setName("Genre " + index + " name");

		return genre;
	}

	/**
	 * Returns TO for genre with updated fields.
	 *
	 * @param genre TO for genre
	 * @return TO for genre with updated fields
	 */
	public static GenreTO updateGenre(final GenreTO genre) {
		genre.setName(NAME);
		return genre;
	}

}
