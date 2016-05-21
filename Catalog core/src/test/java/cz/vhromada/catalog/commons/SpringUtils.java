//package cz.vhromada.catalog.commons;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.Collections;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//
//import cz.vhromada.catalog.dao.entities.Episode;
//import cz.vhromada.catalog.dao.entities.Music;
//import cz.vhromada.catalog.dao.entities.Season;
//import cz.vhromada.catalog.dao.entities.Show;
//import cz.vhromada.catalog.dao.entities.Song;
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
//     * Count of shows
//     */
//    public static final int SHOWS_COUNT = 3;
//
//    /**
//     * Count of seasons
//     */
//    public static final int SEASONS_COUNT = 9;
//
//    /**
//     * Count of seasons in show
//     */
//    public static final int SEASONS_PER_SHOW_COUNT = 3;
//
//    /**
//     * Count of episodes
//     */
//    public static final int EPISODES_COUNT = 27;
//
//    /**
//     * Count of episodes in show
//     */
//    public static final int EPISODES_PER_SHOW_COUNT = 9;
//
//    /**
//     * Count of episodes in season
//     */
//    public static final int EPISODES_PER_SEASON_COUNT = 3;
//
//    /**
//     * Count of music
//     */
//    public static final int MUSIC_COUNT = 3;
//
//    /**
//     * Count of songs
//     */
//    public static final int SONGS_COUNT = 9;
//
//    /**
//     * Count of songs in music
//     */
//    public static final int SONGS_PER_MUSIC_COUNT = 3;
//
//    /**
//     * Multipliers for length
//     */
//    public static final int[] LENGTH_MULTIPLIERS = { 1, 10, 100 };
//
//    /**
//     * Creates a new instance of SpringUtils.
//     */
//    private SpringUtils() {
//    }
//
//    /**
//     * Returns count of shows.
//     *
//     * @param entityManager entity manager
//     * @return count of shows
//     */
//    public static int getShowsCount(final EntityManager entityManager) {
//        return entityManager.createQuery("SELECT COUNT(s.id) FROM Show s", Long.class).getSingleResult().intValue();
//    }
//
//    /**
//     * Returns show.
//     *
//     * @param entityManager entity manager
//     * @param id            show ID
//     * @return show
//     */
//    public static Show getShow(final EntityManager entityManager, final int id) {
//        return entityManager.find(Show.class, id);
//    }
//
//    /**
//     * Returns count of seasons.
//     *
//     * @param entityManager entity manager
//     * @return count of seasons
//     */
//    public static int getSeasonsCount(final EntityManager entityManager) {
//        return entityManager.createQuery("SELECT COUNT(s.id) FROM Season s", Long.class).getSingleResult().intValue();
//    }
//
//    /**
//     * Returns season.
//     *
//     * @param entityManager entity manager
//     * @param id            season ID
//     * @return season
//     */
//    public static Season getSeason(final EntityManager entityManager, final int id) {
//        return entityManager.find(Season.class, id);
//    }
//
//    /**
//     * Returns count of episodes.
//     *
//     * @param entityManager entity manager
//     * @return count of episodes
//     */
//    public static int getEpisodesCount(final EntityManager entityManager) {
//        return entityManager.createQuery("SELECT COUNT(e.id) FROM Episode e", Long.class).getSingleResult().intValue();
//    }
//
//    /**
//     * Returns episode.
//     *
//     * @param entityManager entity manager
//     * @param id            episode ID
//     * @return episode
//     */
//    public static Episode getEpisode(final EntityManager entityManager, final int id) {
//        return entityManager.find(Episode.class, id);
//    }
//    /**
//     * Returns count of music.
//     *
//     * @param entityManager entity manager
//     * @return count of music
//     */
//    public static int getMusicCount(final EntityManager entityManager) {
//        return entityManager.createQuery("SELECT COUNT(m.id) FROM Music m", Long.class).getSingleResult().intValue();
//    }
//
//    /**
//     * Returns music.
//     *
//     * @param entityManager entity manager
//     * @param id            music ID
//     * @return music
//     */
//    public static Music getMusic(final EntityManager entityManager, final int id) {
//        return entityManager.find(Music.class, id);
//    }
//
//    /**
//     * Returns count of songs.
//     *
//     * @param entityManager entity manager
//     * @return count of songs
//     */
//    public static int getSongsCount(final EntityManager entityManager) {
//        return entityManager.createQuery("SELECT COUNT(s.id) FROM Song s", Long.class).getSingleResult().intValue();
//    }
//
//    /**
//     * Returns song.
//     *
//     * @param entityManager entity manager
//     * @param id            song ID
//     * @return song
//     */
//    public static Song getSong(final EntityManager entityManager, final int id) {
//        return entityManager.find(Song.class, id);
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
