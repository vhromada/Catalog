package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Music;

/**
 * An interface represents DAO for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicDAO {

	/**
	 * Returns list of music.
	 *
	 * @return list of music
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *          if there was error with working with data storage
	 */
	List<Music> getMusic();

	/**
	 * Returns music with ID or null if there isn't such music.
	 *
	 * @param id ID
	 * @return music with ID or null if there isn't such music
	 * @throws IllegalArgumentException if ID is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	Music getMusic(Integer id);

	/**
	 * Adds music. Sets new ID and position.
	 *
	 * @param music music
	 * @throws IllegalArgumentException if music is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void add(Music music);

	/**
	 * Updates music.
	 *
	 * @param music music
	 * @throws IllegalArgumentException if music is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void update(Music music);

	/**
	 * Removes music.
	 *
	 * @param music music
	 * @throws IllegalArgumentException if music is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void remove(Music music);

}
