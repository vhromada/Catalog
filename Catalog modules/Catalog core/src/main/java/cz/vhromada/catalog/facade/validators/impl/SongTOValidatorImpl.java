package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.facade.validators.SongTOValidator;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for song.
 *
 * @author Vladimir Hromada
 */
@Component("songTOValidator")
public class SongTOValidatorImpl implements SongTOValidator {

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateNewSongTO(final SongTO song) {
		validateSongTO(song);
		Validators.validateNull(song.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateExistingSongTO(final SongTO song) {
		validateSongTO(song);
		Validators.validateNotNull(song.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateSongTOWithId(final SongTO song) {
		Validators.validateArgumentNotNull(song, "TO for song");
		Validators.validateNotNull(song.getId(), "ID");
	}

	/**
	 * Validates TO for song.
	 *
	 * @param song validating TO for song
	 * @throws IllegalArgumentException if TO for song is null
	 * @throws ValidationException      if name is null
	 *                                  or name is empty string
	 *                                  or length of song is negative value
	 *                                  or note is null
	 *                                  or TO for music is null
	 *                                  or TO for music ID is null
	 */
	private void validateSongTO(final SongTO song) {
		Validators.validateArgumentNotNull(song, "TO for song");
		Validators.validateNotNull(song.getName(), "Name");
		Validators.validateNotEmptyString(song.getName(), "Name");
		Validators.validateNotNegativeNumber(song.getLength(), "Length");
		Validators.validateNotNull(song.getNote(), "Note");
		Validators.validateNotNull(song.getMusic(), "TO for music");
		Validators.validateNotNull(song.getMusic().getId(), "TO for music ID");
	}

}
