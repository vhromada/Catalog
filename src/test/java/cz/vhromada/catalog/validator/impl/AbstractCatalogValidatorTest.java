package cz.vhromada.catalog.validator.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link AbstractCatalogValidator}.
 *
 * @author Vladimir Hromada
 */
class AbstractCatalogValidatorTest extends AbstractValidatorTest<Movable, Movable> {

    /**
     * Event key
     */
    private static final String KEY = "key";

    /**
     * Event value
     */
    private static final String VALUE = "value";

    /**
     * Test method for {@link AbstractCatalogValidator#AbstractCatalogValidator(String, CatalogService)} with null name.
     */
    @Test
    void constructor_NullName() {
        assertThrows(IllegalArgumentException.class, () -> new AbstractCatalogValidatorStub(null, getCatalogService()));
    }

    /**
     * Test method for {@link AbstractCatalogValidator#AbstractCatalogValidator(String, CatalogService)} with null service for catalog.
     */
    @Test
    void constructor_NullCatalogService() {
        assertThrows(IllegalArgumentException.class, () -> new AbstractCatalogValidatorStub(getName(), null));
    }

    /**
     * Test method for {@link AbstractCatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP}.
     */
    @Test
    @Override
    void validate_Deep() {
        final Result<Void> result = getCatalogValidator().validate(getValidatingData(1), ValidationType.DEEP);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.WARN, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.WARN, KEY, VALUE)), result.getEvents())
        );

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Movable> getCatalogValidator() {
        return new AbstractCatalogValidatorStub(getName(), getCatalogService());
    }

    @Override
    protected Movable getValidatingData(final Integer id) {
        return new MovableStub(id);
    }

    @Override
    protected Movable getRepositoryData(final Movable validatingData) {
        return new MovableStub(validatingData.getId());
    }

    @Override
    protected Movable getItem1() {
        return new MovableStub(1);
    }

    @Override
    protected Movable getItem2() {
        return new MovableStub(2);
    }

    @Override
    protected String getName() {
        return "Stub";
    }

    /**
     * A class represents abstract catalog validator stub.
     */
    private static final class AbstractCatalogValidatorStub extends AbstractCatalogValidator<Movable, Movable> {

        /**
         * Creates a new instance of AbstractCatalogValidatorStub.
         *
         * @param name           name of entity
         * @param catalogService service for catalog
         */
        AbstractCatalogValidatorStub(final String name, final CatalogService<Movable> catalogService) {
            super(name, catalogService);
        }

        @Override
        protected Movable getData(final Movable data) {
            return getCatalogService().get(data.getId());
        }

        @Override
        protected List<Movable> getList(final Movable data) {
            return getCatalogService().getAll();
        }

        @Override
        protected void validateDataDeep(final Movable data, final Result result) {
            result.addEvent(new Event(Severity.WARN, KEY, VALUE));
        }

    }

    /**
     * A class represents movable stub.
     */
    private static final class MovableStub implements Movable {

        /**
         * SerialVersionUID
         */
        private static final long serialVersionUID = 1L;

        /**
         * ID
         */
        private Integer id;

        /**
         * Position
         */
        private int position;

        /**
         * Creates a new instance of MovableStub.
         *
         * @param id ID
         */
        MovableStub(final Integer id) {
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public void setId(final Integer id) {
            this.id = id;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public void setPosition(final int position) {
            this.position = position;
        }
    }

}
