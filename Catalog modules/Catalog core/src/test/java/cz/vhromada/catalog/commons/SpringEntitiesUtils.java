package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.BOOK_CATEGORIES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_PER_SEASON_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_PER_SERIE_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.GAMES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.GENRES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.LENGTH_MULTIPLIERS;
import static cz.vhromada.catalog.commons.SpringUtils.MOVIES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.MUSIC_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.PROGRAMS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_PER_SERIE_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SERIES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SONGS_PER_MUSIC_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.AUTHOR;
import static cz.vhromada.catalog.commons.TestConstants.CATEGORY;
import static cz.vhromada.catalog.commons.TestConstants.CRACK;
import static cz.vhromada.catalog.commons.TestConstants.CSFD;
import static cz.vhromada.catalog.commons.TestConstants.CZECH_NAME;
import static cz.vhromada.catalog.commons.TestConstants.EDITOR;
import static cz.vhromada.catalog.commons.TestConstants.END_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.IMDB;
import static cz.vhromada.catalog.commons.TestConstants.LANGUAGE;
import static cz.vhromada.catalog.commons.TestConstants.LANGUAGES;
import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.MEDIA_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.NAME;
import static cz.vhromada.catalog.commons.TestConstants.NOTE;
import static cz.vhromada.catalog.commons.TestConstants.NUMBER;
import static cz.vhromada.catalog.commons.TestConstants.ORIGINAL_NAME;
import static cz.vhromada.catalog.commons.TestConstants.OTHER_DATA;
import static cz.vhromada.catalog.commons.TestConstants.PATCH;
import static cz.vhromada.catalog.commons.TestConstants.PICTURE;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.SAVES;
import static cz.vhromada.catalog.commons.TestConstants.SERIAL_KEY;
import static cz.vhromada.catalog.commons.TestConstants.START_YEAR;
import static cz.vhromada.catalog.commons.TestConstants.SUBTITLES;
import static cz.vhromada.catalog.commons.TestConstants.TITLE;
import static cz.vhromada.catalog.commons.TestConstants.TRAINER;
import static cz.vhromada.catalog.commons.TestConstants.TRAINER_DATA;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_CZ;
import static cz.vhromada.catalog.commons.TestConstants.WIKIPEDIA_EN;
import static cz.vhromada.catalog.commons.TestConstants.YEAR;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.generator.ObjectGenerator;

/**
 * A class represents utility class for entities for Spring framework.
 *
 * @author Vladimir Hromada
 */
public final class SpringEntitiesUtils {

	/** Creates a new instance of SpringEntitiesUtils. */
	private SpringEntitiesUtils() {
	}

	/**
	 * Returns movies.
	 *
	 * @return movies
	 */
	public static List<Movie> getMovies() {
		final List<Movie> movies = new ArrayList<>();
		for (int i = 0; i < MOVIES_COUNT; i++) {
			movies.add(getMovie(i + 1));
		}
		return movies;
	}

