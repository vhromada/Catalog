package cz.vhromada.catalog.facade.converters.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.converters.spring.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieToMovieTOConverterTest.class, MovieTOToMovieConverterTest.class, MediumToIntegerConverterSpringTest.class,
        IntegerToMediaConverterSpringTest.class, ShowToShowTOConverterTest.class, ShowTOToShowConverterTest.class, SeasonToSeasonTOConverterTest.class,
        SeasonTOToSeasonConverterTest.class, EpisodeToEpisodeTOConverterTest.class, EpisodeTOToEpisodeConverterTest.class, GameToGameTOConverterTest.class,
        GameTOToGameConverterTest.class, MusicToMusicTOConverterTest.class, MusicTOToMusicConverterTest.class, SongToSongTOConverterTest.class,
        SongTOToSongConverterTest.class, ProgramToProgramTOConverterTest.class, ProgramTOToProgramConverterTest.class,
        BookCategoryToBookCategoryTOConverterTest.class, BookCategoryTOToBookCategoryConverterTest.class, BookToBookTOConverterTest.class,
        BookTOToBookConverterTest.class, GenreToGenreTOConverterTest.class, GenreTOToGenreConverterTest.class })
public class ConvertersSpringSuite {
}
