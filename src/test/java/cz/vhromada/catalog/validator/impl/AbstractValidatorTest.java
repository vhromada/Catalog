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
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractValidatorTest<T extends Movable, U extends Movable> {

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
     * Instance of {@link T}
     */
    private T validatingData;

    /**
     * Instance of {@link U}
     */
    private U repositoryData;

    /**
     * Data list
     */
    private List<U> dataList;

    /**
     * Initializes validatingData.
     */
    @Before
    public void setUp() {
        catalogValidator = getCatalogValidator();
        validatingData = getValidatingData(null);
        repositoryData = getRepositoryData();
        dataList = CollectionUtils.newList(getItem1(), getItem2());
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#NEW} with correct data.
     */
    @Test
    public void validate_New() {
        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.NEW);

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
    public void validate_New_NotNullId() {
        validatingData.setId(Integer.MAX_VALUE);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.NEW);

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
    public void validate_Exists() {
        validatingData.setId(Integer.MAX_VALUE);

        when(catalogService.get(any(Integer.class))).thenReturn(repositoryData);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.EXISTS);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#EXISTS} with data with null ID.
     */
    @Test
    public void validate_Exists_NullId() {
        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.EXISTS);

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
    public void validate_Exists_NotExistingData() {
        validatingData.setId(Integer.MAX_VALUE);

        when(catalogService.get(any(Integer.class))).thenReturn(null);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.EXISTS);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_EXIST", getName() + " doesn't exist.")));

        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#UP} with correct data.
     */
    @Test
    public void validate_Up() {
        dataList.add(repositoryData);

        when(catalogService.getAll()).thenReturn(dataList);
        when(catalogService.get(any(Integer.class))).thenReturn(repositoryData);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.UP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).getAll();
        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#UP} with invalid data.
     */
    @Test
    public void validate_Up_Invalid() {
        dataList.add(0, repositoryData);

        when(catalogService.getAll()).thenReturn(dataList);
        when(catalogService.get(any(Integer.class))).thenReturn(repositoryData);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.UP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved up.")));

        verify(catalogService).getAll();
        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DOWN} with correct data.
     */
    @Test
    public void validate_Down() {
        dataList.add(0, repositoryData);

        when(catalogService.getAll()).thenReturn(dataList);
        when(catalogService.get(any(Integer.class))).thenReturn(repositoryData);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.DOWN);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).getAll();
        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Test method for {@link CatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DOWN} with invalid data.
     */
    @Test
    public void validate_Down_Invalid() {
        dataList.add(repositoryData);

        when(catalogService.getAll()).thenReturn(dataList);
        when(catalogService.get(any(Integer.class))).thenReturn(repositoryData);

        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.DOWN);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved down.")));

        verify(catalogService).getAll();
        verify(catalogService).get(validatingData.getId());
        verifyNoMoreInteractions(catalogService);
    }

    /**
     * Test method for {@link ProgramValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with correct data.
     */
    @Test
    public void validate_Deep() {
        final Result<Void> result = catalogValidator.validate(validatingData, ValidationType.DEEP);

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
    public CatalogService<U> getCatalogService() {
        return catalogService;
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
     * @return instance of {@link U}
     */
    protected abstract U getRepositoryData();

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
    protected abstract String getPrefix();

}
