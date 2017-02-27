package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicFacade {

    /**
     * Creates new data.
     *
     * @return result
     */
    Result<Void> newData();

    /**
     * Returns music.
     *
     * @return result with list of music
     */
    Result<List<Music>> getAll();

    /**
     * Returns music with ID or null if there isn't such music.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with music or validation errors
     */
    Result<Music> get(Integer id);

    /**
     * Adds music. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about music is null</li>
     * <li>URL to czech Wikipedia page about music is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param music music
     * @return result with validation errors
     */
    Result<Void> add(Music music);

    /**
     * Updates music.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID is null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about music is null</li>
     * <li>URL to czech Wikipedia page about music is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Note is null</li>
     * <li>Music doesn't exist in data storage</li>
     * </ul>
     *
     * @param music new value of music
     * @return result with validation errors
     */
    Result<Void> update(Music music);

    /**
     * Removes music.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID is null</li>
     * <li>Music doesn't exist in data storage</li>
     *
     * @param music music
     * @return result with validation errors
     */
    Result<Void> remove(Music music);

    /**
     * Duplicates music.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID is null</li>
     * <li>Music doesn't exist in data storage</li>
     *
     * @param music music
     * @return result with validation errors
     */
    Result<Void> duplicate(Music music);

    /**
     * Moves music in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID is null</li>
     * <li>Music can't be moved up</li>
     * <li>Music doesn't exist in data storage</li>
     *
     * @param music music
     * @return result with validation errors
     */
    Result<Void> moveUp(Music music);

    /**
     * Moves music in list one position down.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID is null</li>
     * <li>Music can't be moved down</li>
     * <li>Music doesn't exist in data storage</li>
     *
     * @param music music
     * @return result with validation errors
     */
    Result<Void> moveDown(Music music);

    /**
     * Updates positions.
     *
     * @return result
     */
    Result<Void> updatePositions();

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

    /**
     * Returns total length of all songs.
     *
     * @return result with total length of all songs
     */
    Result<Time> getTotalLength();

    /**
     * Returns count of songs from all music.
     *
     * @return result with count of songs from all music
     */
    Result<Integer> getSongsCount();

}
