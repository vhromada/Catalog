package cz.vhromada.catalog.converter;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Song} and {@link Song}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class SongConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertSongDomain() {
        final cz.vhromada.catalog.domain.Song songDomain = SongUtils.newSongDomain(1);
        final Song song = converter.convert(songDomain, Song.class);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null song.
     */
    @Test
    void convertSongDomain_NullSong() {
        assertThat(converter.convert(null, Song.class)).isNull();
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertSong() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song songDomain = converter.convert(song, cz.vhromada.catalog.domain.Song.class);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null song.
     */
    @Test
    void convertSong_NullSong() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Song.class)).isNull();
    }

}
