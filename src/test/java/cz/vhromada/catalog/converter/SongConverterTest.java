package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.SongUtils;

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
     * Instance of {@link SongConverter}
     */
    @Autowired
    private SongConverter converter;

    /**
     * Test method for {@link SongConverter#convert(Song)}.
     */
    @Test
    void convert() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song songDomain = converter.convert(song);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

    /**
     * Test method for {@link SongConverter#convertBack(cz.vhromada.catalog.domain.Song)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Song songDomain = SongUtils.newSongDomain(1);
        final Song song = converter.convertBack(songDomain);

        SongUtils.assertSongDeepEquals(song, songDomain);
    }

}
