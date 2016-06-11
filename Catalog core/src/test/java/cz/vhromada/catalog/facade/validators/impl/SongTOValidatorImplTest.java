package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.SongUtils;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.facade.validators.SongTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SongTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class SongTOValidatorImplTest {

    /**
     * Instance of {@link SongTOValidator}
     */
    private SongTOValidator songTOValidator;

    /**
     * Initializes validator for TO for song.
     */
    @Before
    public void setUp() {
        songTOValidator = new SongTOValidatorImpl();
    }

    /**
     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSongTO_NullArgument() {
        songTOValidator.validateNewSongTO(null);
    }

    /**
     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NotNullId() {
        songTOValidator.validateNewSongTO(SongUtils.newSongTO(1));
    }

    /**
     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NullName() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setName(null);

        songTOValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_EmptyName() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setName("");

        songTOValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with negative length of song.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NegativeLength() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setLength(-1);

        songTOValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSongTO_NullNote() {
        final SongTO song = SongUtils.newSongTO(null);
        song.setNote(null);

        songTOValidator.validateNewSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSongTO_NullArgument() {
        songTOValidator.validateExistingSongTO(null);
    }

    /**
     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullId() {
        songTOValidator.validateExistingSongTO(SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullName() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setName(null);

        songTOValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_EmptyName() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setName("");

        songTOValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with negative length of song.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NegativeLength() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setLength(-1);

        songTOValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSongTO_NullNote() {
        final SongTO song = SongUtils.newSongTO(1);
        song.setNote(null);

        songTOValidator.validateExistingSongTO(song);
    }

    /**
     * Test method for {@link SongTOValidator#validateSongTOWithId(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSongTOWithId_NullArgument() {
        songTOValidator.validateSongTOWithId(null);
    }

    /**
     * Test method for {@link SongTOValidator#validateSongTOWithId(SongTO)} with TO for song with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateSongTOWithId_NullId() {
        songTOValidator.validateSongTOWithId(SongUtils.newSongTO(null));
    }

}
