package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.facade.converters.spring.ConvertersSpringSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.converters.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieToMovieTOConverterTest.class, MovieTOToMovieConverterTest.class, MediumToIntegerConverterTest.class,
		IntegerToMediumConverterTest.class, SerieToSerieTOConverterTest.class, SerieTOToSerieConverterTest.class, SeasonToSeasonTOConverterTest.class,
		SeasonTOToSeasonConverterTest.class, EpisodeToEpisodeTOConverterTest.class, EpisodeTOToEpisodeConverterTest.class, GameToGameTOConverterTest.class,
		GameTOToGameConverterTest.class, MusicToMusicTOConverterTest.class, MusicTOToMusicConverterTest.class, SongToSongTOConverterTest.class,
		SongTOToSongConverterTest.class, ProgramToProgramTOConverterTest.class, ProgramTOToProgramConverterTest.class,
		BookCategoryToBookCategoryTOConverterTest.class, BookCategoryTOToBookCategoryConverterTest.class, BookToBookTOConverterTest.class,
		BookTOToBookConverterTest.class, GenreToGenreTOConverterTest.class, GenreTOToGenreConverterTest.class, ConvertersSpringSuite.class })
public class ConvertersSuite {
}
