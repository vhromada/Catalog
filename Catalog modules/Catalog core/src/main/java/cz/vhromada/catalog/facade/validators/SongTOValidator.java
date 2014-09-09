package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.validators.exceptions.ValidationException;

/**
 * An interface represents validator for TO for song.
 *
 * @author Vladimir Hromada
 */
public interface SongTOValidator {

	/**
	 * Validates new TO for song.
	 *
	 * @param song validating TO for song
	 * @throws IllegalArgumentException if TO for song is null
	 * @throws ValidationException      if ID isn't null
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of song is negative value
	 *                                  or note is null
	 *                                  or TO for music is null
	 *                                  or TO for music ID is null
	 */
	void validateNewSongTO(SongTO song);

	/**
	 * Validates existing TO for song.
	 *
	 * @param song validating TO for song
	 * @throws IllegalArgumentException if TO for song is null
	 * @throws ValidationException      if ID is null
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of song is negative value
	 *                                  or note is null
	 *                                  or TO for music is null
	 *                                  or TO for music ID is null
	 */
	void validateExistingSongTO(SongTO song);

	/**
	 * Validates TO for song with ID.
	 *
	 * @param song validating TO for song
	 * @throws IllegalArgumentException if TO for song is null
	 * @throws ValidationException      if ID is null
	 */
	void validateSongTOWithId(SongTO song);

}

