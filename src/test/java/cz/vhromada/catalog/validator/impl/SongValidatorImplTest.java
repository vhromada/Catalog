package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.SongUtils;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.validator.SongValidator;
import cz.vhromada.validators.exceptions.ValidationException;

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
     * Initializes validator for TO for song.
     */
    @Before
    public void setUp() {
        songValidator = new SongValidatorImpl();
    }

    /**
     * Test method for {@link SongValidator#validateNewSong(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSongTO_NullArgument() {
        songValidator.validateNewSong(null);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NotNullId() {
        songValidator.validateNewSong(SongUtils.newSongTO(1));
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NullName() {
        final Song song = SongUtils.newSongTO(null);
        song.setName(null);

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_EmptyName() {
        final Song song = SongUtils.newSongTO(null);
        song.setName("");

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with negative length of song.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NegativeLength() {
        final Song song = SongUtils.newSongTO(null);
        song.setLength(-1);

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NullNote() {
        final Song song = SongUtils.newSongTO(null);
        song.setNote(null);

        songValidator.validateNewSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSong(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSongTO_NullArgument() {
        songValidator.validateExistingSong(null);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullId() {
        songValidator.validateExistingSong(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullName() {
        final Song song = SongUtils.newSongTO(1);
        song.setName(null);

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_EmptyName() {
        final Song song = SongUtils.newSongTO(1);
        song.setName("");

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with negative length of song.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NegativeLength() {
        final Song song = SongUtils.newSongTO(1);
        song.setLength(-1);

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullNote() {
        final Song song = SongUtils.newSongTO(1);
        song.setNote(null);

        songValidator.validateExistingSong(song);
    }

    /**
     * Test method for {@link SongValidator#validateSongWithId(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSongTOWithId_NullArgument() {
        songValidator.validateSongWithId(null);
    }

    /**
     * Test method for {@link SongValidator#validateSongTOWithId(SongTO)} with TO for song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateSongTOWithId_NullId() {
        songValidator.validateSongWithId(SongUtils.newSongTO(null));
    }

}
