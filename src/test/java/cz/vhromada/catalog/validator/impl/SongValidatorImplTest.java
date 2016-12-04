package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.SongUtils;
import cz.vhromada.catalog.entity.SongTO;
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
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSongTO_NullArgument() {
        songValidator.validateNewSongTO(null);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NotNullId() {
        songValidator.validateNewSongTO(SongUtils.newSongTO(1));
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NullName() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setName(null);

        songValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_EmptyName() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setName("");

        songValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with negative length of song.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NegativeLength() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setLength(-1);

        songValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateNewSongTO(SongTO)} with TO for song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NullNote() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setNote(null);

        songValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSongTO_NullArgument() {
        songValidator.validateExistingSongTO(null);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullId() {
        songValidator.validateExistingSongTO(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullName() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setName(null);

        songValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_EmptyName() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setName("");

        songValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with negative length of song.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NegativeLength() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setLength(-1);

        songValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateExistingSongTO(SongTO)} with TO for song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullNote() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setNote(null);

        songValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongValidator#validateSongTOWithId(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSongTOWithId_NullArgument() {
        songValidator.validateSongTOWithId(null);
    }

    /**
     * Test method for {@link SongValidator#validateSongTOWithId(SongTO)} with TO for song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateSongTOWithId_NullId() {
        songValidator.validateSongTOWithId(SongUtils.newSongTO(null));
    }

}
