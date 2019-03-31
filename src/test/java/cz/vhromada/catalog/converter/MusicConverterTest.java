package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Music} and {@link Music}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class MusicConverterTest {

    /**
     * Instance of {@link MusicConverter}
     */
    @Autowired
    private MusicConverter converter;

    /**
     * Test method for {@link MusicConverter#convert(Music)}.
     */
    @Test
    void convert() {
        final Music music = MusicUtils.newMusic(1);
        final cz.vhromada.catalog.domain.Music musicDomain = converter.convert(music);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link MusicConverter#convertBack(cz.vhromada.catalog.domain.Music)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Music musicDomain = MusicUtils.newMusicDomain(1);
        final Music music = converter.convertBack(musicDomain);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

}
