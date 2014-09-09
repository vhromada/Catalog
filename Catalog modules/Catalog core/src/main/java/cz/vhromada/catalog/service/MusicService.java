package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;

/**
 * An interface represents service for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicService {

	/**
	 * Creates new data.
	 *
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void newData();

	/**
	 * Returns list of music.
	 *
	 * @return list of music
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	List<Music> getMusic();

	/**
	 * Returns music with ID or null if there isn't such music.
	 *
	 * @param id ID
	 * @return music with ID or null if there isn't such music
	 * @throws IllegalArgumentException  if ID is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	Music getMusic(Integer id);

	/**
	 * Adds music. Sets new ID and position.
	 *
	 * @param music music
	 * @throws IllegalArgumentException  if music is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void add(Music music);

	/**
	 * Updates music.
	 *
	 * @param music new value of music
	 * @throws IllegalArgumentException  if music is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void update(Music music);

	/**
	 * Removes music.
	 *
	 * @param music music
	 * @throws IllegalArgumentException  if music is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void remove(Music music);

	/**
	 * Duplicates music.
	 *
	 * @param music music
	 * @throws IllegalArgumentException  if music is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void duplicate(Music music);

	/**
	 * Moves music in list one position up.
	 *
	 * @param music music
	 * @throws IllegalArgumentException  if music is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void moveUp(Music music);

	/**
	 * Moves music in list one position down.
	 *
	 * @param music music
	 * @throws IllegalArgumentException  if music is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void moveDown(Music music);

	/**
	 * Returns true if music exists.
	 *
	 * @param music music
	 * @return true if music exists
	 * @throws IllegalArgumentException  if music is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	boolean exists(Music music);

	/**
	 * Updates positions.
	 *
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void updatePositions();

	/**
	 * Returns total count of media.
	 *
	 * @return total count of media
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	int getTotalMediaCount();

	/**
	 * Returns total length of all songs.
	 *
	 * @return total length of all songs
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	Time getTotalLength();

	/**
	 * Returns count of songs from all music.
	 *
	 * @return count of songs from all music
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	int getSongsCount();

}
