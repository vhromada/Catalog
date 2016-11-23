package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.entity.MusicTO;
import cz.vhromada.catalog.validator.MusicTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MusicTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MusicTOValidatorImplTest {

    /**
     * Instance of {@link MusicTOValidator}
     */
    private MusicTOValidator musicTOValidator;

    /**
     * Initializes validator for TO for music.
     */
    @Before
    public void setUp() {
        musicTOValidator = new MusicTOValidatorImpl();
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusicTO_NullArgument() {
        musicTOValidator.validateNewMusicTO(null);
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NotNullId() {
        musicTOValidator.validateNewMusicTO(MusicUtils.newMusicTO(1));
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullName() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setName(null);

        musicTOValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_EmptyName() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setName("");

        musicTOValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullWikiEn() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setWikiEn(null);

        musicTOValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullWikiCz() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setWikiCz(null);

        musicTOValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NotPositiveMediaCount() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setMediaCount(0);

        musicTOValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateNewMusicTO(MusicTO)} with TO for music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullNote() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setNote(null);

        musicTOValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusicTO_NullArgument() {
        musicTOValidator.validateExistingMusicTO(null);
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullId() {
        musicTOValidator.validateExistingMusicTO(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullName() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setName(null);

        musicTOValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_EmptyName() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setName("");

        musicTOValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullWikiEn() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setWikiEn(null);

        musicTOValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullWikiCz() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setWikiCz(null);

        musicTOValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NotPositiveMediaCount() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setMediaCount(0);

        musicTOValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateExistingMusicTO(MusicTO)} with TO for music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullNote() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setNote(null);

        musicTOValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicTOValidator#validateMusicTOWithId(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMusicTOWithId_NullArgument() {
        musicTOValidator.validateMusicTOWithId(null);
    }

    /**
     * Test method for {@link MusicTOValidator#validateMusicTOWithId(MusicTO)} with TO for music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateMusicTOWithId_NullId() {
        musicTOValidator.validateMusicTOWithId(MusicUtils.newMusicTO(null));
    }

}
