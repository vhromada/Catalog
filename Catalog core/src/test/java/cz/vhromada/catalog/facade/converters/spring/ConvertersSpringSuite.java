package cz.vhromada.catalog.facade.converters.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.converters.spring.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieToMovieTOConverterSpringTest.class, MovieTOToMovieConverterSpringTest.class, MediumToIntegerConverterSpringTest.class,
        IntegerToMediumConverterSpringTest.class, SerieToSerieTOConverterSpringTest.class, SerieTOToSerieConverterSpringTest.class,
        SeasonToSeasonTOConverterSpringTest.class, SeasonTOToSeasonConverterSpringTest.class, EpisodeToEpisodeTOConverterSpringTest.class,
        EpisodeTOToEpisodeConverterSpringTest.class, GameToGameTOConverterSpringTest.class, GameTOToGameConverterSpringTest.class,
        MusicToMusicTOConverterSpringTest.class, MusicTOToMusicConverterSpringTest.class, SongToSongTOConverterSpringTest.class,
        SongTOToSongConverterSpringTest.class, ProgramToProgramTOConverterSpringTest.class, ProgramTOToProgramConverterSpringTest.class,
        BookCategoryToBookCategoryTOConverterSpringTest.class, BookCategoryTOToBookCategoryConverterSpringTest.class, BookToBookTOConverterSpringTest.class,
        BookTOToBookConverterSpringTest.class, GenreToGenreTOConverterSpringTest.class, GenreTOToGenreConverterSpringTest.class })
public class ConvertersSpringSuite {
}
