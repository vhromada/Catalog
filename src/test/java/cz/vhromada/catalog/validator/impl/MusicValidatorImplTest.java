package cz.vhromada.catalog.validator.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link MusicValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
class MusicValidatorImplTest extends AbstractValidatorTest<Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Test method for {@link MusicValidatorImpl#MusicValidatorImpl(CatalogService)} with null service for music.
     */
    @Test
    void constructor_NullMusicService() {
        assertThrows(IllegalArgumentException.class, () -> new MusicValidatorImpl(null));
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullName() {
        final Music music = getValidatingData(1);
        music.setName(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    void validate_Deep_EmptyName() {
        final Music music = getValidatingData(1);
        music.setName("");

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about music.
     */
    @Test
    void validate_Deep_NullWikiEn() {
        final Music music = getValidatingData(1);
        music.setWikiEn(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                "URL to english Wikipedia page about music mustn't be null.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about music.
     */
    @Test
    void validate_Deep_NullWikiCz() {
        final Music music = getValidatingData(1);
        music.setWikiCz(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about music mustn't be null.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * count of media.
     */
    @Test
    void validate_Deep_NotPositiveMediaCount() {
        final Music music = getValidatingData(1);
        music.setMediaCount(0);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE",
                "Count of media must be positive number.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Music music = getValidatingData(1);
        music.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Music> getCatalogValidator() {
        return new MusicValidatorImpl(getCatalogService());
    }

    @Override
    protected Music getValidatingData(final Integer id) {
        return MusicUtils.newMusic(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getRepositoryData(final Music validatingData) {
        return MusicUtils.newMusicDomain(validatingData.getId());
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getItem1() {
        return MusicUtils.newMusicDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getItem2() {
        return MusicUtils.newMusicDomain(2);
    }

    @Override
    protected String getName() {
        return "Music";
    }

}
