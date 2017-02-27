package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;

/**
 * A class represents test for class {@link AbstractParentCatalogFacade}.
 *
 * @author Vladimir Hromada
 */
public class AbstractParentCatalogFacadeTest extends AbstractParentFacadeTest<Movable, Movable> {

    @Override
    protected AbstractParentCatalogFacade<Movable, Movable> getParentCatalogFacade() {
        return new AbstractParentCatalogFacadeStub(getCatalogService(), getConverter(), getCatalogValidator());
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
    private static final class AbstractParentCatalogFacadeStub extends AbstractParentCatalogFacade<Movable, Movable> {

        /**
         * Creates a new instance of AbstractParentCatalogFacadeStub.
         *
         * @param catalogService   service for catalog
         * @param converter        converter
         * @param catalogValidator validator for catalog
         */
        AbstractParentCatalogFacadeStub(final CatalogService<Movable> catalogService, final Converter converter,
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
