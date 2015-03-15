package cz.vhromada.catalog.commons;

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
import cz.vhromada.generator.ObjectGenerator;

import org.joda.time.DateTime;

/**
 * A class represents TO generators.
 *
 * @author Vladimir Hromada
 */
public final class ToGenerator {

    /**
     * Creates a new instance of ToGenerator.
     */
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
     * Returns new TO for serie.
     *
     * @param objectGenerator object generator
     * @return new TO for serie
     */
    public static SerieTO newSerie(final ObjectGenerator objectGenerator) {
        return newSerie(objectGenerator, false);
    }

    /**
     * Returns new TO for serie with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for serie with ID
     */
    public static SerieTO newSerieWithId(final ObjectGenerator objectGenerator) {
        return newSerie(objectGenerator, true);
    }

    /**
     * Returns new TO for season.
     *
     * @param objectGenerator object generator
     * @return new TO for season
     */
    public static SeasonTO newSeason(final ObjectGenerator objectGenerator) {
        return newSeason(objectGenerator, false);
    }

    /**
     * Returns new TO for season with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for season with ID
     */
    public static SeasonTO newSeasonWithId(final ObjectGenerator objectGenerator) {
        return newSeason(objectGenerator, true);
    }

    /**
     * Returns new TO for episode.
     *
     * @param objectGenerator object generator
     * @return new TO for episode
     */
    public static EpisodeTO newEpisode(final ObjectGenerator objectGenerator) {
        return newEpisode(objectGenerator, false);
    }

    /**
     * Returns new TO for episode with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for episode with ID
     */
    public static EpisodeTO newEpisodeWithId(final ObjectGenerator objectGenerator) {
        return newEpisode(objectGenerator, true);
    }

    /**
     * Returns new TO for game.
     *
     * @param objectGenerator object generator
     * @return new TO for game
     */
    public static GameTO newGame(final ObjectGenerator objectGenerator) {
        return newGame(objectGenerator, false);
    }

    /**
     * Returns new TO for game with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for game with ID
     */
    public static GameTO newGameWithId(final ObjectGenerator objectGenerator) {
        return newGame(objectGenerator, true);
    }

    /**
     * Returns new TO for music.
     *
     * @param objectGenerator object generator
     * @return new TO for music
     */
    public static MusicTO newMusic(final ObjectGenerator objectGenerator) {
        return newMusic(objectGenerator, false);
    }

    /**
     * Returns new TO for music with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for music with ID
     */
    public static MusicTO newMusicWithId(final ObjectGenerator objectGenerator) {
        return newMusic(objectGenerator, true);
    }

    /**
     * Returns new TO for song.
     *
     * @param objectGenerator object generator
     * @return new TO for song
     */
    public static SongTO newSong(final ObjectGenerator objectGenerator) {
        return newSong(objectGenerator, false);
    }

    /**
     * Returns new TO for song with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for song with ID
     */
    public static SongTO newSongWithId(final ObjectGenerator objectGenerator) {
        return newSong(objectGenerator, true);
    }

    /**
     * Returns new TO for program.
     *
     * @param objectGenerator object generator
     * @return new TO for program
     */
    public static ProgramTO newProgram(final ObjectGenerator objectGenerator) {
        return newProgram(objectGenerator, false);
    }

    /**
     * Returns new TO for program with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for program with ID
     */
    public static ProgramTO newProgramWithId(final ObjectGenerator objectGenerator) {
        return newProgram(objectGenerator, true);
    }

    /**
     * Returns new TO for book category.
     *
     * @param objectGenerator object generator
     * @return new TO for book category
     */
    public static BookCategoryTO newBookCategory(final ObjectGenerator objectGenerator) {
        return newBookCategory(objectGenerator, false);
    }

    /**
     * Returns new TO for book category with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for book category with ID
     */
    public static BookCategoryTO newBookCategoryWithId(final ObjectGenerator objectGenerator) {
        return newBookCategory(objectGenerator, true);
    }

    /**
     * Returns new TO for book.
     *
     * @param objectGenerator object generator
     * @return new TO for book
     */
    public static BookTO newBook(final ObjectGenerator objectGenerator) {
        return newBook(objectGenerator, false);
    }

    /**
     * Returns new TO for book with ID.
     *
     * @param objectGenerator object generator
     * @return new TO for book with ID
     */
    public static BookTO newBookWithId(final ObjectGenerator objectGenerator) {
        return newBook(objectGenerator, true);
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
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for serie
     */
    private static SerieTO newSerie(final ObjectGenerator objectGenerator, final boolean id) {
        final SerieTO serie = objectGenerator.generate(SerieTO.class);
        if (!id) {
            serie.setId(null);
        }

        return serie;
    }

    /**
     * Returns new TO for season.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for season
     */
    private static SeasonTO newSeason(final ObjectGenerator objectGenerator, final boolean id) {
        final SeasonTO season = objectGenerator.generate(SeasonTO.class);
        season.setStartYear(objectGenerator.generate(DateTime.class).getYear());
        season.setEndYear(objectGenerator.generate(DateTime.class).getYear());
        if (!id) {
            season.setId(null);
        }

        return season;
    }

    /**
     * Returns new TO for episode.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for episode
     */
    private static EpisodeTO newEpisode(final ObjectGenerator objectGenerator, final boolean id) {
        final EpisodeTO episode = objectGenerator.generate(EpisodeTO.class);
        if (!id) {
            episode.setId(null);
        }

        return episode;
    }

    /**
     * Returns new TO for game.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for game
     */
    private static GameTO newGame(final ObjectGenerator objectGenerator, final boolean id) {
        final GameTO game = objectGenerator.generate(GameTO.class);
        if (!id) {
            game.setId(null);
        }

        return game;
    }

    /**
     * Returns new TO for music.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for music
     */
    private static MusicTO newMusic(final ObjectGenerator objectGenerator, final boolean id) {
        final MusicTO music = objectGenerator.generate(MusicTO.class);
        if (!id) {
            music.setId(null);
        }

        return music;
    }

    /**
     * Returns new TO for song.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for song
     */
    private static SongTO newSong(final ObjectGenerator objectGenerator, final boolean id) {
        final SongTO song = objectGenerator.generate(SongTO.class);
        if (!id) {
            song.setId(null);
        }

        return song;
    }

    /**
     * Returns new TO for program.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for program
     */
    private static ProgramTO newProgram(final ObjectGenerator objectGenerator, final boolean id) {
        final ProgramTO program = objectGenerator.generate(ProgramTO.class);
        if (!id) {
            program.setId(null);
        }

        return program;
    }

    /**
     * Returns new TO for book category.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for book category
     */
    private static BookCategoryTO newBookCategory(final ObjectGenerator objectGenerator, final boolean id) {
        final BookCategoryTO bookCategory = objectGenerator.generate(BookCategoryTO.class);
        if (!id) {
            bookCategory.setId(null);
        }

        return bookCategory;
    }

    /**
     * Returns new TO for book.
     *
     * @param objectGenerator object generator
     * @param id              true if id should be generated
     * @return new TO for book
     */
    private static BookTO newBook(final ObjectGenerator objectGenerator, final boolean id) {
        final BookTO book = objectGenerator.generate(BookTO.class);
        if (!id) {
            book.setId(null);
        }

        return book;
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
