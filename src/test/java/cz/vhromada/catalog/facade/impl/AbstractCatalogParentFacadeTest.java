package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;

import org.junit.Test;

/**
 * A class represents test for class {@link AbstractCatalogParentFacade}.
 *
 * @author Vladimir Hromada
 */
public class AbstractCatalogParentFacadeTest extends AbstractParentFacadeTest<Movable, Movable> {

    /**
     * Test method for {@link AbstractCatalogParentFacade#AbstractCatalogParentFacade(CatalogService, Converter, CatalogValidator)} with null
     * service for catalog.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullCatalogService() {
        new AbstractCatalogParentFacadeStub(null, getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link AbstractCatalogParentFacade#AbstractCatalogParentFacade(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new AbstractCatalogParentFacadeStub(getCatalogService(), null, getCatalogValidator());
    }

    /**
     * Test method for {@link AbstractCatalogParentFacade#AbstractCatalogParentFacade(CatalogService, Converter, CatalogValidator)} with null
     * validator for catalog.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullCatalogValidator() {
        new AbstractCatalogParentFacadeStub(getCatalogService(), getConverter(), null);
    }

    @Override
    protected CatalogParentFacade<Movable> getCatalogParentFacade() {
        return new AbstractCatalogParentFacadeStub(getCatalogService(), getConverter(), getCatalogValidator());
    }

    @Override
    protected Movable newEntity(final Integer id) {
        return new MovableStub(id);
    }

    @Override
    protected Movable newDomain(final Integer id) {
        return new MovableStub(id);
    }

    @Override
    protected Class<Movable> getEntityClass() {
        return Movable.class;
    }

    @Override
    protected Class<Movable> getDomainClass() {
        return Movable.class;
    }

    /**
     * A class represents abstract facade for catalog for parent data stub.
     */
    private static final class AbstractCatalogParentFacadeStub extends AbstractCatalogParentFacade<Movable, Movable> {

        /**
         * Creates a new instance of AbstractCatalogParentFacadeStub.
         *
         * @param catalogService   service for catalog
         * @param converter        converter
         * @param catalogValidator validator for catalog
         */
        AbstractCatalogParentFacadeStub(final CatalogService<Movable> catalogService, final Converter converter,
                final CatalogValidator<Movable> catalogValidator) {
            super(catalogService, converter, catalogValidator);
        }

        @Override
        protected Class<Movable> getEntityClass() {
            return Movable.class;
        }

        @Override
        protected Class<Movable> getDomainClass() {
            return Movable.class;
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
