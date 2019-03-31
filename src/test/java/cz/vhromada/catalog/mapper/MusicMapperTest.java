package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Music} and {@link Music}.
 *
 * @author Vladimir Hromada
 */
class MusicMapperTest {

    private MusicMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(MusicMapper.class);
    }

    /**
     * Test method for {@link MusicMapper#map(Music)}.
     */
    @Test
    void map() {
        final Music music = MusicUtils.newMusic(1);
        final cz.vhromada.catalog.domain.Music musicDomain = mapper.map(music);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link MusicMapper#map(Music)} with null music.
     */
    @Test
    void map_NullMusic() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link MusicMapper#mapBack(cz.vhromada.catalog.domain.Music)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Music musicDomain = MusicUtils.newMusicDomain(1);
        final Music music = mapper.mapBack(musicDomain);

        MusicUtils.assertMusicDeepEquals(music, musicDomain);
    }

    /**
     * Test method for {@link MusicMapper#mapBack(cz.vhromada.catalog.domain.Music)} with null music.
     */
    @Test
    void mapBack_NullMusic() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
