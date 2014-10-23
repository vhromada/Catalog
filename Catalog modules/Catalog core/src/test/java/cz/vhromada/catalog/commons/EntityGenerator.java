package cz.vhromada.catalog.commons;

import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;

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
	 * Returns new movie.
	 *
	 * @return new movie
	 */
	public static Movie createMovie() {
		final Movie movie = SpringEntitiesUtils.updateMovie(new Movie());
		movie.setMedia(CollectionUtils.newList(createMedium(INNER_ID), createMedium(SECONDARY_INNER_ID)));
		movie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre(SECONDARY_INNER_ID)));
		return movie;
	}

	/**
	 * Returns new movie with specified ID.
	 *
	 * @param id ID
	 * @return new movie with specified ID
	 */
	public static Movie createMovie(final Integer id) {
		final Movie movie = createMovie();
		movie.setId(id);
		return movie;
	}

	/**
	 * Returns new serie.
	 *
	 * @return new serie
	 */
	public static Serie createSerie() {
		final Serie serie = SpringEntitiesUtils.updateSerie(new Serie());
		serie.setGenres(CollectionUtils.newList(createGenre(INNER_ID), createGenre(SECONDARY_INNER_ID)));
		return serie;
	}

	/**
	 * Returns new serie with specified ID.
	 *
	 * @param id ID
	 * @return new serie with specified ID
	 */
	public static Serie createSerie(final Integer id) {
		final Serie serie = createSerie();
		serie.setId(id);
		return serie;
	}

	/**
	 * Returns new season.
	 *
	 * @return new season
	 */
	public static Season createSeason() {
		return SpringEntitiesUtils.updateSeason(new Season());
	}

	/**
	 * Returns new season with specified ID.
	 *
	 * @param id ID
	 * @return new season with specified ID
	 */
	public static Season createSeason(final Integer id) {
		final Season season = createSeason();
		season.setId(id);
		return season;
	}

	/**
	 * Returns new season with specified serie.
	 *
	 * @param serie serie
	 * @return new season with specified serie
	 */
	public static Season createSeason(final Serie serie) {
		final Season season = createSeason();
		season.setSerie(serie);
		return season;
	}

	/**
	 * Returns new season with specified ID and serie.
	 *
	 * @param id    ID
	 * @param serie serie
	 * @return new season with specified ID and serie
	 */
	public static Season createSeason(final Integer id, final Serie serie) {
		final Season season = createSeason(serie);
		season.setId(id);
		return season;
	}

	/**
	 * Creates and returns new episode.
	 *
	 * @return new episode
	 */
	public static Episode createEpisode() {
		return SpringEntitiesUtils.updateEpisode(new Episode());
	}

	/**
	 * Creates and returns new episode with specified ID.
	 *
	 * @param id ID
	 * @return new episode with specified ID
	 */
	public static Episode createEpisode(final Integer id) {
		final Episode episode = createEpisode();
		episode.setId(id);
		return episode;
	}

	/**
	 * Creates and returns new episode with specified season.
	 *
	 * @param season season
	 * @return new episode with specified season
	 */
	public static Episode createEpisode(final Season season) {
		final Episode episode = createEpisode();
		episode.setSeason(season);
		return episode;
	}

	/**
	 * Creates and returns new episode with specified ID and season.
	 *
	 * @param id     ID
	 * @param season season
	 * @return new episode with specified ID and season
	 */
	public static Episode createEpisode(final Integer id, final Season season) {
		final Episode episode = createEpisode(season);
		episode.setId(id);
		return episode;
	}

	/**
	 * Returns new game.
	 *
	 * @return new game
	 */
	public static Game createGame() {
		return SpringEntitiesUtils.updateGame(new Game());
	}

	/**
	 * Returns new game with specified ID.
	 *
	 * @param id ID
	 * @return new game with specified ID
	 */
	public static Game createGame(final Integer id) {
		final Game game = createGame();
		game.setId(id);
		return game;
	}

	/**
	 * Returns new music.
	 *
	 * @return new music
	 */
	public static Music createMusic() {
		return SpringEntitiesUtils.updateMusic(new Music());
	}

	/**
	 * Returns new music with specified ID.
	 *
	 * @param id ID
	 * @return new music with specified ID
	 */
	public static Music createMusic(final Integer id) {
		final Music music = createMusic();
		music.setId(id);
		return music;
	}

	/**
	 * Returns new song.
	 *
	 * @return new song
	 */
	public static Song createSong() {
		return SpringEntitiesUtils.updateSong(new Song());
	}

	/**
	 * Returns new song with specified ID.
	 *
	 * @param id ID
	 * @return new song with specified ID
	 */
	public static Song createSong(final Integer id) {
		final Song song = createSong();
		song.setId(id);
		return song;
	}

	/**
	 * Returns new song with specified music.
	 *
	 * @param music music
	 * @return new song with specified music
	 */
	public static Song createSong(final Music music) {
		final Song song = createSong();
		song.setMusic(music);
		return song;
	}

	/**
	 * Returns new song with specified ID and music.
	 *
	 * @param id    ID
	 * @param music music
	 * @return new song with specified ID and music
	 */
	public static Song createSong(final Integer id, final Music music) {
		final Song song = createSong(music);
		song.setId(id);
		return song;
	}

	/**
	 * Returns new program.
	 *
	 * @return new program
	 */
	public static Program createProgram() {
		return SpringEntitiesUtils.updateProgram(new Program());
	}

	/**
	 * Returns new program with specified ID.
	 *
	 * @param id ID
	 * @return new program with specified ID
	 */
	public static Program createProgram(final Integer id) {
		final Program program = createProgram();
		program.setId(id);
		return program;
	}

	/**
	 * Returns new book category.
	 *
	 * @return new book category
	 */
	public static BookCategory createBookCategory() {
		return SpringEntitiesUtils.updateBookCategory(new BookCategory());
	}

	/**
	 * Returns new book category with specified ID.
	 *
	 * @return new book category with specified ID
	 */
	public static BookCategory createBookCategory(final Integer id) {
		final BookCategory bookCategory = createBookCategory();
		bookCategory.setId(id);
		return bookCategory;
	}

	/**
	 * Returns new book.
	 *
	 * @return new book
	 */
	public static Book createBook() {
		return SpringEntitiesUtils.updateBook(new Book());
	}

	/**
	 * Returns new book with specified ID.
	 *
	 * @param id ID
	 * @return new book with specified ID
	 */
	public static Book createBook(final Integer id) {
		final Book book = createBook();
		book.setId(id);
		return book;
	}

	/**
	 * Returns new book with specified book category.
	 *
	 * @param bookCategory bookCategory
	 * @return new book with specified book category
	 */
	public static Book createBook(final BookCategory bookCategory) {
		final Book book = createBook();
		book.setBookCategory(bookCategory);
		return book;
	}

	/**
	 * Returns new book with specified ID and book category.
	 *
	 * @param id           ID
	 * @param bookCategory bookCategory
	 * @return new book with specified ID and book category
	 */
	public static Book createBook(final Integer id, final BookCategory bookCategory) {
		final Book book = createBook(bookCategory);
		book.setId(id);
		return book;
	}

	/**
	 * Returns new genre.
	 *
	 * @return new genre
	 */
	public static Genre createGenre() {
		return SpringEntitiesUtils.updateGenre(new Genre());
	}

	/**
	 * Returns new genre with specified ID.
	 *
	 * @param id ID
	 * @return new genre with specified ID
	 */
	public static Genre createGenre(final Integer id) {
		final Genre genre = createGenre();
		genre.setId(id);
		return genre;
	}

	/**
	 * Returns new medium.
	 *
	 * @return new medium
	 */
	public static Medium createMedium() {
		return SpringEntitiesUtils.updateMedium(new Medium());
	}

	/**
	 * Returns new medium with specified ID.
	 *
	 * @param id ID
	 * @return new medium with specified ID
	 */
	public static Medium createMedium(final Integer id) {
		final Medium medium = createMedium();
		medium.setId(id);
		return medium;
	}

}
