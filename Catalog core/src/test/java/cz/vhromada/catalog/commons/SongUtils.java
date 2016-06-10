package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entities.Song;

/**
 * A class represents utility class for songs.
 *
 * @author Vladimir Hromada
 */
public final class SongUtils {

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Position
     */
    public static final Integer POSITION = 10;

    /**
     * Count of songs
     */
    public static final int SONGS_COUNT = 9;

    /**
     * Multipliers for length
     */
    private static final int[] LENGTH_MULTIPLIERS = { 1, 10, 100 };

    /**
     * Count of songs in music
     */
    private static final int SONGS_PER_MUSIC_COUNT = 3;

    /**
     * Creates a new instance of SongUtils.
     */
    private SongUtils() {
    }

    /**
     * Returns song.
     *
     * @param id ID
     * @return song
     */
    public static Song newSong(final Integer id) {
        final Song song = new Song();
        updateSong(song);
        if (id != null) {
            song.setId(id);
            song.setPosition(id - 1);
        }

        return song;
    }

    /**
     * Updates song fields.
     *
     * @param song song
     */
    public static void updateSong(final Song song) {
        song.setName("Name");
        song.setLength(5);
        song.setNote("Note");
    }

    /**
     * Returns songs for music.
     *
     * @param music music
     * @return songs for music
     */
    public static List<Song> getSongs(final int music) {
        final List<Song> songs = new ArrayList<>();
        for (int i = 1; i <= SONGS_PER_MUSIC_COUNT; i++) {
            songs.add(getSong(music, i));
        }

        return songs;
    }

    /**
     * Returns song for index.
     *
     * @param index song index
     * @return song for index
     */
    public static Song getSong(final int index) {
        final int musicNumber = (index - 1) / SONGS_PER_MUSIC_COUNT + 1;
        final int songNumber = (index - 1) % SONGS_PER_MUSIC_COUNT + 1;

        return getSong(musicNumber, songNumber);
    }

    /**
     * Returns song for indexes.
     *
     * @param musicIndex music index
     * @param songIndex  song index
     * @return song for indexes
     */
    public static Song getSong(final int musicIndex, final int songIndex) {
        final Song song = new Song();
        song.setId((musicIndex - 1) * SONGS_PER_MUSIC_COUNT + songIndex);
        song.setName("Music " + musicIndex + " Song " + songIndex);
        song.setLength(songIndex * LENGTH_MULTIPLIERS[musicIndex - 1]);
        song.setNote(songIndex == 2 ? "Music " + musicIndex + " Song 2 note" : "");
        song.setPosition(songIndex - 1);

        return song;
    }

    /**
     * Returns song.
     *
     * @param entityManager entity manager
     * @param id            song ID
     * @return song
     */
    public static Song getSong(final EntityManager entityManager, final int id) {
        return entityManager.find(Song.class, id);
    }

    /**
     * Returns song with updated fields.
     *
     * @param id            song ID
     * @param entityManager entity manager
     * @return song with updated fields
     */
    public static Song updateSong(final EntityManager entityManager, final int id) {
        final Song song = getSong(entityManager, id);
        updateSong(song);
        song.setPosition(POSITION);

        return song;
    }

    /**
     * Returns count of songs.
     *
     * @param entityManager entity manager
     * @return count of songs
     */
    public static int getSongsCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Song s", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts songs deep equals.
     *
     * @param expected expected songs
     * @param actual   actual songs
     */
    public static void assertSongsDeepEquals(final List<Song> expected, final List<Song> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertSongDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts song deep equals.
     *
     * @param expected expected song
     * @param actual   actual song
     */
    public static void assertSongDeepEquals(final Song expected, final Song actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLength(), actual.getLength());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

}
