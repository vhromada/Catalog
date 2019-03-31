package cz.vhromada.catalog.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link GenreValidator}.
 *
 * @author Vladimir Hromada
 */
class GenreValidatorTest extends MovableValidatorTest<Genre, cz.vhromada.catalog.domain.Genre> {

    /**
     * Test method for {@link GenreValidator#GenreValidator(MovableService)} with null service for genres.
     */
    @Test
    void constructor_NullGenreService() {
        assertThatThrownBy(() -> new GenreValidator(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GenreValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullName() {
        final Genre genre = getValidatingData(1);
        genre.setName(null);

        final Result<Void> result = getValidator().validate(genre, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));
        });

        verifyZeroInteractions(getService());
    }

    /**
     * Test method for {@link GenreValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    void validate_Deep_EmptyName() {
        final Genre genre = getValidatingData(1);
        genre.setName("");

        final Result<Void> result = getValidator().validate(genre, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        verifyZeroInteractions(getService());
    }

    @Override
    protected MovableValidator<Genre> getValidator() {
        return new GenreValidator(getService());
    }

    @Override
    protected Genre getValidatingData(final Integer id) {
        return GenreUtils.newGenre(id);
    }

    @Override
    protected Genre getValidatingData(final Integer id, final Integer position) {
        final Genre genre = GenreUtils.newGenre(id);
        genre.setPosition(position);

        return genre;
    }

    @Override
    protected cz.vhromada.catalog.domain.Genre getRepositoryData(final Genre validatingData) {
        return GenreUtils.newGenreDomain(validatingData.getId());
    }

    @Override
    protected cz.vhromada.catalog.domain.Genre getItem1() {
        return GenreUtils.newGenreDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Genre getItem2() {
        return GenreUtils.newGenreDomain(2);
    }

    @Override
    protected String getName() {
        return "Genre";
    }

}
