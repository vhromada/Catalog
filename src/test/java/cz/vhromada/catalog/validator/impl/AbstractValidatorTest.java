package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * An abstract class represents test for validator.
 *
 * @param <T> type of entity data
 * @param <U> type of domain data
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractValidatorTest<T extends Movable, U extends Movable> {

    /**
     * ID
     */
    private static final Integer ID = 5;

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<U> catalogService;

    /**
     * Instance of {@link CatalogValidator}
     */
    private CatalogValidator<T> catalogValidator;

    /**
     * Initializes validator.
     */
    @Before
    public void setUp() {
        catalogValidator = getCatalogValidator();
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#NEW} with correct data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_New() {
        final Result<Void> result = catalogValidator.validate(getValidatingData(null), ValidationType.NEW);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verifyZeroInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#NEW} with data with not null ID.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_New_NotNullId() {
        final Result<Void> result = catalogValidator.validate(getValidatingData(Integer.MAX_VALUE), ValidationType.NEW);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_ID_NOT_NULL", "ID must be null.")));

        verifyZeroInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#EXISTS} with correct data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Exists() {
        final T validatingData = getValidatingData(ID);

        initExistsMock(validatingData, true);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.EXISTS);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verifyExistsMock(validatingData);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#EXISTS} with data with null ID.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Exists_NullId() {
        final Result<Void> result = catalogValidator.validate(getValidatingData(null), ValidationType.EXISTS);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_ID_NULL", "ID mustn't be null.")));

        verifyZeroInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#EXISTS} with not existing data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Exists_NotExistingData() {
        final T validatingData = getValidatingData(ID);

        initExistsMock(validatingData, false);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.EXISTS);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_EXIST", getName() + " doesn't exist.")));

        verifyExistsMock(validatingData);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#UP} with correct data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Up() {
        final T validatingData = getValidatingData(ID);

        initMovingMock(validatingData, true, true);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.UP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verifyMovingMock(validatingData);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#UP} with invalid data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Up_Invalid() {
        final T validatingData = getValidatingData(Integer.MAX_VALUE);

        initMovingMock(validatingData, true, false);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.UP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved up.")));

        verifyMovingMock(validatingData);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DOWN} with correct data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Down() {
        final T validatingData = getValidatingData(ID);

        initMovingMock(validatingData, false, true);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.DOWN);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verifyMovingMock(validatingData);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DOWN} with invalid data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Down_Invalid() {
        final T validatingData = getValidatingData(Integer.MAX_VALUE);

        initMovingMock(validatingData, false, false);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.DOWN);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved down.")));

        verifyMovingMock(validatingData);
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with correct data.
     */
    @Test
    @SuppressWarnings("InstanceMethodNamingConvention")
    public void validate_Deep() {
        final Result<Void> result = catalogValidator.validate(getValidatingData(ID), ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verifyZeroInteractions(catalogService);
    }

    /**
     * Returns instance of {@link CatalogService}.
     *
     * @return instance of {@link CatalogService}
     */
    protected CatalogService<U> getCatalogService() {
        return catalogService;
    }

    /**
     * Initializes mock for exists.
     *
     * @param validatingData validating data
     * @param exists         true if data exists
     */
    protected void initExistsMock(final T validatingData, final boolean exists) {
        final U result = exists ? getRepositoryData(validatingData) : null;

        when(catalogService.get(any(Integer.class))).thenReturn(result);
    }

    /**
     * Verifies mock for exists.
     *
     * @param validatingData validating data
     */
    protected void verifyExistsMock(final T validatingData) {
        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Initializes mock for moving.
     *
     * @param validatingData validating data
     * @param up             true if moving up
     * @param valid          true if data should be valid
     */
    protected void initMovingMock(final T validatingData, final boolean up, final boolean valid) {
        final List<U> dataList = CollectionUtils.newList(getItem1(), getItem2());
        final U repositoryData = getRepositoryData(validatingData);
        if (up && valid || !up && !valid) {
            dataList.add(repositoryData);
        } else {
            dataList.add(0, repositoryData);
        }

        when(catalogService.getAll()).thenReturn(dataList);
        when(catalogService.get(any(Integer.class))).thenReturn(repositoryData);
    }

    /**
     * Verifies mock for moving.
     *
     * @param validatingData validating data
     */
    protected void verifyMovingMock(final T validatingData) {
        verify(catalogService).getAll();
        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Returns instance of {@link CatalogValidator}.
     *
     * @return instance of {@link CatalogValidator}
     */
    protected abstract CatalogValidator<T> getCatalogValidator();

    /**
     * Returns instance of {@link T}.
     *
     * @param id ID
     * @return instance of {@link T}
     */
    protected abstract T getValidatingData(Integer id);

    /**
     * Returns instance of {@link U}.
     *
     * @param validatingData validating data
     * @return instance of {@link U}
     */
    protected abstract U getRepositoryData(T validatingData);

    /**
     * Returns 1st item in data list.
     *
     * @return 1st item in data list
     */
    protected abstract U getItem1();

    /**
     * Returns 2nd item in data list.
     *
     * @return 2nd item in data list
     */
    protected abstract U getItem2();

    /**
     * Returns name of entity.
     *
     * @return name of entity
     */
    protected abstract String getName();

    /**
     * Returns prefix for validation keys.
     *
     * @return prefix for validation keys
     */
    private String getPrefix() {
        return getName().toUpperCase();
    }

}
