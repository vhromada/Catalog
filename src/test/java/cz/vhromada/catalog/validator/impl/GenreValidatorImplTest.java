package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link GenreValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GenreValidatorImplTest extends AbstractValidatorTest<Genre, cz.vhromada.catalog.domain.Genre> {

    /**
     * Test method for {@link GenreValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    public void validate_Deep_NullName() {
        final Genre genre = getValidatingData(1);
        genre.setName(null);

        final Result<Void> result = getCatalogValidator().validate(genre, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GenreValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    public void validate_Deep_EmptyName() {
        final Genre genre = getValidatingData(1);
        genre.setName("");

        final Result<Void> result = getCatalogValidator().validate(genre, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

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
    protected cz.vhromada.catalog.domain.Genre getRepositoryData() {
        return GenreUtils.newGenreDomain(null);
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

    @Override
    protected String getPrefix() {
        return "GENRE";
    }

}
