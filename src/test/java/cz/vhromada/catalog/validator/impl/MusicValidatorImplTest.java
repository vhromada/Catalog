package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.entity.MusicTO;
import cz.vhromada.catalog.validator.MusicValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MusicValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MusicValidatorImplTest {

    /**
     * Instance of {@link MusicValidator}
     */
    private MusicValidator musicValidator;

    /**
     * Initializes validator for TO for music.
     */
    @Before
    public void setUp() {
        musicValidator = new MusicValidatorImpl();
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusicTO_NullArgument() {
        musicValidator.validateNewMusicTO(null);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NotNullId() {
        musicValidator.validateNewMusicTO(MusicUtils.newMusicTO(1));
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullName() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setName(null);

        musicValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_EmptyName() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setName("");

        musicValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullWikiEn() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setWikiEn(null);

        musicValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullWikiCz() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setWikiCz(null);

        musicValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NotPositiveMediaCount() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setMediaCount(0);

        musicValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullNote() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setNote(null);

        musicValidator.validateNewMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusicTO_NullArgument() {
        musicValidator.validateExistingMusicTO(null);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullId() {
        musicValidator.validateExistingMusicTO(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullName() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setName(null);

        musicValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_EmptyName() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setName("");

        musicValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullWikiEn() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setWikiEn(null);

        musicValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullWikiCz() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setWikiCz(null);

        musicValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NotPositiveMediaCount() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setMediaCount(0);

        musicValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullNote() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setNote(null);

        musicValidator.validateExistingMusicTO(music);
    }

    /**
     * Test method for {@link MusicValidator#validateMusicTOWithId(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMusicTOWithId_NullArgument() {
        musicValidator.validateMusicTOWithId(null);
    }

    /**
     * Test method for {@link MusicValidator#validateMusicTOWithId(MusicTO)} with TO for music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateMusicTOWithId_NullId() {
        musicValidator.validateMusicTOWithId(MusicUtils.newMusicTO(null));
    }

}
