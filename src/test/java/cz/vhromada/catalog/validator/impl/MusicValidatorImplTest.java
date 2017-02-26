package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link MusicValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MusicValidatorImplTest extends AbstractValidatorTest<Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    public void validate_Deep_NullName() {
        final Music music = getValidatingData();
        music.setName(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    public void validate_Deep_EmptyName() {
        final Music music = getValidatingData();
        music.setName("");

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about music.
     */
    @Test
    public void validate_Deep_NullWikiEn() {
        final Music music = getValidatingData();
        music.setWikiEn(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                "URL to english Wikipedia page about music mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about music.
     */
    @Test
    public void validate_Deep_NullWikiCz() {
        final Music music = getValidatingData();
        music.setWikiCz(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL", "URL to czech Wikipedia page about music mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * count of media.
     */
    @Test
    public void validate_Deep_NotPositiveMediaCount() {
        final Music music = getValidatingData();
        music.setMediaCount(0);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MusicValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    public void validate_Deep_NullNote() {
        final Music music = getValidatingData();
        music.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(music, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Music> getCatalogValidator() {
        return new MusicValidatorImpl(getCatalogService());
    }

    @Override
    protected Music getValidatingData() {
        return MusicUtils.newMusic(null);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getRepositoryData() {
        return MusicUtils.newMusicDomain(null);
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

    @Override
    protected String getPrefix() {
        return "MUSIC";
    }

}
