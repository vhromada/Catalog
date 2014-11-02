package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicTOValidator")
public class MusicTOValidatorImpl implements MusicTOValidator {

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateNewMusicTO(final MusicTO music) {
		validateMusicTO(music);
		Validators.validateNull(music.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateExistingMusicTO(final MusicTO music) {
		validateMusicTO(music);
		Validators.validateNotNull(music.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateMusicTOWithId(final MusicTO music) {
		Validators.validateArgumentNotNull(music, "TO for music");
		Validators.validateNotNull(music.getId(), "ID");
	}

	/**
	 * Validates TO for music.
	 *
	 * @param music validating TO for music
	 * @throws IllegalArgumentException if TO for music is null
	 * @throws ValidationException      if name is null
	 *                                  or name is empty string
	 *                                  or URL to english Wikipedia page about music is null
	 *                                  or URL to czech Wikipedia page about music is null
	 *                                  or count of media isn't positive number
	 *                                  or count of songs is negative number
	 *                                  or total length of songs is null
	 *                                  or total length of songs is negative number
	 *                                  or note is null
	 */
	private void validateMusicTO(final MusicTO music) {
		Validators.validateArgumentNotNull(music, "TO for music");
		Validators.validateNotNull(music.getName(), "Name");
		Validators.validateNotEmptyString(music.getName(), "Name");
		Validators.validateNotNull(music.getWikiEn(), "URL to english Wikipedia page about music");
		Validators.validateNotNull(music.getWikiCz(), "URL to czech Wikipedia page about music");
		Validators.validatePositiveNumber(music.getMediaCount(), "Count of media");
		Validators.validateNotNegativeNumber(music.getSongsCount(), "Count of songs");
		Validators.validateNotNull(music.getTotalLength(), "Total length of songs");
		Validators.validateNotNegativeNumber(music.getTotalLength().getLength(), "Total length of songs");
		Validators.validateNotNull(music.getNote(), "Note");
	}

}
