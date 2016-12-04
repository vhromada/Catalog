package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.entity.Music;
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
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusicTO_NullArgument() {
        musicValidator.validateNewMusic(null);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NotNullId() {
        musicValidator.validateNewMusic(MusicUtils.newMusicTO(1));
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullName() {
        final Music music = MusicUtils.newMusicTO(null);
        music.setName(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_EmptyName() {
        final Music music = MusicUtils.newMusicTO(null);
        music.setName("");

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullWikiEn() {
        final Music music = MusicUtils.newMusicTO(null);
        music.setWikiEn(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullWikiCz() {
        final Music music = MusicUtils.newMusicTO(null);
        music.setWikiCz(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NotPositiveMediaCount() {
        final Music music = MusicUtils.newMusicTO(null);
        music.setMediaCount(0);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusicTO(MusicTO)} with TO for music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMusicTO_NullNote() {
        final Music music = MusicUtils.newMusicTO(null);
        music.setNote(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusicTO_NullArgument() {
        musicValidator.validateExistingMusic(null);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullId() {
        musicValidator.validateExistingMusic(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullName() {
        final Music music = MusicUtils.newMusicTO(1);
        music.setName(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_EmptyName() {
        final Music music = MusicUtils.newMusicTO(1);
        music.setName("");

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullWikiEn() {
        final Music music = MusicUtils.newMusicTO(1);
        music.setWikiEn(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullWikiCz() {
        final Music music = MusicUtils.newMusicTO(1);
        music.setWikiCz(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NotPositiveMediaCount() {
        final Music music = MusicUtils.newMusicTO(1);
        music.setMediaCount(0);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusicTO(MusicTO)} with TO for music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMusicTO_NullNote() {
        final Music music = MusicUtils.newMusicTO(1);
        music.setNote(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateMusicWithId(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMusicTOWithId_NullArgument() {
        musicValidator.validateMusicWithId(null);
    }

    /**
     * Test method for {@link MusicValidator#validateMusicTOWithId(MusicTO)} with TO for music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateMusicTOWithId_NullId() {
        musicValidator.validateMusicWithId(MusicUtils.newMusicTO(null));
    }

}
