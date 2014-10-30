package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.commons.TestConstants.NEGATIVE_TIME;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MusicTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MusicTOValidatorImplTest extends ObjectGeneratorTest {

	/** Instance of {@link MusicTOValidator} */
	private MusicTOValidator musicTOValidator;

	/** Initializes validator for TO for music. */
	@Before
	public void setUp() {
		musicTOValidator = new MusicTOValidatorImpl();
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewMusicTOWithNullArgument() {
		musicTOValidator.validateNewMusicTO(null);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNotNullId() {
		musicTOValidator.validateNewMusicTO(generate(MusicTO.class));
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNullName() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setName(null);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithEmptyName() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setName("");

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is null. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNullWikiEn() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setWikiEn(null);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is null. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNullWikiCz() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setWikiCz(null);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNotPositiveMediaCount() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setMediaCount(0);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with negative count of songs. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNegativeSongsCount() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setSongsCount(-1);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null total length of songs. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNullTotalLength() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setTotalLength(null);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with negative total length of songs. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNegativeTotalLength() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setTotalLength(NEGATIVE_TIME);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMusicTOWithNullNote() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setNote(null);

		musicTOValidator.validateNewMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingMusicTOWithNullArgument() {
		musicTOValidator.validateExistingMusicTO(null);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNullId() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNullName() {
		final MusicTO music = generate(MusicTO.class);
		music.setName(null);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithEmptyName() {
		final MusicTO music = generate(MusicTO.class);
		music.setName("");

		musicTOValidator.validateExistingMusicTO(music);
	}

	/**
	 * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is
	 * null.
	 */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNullWikiEn() {
		final MusicTO music = generate(MusicTO.class);
		music.setWikiEn(null);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/**
	 * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is
	 * null.
	 */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNullWikiCz() {
		final MusicTO music = generate(MusicTO.class);
		music.setWikiCz(null);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNotPositiveMediaCount() {
		final MusicTO music = generate(MusicTO.class);
		music.setMediaCount(0);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with negative count of songs. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNegativeSongsCount() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setSongsCount(-1);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null total length of songs. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNullTotalLength() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setTotalLength(null);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with negative total length of songs. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNegativeTotalLength() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);
		music.setTotalLength(NEGATIVE_TIME);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMusicTOWithNullNote() {
		final MusicTO music = generate(MusicTO.class);
		music.setNote(null);

		musicTOValidator.validateExistingMusicTO(music);
	}

	/** Test method for {@link MusicTOValidator#validateMusicTOWithId(MusicTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateMusicTOWithIdWithNullArgument() {
		musicTOValidator.validateMusicTOWithId(null);
	}

	/** Test method for {@link MusicTOValidator#validateMusicTOWithId(MusicTO)} with TO for music with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateMusicTOWithIdWithNullId() {
		final MusicTO music = generate(MusicTO.class);
		music.setId(null);

		musicTOValidator.validateMusicTOWithId(music);
	}

}
