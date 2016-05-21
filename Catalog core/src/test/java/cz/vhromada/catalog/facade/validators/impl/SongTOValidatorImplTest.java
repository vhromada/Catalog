//package cz.vhromada.catalog.facade.validators.impl;
//
//import cz.vhromada.catalog.commons.ObjectGeneratorTest;
//import cz.vhromada.catalog.commons.ToGenerator;
//import cz.vhromada.catalog.facade.to.SongTO;
//import cz.vhromada.catalog.facade.validators.SongTOValidator;
//import cz.vhromada.validators.exceptions.ValidationException;
//
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * A class represents test for class {@link SongTOValidatorImpl}.
// *
// * @author Vladimir Hromada
// */
//public class SongTOValidatorImplTest extends ObjectGeneratorTest {
//
//    /**
//     * Instance of {@link SongTOValidator}
//     */
//    private SongTOValidator songTOValidator;
//
//    /**
//     * Initializes validator for TO for song.
//     */
//    @Before
//    public void setUp() {
//        songTOValidator = new SongTOValidatorImpl();
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testValidateNewSongTOWithNullArgument() {
//        songTOValidator.validateNewSongTO(null);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with not null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSongTOWithNotNullId() {
//        songTOValidator.validateNewSongTO(ToGenerator.newSongWithId(getObjectGenerator()));
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with null name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSongTOWithNullName() {
//        final SongTO song = ToGenerator.newSong(getObjectGenerator());
//        song.setName(null);
//
//        songTOValidator.validateNewSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with empty string as name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSongTOWithEmptyName() {
//        final SongTO song = ToGenerator.newSong(getObjectGenerator());
//        song.setName("");
//
//        songTOValidator.validateNewSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with negative length of song.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSongTOWithNegativeLength() {
//        final SongTO song = ToGenerator.newSong(getObjectGenerator());
//        song.setLength(-1);
//
//        songTOValidator.validateNewSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSongTOWithNullNote() {
//        final SongTO song = ToGenerator.newSong(getObjectGenerator());
//        song.setNote(null);
//
//        songTOValidator.validateNewSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with null TO for music.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSongTOWithNullMusicTO() {
//        final SongTO song = ToGenerator.newSong(getObjectGenerator());
//        song.setMusic(null);
//
//        songTOValidator.validateNewSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateNewSongTO(SongTO)} with TO for song with TO for music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSongTOWithMusicTOWithNullId() {
//        final SongTO song = ToGenerator.newSong(getObjectGenerator());
//        song.getMusic().setId(null);
//
//        songTOValidator.validateNewSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testValidateExistingSongTOWithNullArgument() {
//        songTOValidator.validateExistingSongTO(null);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSongTOWithNullId() {
//        songTOValidator.validateExistingSongTO(ToGenerator.newSong(getObjectGenerator()));
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with null name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSongTOWithNullName() {
//        final SongTO song = ToGenerator.newSongWithId(getObjectGenerator());
//        song.setName(null);
//
//        songTOValidator.validateExistingSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with empty string as name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSongTOWithEmptyName() {
//        final SongTO song = ToGenerator.newSongWithId(getObjectGenerator());
//        song.setName("");
//
//        songTOValidator.validateExistingSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with negative length of song.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSongTOWithNegativeLength() {
//        final SongTO song = ToGenerator.newSongWithId(getObjectGenerator());
//        song.setLength(-1);
//
//        songTOValidator.validateExistingSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSongTOWithNullNote() {
//        final SongTO song = ToGenerator.newSongWithId(getObjectGenerator());
//        song.setNote(null);
//
//        songTOValidator.validateExistingSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with null TO for music.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSongTOWithNullMusicTO() {
//        final SongTO song = ToGenerator.newSongWithId(getObjectGenerator());
//        song.setMusic(null);
//
//        songTOValidator.validateExistingSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateExistingSongTO(SongTO)} with TO for song with TO for music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSongTOWithMusicTOWithNullId() {
//        final SongTO song = ToGenerator.newSongWithId(getObjectGenerator());
//        song.getMusic().setId(null);
//
//        songTOValidator.validateExistingSongTO(song);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateSongTOWithId(SongTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testValidateSongTOWithIdWithNullArgument() {
//        songTOValidator.validateSongTOWithId(null);
//    }
//
//    /**
//     * Test method for {@link SongTOValidator#validateSongTOWithId(SongTO)} with TO for song with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateSongTOWithIdWithNullId() {
//        songTOValidator.validateSongTOWithId(ToGenerator.newSong(getObjectGenerator()));
//    }
//
//}
