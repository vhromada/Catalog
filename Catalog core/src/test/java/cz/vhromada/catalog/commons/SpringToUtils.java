package cz.vhromada.catalog.commons;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.ShowTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.generator.ObjectGenerator;

import org.joda.time.DateTime;

/**
 * A class represents utility class for TO for Spring framework.
 *
 * @author Vladimir Hromada
 */
public final class SpringToUtils {

    /**
     * Creates a new instance of SpringToUtils.
     */
    private SpringToUtils() {
    }

    /**
     * Returns movies.
     *
     * @return movies
     */
    public static List<MovieTO> getMovies() {
        final List<MovieTO> movies = new ArrayList<>();
        for (int i = 0; i < SpringUtils.MOVIES_COUNT; i++) {
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
     * Returns new TO for movie.
     *
     * @param objectGenerator object generator
     * @return new TO for movie
     */
    public static MovieTO newMovie(final ObjectGenerator objectGenerator) {
        return newMovie(objectGenerator, null);
    }

    /**
     * Returns new TO for movie with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for movie  with ID
     */
    public static MovieTO newMovieWithId(final ObjectGenerator objectGenerator) {
        return newMovie(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for movie with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              movie ID
     * @return new TO for movie with specified ID
     */
    public static MovieTO newMovie(final ObjectGenerator objectGenerator, final Integer id) {
        final MovieTO movie = objectGenerator.generate(MovieTO.class);
        movie.setId(id);
        movie.setYear(objectGenerator.generate(DateTime.class).getYear());
        movie.setGenres(CollectionUtils.newList(getGenre(4)));

        return movie;
    }

    /**
     * Returns shows.
     *
     * @return shows
     */
    public static List<ShowTO> getShows() {
        final List<ShowTO> shows = new ArrayList<>();
        for (int i = 0; i < SpringUtils.SHOWS_COUNT; i++) {
            shows.add(getShow(i + 1));
        }
        return shows;
    }

    /**
     * Returns TO for show for index.
     *
     * @param index index
     * @return TO for show for index
     */
    public static ShowTO getShow(final int index) {
        final ShowTO show = new ShowTO();
        show.setId(index);
        show.setCzechName("Show " + index + " czech name");
        show.setOriginalName("Show " + index + " original name");
        show.setCsfd("Show " + index + " CSFD");
        show.setImdbCode(index * 100);
        show.setWikiEn("Show " + index + " English Wikipedia");
        show.setWikiCz("Show " + index + " Czech Wikipedia");
        show.setPicture("Show " + index + " pc");
        show.setNote(index == 2 ? "Show 2 note" : "");
        show.setPosition(index - 1);
        final List<GenreTO> genres = new ArrayList<>();
        genres.add(getGenre(index));
        if (index == 3) {
            genres.add(getGenre(4));
        }
        show.setGenres(genres);

        return show;
    }

    /**
     * Returns new TO for show.
     *
     * @param objectGenerator object generator
     * @return new TO for show
     */
    public static ShowTO newShow(final ObjectGenerator objectGenerator) {
        return newShow(objectGenerator, null);
    }

    /**
     * Returns new TO for show with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for show  with ID
     */
    public static ShowTO newShowWithId(final ObjectGenerator objectGenerator) {
        return newShow(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for show with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              show ID
     * @return new TO for show with specified ID
     */
    public static ShowTO newShow(final ObjectGenerator objectGenerator, final Integer id) {
        final ShowTO show = objectGenerator.generate(ShowTO.class);
        show.setId(id);
        show.setGenres(CollectionUtils.newList(getGenre(4)));

        return show;
    }

    /**
     * Returns seasons.
     *
     * @param show index of TO for show
     * @return seasons
     */
    public static List<SeasonTO> getSeasons(final int show) {
        final List<SeasonTO> seasons = new ArrayList<>();
        for (int i = 0; i < SpringUtils.SEASONS_PER_SHOW_COUNT; i++) {
            seasons.add(getSeason(show, i + 1));
        }
        return seasons;
    }

    /**
     * Returns TO for season for indexes.
     *
     * @param showIndex  TO for show index
     * @param seasonIndex TO for season index
     * @return TO for season for indexes
     */
    public static SeasonTO getSeason(final int showIndex, final int seasonIndex) {
        final SeasonTO season = new SeasonTO();
        season.setId((showIndex - 1) * SpringUtils.SEASONS_PER_SHOW_COUNT + seasonIndex);
        season.setNumber(seasonIndex);
        season.setStartYear(1980 + seasonIndex);
        season.setEndYear(seasonIndex == 3 ? 1984 : 1982);
        season.setNote(seasonIndex == 2 ? "Show " + showIndex + " Season 2 note" : "");
        season.setPosition(seasonIndex - 1);
        season.setShow(getShow(showIndex));
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
     * Returns new TO for season.
     *
     * @param objectGenerator object generator
     * @return new TO for season
     */
    public static SeasonTO newSeason(final ObjectGenerator objectGenerator) {
        return newSeason(objectGenerator, null);
    }

    /**
     * Returns new TO for season with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for season  with ID
     */
    public static SeasonTO newSeasonWithId(final ObjectGenerator objectGenerator) {
        return newSeason(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for season with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              season ID
     * @return new TO for season with specified ID
     */
    public static SeasonTO newSeason(final ObjectGenerator objectGenerator, final Integer id) {
        final SeasonTO season = objectGenerator.generate(SeasonTO.class);
        season.setId(id);
        season.setStartYear(objectGenerator.generate(DateTime.class).getYear());
        season.setEndYear(objectGenerator.generate(DateTime.class).getYear());
        season.setShow(getShow(1));

        return season;
    }

    /**
     * Returns episodes.
     *
     * @param show  index of TO for show
     * @param season index of TO for season
     * @return episodes
     */
    public static List<EpisodeTO> getEpisodes(final int show, final int season) {
        final List<EpisodeTO> episodes = new ArrayList<>();
        for (int i = 0; i < SpringUtils.EPISODES_PER_SEASON_COUNT; i++) {
            episodes.add(getEpisode(show, season, i + 1));
        }
        return episodes;
    }

    /**
     * Returns TO for episode for indexes.
     *
     * @param showIndex   TO for show index
     * @param seasonIndex  TO for season index
     * @param episodeIndex TO for episode index
     * @return TO for episode for indexes
     */
    public static EpisodeTO getEpisode(final int showIndex, final int seasonIndex, final int episodeIndex) {
        final EpisodeTO episode = new EpisodeTO();
        episode.setId((showIndex - 1) * SpringUtils.EPISODES_PER_SHOW_COUNT + (seasonIndex - 1) * SpringUtils.EPISODES_PER_SEASON_COUNT + episodeIndex);
        episode.setNumber(episodeIndex);
        episode.setName("Show " + showIndex + " Season " + seasonIndex + " Episode " + episodeIndex);
        episode.setLength(episodeIndex * SpringUtils.LENGTH_MULTIPLIERS[seasonIndex - 1]);
        episode.setNote(episodeIndex == 2 ? "Show " + showIndex + " Season " + seasonIndex + " Episode 2 note" : "");
        episode.setPosition(episodeIndex - 1);
        episode.setSeason(getSeason(showIndex, seasonIndex));

        return episode;
    }

    /**
     * Returns new TO for episode.
     *
     * @param objectGenerator object generator
     * @return new TO for episode
     */
    public static EpisodeTO newEpisode(final ObjectGenerator objectGenerator) {
        return newEpisode(objectGenerator, null);
    }

    /**
     * Returns new TO for episode with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for episode  with ID
     */
    public static EpisodeTO newEpisodeWithId(final ObjectGenerator objectGenerator) {
        return newEpisode(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for episode with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              episode ID
     * @return new TO for episode with specified ID
     */
    public static EpisodeTO newEpisode(final ObjectGenerator objectGenerator, final Integer id) {
        final EpisodeTO episode = objectGenerator.generate(EpisodeTO.class);
        episode.setId(id);
        episode.setSeason(getSeason(1, 1));

        return episode;
    }

    /**
     * Returns games.
     *
     * @return games
     */
    public static List<GameTO> getGames() {
        final List<GameTO> games = new ArrayList<>();
        for (int i = 0; i < SpringUtils.GAMES_COUNT; i++) {
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
     * Returns new TO for game.
     *
     * @param objectGenerator object generator
     * @return new TO for game
     */
    public static GameTO newGame(final ObjectGenerator objectGenerator) {
        return newGame(objectGenerator, null);
    }

    /**
     * Returns new TO for game with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for game  with ID
     */
    public static GameTO newGameWithId(final ObjectGenerator objectGenerator) {
        return newGame(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for game with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              game ID
     * @return new TO for game with specified ID
     */
    public static GameTO newGame(final ObjectGenerator objectGenerator, final Integer id) {
        final GameTO game = objectGenerator.generate(GameTO.class);
        game.setId(id);

        return game;
    }

    /**
     * Returns music.
     *
     * @return music
     */
    public static List<MusicTO> getMusic() {
        final List<MusicTO> music = new ArrayList<>();
        for (int i = 0; i < SpringUtils.MUSIC_COUNT; i++) {
            music.add(getMusic(i + 1));
        }
        return music;
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
        music.setNote(index == 2 ? "Music 2 note" : "");
        music.setPosition(index - 1);

        return music;
    }

    /**
     * Returns new TO for music.
     *
     * @param objectGenerator object generator
     * @return new TO for music
     */
    public static MusicTO newMusic(final ObjectGenerator objectGenerator) {
        return newMusic(objectGenerator, null);
    }

    /**
     * Returns new TO for music with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for music  with ID
     */
    public static MusicTO newMusicWithId(final ObjectGenerator objectGenerator) {
        return newMusic(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for music with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              music ID
     * @return new TO for music with specified ID
     */
    public static MusicTO newMusic(final ObjectGenerator objectGenerator, final Integer id) {
        final MusicTO music = objectGenerator.generate(MusicTO.class);
        music.setId(id);

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
        for (int i = 0; i < SpringUtils.SONGS_PER_MUSIC_COUNT; i++) {
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
        song.setId((musicIndex - 1) * SpringUtils.SONGS_PER_MUSIC_COUNT + songIndex);
        song.setName("Music " + musicIndex + " Song " + songIndex);
        song.setLength(songIndex * SpringUtils.LENGTH_MULTIPLIERS[musicIndex - 1]);
        song.setNote(songIndex == 2 ? "Music " + musicIndex + " Song 2 note" : "");
        song.setPosition(songIndex - 1);
        song.setMusic(getMusic(musicIndex));

        return song;
    }

    /**
     * Returns new TO for song.
     *
     * @param objectGenerator object generator
     * @return new TO for song
     */
    public static SongTO newSong(final ObjectGenerator objectGenerator) {
        return newSong(objectGenerator, null);
    }

    /**
     * Returns new TO for song with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for song  with ID
     */
    public static SongTO newSongWithId(final ObjectGenerator objectGenerator) {
        return newSong(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for song with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              song ID
     * @return new TO for song with specified ID
     */
    public static SongTO newSong(final ObjectGenerator objectGenerator, final Integer id) {
        final SongTO song = objectGenerator.generate(SongTO.class);
        song.setId(id);
        song.setMusic(getMusic(1));

        return song;
    }

    /**
     * Returns programs.
     *
     * @return programs
     */
    public static List<ProgramTO> getPrograms() {
        final List<ProgramTO> programs = new ArrayList<>();
        for (int i = 0; i < SpringUtils.PROGRAMS_COUNT; i++) {
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
     * Returns new TO for program.
     *
     * @param objectGenerator object generator
     * @return new TO for program
     */
    public static ProgramTO newProgram(final ObjectGenerator objectGenerator) {
        return newProgram(objectGenerator, null);
    }

    /**
     * Returns new TO for program with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for program  with ID
     */
    public static ProgramTO newProgramWithId(final ObjectGenerator objectGenerator) {
        return newProgram(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for program with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              program ID
     * @return new TO for program with specified ID
     */
    public static ProgramTO newProgram(final ObjectGenerator objectGenerator, final Integer id) {
        final ProgramTO program = objectGenerator.generate(ProgramTO.class);
        program.setId(id);

        return program;
    }

    /**
     * Returns book categories.
     *
     * @return book categories
     */
    public static List<BookCategoryTO> getBookCategories() {
        final List<BookCategoryTO> programs = new ArrayList<>();
        for (int i = 0; i < SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
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
        bookCategory.setNote(index == 1 ? "Book category 1 note" : "");
        bookCategory.setPosition(index - 1);

        return bookCategory;
    }

    /**
     * Returns new TO for book category.
     *
     * @param objectGenerator object generator
     * @return new TO for book category
     */
    public static BookCategoryTO newBookCategory(final ObjectGenerator objectGenerator) {
        return newBookCategory(objectGenerator, null);
    }

    /**
     * Returns new TO for book category with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for boo category  with ID
     */
    public static BookCategoryTO newBookCategoryWithId(final ObjectGenerator objectGenerator) {
        return newBookCategory(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for book category with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              book category ID
     * @return new TO for book category with specified ID
     */
    public static BookCategoryTO newBookCategory(final ObjectGenerator objectGenerator, final Integer id) {
        final BookCategoryTO bookCategory = objectGenerator.generate(BookCategoryTO.class);
        bookCategory.setId(id);

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
        for (int i = 0; i < SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT; i++) {
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
        book.setId((bookCategoryIndex - 1) * SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + bookIndex);
        book.setAuthor("Book category " + bookCategoryIndex + " Book " + bookIndex + " author");
        book.setTitle("Book category " + bookCategoryIndex + " Book " + bookIndex + " title");
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
     * Returns new TO for book.
     *
     * @param objectGenerator object generator
     * @return new TO for book
     */
    public static BookTO newBook(final ObjectGenerator objectGenerator) {
        return newBook(objectGenerator, null);
    }

    /**
     * Returns new TO for book with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for book  with ID
     */
    public static BookTO newBookWithId(final ObjectGenerator objectGenerator) {
        return newBook(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for book with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              book ID
     * @return new TO for book with specified ID
     */
    public static BookTO newBook(final ObjectGenerator objectGenerator, final Integer id) {
        final BookTO book = objectGenerator.generate(BookTO.class);
        book.setId(id);
        book.setBookCategory(getBookCategory(1));

        return book;
    }

    /**
     * Returns genres.
     *
     * @return genres
     */
    public static List<GenreTO> getGenres() {
        final List<GenreTO> genres = new ArrayList<>();
        for (int i = 0; i < SpringUtils.GENRES_COUNT; i++) {
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
     * Returns new TO for genre.
     *
     * @param objectGenerator object generator
     * @return new TO for genre
     */
    public static GenreTO newGenre(final ObjectGenerator objectGenerator) {
        return newGenre(objectGenerator, null);
    }

    /**
     * Returns new TO for genre with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for genre  with ID
     */
    public static GenreTO newGenreWithId(final ObjectGenerator objectGenerator) {
        return newGenre(objectGenerator, objectGenerator.generate(Integer.class));
    }

    /**
     * Returns new TO for genre with specified ID.
     *
     * @param objectGenerator object generator
     * @param id              genre ID
     * @return new TO for genre with specified ID
     */
    public static GenreTO newGenre(final ObjectGenerator objectGenerator, final Integer id) {
        final GenreTO genre = objectGenerator.generate(GenreTO.class);
        genre.setId(id);

        return genre;
    }

}
