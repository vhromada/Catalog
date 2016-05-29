package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import cz.vhromada.catalog.dao.entities.Movable;
import cz.vhromada.catalog.service.CatalogService;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link AbstractCatalogService}.
 *
 * @author Vladimir Hromada
 */
public class AbstractCatalogServiceTest extends AbstractServiceTest<Movable> {

    /**
     * Instance of {@link JpaRepository}
     */
    @Mock
    private JpaRepository<Movable, Integer> repository;

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null repository.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMovableRepository() {
        new AbstractCatalogServiceStub(null, getCache(), getCacheKey());
    }

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null cache for datas.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullCache() {
        new AbstractCatalogServiceStub(repository, null, getCacheKey());
    }

    /**
     * Test method for {@link AbstractCatalogService#AbstractCatalogService(JpaRepository, Cache, String)} with null cache key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullCacheKey() {
        new AbstractCatalogServiceStub(repository, getCache(), null);
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
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPosition(), actual.getPosition());

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
