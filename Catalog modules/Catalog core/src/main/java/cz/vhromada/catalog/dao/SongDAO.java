package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

/**
 * An interface represents DAO for songs.
 *
 * @author Vladimir Hromada
 */
public interface SongDAO {

	/**
	 * Returns song with ID or null if there isn't such song.
	 *
	 * @param id ID
	 * @return song with ID or null if there isn't such song
	 * @throws IllegalArgumentException if ID is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	Song getSong(Integer id);

	/**
	 * Adds song. Sets new ID and position.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void add(Song song);

	/**
	 * Updates song.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void update(Song song);

	/**
	 * Removes song.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void remove(Song song);

	/**
	 * Returns list of songs for specified music.
	 *
	 * @param music music
	 * @return list of songs for specified music
	 * @throws IllegalArgumentException if music is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	List<Song> findSongsByMusic(Music music);

}
