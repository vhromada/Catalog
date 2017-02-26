package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link EpisodeValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeValidatorImplTest extends AbstractValidatorTest<Episode, Show> {

    @Override
    protected CatalogValidator<Episode> getCatalogValidator() {
        return new EpisodeValidatorImpl(getCatalogService());
    }

    @Override
    protected Episode getValidatingData(final Integer id) {
        return EpisodeUtils.newEpisode(id);
    }

    @Override
    protected Show getRepositoryData(final Episode validatingData) {
        return ShowUtils.newShowWithSeasons(validatingData.getId());
    }

    @Override
    protected Show getItem1() {
        return null;
    }

    @Override
    protected Show getItem2() {
        return null;
    }

    @Override
    protected String getName() {
        return "Episode";
    }

    @Override
    protected String getPrefix() {
        return "EPISODE";
    }

    @Override
    protected void initExistsMock(final Episode validatingData, final boolean exists) {
        final Show show = exists ? ShowUtils.newShowWithSeasons(validatingData.getId()) : ShowUtils.newShowDomain(Integer.MAX_VALUE);

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(show));
    }

    @Override
    protected void verifyExistsMock(final Episode validatingData) {
        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
    }

    @Override
    protected void initMovingMock(final Episode validatingData, final boolean up, final boolean valid) {
        final List<cz.vhromada.catalog.domain.Episode> episodes;
        if (up && valid || !up && !valid) {
            episodes = CollectionUtils.newList(EpisodeUtils.newEpisodeDomain(1), EpisodeUtils.newEpisodeDomain(validatingData.getId()));
        } else {
            episodes = CollectionUtils.newList(EpisodeUtils.newEpisodeDomain(validatingData.getId()), EpisodeUtils.newEpisodeDomain(Integer.MAX_VALUE));
        }
        final Season season = SeasonUtils.newSeasonDomain(1);
        season.setEpisodes(episodes);
        final Show show = ShowUtils.newShowDomain(1);
        show.setSeasons(CollectionUtils.newList(season));

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(show));
    }

    @Override
    protected void verifyMovingMock(final Episode validatingData) {
        verify(getCatalogService(), times(2)).getAll();
        verifyNoMoreInteractions(getCatalogService());
    }

    /**
     * Test method for {@link EpisodeValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * number of episode.
     */
    @Test
    public void validate_Deep_NotPositiveNumber() {
        final Episode episode = getValidatingData(1);
        episode.setNumber(0);

        final Result<Void> result = getCatalogValidator().validate(episode, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link EpisodeValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    public void validate_Deep_NullName() {
        final Episode episode = getValidatingData(1);
        episode.setName(null);

        final Result<Void> result = getCatalogValidator().validate(episode, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link EpisodeValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    public void validate_Deep_EmptyName() {
        final Episode episode = getValidatingData(1);
        episode.setName("");

        final Result<Void> result = getCatalogValidator().validate(episode, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link EpisodeValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with negative length of
     * episode.
     */
    @Test
    public void validate_Deep_NegativeLeng() {
        final Episode episode = getValidatingData(1);
        episode.setLength(-1);

        final Result<Void> result = getCatalogValidator().validate(episode, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link EpisodeValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    public void validate_Deep_NullNote() {
        final Episode episode = getValidatingData(1);
        episode.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(episode, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

}
