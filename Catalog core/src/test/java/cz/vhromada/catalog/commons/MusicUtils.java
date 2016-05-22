package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.entities.Music;

/**
 * A class represents utility class for music.
 *
 * @author Vladimir Hromada
 */
public final class MusicUtils {

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Count of music
     */
    public static final int MUSIC_COUNT = 3;

    /**
     * Position
     */
    public static final Integer POSITION = 10;

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
    public static List<Music> getMusic() {
        final List<Music> music = new ArrayList<>();
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
    public static Music getMusic(final int index) {
        final Music music = new Music();
        music.setId(index);
        music.setName("Music " + index + " name");
        music.setWikiEn("Music " + index + " English Wikipedia");
        music.setWikiCz("Music " + index + " Czech Wikipedia");
        music.setMediaCount(index * 10);
        music.setNote(index == 2 ? "Music 2 note" : "");
        music.setPosition(index - 1);

        return music;
    }

    /**
     * Returns music.
     *
     * @param entityManager entity manager
     * @param id            music ID
     * @return music
     */
    public static Music getMusic(final EntityManager entityManager, final int id) {
        return entityManager.find(Music.class, id);
    }

    /**
     * Returns music with updated fields.
     *
     * @param id            music ID
     * @param entityManager entity manager
     * @return music with updated fields
     */
    public static Music updateMusic(final int id, final EntityManager entityManager) {
        final Music music = getMusic(entityManager, id);
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
    public static void assertMusicDeepEquals(final List<Music> expected, final List<Music> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
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
    public static void assertMusicDeepEquals(final Music expected, final Music actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWikiEn(), actual.getWikiEn());
        assertEquals(expected.getWikiCz(), actual.getWikiCz());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

}