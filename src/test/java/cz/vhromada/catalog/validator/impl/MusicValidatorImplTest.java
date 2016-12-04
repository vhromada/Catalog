package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.validator.MusicValidator;

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
     * Initializes validator for music.
     */
    @Before
    public void setUp() {
        musicValidator = new MusicValidatorImpl();
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_NullArgument() {
        musicValidator.validateNewMusic(null);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with music with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_NotNullId() {
        musicValidator.validateNewMusic(MusicUtils.newMusic(1));
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with music with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_NullName() {
        final Music music = MusicUtils.newMusic(null);
        music.setName(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with music with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_EmptyName() {
        final Music music = MusicUtils.newMusic(null);
        music.setName("");

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_NullWikiEn() {
        final Music music = MusicUtils.newMusic(null);
        music.setWikiEn(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_NullWikiCz() {
        final Music music = MusicUtils.newMusic(null);
        music.setWikiCz(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with music with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_NotPositiveMediaCount() {
        final Music music = MusicUtils.newMusic(null);
        music.setMediaCount(0);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateNewMusic(Music)} with music with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMusic_NullNote() {
        final Music music = MusicUtils.newMusic(null);
        music.setNote(null);

        musicValidator.validateNewMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_NullArgument() {
        musicValidator.validateExistingMusic(null);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with music with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_NullId() {
        musicValidator.validateExistingMusic(MusicUtils.newMusic(null));
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with music with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_NullName() {
        final Music music = MusicUtils.newMusic(1);
        music.setName(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with music with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_EmptyName() {
        final Music music = MusicUtils.newMusic(1);
        music.setName("");

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with music with null URL to english Wikipedia page about music is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_NullWikiEn() {
        final Music music = MusicUtils.newMusic(1);
        music.setWikiEn(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with music with null URL to czech Wikipedia page about music is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_NullWikiCz() {
        final Music music = MusicUtils.newMusic(1);
        music.setWikiCz(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with music with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_NotPositiveMediaCount() {
        final Music music = MusicUtils.newMusic(1);
        music.setMediaCount(0);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateExistingMusic(Music)} with music with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMusic_NullNote() {
        final Music music = MusicUtils.newMusic(1);
        music.setNote(null);

        musicValidator.validateExistingMusic(music);
    }

    /**
     * Test method for {@link MusicValidator#validateMusicWithId(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMusicWithId_NullArgument() {
        musicValidator.validateMusicWithId(null);
    }

    /**
     * Test method for {@link MusicValidator#validateMusicWithId(Music)} with music with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMusicWithId_NullId() {
        musicValidator.validateMusicWithId(MusicUtils.newMusic(null));
    }

}
