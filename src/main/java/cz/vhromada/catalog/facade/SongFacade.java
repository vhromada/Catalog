package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for songs.
 *
 * @author Vladimir Hromada
 */
public interface SongFacade {

    /**
     * Returns song with ID or null if there isn't such song.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with song or validation errors
     */
    Result<Song> get(Integer id);

    /**
     * Adds song. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>Music ID is null</li>
     * <li>Music doesn't exist in data storage</li>
     * <li>Song is null</li>
     * <li>Song ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of song is negative value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param music music
     * @param song  song
     * @return result with validation errors
     */
    Result<Void> add(Music music, Song song);

    /**
     * Updates song.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Song is null</li>
     * <li>ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of song is negative value</li>
     * <li>Note is null</li>
     * <li>Song doesn't exist in data storage</li>
     * </ul>
     *
     * @param song new value of song
     * @return result with validation errors
     */
    Result<Void> update(Song song);

    /**
     * Removes song.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Song is null</li>
     * <li>ID is null</li>
     * <li>Song doesn't exist in data storage</li>
     *
     * @param song song
     * @return result with validation errors
     */
    Result<Void> remove(Song song);

    /**
     * Duplicates song.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Song is null</li>
     * <li>ID is null</li>
     * <li>Song doesn't exist in data storage</li>
     *
     * @param song song
     * @return result with validation errors
     */
    Result<Void> duplicate(Song song);

    /**
     * Moves song in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Song is null</li>
     * <li>ID is null</li>
     * <li>Song can't be moved up</li>
     * <li>Song doesn't exist in data storage</li>
     *
     * @param song song
     * @return result with validation errors
     */
    Result<Void> moveUp(Song song);

    /**
     * Moves song in list one position down.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Song is null</li>
     * <li>ID is null</li>
     * <li>Song can't be moved down</li>
     * <li>Song doesn't exist in data storage</li>
     *
     * @param song song
     * @return result with validation errors
     */
    Result<Void> moveDown(Song song);

    /**
     * Returns songs for specified music.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID is null</li>
     * <li>Music doesn't exist in data storage</li>
     *
     * @param music music
     * @return result with songs or validation errors
     */
    Result<List<Song>> find(Music music);

}
