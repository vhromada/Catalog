package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Genre} and {@link Genre}.
 *
 * @author Vladimir Hromada
 */
class GenreMapperTest {

    /**
     * Instance of {@link GenreMapper}
     */
    private GenreMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(GenreMapper.class);
    }

    /**
     * Test method for {@link GenreMapper#map(Genre)}.
     */
    @Test
    void map() {
        final Genre genre = GenreUtils.newGenre(1);
        final cz.vhromada.catalog.domain.Genre genreDomain = mapper.map(genre);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link GenreMapper#map(Genre)} with null genre.
     */
    @Test
    void map_NullGenre() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link GenreMapper#mapBack(cz.vhromada.catalog.domain.Genre)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Genre genreDomain = GenreUtils.newGenreDomain(1);
        final Genre genre = mapper.mapBack(genreDomain);

        GenreUtils.assertGenreDeepEquals(genre, genreDomain);
    }

    /**
     * Test method for {@link GenreMapper#mapBack(cz.vhromada.catalog.domain.Genre)} with null genre.
     */
    @Test
    void mapBack_NullGenre() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
