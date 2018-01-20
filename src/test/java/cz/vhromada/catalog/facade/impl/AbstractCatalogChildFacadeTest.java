package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.facade.CatalogChildFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link AbstractCatalogChildFacade}.
 *
 * @author Vladimir Hromada
 */
class AbstractCatalogChildFacadeTest extends AbstractChildFacadeTest<Movable, Movable, Movable, Movable> {

    private Movable movable;

    /**
     * Test method for {@link AbstractCatalogChildFacade#AbstractCatalogChildFacade(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null
     * service for catalog.
     */
    @Test
    void constructor_NullCatalogService() {
        assertThatThrownBy(() -> new AbstractCatalogChildFacadeStub(null, getConverter(), getParentCatalogValidator(), getChildCatalogValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link AbstractCatalogChildFacade#AbstractCatalogChildFacade(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null
     * converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new AbstractCatalogChildFacadeStub(getCatalogService(), null, getParentCatalogValidator(), getChildCatalogValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link AbstractCatalogChildFacade#AbstractCatalogChildFacade(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null
     * parent catalog validator.
     */
    @Test
    void constructor_NullParentCatalogValidator() {
        assertThatThrownBy(() -> new AbstractCatalogChildFacadeStub(getCatalogService(), getConverter(), null, getChildCatalogValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link AbstractCatalogChildFacade#AbstractCatalogChildFacade(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null
     * child catalog validator.
     */
    @Test
    void constructor_NullChildCatalogValidator() {
        assertThatThrownBy(() -> new AbstractCatalogChildFacadeStub(getCatalogService(), getConverter(), getParentCatalogValidator(), null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected CatalogChildFacade<Movable, Movable> getCatalogChildFacade() {
        return new AbstractCatalogChildFacadeStub(getCatalogService(), getConverter(), getParentCatalogValidator(), getChildCatalogValidator());
    }

    @Override
    protected Movable newParentEntity(final Integer id) {
        return getMovable(id);
    }

    @Override
    protected Movable newParentDomain(final Integer id) {
        return getMovable(id);
    }

    @Override
    protected Movable newParentDomainWithChildren(final Integer id, final List<Movable> children) {
        return getMovable(id);
    }

    @Override
    protected Movable newChildEntity(final Integer id) {
        return getMovable(id);
    }

    @Override
    protected Movable newChildDomain(final Integer id) {
        return getMovable(id);
    }

    @Override
    protected Class<Movable> getParentEntityClass() {
        return Movable.class;
    }

    @Override
    protected Class<Movable> getParentDomainClass() {
        return Movable.class;
    }

    @Override
    protected Class<Movable> getChildEntityClass() {
        return Movable.class;
    }

    @Override
    protected Class<Movable> getChildDomainClass() {
        return Movable.class;
    }

    @Override
    protected void assertParentDeepEquals(final Movable expected, final Movable actual) {
    }

    /**
     * Returns movable object.
     *
     * @param id ID
     * @return movable object
     */
    private Movable getMovable(final Integer id) {
        if (movable == null) {
            movable = new MovableStub(id);
        } else {
            movable.setId(id);
        }

        return movable;
    }

    /**
     * A class represents abstract facade for catalog for child data stub.
     */
    private static final class AbstractCatalogChildFacadeStub extends AbstractCatalogChildFacade<Movable, Movable, Movable, Movable> {

        /**
         * Creates a new instance of AbstractCatalogChildFacadeStub.
         *
         * @param catalogService         service for catalog
         * @param converter              converter
         * @param parentCatalogValidator validator for catalog for parent data
         * @param childCatalogValidator  validator for catalog for child data
         */
        AbstractCatalogChildFacadeStub(final CatalogService<Movable> catalogService, final Converter converter,
            final CatalogValidator<Movable> parentCatalogValidator, final CatalogValidator<Movable> childCatalogValidator) {
            super(catalogService, converter, parentCatalogValidator, childCatalogValidator);
        }

        @Override
        protected Movable getDomainData(final Integer id) {
            for (final Movable movable : getCatalogService().getAll()) {
                if (id.equals(movable.getId())) {
                    return movable;
                }
            }

            return null;
        }

        @Override
        protected List<Movable> getDomainList(final Movable parent) {
            return CollectionUtils.newList(getCatalogService().get(parent.getId()));
        }

        @Override
        protected Movable getForAdd(final Movable parent, final Movable data) {
            return getCatalogService().get(parent.getId());
        }

        @Override
        protected Movable getForUpdate(final Movable data) {
            return getDomainData(getDataForUpdate(data).getId());
        }

        @Override
        protected Movable getForRemove(final Movable data) {
            return getDomainData(data.getId());
        }

        @Override
        protected Movable getForDuplicate(final Movable data) {
            return getDomainData(data.getId());
        }

        @Override
        protected Movable getForMove(final Movable data, final boolean up) {
            return getDomainData(data.getId());
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
