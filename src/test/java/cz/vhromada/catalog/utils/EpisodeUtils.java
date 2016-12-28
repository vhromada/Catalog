package cz.vhromada.catalog.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Episode;

/**
 * A class represents utility class for episodes.
 *
 * @author Vladimir Hromada
 */
public final class EpisodeUtils {

    /**
     * Count of episodes
     */
    public static final int EPISODES_COUNT = 27;

    /**
     * Count of episodes in season
     */
    public static final int EPISODES_PER_SEASON_COUNT = 3;

    /**
     * Count of episodes in show
     */
    public static final int EPISODES_PER_SHOW_COUNT = 9;

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Position
     */
    public static final Integer POSITION = 10;

    /**
     * Multipliers for length
     */
    private static final int[] LENGTH_MULTIPLIERS = { 1, 10, 100 };

    /**
     * Creates a new instance of EpisodeUtils.
     */
    private EpisodeUtils() {
    }

    /**
     * Returns episode.
     *
     * @param id ID
     * @return episode
     */
    public static cz.vhromada.catalog.domain.Episode newEpisodeDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Episode episode = new cz.vhromada.catalog.domain.Episode();
        updateEpisode(episode);
        if (id != null) {
            episode.setId(id);
            episode.setPosition(id - 1);
        }

        return episode;
    }

    /**
     * Updates episode fields.
     *
     * @param episode episode
     */
    public static void updateEpisode(final cz.vhromada.catalog.domain.Episode episode) {
        episode.setNumber(2);
        episode.setName("Name");
        episode.setLength(5);
        episode.setNote("Note");
    }

    /**
     * Returns episode.
     *
     * @param id ID
     * @return episode
     */
    public static Episode newEpisode(final Integer id) {
        final Episode episode = new Episode();
        updateEpisode(episode);
        if (id != null) {
            episode.setId(id);
            episode.setPosition(id - 1);
        }

        return episode;
    }

    /**
     * Updates episode fields.
     *
     * @param episode episode
     */
    public static void updateEpisode(final Episode episode) {
        episode.setNumber(2);
        episode.setName("Name");
        episode.setLength(5);
        episode.setNote("Note");
    }

    /**
     * Returns episodes for show and season.
     *
     * @param show   show
     * @param season season
     * @return episodes for show and season
     */
    public static List<cz.vhromada.catalog.domain.Episode> getEpisodes(final int show, final int season) {
        final List<cz.vhromada.catalog.domain.Episode> episodes = new ArrayList<>();
        for (int i = 1; i <= EPISODES_PER_SEASON_COUNT; i++) {
            episodes.add(getEpisode(show, season, i));
        }

        return episodes;
    }

    /**
     * Returns episode for index.
     *
     * @param index index
     * @return episode for index
     */
    public static cz.vhromada.catalog.domain.Episode getEpisode(final int index) {
        final int showNumber = (index - 1) / EPISODES_PER_SHOW_COUNT + 1;
        final int seasonNumber = (index - 1) % EPISODES_PER_SHOW_COUNT / EPISODES_PER_SEASON_COUNT + 1;
        final int episodeNumber = (index - 1) % EPISODES_PER_SEASON_COUNT + 1;

        return getEpisode(showNumber, seasonNumber, episodeNumber);
    }

    /**
     * Returns episode for indexes.
     *
     * @param showIndex    show index
     * @param seasonIndex  season index
     * @param episodeIndex episode index
     * @return episode for indexes
     */
    public static cz.vhromada.catalog.domain.Episode getEpisode(final int showIndex, final int seasonIndex, final int episodeIndex) {
        final cz.vhromada.catalog.domain.Episode episode = new cz.vhromada.catalog.domain.Episode();
        episode.setId((showIndex - 1) * EPISODES_PER_SHOW_COUNT + (seasonIndex - 1) * EPISODES_PER_SEASON_COUNT + episodeIndex);
        episode.setNumber(episodeIndex);
        episode.setName("Show " + showIndex + " Season " + seasonIndex + " Episode " + episodeIndex);
        episode.setLength(episodeIndex * LENGTH_MULTIPLIERS[seasonIndex - 1]);
        episode.setNote(episodeIndex == 2 ? "Show " + showIndex + " Season " + seasonIndex + " Episode 2 note" : "");
        episode.setPosition(episodeIndex - 1);

        return episode;
    }

    /**
     * Returns episode.
     *
     * @param entityManager entity manager
     * @param id            episode ID
     * @return episode
     */
    public static cz.vhromada.catalog.domain.Episode getEpisode(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Episode.class, id);
    }

    /**
     * Returns count of episodes.
     *
     * @param entityManager entity manager
     * @return count of episodes
     */
    public static int getEpisodesCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(e.id) FROM Episode e", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts episodes deep equals.
     *
     * @param expected expected episodes
     * @param actual   actual episodes
     */
    public static void assertEpisodesDeepEquals(final List<cz.vhromada.catalog.domain.Episode> expected,
            final List<cz.vhromada.catalog.domain.Episode> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertEpisodeDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected episode
     * @param actual   actual episode
     */
    public static void assertEpisodeDeepEquals(final cz.vhromada.catalog.domain.Episode expected, final cz.vhromada.catalog.domain.Episode actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLength(), actual.getLength());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    /**
     * Asserts episodes deep equals.
     *
     * @param expected expected list of episode
     * @param actual   actual episodes
     */
    public static void assertEpisodeListDeepEquals(final List<Episode> expected, final List<cz.vhromada.catalog.domain.Episode> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertEpisodeDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected episode
     * @param actual   actual episode
     */
    public static void assertEpisodeDeepEquals(final Episode expected, final cz.vhromada.catalog.domain.Episode actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLength(), actual.getLength());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

}