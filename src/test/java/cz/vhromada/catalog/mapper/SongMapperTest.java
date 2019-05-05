package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.SongUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Song} and {@link Song}.
 *
 * @author Vladimir Hromada
 */
class SongMapperTest {

    /**
     * Instance of {@link SongMapper}
     */
    private SongMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SongMapper.class);
    }

    /**
     * Test method for {@link SongMapper#map(Song)}.
     */
    @Test
    void map() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song songDomain = mapper.map(song);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link SongMapper#map(Song)} with null song.
     */
    @Test
    void map_NullSong() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link SongMapper#mapBack(cz.vhromada.catalog.domain.Song)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Song songDomain = SongUtils.newSongDomain(1);
        final Song song = mapper.mapBack(songDomain);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link SongMapper#mapBack(cz.vhromada.catalog.domain.Song)} with null song.
     */
    @Test
    void mapBack_NullSong() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
