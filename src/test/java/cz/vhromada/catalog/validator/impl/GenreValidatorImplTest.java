package cz.vhromada.catalog.validator.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link GenreValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
class GenreValidatorImplTest extends AbstractValidatorTest<Genre, cz.vhromada.catalog.domain.Genre> {

    /**
     * Test method for {@link GenreValidatorImpl#GenreValidatorImpl(CatalogService)} with null service for genres.
     */
    @Test
    void constructor_NullGenreService() {
        assertThrows(IllegalArgumentException.class, () -> new GenreValidatorImpl(null));
    }

    /**
     * Test method for {@link GenreValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullName() {
        final Genre genre = getValidatingData(1);
        genre.setName(null);

        final Result<Void> result = getCatalogValidator().validate(genre, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GenreValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    void validate_Deep_EmptyName() {
        final Genre genre = getValidatingData(1);
        genre.setName("");

        final Result<Void> result = getCatalogValidator().validate(genre, ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Genre> getCatalogValidator() {
        return new GenreValidatorImpl(getCatalogService());
    }

    @Override
    protected Genre getValidatingData(final Integer id) {
        return GenreUtils.newGenre(id);
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
