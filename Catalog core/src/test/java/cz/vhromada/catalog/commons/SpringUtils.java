//package cz.vhromada.catalog.commons;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.Collections;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//
//import cz.vhromada.catalog.entities.Episode;
//import cz.vhromada.catalog.entities.Music;
//import cz.vhromada.catalog.entities.Season;
//import cz.vhromada.catalog.entities.Show;
//import cz.vhromada.catalog.entities.Song;
//import cz.vhromada.test.DeepAsserts;
//
//import net.sf.ehcache.Ehcache;
//import org.springframework.cache.Cache;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.DefaultTransactionDefinition;
//
///**
// * A class represents utility class for Spring framework.
// *
// * @author Vladimir Hromada
// */
//public final class SpringUtils {
//
//
//    /**
//     * Creates a new instance of SpringUtils.
//     */
//    private SpringUtils() {
//    }
//
//    /**
//     * Returns cache keys.
//     *
//     * @param cache cache
//     * @return cache keys
//     */
//    @SuppressWarnings("unchecked")
//    public static List getCacheKeys(final Cache cache) {
//        final List keys = ((Ehcache) cache.getNativeCache()).getKeys();
//        Collections.sort(keys);
//        return keys;
//    }
//
//    /**
//     * Asserts if value in cache is same as expected value.
//     *
//     * @param cache         cache
//     * @param key           key for getting value from cache
//     * @param expectedValue expected value
//     */
//    public static void assertCacheValue(final Cache cache, final String key, final Object expectedValue) {
//        final Cache.ValueWrapper valueWrapper = cache.get(key);
//        assertNotNull(valueWrapper);
//        DeepAsserts.assertEquals(expectedValue, valueWrapper.get());
//    }
//
//    /**
//     * Persists entity.
//     *
//     * @param transactionManager transaction manager
//     * @param entityManager      entity manager
//     * @param entity             entity
//     */
//    public static void persist(final PlatformTransactionManager transactionManager, final EntityManager entityManager, final Object entity) {
//        final TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
//        entityManager.persist(entity);
//        transactionManager.commit(transactionStatus);
//    }
//
//    /**
//     * Update entity.
//     *
//     * @param transactionManager transaction manager
//     * @param entityManager      entity manager
//     */
//    public static void merge(final PlatformTransactionManager transactionManager, final EntityManager entityManager, final Object entity) {
//        final TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
//        entityManager.merge(entity);
//        transactionManager.commit(transactionStatus);
//    }
//
//    /**
//     * Removes entities.
//     *
//     * @param transactionManager transaction manager
//     * @param entityManager      entity manager
//     * @param entityClass        entity class
//     * @param <T>                type of entity
//     */
//    public static <T> void remove(final PlatformTransactionManager transactionManager, final EntityManager entityManager, final Class<T> entityClass) {
//        final TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
//        final List<T> entities = entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
//        for (final T entity : entities) {
//            entityManager.remove(entity);
//        }
//        transactionManager.commit(transactionStatus);
//    }
//
//    /**
//     * Persists entity.
//     *
//     * @param transactionManager transaction manager
//     * @param entityManager      entity manager
//     * @param sequence           sequence
//     */
//    public static void updateSequence(final PlatformTransactionManager transactionManager, final EntityManager entityManager, final String sequence) {
//        final TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
//        entityManager.createNativeQuery("ALTER SEQUENCE " + sequence + " RESTART WITH 1").executeUpdate();
//        transactionManager.commit(transactionStatus);
//    }
//
//}
