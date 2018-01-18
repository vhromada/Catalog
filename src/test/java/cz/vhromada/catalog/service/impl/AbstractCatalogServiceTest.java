package cz.vhromada.catalog.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.service.CatalogService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link AbstractCatalogService}.
 *
 * @author Vladimir Hromada
 */
class AbstractCatalogServiceTest extends AbstractServiceTest<Movable> {

    /**
     * Instance of {@link JpaRepository}
     */
    @Mock
    private JpaRepository<Movable, Integer> repository;

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null repository.
     */
    @Test
    void constructor_NullMovableRepository() {
        assertThrows(IllegalArgumentException.class, () -> new AbstractCatalogServiceStub(null, getCache(), getCacheKey()));
    }

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null cache for data.
     */
    @Test
    void constructor_NullCache() {
        assertThrows(IllegalArgumentException.class, () -> new AbstractCatalogServiceStub(repository, null, getCacheKey()));
    }

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null cache key.
     */
    @Test
    void constructor_NullCacheKey() {
        assertThrows(IllegalArgumentException.class, () -> new AbstractCatalogServiceStub(repository, getCache(), null));
    }

    @Override
    protected JpaRepository<Movable, Integer> getRepository() {
        return repository;
    }

    @Override
    protected CatalogService<Movable> getCatalogService() {
        return new AbstractCatalogServiceStub(repository, getCache(), getCacheKey());
    }

    @Override
    protected String getCacheKey() {
        return "data";
    }

    @Override
    protected Movable getItem1() {
        return new MovableStub(1, 0);
    }

    @Override
    protected Movable getItem2() {
        return new MovableStub(2, 1);
    }

    @Override
    protected Movable getAddItem() {
        return new MovableStub(null, 4);
    }

    @Override
    protected Movable getCopyItem() {
        return new MovableStub(10, 10);
    }

    @Override
    protected Class<Movable> getItemClass() {
        return Movable.class;
    }

    @Override
    protected void assertDataDeepEquals(final Movable expected, final Movable actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getPosition(), actual.getPosition())
        );
    }

    /**
     * A class represents abstract catalog service stub.
     */
    private final class AbstractCatalogServiceStub extends AbstractCatalogService<Movable> {

        /**
         * Creates a new instance of AbstractCatalogServiceStub.
         *
         * @param repository repository for data
         * @param cache      cache for data
         * @param key        cache key
         * @throws IllegalArgumentException if repository for data is null
         *                                  or cache for data is null
         *                                  or cache key is null
         */
        AbstractCatalogServiceStub(final JpaRepository<Movable, Integer> repository, final Cache cache, final String key) {
            super(repository, cache, key);
        }

        @Override
        protected Movable getCopy(final Movable data) {
            return getCopyItem();
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
         * @param id       ID
         * @param position position
         */
        MovableStub(final Integer id, final int position) {
            this.id = id;
            this.position = position;
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
