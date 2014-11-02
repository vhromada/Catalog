package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;

/**
 * An interface represents service for songs.
 *
 * @author Vladimir Hromada
 */
public interface SongService {

	/**
	 * Returns song with ID or null if there isn't such song.
	 *
	 * @param id ID
	 * @return song with ID or null if there isn't such song
	 * @throws IllegalArgumentException if ID is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	Song getSong(Integer id);

	/**
	 * Adds song. Sets new ID and position.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	void add(Song song);

	/**
	 * Updates song.
	 *
	 * @param song new value of song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	void update(Song song);

	/**
	 * Removes song.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	void remove(Song song);

	/**
	 * Duplicates song.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	void duplicate(Song song);

	/**
	 * Moves song in list one position up.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	void moveUp(Song song);

	/**
	 * Moves song in list one position down.
	 *
	 * @param song song
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	void moveDown(Song song);

	/**
	 * Returns true if song exists.
	 *
	 * @param song song
	 * @return true if song exists
	 * @throws IllegalArgumentException if song is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	boolean exists(Song song);

	/**
	 * Returns list of songs for specified music.
	 *
	 * @param music music
	 * @return list of songs for specified music
	 * @throws IllegalArgumentException if music is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error in working with DAO tier
	 */
	List<Song> findSongsByMusic(Music music);

	/**
	 * Returns total length of songs for specified music.
	 *
	 * @param music music
	 * @return total length of songs for specified music
	 * @throws IllegalArgumentException if music is null
	 * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
	 *                                  if there was error with working with DAO tier
	 */
	Time getTotalLengthByMusic(Music music);

}
