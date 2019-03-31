package cz.vhromada.catalog.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;

/**
 * A class represents utility class for music.
 *
 * @author Vladimir Hromada
 */
public final class MusicUtils {

    /**
     * ID
     */
    public static final int ID = 1;

    /**
     * Count of music
     */
    public static final int MUSIC_COUNT = 3;

    /**
     * Position
     */
    public static final int POSITION = 10;

    /**
     * Music name
     */
    private static final String MUSIC = "Music ";

    /**
     * Creates a new instance of MusicUtils.
     */
    private MusicUtils() {
    }

    /**
     * Returns music.
     *
     * @param id ID
     * @return music
     */
    public static cz.vhromada.catalog.domain.Music newMusicDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Music music = new cz.vhromada.catalog.domain.Music();
        updateMusic(music);
        if (id != null) {
            music.setId(id);
            music.setPosition(id - 1);
        }
        music.setSongs(Collections.emptyList());

        return music;
    }

    /**
     * Returns music with songs.
     *
     * @param id ID
     * @return music with songs
     */
    public static cz.vhromada.catalog.domain.Music newMusicWithSongs(final Integer id) {
        final Song song = SongUtils.newSongDomain(id);
        if (id == null) {
            song.setPosition(0);
        }

        final cz.vhromada.catalog.domain.Music music = newMusicDomain(id);
        music.setSongs(Collections.singletonList(song));

        return music;
    }

    /**
     * Updates music fields.
     *
     * @param music music
     */
    @SuppressWarnings("Duplicates")
    public static void updateMusic(final cz.vhromada.catalog.domain.Music music) {
        music.setName("Name");
        music.setWikiEn("enWiki");
        music.setWikiCz("czWiki");
        music.setMediaCount(1);
        music.setNote("Note");
    }

    /**
     * Returns music.
     *
     * @param id ID
     * @return music
     */
    public static Music newMusic(final Integer id) {
        final Music music = new Music();
        updateMusic(music);
        if (id != null) {
            music.setId(id);
            music.setPosition(id - 1);
        }

        return music;
    }

    /**
     * Updates music fields.
     *
     * @param music music
     */
    @SuppressWarnings("Duplicates")
    public static void updateMusic(final Music music) {
        music.setName("Name");
        music.setWikiEn("enWiki");
        music.setWikiCz("czWiki");
        music.setMediaCount(1);
        music.setNote("Note");
    }

    /**
     * Returns music.
     *
     * @return music
     */
    public static List<cz.vhromada.catalog.domain.Music> getMusic() {
        final List<cz.vhromada.catalog.domain.Music> music = new ArrayList<>();
        for (int i = 0; i < MUSIC_COUNT; i++) {
            music.add(getMusic(i + 1));
        }

        return music;
    }

    /**
     * Returns music for index.
     *
     * @param index index
     * @return music for index
     */
    public static cz.vhromada.catalog.domain.Music getMusic(final int index) {
        final cz.vhromada.catalog.domain.Music music = new cz.vhromada.catalog.domain.Music();
        music.setId(index);
        music.setName(MUSIC + index + " name");
        music.setWikiEn(MUSIC + index + " English Wikipedia");
        music.setWikiCz(MUSIC + index + " Czech Wikipedia");
        music.setMediaCount(index * 10);
        music.setNote(index == 2 ? MUSIC + "2 note" : "");
        music.setPosition(index - 1);
        music.setSongs(SongUtils.getSongs(index));

        return music;
    }

    /**
     * Returns music.
     *
     * @param entityManager entity manager
     * @param id            music ID
     * @return music
     */
    public static cz.vhromada.catalog.domain.Music getMusic(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Music.class, id);
    }

    /**
     * Returns music with updated fields.
     *
     * @param id            music ID
     * @param entityManager entity manager
     * @return music with updated fields
     */
    public static cz.vhromada.catalog.domain.Music updateMusic(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Music music = getMusic(entityManager, id);
        updateMusic(music);
        music.setPosition(POSITION);

        return music;
    }

    /**
     * Returns count of music.
     *
     * @param entityManager entity manager
     * @return count of music
     */
    public static int getMusicCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Music m", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    public static void assertMusicDeepEquals(final List<cz.vhromada.catalog.domain.Music> expected, final List<cz.vhromada.catalog.domain.Music> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertMusicDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    public static void assertMusicDeepEquals(final cz.vhromada.catalog.domain.Music expected, final cz.vhromada.catalog.domain.Music actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getName()).isEqualTo(actual.getName());
            softly.assertThat(expected.getWikiEn()).isEqualTo(actual.getWikiEn());
            softly.assertThat(expected.getWikiCz()).isEqualTo(actual.getWikiCz());
            softly.assertThat(expected.getMediaCount()).isEqualTo(actual.getMediaCount());
            softly.assertThat(expected.getNote()).isEqualTo(actual.getNote());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
            if (expected.getSongs() == null) {
                softly.assertThat(actual.getSongs()).isNull();
            } else {
                SongUtils.assertSongsDeepEquals(expected.getSongs(), actual.getSongs());
            }
        });
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected list of music
     * @param actual   actual music
     */
    public static void assertMusicListDeepEquals(final List<Music> expected, final List<cz.vhromada.catalog.domain.Music> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertMusicDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts music deep equals.
     *
     * @param expected expected music
     * @param actual   actual music
     */
    public static void assertMusicDeepEquals(final Music expected, final cz.vhromada.catalog.domain.Music actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getWikiEn()).isEqualTo(expected.getWikiEn());
            softly.assertThat(actual.getWikiCz()).isEqualTo(expected.getWikiCz());
            softly.assertThat(actual.getMediaCount()).isEqualTo(expected.getMediaCount());
            softly.assertThat(actual.getNote()).isEqualTo(expected.getNote());
            softly.assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        });
    }

}
