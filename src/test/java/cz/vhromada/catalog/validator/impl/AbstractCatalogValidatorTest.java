package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link AbstractCatalogValidator}.
 *
 * @author Vladimir Hromada
 */
public class AbstractCatalogValidatorTest extends AbstractValidatorTest<Movable, Movable> {

    /**
     * Event key
     */
    private static final String KEY = "key";

    /**
     * Event value
     */
    private static final String VALUE = "value";

    /**
     * Test method for {@link AbstractCatalogValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP}.
     */
    @Test
    @Override
    public void validate_Deep() {
        final Result<Void> result = getCatalogValidator().validate(getValidatingData(), ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.WARN));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.WARN, KEY, VALUE)));

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Movable> getCatalogValidator() {
        return new AbstractCatalogValidatorStub(getName());
    }

    @Override
    protected Movable getValidatingData() {
        return new MovableStub();
    }

    @Override
    protected Movable getRepositoryData() {
        return new MovableStub();
    }

    @Override
    protected Movable getItem1() {
        return new MovableStub();
    }

    @Override
    protected Movable getItem2() {
        return new MovableStub();
    }

    @Override
    protected String getName() {
        return "Stub";
    }

    @Override
    protected String getPrefix() {
        return "STUB";
    }

    /**
     * A class represents abstract catalog validator stub.
     */
    private final class AbstractCatalogValidatorStub extends AbstractCatalogValidator<Movable, Movable> {

        /**
         * Creates a new instance of AbstractCatalogValidatorStub.
         *
         * @param name name of entity
         */
        AbstractCatalogValidatorStub(final String name) {
            super(name);
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
