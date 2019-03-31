package cz.vhromada.catalog.validator;

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

import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.common.Language;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.utils.TestConstants;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.utils.Constants;
import cz.vhromada.common.validator.AbstractMovableValidator;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link SeasonValidator}.
 *
 * @author Vladimir Hromada
 */
class SeasonValidatorTest extends MovableValidatorTest<Season, Show> {

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
     * Test method for {@link SeasonValidator#SeasonValidator(MovableService)} with null service for shows.
     */
    @Test
    void constructor_NullShowService() {
        assertThatThrownBy(() -> new SeasonValidator(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * number of season.
     */
    @Test
    void validate_Deep_NotPositiveNumber() {
        final Season season = getValidatingData(1);
        season.setNumber(0);

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with bad minimum
     * starting year and bad minimum ending year.
     */
    @Test
    void validate_Deep_BadMinimumYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with bad maximum
     * starting year and bad maximum ending year.
     */
    @Test
    void validate_Deep_BadMaximumYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(INVALID_STARTING_YEAR_EVENT, INVALID_ENDING_YEAR_EVENT));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with starting year
     * greater than ending year.
     */
    @Test
    void validate_Deep_BadYears() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(season.getEndYear() + 1);

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID",
                "Starting year mustn't be greater than ending year.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null language.
     */
    @Test
    void validate_Deep_NullLanguage() {
        final Season season = getValidatingData(1);
        season.setLanguage(null);

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null subtitles.
     */
    @Test
    void validate_Deep_NullSubtitles() {
        final Season season = getValidatingData(1);
        season.setSubtitles(null);

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with subtitles with
     * null value.
     */
    @Test
    void validate_Deep_BadSubtitles() {
        final Season season = getValidatingData(1);
        season.setSubtitles(Arrays.asList(Language.CZ, null));

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Season season = getValidatingData(1);
        season.setNote(null);

        final Result<Void> result = getValidator().validate(season, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    @Override
    protected MovableValidator<Season> getValidator() {
        return new SeasonValidator(getService());
    }

    @Override
    protected Season getValidatingData(final Integer id) {
        return SeasonUtils.newSeason(id);
    }

    @Override
    protected Season getValidatingData(final Integer id, final Integer position) {
        final Season season = SeasonUtils.newSeason(id);
        season.setPosition(position);

        return season;
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

        when(getService().getAll()).thenReturn(Collections.singletonList(show));
    }

    @Override
    protected void verifyExistsMock(final Season validatingData) {
        verify(getService()).getAll();
        verifyNoMoreInteractions(getService());
    }

    @Override
    protected void initMovingMock(final Season validatingData, final boolean up, final boolean valid) {
        final List<cz.vhromada.catalog.domain.Season> seasons;
        if (up && valid || !up && !valid) {
            seasons = List.of(SeasonUtils.newSeasonDomain(1), SeasonUtils.newSeasonDomain(validatingData.getId()));
        } else {
            seasons = List.of(SeasonUtils.newSeasonDomain(validatingData.getId()), SeasonUtils.newSeasonDomain(Integer.MAX_VALUE));
        }
        final Show show = ShowUtils.newShowDomain(1);
        show.setSeasons(seasons);

        when(getService().getAll()).thenReturn(Collections.singletonList(show));
    }

    @Override
    protected void verifyMovingMock(final Season validatingData) {
        verify(getService(), times(2)).getAll();
        verifyNoMoreInteractions(getService());
    }

}