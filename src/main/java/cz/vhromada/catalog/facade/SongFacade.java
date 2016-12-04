package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;

/**
 * An interface represents facade for songs.
 *
 * @author Vladimir Hromada
 */
public interface SongFacade {

    /**
     * Returns song with ID or null if there isn't such song.
     *
     * @param id ID
     * @return song with ID or null if there isn't such song
     * @throws IllegalArgumentException if ID is null
     */
    Song getSong(Integer id);

    /**
     * Adds song. Sets new ID and position.
     *
     * @param music music
     * @param song  song
     * @throws IllegalArgumentException                                  if music is null
     *                                                                   or song is null
     *                                                                   or music ID is null
     *                                                                   or song ID isn't null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of song is negative value
     *                                                                   or note is null
     *                                                                   or music doesn't exist in data storage
     */
    void add(Music music, Song song);

    /**
     * Updates song.
     *
     * @param song new value of song
     * @throws IllegalArgumentException                                  if song is null
     *                                                                   or ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or length of song is negative value
     *                                                                   or note is null
     *                                                                   or song doesn't exist in data storage
     */
    void update(Song song);

    /**
     * Removes song.
     *
     * @param song song
     * @throws IllegalArgumentException                                  if song is null
     *                                                                   or ID is null
     *                                                                   or song doesn't exist in data storage
     */
    void remove(Song song);

    /**
     * Duplicates song.
     *
     * @param song song
     * @throws IllegalArgumentException                                  if song is null
     *                                                                   or ID is null
     *                                                                   or song doesn't exist in data storage
     */
    void duplicate(Song song);

    /**
     * Moves song in list one position up.
     *
     * @param song song
     * @throws IllegalArgumentException                                  if song is null
     *                                                                   or ID is null
     *                                                                   or song can't be moved up
     *                                                                   or song doesn't exist in data storage
     */
    void moveUp(Song song);

    /**
     * Moves song in list one position down.
     *
     * @param song song
     * @throws IllegalArgumentException                                  if song is null
     *                                                                   or ID is null
     *                                                                   or song can't be moved down
     *                                                                   or song doesn't exist in data storage
     */
    void moveDown(Song song);

    /**
     * Returns songs for specified music.
     *
     * @param music music
     * @return songs for specified music
     * @throws IllegalArgumentException                                  if music is null
     *                                                                   or ID is null
     *                                                                   or music doesn't exist in data storage
     */
    List<Song> findSongsByMusic(Music music);

}
