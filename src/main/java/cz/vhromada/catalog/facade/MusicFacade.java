package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Music;

/**
 * An interface represents facade for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicFacade {

    /**
     * Creates new data.
     */
    void newData();

    /**
     * Returns music.
     *
     * @return music
     */
    List<Music> getMusic();

    /**
     * Returns music with ID or null if there isn't such music.
     *
     * @param id ID
     * @return music with ID or null if there isn't such music
     * @throws IllegalArgumentException if ID is null
     */
    Music getMusic(Integer id);

    /**
     * Adds music. Sets new ID and position.
     *
     * @param music music
     * @throws IllegalArgumentException if music is null
     *                                  or ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about music is null
     *                                  or URL to czech Wikipedia page about music is null
     *                                  or count of media isn't positive number
     *                                  or note is null
     */
    void add(Music music);

    /**
     * Updates music.
     *
     * @param music new value of music
     * @throws IllegalArgumentException if music is null
     *                                  or ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about music is null
     *                                  or URL to czech Wikipedia page about music is null
     *                                  or count of media isn't positive number
     *                                  or note is null
     *                                  or music doesn't exist in data storage
     */
    void update(Music music);

    /**
     * Removes music.
     *
     * @param music music
     * @throws IllegalArgumentException if music is null
     *                                  or ID is null
     *                                  or music doesn't exist in data storage
     */
    void remove(Music music);

    /**
     * Duplicates music.
     *
     * @param music music
     * @throws IllegalArgumentException if music is null
     *                                  or ID is null
     *                                  or music doesn't exist in data storage
     */
    void duplicate(Music music);

    /**
     * Moves music in list one position up.
     *
     * @param music music
     * @throws IllegalArgumentException if music is null
     *                                  or ID is null
     *                                  or music can't be moved up
     *                                  or music doesn't exist in data storage
     */
    void moveUp(Music music);

    /**
     * Moves music in list one position down.
     *
     * @param music music
     * @throws IllegalArgumentException if music is null
     *                                  or ID is null
     *                                  or music can't be moved down
     *                                  or music doesn't exist in data storage
     */
    void moveDown(Music music);

    /**
     * Updates positions.
     */
    void updatePositions();

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    int getTotalMediaCount();

    /**
     * Returns total length of all songs.
     *
     * @return total length of all songs
     */
    Time getTotalLength();

    /**
     * Returns count of songs from all music.
     *
     * @return count of songs from all music
     */
    int getSongsCount();

}
