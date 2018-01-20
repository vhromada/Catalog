package cz.vhromada.catalog.validator.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.Constants;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.utils.TestConstants;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link SeasonValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
class SeasonValidatorImplTest extends AbstractValidatorTest<Season, Show> {

    /**
     * Event for invalid starting year
     */
    private static final Event INVALID_STARTING_YEAR_EVENT = new Event(Severity.ERROR, "SEASON_START_YEAR_NOT_VALID", "Starting year must be between "
        + Constants.MIN_YEAR + " and " + Constants.CURRENT_YEAR + '.');

    /**
     * Event for invalid ending year
     */
    private static final Event INVALID_ENDING_YEAR_EVENT = new Event(Severity.ERROR, "SEASON_END_YEAR_NOT_VALID", "Ending year must be between "
        + Constants.MIN_YEAR + " and " + Constants.CURRENT_YEAR + '.');

    /**
     * Test method for {@link SeasonValidatorImpl#SeasonValidatorImpl(CatalogService)} with null service for shows.
     */
    @Test
    void constructor_NullShowService() {
        assertThatThrownBy(() -> new SeasonValidatorImpl(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * number of season.
     */
    @Test
    void validate_Deep_NotPositiveNumber() {
        final Season season = getValidatingData(1);
        season.setNumber(0);

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")));
        });

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with bad minimum starting
     * year and bad minimum ending year.
     */
    @Test
    void validate_Deep_BadMinimumYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with bad maximum starting
     * year and bad maximum ending year.
     */
    @Test
    void validate_Deep_BadMaximumYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with starting year greater
     * than ending year.
     */
    @Test
    void validate_Deep_BadYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(season.getEndYear() + 1);

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID",
                "Starting year mustn't be greater than ending year.")));
        });

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null language.
     */
    @Test
    void validate_Deep_NullLanguage() {
        final Season season = getValidatingData(1);
        season.setLanguage(null);

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")));
        });

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null subtitles.
     */
    @Test
    void validate_Deep_NullSubtitles() {
        final Season season = getValidatingData(1);
        season.setSubtitles(null);

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with subtitles with
     * null value.
     */
    @Test
    void validate_Deep_BadSubtitles() {
        final Season season = getValidatingData(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link SeasonValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Season season = getValidatingData(1);
        season.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")));
        });

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Season> getCatalogValidator() {
        return new SeasonValidatorImpl(getCatalogService());
    }

    @Override
    protected Season getValidatingData(final Integer id) {
        return SeasonUtils.newSeason(id);
    }

    @Override
    protected Show getRepositoryData(final Season validatingData) {
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
        return "Season";
    }

    @Override
    protected void initExistsMock(final Season validatingData, final boolean exists) {
        final Show show = exists ? ShowUtils.newShowWithSeasons(validatingData.getId()) : ShowUtils.newShowDomain(Integer.MAX_VALUE);

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(show));
    }

    @Override
    protected void verifyExistsMock(final Season validatingData) {
        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
    }

    @Override
    protected void initMovingMock(final Season validatingData, final boolean up, final boolean valid) {
        final List<cz.vhromada.catalog.domain.Season> seasons;
        if (up && valid || !up && !valid) {
            seasons = CollectionUtils.newList(SeasonUtils.newSeasonDomain(1), SeasonUtils.newSeasonDomain(validatingData.getId()));
        } else {
            seasons = CollectionUtils.newList(SeasonUtils.newSeasonDomain(validatingData.getId()), SeasonUtils.newSeasonDomain(Integer.MAX_VALUE));
        }
        final Show show = ShowUtils.newShowDomain(1);
        show.setSeasons(seasons);

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(show));
    }

    @Override
    protected void verifyMovingMock(final Season validatingData) {
        verify(getCatalogService(), times(2)).getAll();
        verifyNoMoreInteractions(getCatalogService());
    }

}
