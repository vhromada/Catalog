package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;

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
 * A class represents TO generators.
 *
 * @author Vladimir Hromada
 */
public final class ToGenerator {

	/** Creates a new instance of ToGenerator. */
	private ToGenerator() {
	}

	/**
	 * Returns new TO for TO for movie.
	 *
	 * @return new TO for TO for movie
	 */
	public static MovieTO createMovie() {
		final MovieTO movie = SpringToUtils.updateMovie(new MovieTO());
		movie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre(SECONDARY_INNER_ID)));
		return movie;
	}

	/**
	 * Returns new TO for movie with specified ID.
	 *
	 * @param id ID
	 * @return new TO for movie with specified ID
	 */
	public static MovieTO createMovie(final Integer id) {
		final MovieTO movie = createMovie();
		movie.setId(id);
		return movie;
	}

	/**
	 * Returns new TO for serie.
	 *
	 * @return new TO for serie
	 */
	public static SerieTO createSerie() {
		final SerieTO serie = SpringToUtils.updateSerie(new SerieTO());
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre(SECONDARY_INNER_ID)));
		return serie;
	}

	/**
	 * Returns new TO for serie with specified ID.
	 *
	 * @param id ID
	 * @return new TO for serie with specified ID
	 */
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
	public static SeasonTO createSeason() {
		return SpringToUtils.updateSeason(new SeasonTO());
	}

	/**
	 * Returns new TO for season with specified ID.
	 *
	 * @param id ID
	 * @return new TO for season with specified ID
	 */
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
	public static EpisodeTO createEpisode() {
		return SpringToUtils.updateEpisode(new EpisodeTO());
	}

	/**
	 * Creates and returns new TO for episode with specified ID.
	 *
	 * @param id ID
	 * @return new TO for episode with specified ID
	 */
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
	public static EpisodeTO createEpisode(final Integer id, final SeasonTO season) {
		final EpisodeTO episode = createEpisode(season);
		episode.setId(id);
		return episode;
	}

	/**
	 * Returns new TO for game.
	 *
	 * @return new TO for game
	 */
	public static GameTO createGame() {
		return SpringToUtils.updateGame(new GameTO());
	}

	/**
	 * Returns new TO for game with specified ID.
	 *
	 * @param id ID
	 * @return new TO for game with specified ID
	 */
	public static GameTO createGame(final Integer id) {
		final GameTO game = createGame();
		game.setId(id);
		return game;
	}

	/**
	 * Returns new TO for music.
	 *
	 * @return new TO for music
	 */
	public static MusicTO createMusic() {
		return SpringToUtils.updateMusic(new MusicTO());
	}

	/**
	 * Returns new TO for music with specified ID.
	 *
	 * @param id ID
	 * @return new TO for music with specified ID
	 */
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
	public static SongTO createSong() {
		return SpringToUtils.updateSong(new SongTO());
	}

	/**
	 * Returns new TO for song with specified ID.
	 *
	 * @param id ID
	 * @return new TO for song with specified ID
	 */
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
	public static SongTO createSong(final Integer id, final MusicTO music) {
		final SongTO song = createSong(music);
		song.setId(id);
		return song;
	}

	/**
	 * Returns new TO for program.
	 *
	 * @return new TO for program
	 */
	public static ProgramTO createProgram() {
		return SpringToUtils.updateProgram(new ProgramTO());
	}

	/**
	 * Returns new TO for program with specified ID.
	 *
	 * @param id ID
	 * @return new TO for program with specified ID
	 */
	public static ProgramTO createProgram(final Integer id) {
		final ProgramTO program = createProgram();
		program.setId(id);
		return program;
	}

	/**
	 * Returns new TO for book category.
	 *
	 * @return new TO for book category
	 */
	public static BookCategoryTO createBookCategory() {
		return SpringToUtils.updateBookCategory(new BookCategoryTO());
	}

	/**
	 * Returns new TO for book category with specified ID.
	 *
	 * @return new TO for book category with specified ID
	 */
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
	public static BookTO createBook() {
		return SpringToUtils.updateBook(new BookTO());
	}

	/**
	 * Returns new TO for book with specified ID.
	 *
	 * @param id ID
	 * @return new TO for book with specified ID
	 */
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
	public static BookTO createBook(final Integer id, final BookCategoryTO bookCategory) {
		final BookTO book = createBook(bookCategory);
		book.setId(id);
		return book;
	}

	/**
	 * Returns new TO for genre.
	 *
	 * @return new TO for genre
	 */
	public static GenreTO createGenre() {
		return SpringToUtils.updateGenre(new GenreTO());
	}

	/**
	 * Returns new TO for genre with specified ID.
	 *
	 * @param id ID
	 * @return new TO for genre with specified ID
	 */
	public static GenreTO createGenre(final Integer id) {
		final GenreTO genre = createGenre();
		genre.setId(id);
		return genre;
	}

}