	/**
	 * Returns movie for index.
	 *
	 * @param index index
	 * @return movie for index
	 */
	public static Movie getMovie(final int index) {
		final Movie movie = new Movie();
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
		final List<Medium> media = new ArrayList<>();
		final List<Genre> genres = new ArrayList<>();
		final Language language;
		media.add(getMedium(index));
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
				media.add(getMedium(4));
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
	 * Returns movie with updated fields.
	 *
	 * @param movie           movie
	 * @param objectGenerator object generator
	 * @return movie with updated fields
	 */
	public static Movie updateMovie(final Movie movie, final ObjectGenerator objectGenerator) {
		movie.setCzechName(objectGenerator.generate(String.class));
		movie.setOriginalName(objectGenerator.generate(String.class));
		movie.setYear(1940 + objectGenerator.generate(Integer.class));
		movie.setLanguage(objectGenerator.generate(Language.class));
		movie.setSubtitles(CollectionUtils.newList(objectGenerator.generate(Language.class), objectGenerator.generate(Language.class)));
		movie.setCsfd(objectGenerator.generate(String.class));
		movie.setImdbCode(objectGenerator.generate(Integer.class));
		movie.setWikiEn(objectGenerator.generate(String.class));
		movie.setWikiCz(objectGenerator.generate(String.class));
		movie.setPicture(objectGenerator.generate(String.class));
		movie.setNote(objectGenerator.generate(String.class));
		movie.setPosition(objectGenerator.generate(Integer.class));
		return movie;
	}

	/**
	 * Returns movie with updated fields.
	 *
	 * @param movie movie
	 * @return movie with updated fields
	 */
	@Deprecated
	public static Movie updateMovie(final Movie movie) {
		movie.setCzechName(CZECH_NAME);
		movie.setOriginalName(ORIGINAL_NAME);
		movie.setYear(YEAR);
		movie.setLanguage(LANGUAGE);
		movie.setSubtitles(SUBTITLES);
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
	public static List<Serie> getSeries() {
		final List<Serie> series = new ArrayList<>();
		for (int i = 0; i < SERIES_COUNT; i++) {
			series.add(getSerie(i + 1));
		}
		return series;
	}

	/**
	 * Returns serie for index.
	 *
	 * @param index index
	 * @return serie for index
	 */
	public static Serie getSerie(final int index) {
		final Serie serie = new Serie();
		serie.setId(index);
		serie.setCzechName("Serie " + index + " czech name");
		serie.setOriginalName("Serie " + index + " original name");
		serie.setCsfd("Serie " + index + " CSFD");
		serie.setImdbCode(index * 100);
		serie.setWikiEn("Serie " + index + " English Wikipedia");
		serie.setWikiCz("Serie " + index + " Czech Wikipedia");
		serie.setPicture("Serie " + index + " pc");
		serie.setNote(index == 2 ? "Serie 2 note" : "");
		serie.setPosition(index - 1);
		final List<Genre> genres = new ArrayList<>();
		genres.add(getGenre(index));
		if (index == 3) {
			genres.add(getGenre(4));
		}
		serie.setGenres(genres);

		return serie;
	}

	/**
	 * Returns serie with updated fields.
	 *
	 * @param serie           serie
	 * @param objectGenerator object generator
	 * @return serie with updated fields
	 */
	public static Serie updateSerie(final Serie serie, final ObjectGenerator objectGenerator) {
		serie.setCzechName(objectGenerator.generate(String.class));
		serie.setOriginalName(objectGenerator.generate(String.class));
		serie.setCsfd(objectGenerator.generate(String.class));
		serie.setImdbCode(objectGenerator.generate(Integer.class));
		serie.setWikiEn(objectGenerator.generate(String.class));
		serie.setWikiCz(objectGenerator.generate(String.class));
		serie.setPicture(objectGenerator.generate(String.class));
		serie.setNote(objectGenerator.generate(String.class));
		serie.setPosition(objectGenerator.generate(Integer.class));
		return serie;
	}

	/**
	 * Returns serie with updated fields.
	 *
	 * @param serie serie
	 * @return serie with updated fields
	 */
	@Deprecated
	public static Serie updateSerie(final Serie serie) {
		serie.setCzechName(CZECH_NAME);
		serie.setOriginalName(ORIGINAL_NAME);
		serie.setCsfd(CSFD);
		serie.setImdbCode(IMDB);
		serie.setWikiEn(WIKIPEDIA_EN);
		serie.setWikiCz(WIKIPEDIA_CZ);
		serie.setPicture(PICTURE);
		serie.setNote(NOTE);
		serie.setPosition(POSITION);
		return serie;
	}

	/**
	 * Returns seasons.
	 *
	 * @param serie index of serie
	 * @return seasons
	 */
	public static List<Season> getSeasons(final int serie) {
		final List<Season> seasons = new ArrayList<>();
		for (int i = 0; i < SEASONS_PER_SERIE_COUNT; i++) {
			seasons.add(getSeason(serie, i + 1));
		}
		return seasons;
	}

	/**
	 * Returns season for indexes.
	 *
	 * @param serieIndex  serie index
	 * @param seasonIndex season index
	 * @return season for indexes
	 */
	public static Season getSeason(final int serieIndex, final int seasonIndex) {
		final Season season = new Season();
		season.setId((serieIndex - 1) * SEASONS_PER_SERIE_COUNT + seasonIndex);
		season.setNumber(seasonIndex);
		season.setStartYear(1980 + seasonIndex);
		season.setEndYear(seasonIndex == 3 ? 1984 : 1982);
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
	 * Returns season with updated fields.
	 *
	 * @param season          season
	 * @param objectGenerator object generator
	 * @return season with updated fields
	 */
	public static Season updateSeason(final Season season, final ObjectGenerator objectGenerator) {
		season.setNumber(objectGenerator.generate(Integer.class));
		season.setStartYear(1940 + objectGenerator.generate(Integer.class));
		season.setEndYear(1940 + objectGenerator.generate(Integer.class));
		season.setLanguage(objectGenerator.generate(Language.class));
		season.setSubtitles(CollectionUtils.newList(objectGenerator.generate(Language.class), objectGenerator.generate(Language.class)));
		season.setNote(objectGenerator.generate(String.class));
		season.setPosition(objectGenerator.generate(Integer.class));
		return season;
	}

	/**
	 * Returns season with updated fields.
	 *
	 * @param season season
	 * @return season with updated fields
	 */
	@Deprecated
	public static Season updateSeason(final Season season) {
		season.setNumber(NUMBER);
		season.setStartYear(START_YEAR);
		season.setEndYear(END_YEAR);
		season.setLanguage(LANGUAGE);
		season.setSubtitles(SUBTITLES);
		season.setNote(NOTE);
		season.setPosition(POSITION);
		return season;
	}

	/**
	 * Returns episodes.
	 *
	 * @param serie  index of serie
	 * @param season index of season
	 * @return episodes
	 */
	public static List<Episode> getEpisodes(final int serie, final int season) {
		final List<Episode> episodes = new ArrayList<>();
		for (int i = 0; i < EPISODES_PER_SEASON_COUNT; i++) {
			episodes.add(getEpisode(serie, season, i + 1));
		}
		return episodes;
	}

	/**
	 * Returns episode for indexes.
	 *
	 * @param serieIndex   serie index
	 * @param seasonIndex  season index
	 * @param episodeIndex episode index
	 * @return episode for indexes
	 */
	public static Episode getEpisode(final int serieIndex, final int seasonIndex, final int episodeIndex) {
		final Episode episode = new Episode();
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
	 * Returns episode with updated fields.
	 *
	 * @param episode         episode
	 * @param objectGenerator object generator
	 * @return episode with updated fields
	 */
	public static Episode updateEpisode(final Episode episode, final ObjectGenerator objectGenerator) {
		episode.setNumber(objectGenerator.generate(Integer.class));
		episode.setName(objectGenerator.generate(String.class));
		episode.setLength(objectGenerator.generate(Integer.class));
		episode.setNote(objectGenerator.generate(String.class));
		episode.setPosition(objectGenerator.generate(Integer.class));
		return episode;
	}

	/**
	 * Returns episode with updated fields.
	 *
	 * @param episode episode
	 * @return episode with updated fields
	 */
	@Deprecated
	public static Episode updateEpisode(final Episode episode) {
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
	public static List<Game> getGames() {
		final List<Game> games = new ArrayList<>();
		for (int i = 0; i < GAMES_COUNT; i++) {
			games.add(getGame(i + 1));
		}
		return games;
	}

	/**
	 * Returns game for index.
	 *
	 * @param index index
	 * @return game for index
	 */
	public static Game getGame(final int index) {
		final Game game = new Game();
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
	 * Returns game with updated fields.
	 *
	 * @param game            game
	 * @param objectGenerator object generator
	 * @return game with updated fields
	 */
	public static Game updateGame(final Game game, final ObjectGenerator objectGenerator) {
		game.setName(objectGenerator.generate(String.class));
		game.setWikiEn(objectGenerator.generate(String.class));
		game.setWikiCz(objectGenerator.generate(String.class));
		game.setMediaCount(objectGenerator.generate(Integer.class));
		game.setCrack(objectGenerator.generate(Boolean.class));
		game.setSerialKey(objectGenerator.generate(Boolean.class));
		game.setPatch(objectGenerator.generate(Boolean.class));
		game.setTrainer(objectGenerator.generate(Boolean.class));
		game.setTrainerData(objectGenerator.generate(Boolean.class));
		game.setEditor(objectGenerator.generate(Boolean.class));
		game.setSaves(objectGenerator.generate(Boolean.class));
		game.setOtherData(objectGenerator.generate(String.class));
		game.setNote(objectGenerator.generate(String.class));
		game.setPosition(objectGenerator.generate(Integer.class));
		return game;
	}

	/**
	 * Returns game with updated fields.
	 *
	 * @param game game
	 * @return game with updated fields
	 */
	@Deprecated
	public static Game updateGame(final Game game) {
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
	public static List<Music> getMusic() {
		final List<Music> musics = new ArrayList<>();
		for (int i = 0; i < MUSIC_COUNT; i++) {
			musics.add(getMusic(i + 1));
		}
		return musics;
	}

	/**
	 * Returns music for index.
	 *
	 * @param index index
	 * @return music for index
	 */
	public static Music getMusic(final int index) {
		final Music music = new Music();
		music.setId(index);
		music.setName("Music " + index + " name");
		music.setWikiEn("Music " + index + " English Wikipedia");
		music.setWikiCz("Music " + index + " Czech Wikipedia");
		music.setMediaCount(index * 10);
		music.setNote(index == 2 ? "Music 2 note" : "");
		music.setPosition(index - 1);

		return music;
	}

	/**
	 * Returns music with updated fields.
	 *
	 * @param music           music
	 * @param objectGenerator object generator
	 * @return music with updated fields
	 */
	public static Music updateMusic(final Music music, final ObjectGenerator objectGenerator) {
		music.setName(objectGenerator.generate(String.class));
		music.setWikiEn(objectGenerator.generate(String.class));
		music.setWikiCz(objectGenerator.generate(String.class));
		music.setMediaCount(objectGenerator.generate(Integer.class));
		music.setNote(objectGenerator.generate(String.class));
		music.setPosition(objectGenerator.generate(Integer.class));
		return music;
	}

	/**
	 * Returns music with updated fields.
	 *
	 * @param music music
	 * @return music with updated fields
	 */
	@Deprecated
	public static Music updateMusic(final Music music) {
		music.setName(NAME);
		music.setWikiEn(WIKIPEDIA_EN);
		music.setWikiCz(WIKIPEDIA_CZ);
		music.setMediaCount(MEDIA_COUNT);
		music.setNote(NOTE);
		music.setPosition(POSITION);
		return music;
	}

	/**
	 * Returns songs.
	 *
	 * @param music index of music
	 * @return songs
	 */
	public static List<Song> getSongs(final int music) {
		final List<Song> songs = new ArrayList<>();
		for (int i = 0; i < SONGS_PER_MUSIC_COUNT; i++) {
			songs.add(getSong(music, i + 1));
		}
		return songs;
	}

	/**
	 * Returns song for indexes.
	 *
	 * @param musicIndex music index
	 * @param songIndex  song index
	 * @return song for indexes
	 */
	public static Song getSong(final int musicIndex, final int songIndex) {
		final Song song = new Song();
		song.setId((musicIndex - 1) * SONGS_PER_MUSIC_COUNT + songIndex);
		song.setName("Music " + musicIndex + " Song " + songIndex);
		song.setLength(songIndex * LENGTH_MULTIPLIERS[musicIndex - 1]);
		song.setNote(songIndex == 2 ? "Music " + musicIndex + " Song 2 note" : "");
		song.setPosition(songIndex - 1);
		song.setMusic(getMusic(musicIndex));

		return song;
	}

	/**
	 * Returns song with updated fields.
	 *
	 * @param song            song
	 * @param objectGenerator object generator
	 * @return song with updated fields
	 */
	public static Song updateSong(final Song song, final ObjectGenerator objectGenerator) {
		song.setName(objectGenerator.generate(String.class));
		song.setLength(objectGenerator.generate(Integer.class));
		song.setNote(objectGenerator.generate(String.class));
		song.setPosition(objectGenerator.generate(Integer.class));
		return song;
	}

	/**
	 * Returns song with updated fields.
	 *
	 * @param song song
	 * @return song with updated fields
	 */
	@Deprecated
	public static Song updateSong(final Song song) {
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
	public static List<Program> getPrograms() {
		final List<Program> programs = new ArrayList<>();
		for (int i = 0; i < PROGRAMS_COUNT; i++) {
			programs.add(getProgram(i + 1));
		}
		return programs;
	}

	/**
	 * Returns program for index.
	 *
	 * @param index index
	 * @return program for index
	 */
	public static Program getProgram(final int index) {
		final Program program = new Program();
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
	 * Returns program with updated fields.
	 *
	 * @param program         program
	 * @param objectGenerator object generator
	 * @return program with updated fields
	 */
	public static Program updateProgram(final Program program, final ObjectGenerator objectGenerator) {
		program.setName(objectGenerator.generate(String.class));
		program.setWikiEn(objectGenerator.generate(String.class));
		program.setWikiCz(objectGenerator.generate(String.class));
		program.setMediaCount(objectGenerator.generate(Integer.class));
		program.setCrack(objectGenerator.generate(Boolean.class));
		program.setSerialKey(objectGenerator.generate(Boolean.class));
		program.setOtherData(objectGenerator.generate(String.class));
		program.setNote(objectGenerator.generate(String.class));
		program.setPosition(objectGenerator.generate(Integer.class));
		return program;
	}

	/**
	 * Returns program with updated fields.
	 *
	 * @param program program
	 * @return program with updated fields
	 */
	@Deprecated
	public static Program updateProgram(final Program program) {
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
	public static List<BookCategory> getBookCategories() {
		final List<BookCategory> programs = new ArrayList<>();
		for (int i = 0; i < BOOK_CATEGORIES_COUNT; i++) {
			programs.add(getBookCategory(i + 1));
		}
		return programs;
	}

	/**
	 * Returns book category for index.
	 *
	 * @param index index
	 * @return book category for index
	 */
	public static BookCategory getBookCategory(final int index) {
		final BookCategory bookCategory = new BookCategory();
		bookCategory.setId(index);
		bookCategory.setName("Book category " + index + " name");
		bookCategory.setNote(index == 1 ? "Book category 1 note" : "");
		bookCategory.setPosition(index - 1);

		return bookCategory;
	}

	/**
	 * Returns book category with updated fields.
	 *
	 * @param bookCategory    book category
	 * @param objectGenerator object generator
	 * @return book category with updated fields
	 */
	public static BookCategory updateBookCategory(final BookCategory bookCategory, final ObjectGenerator objectGenerator) {
		bookCategory.setName(objectGenerator.generate(String.class));
		bookCategory.setNote(objectGenerator.generate(String.class));
		bookCategory.setPosition(objectGenerator.generate(Integer.class));
		return bookCategory;
	}

	/**
	 * Returns book category with updated fields.
	 *
	 * @param bookCategory book category
	 * @return book category with updated fields
	 */
	@Deprecated
	public static BookCategory updateBookCategory(final BookCategory bookCategory) {
		bookCategory.setName(NAME);
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
	public static List<Book> getBooks(final int bookCategory) {
		final List<Book> books = new ArrayList<>();
		for (int i = 0; i < BOOKS_PER_BOOK_CATEGORY_COUNT; i++) {
			books.add(getBook(bookCategory, i + 1));
		}
		return books;
	}

	/**
	 * Returns book for indexes.
	 *
	 * @param bookCategoryIndex book category index
	 * @param bookIndex         book index
	 * @return book for indexes
	 */
	public static Book getBook(final int bookCategoryIndex, final int bookIndex) {
		final Book book = new Book();
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
	 * Returns book with updated fields.
	 *
	 * @param book            book
	 * @param objectGenerator object generator
	 * @return book with updated fields
	 */
	public static Book updateBook(final Book book, final ObjectGenerator objectGenerator) {
		book.setAuthor(objectGenerator.generate(String.class));
		book.setTitle(objectGenerator.generate(String.class));
		book.setLanguages(CollectionUtils.newList(objectGenerator.generate(Language.class), objectGenerator.generate(Language.class)));
		book.setCategory(objectGenerator.generate(String.class));
		book.setNote(objectGenerator.generate(String.class));
		book.setPosition(objectGenerator.generate(Integer.class));
		return book;
	}

	/**
	 * Returns book with updated fields.
	 *
	 * @param book book
	 * @return book with updated fields
	 */
	@Deprecated
	public static Book updateBook(final Book book) {
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
	public static List<Genre> getGenres() {
		final List<Genre> genres = new ArrayList<>();
		for (int i = 0; i < GENRES_COUNT; i++) {
			genres.add(getGenre(i + 1));
		}
		return genres;
	}

	/**
	 * Returns genre for index.
	 *
	 * @param index index
	 * @return genre for index
	 */
	public static Genre getGenre(final int index) {
		final Genre genre = new Genre();
		genre.setId(index);
		genre.setName("Genre " + index + " name");

		return genre;
	}

	/**
	 * Returns genre with updated fields.
	 *
	 * @param genre           genre
	 * @param objectGenerator object generator
	 * @return genre with updated fields
	 */
	public static Genre updateGenre(final Genre genre, final ObjectGenerator objectGenerator) {
		genre.setName(objectGenerator.generate(String.class));
		return genre;
	}

	/**
	 * Returns genre with updated fields.
	 *
	 * @param genre genre
	 * @return genre with updated fields
	 */
	@Deprecated
	public static Genre updateGenre(final Genre genre) {
		genre.setName(NAME);
		return genre;
	}

	/**
	 * Returns media.
	 *
	 * @return media
	 */
	public static List<Medium> getMedia() {
		final List<Medium> media = new ArrayList<>();
		for (int i = 0; i < SpringUtils.MEDIA_COUNT; i++) {
			media.add(getMedium(i + 1));
		}
		return media;
	}

	/**
	 * Returns medium for index.
	 *
	 * @param index index
	 * @return medium for index
	 */
	public static Medium getMedium(final int index) {
		final Medium medium = new Medium();
		medium.setId(index);
		medium.setNumber(index < 4 ? 1 : 2);
		medium.setLength(index * 100);

		return medium;
	}

	/**
	 * Returns medium with updated fields.
	 *
	 * @param medium medium
	 * @return medium with updated fields
	 */
	@Deprecated
	public static Medium updateMedium(final Medium medium) {
		medium.setNumber(NUMBER);
		medium.setLength(LENGTH);
		return medium;
	}

}
