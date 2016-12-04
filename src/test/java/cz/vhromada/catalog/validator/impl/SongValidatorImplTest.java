package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.catalog.validator.SongValidator;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SongValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class SongValidatorImplTest {

    /**
     * Instance of {@link SongValidator}
     */
    private SongValidator songValidator;

    /**
     * Initializes validator for song.
     */
    @Before
    public void setUp() {
        songValidator = new SongValidatorImpl();
    }

    /**
     * Test method for {@link SongValidator#validateNewSong(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSong_NullArgument() {
        songValidator.validateNewSong(null);
    }

    /**
     * Test method for {@link SongValidator#validateNewSong(Song)} with song with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSong_NotNullId() {
        songValidator.validateNewSong(SongUtils.newSong(1));
    }

    /**
     * Test method for {@link SongValidator#validateNewSong(Song)} with song with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSong_NullName() {
        final Song song = SongUtils.newSong(null);
        song.setName(null);

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSong(Song)} with song with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSong_EmptyName() {
        final Song song = SongUtils.newSong(null);
        song.setName("");

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSong(Song)} with song with negative length of song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSong_NegativeLength() {
        final Song song = SongUtils.newSong(null);
        song.setLength(-1);

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSong(Song)} with song with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSong_NullNote() {
        final Song song = SongUtils.newSong(null);
        song.setNote(null);

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSong(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSong_NullArgument() {
        songValidator.validateExistingSong(null);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSong(Song)} with song with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSong_NullId() {
        songValidator.validateExistingSong(SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongValidator#validateExistingSong(Song)} with song with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSong_NullName() {
        final Song song = SongUtils.newSong(1);
        song.setName(null);

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSong(Song)} with song with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSong_EmptyName() {
        final Song song = SongUtils.newSong(1);
        song.setName("");

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSong(Song)} with song with negative length of song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSong_NegativeLength() {
        final Song song = SongUtils.newSong(1);
        song.setLength(-1);

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSong(Song)} with song with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSong_NullNote() {
        final Song song = SongUtils.newSong(1);
        song.setNote(null);

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateSongWithId(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSongWithId_NullArgument() {
        songValidator.validateSongWithId(null);
    }

    /**
     * Test method for {@link SongValidator#validateSongWithId(Song)} with song with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSongWithId_NullId() {
        songValidator.validateSongWithId(SongUtils.newSong(null));
    }

}
