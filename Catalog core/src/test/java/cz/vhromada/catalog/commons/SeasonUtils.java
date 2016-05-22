package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.entities.Season;

/**
 * A class represents utility class for seasons.
 *
 * @author Vladimir Hromada
 */
public final class SeasonUtils {

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Position
     */
    public static final Integer POSITION = 10;

    /**
     * Count of seasons
     */
    public static final int SEASONS_COUNT = 9;

    /**
     * Count of seasons in show
     */
    public static final int SEASONS_PER_SHOW_COUNT = 3;

    /**
     * Creates a new instance of SeasonUtils.
     */
    private SeasonUtils() {
    }

    /**
     * Returns season.
     *
     * @param id ID
     * @return season
     */
    public static Season newSeason(final Integer id) {
        final Season season = new Season();
        updateSeason(season);
        season.setShow(5);
        if (id != null) {
            season.setId(id);
            season.setPosition(id - 1);
        }

        return season;
    }

    /**
     * Updates season fields.
     *
     * @param season season
     */
    public static void updateSeason(final Season season) {
        season.setNumber(2);
        season.setStartYear(2000);
        season.setEndYear(2001);
        season.setLanguage(Language.EN);
        season.setSubtitles(CollectionUtils.newList(Language.CZ));
        season.setNote("Note");
    }

    /**
     * Returns seasons.
     *
     * @param show index of show
     * @return seasons
     */
    public static List<Season> getSeasons(final int show) {
        final List<Season> seasons = new ArrayList<>();
        for (int i = 0; i < SEASONS_PER_SHOW_COUNT; i++) {
            seasons.add(getSeason(show, i + 1));
        }

        return seasons;
    }

    /**
     * Returns season for indexes.
     *
     * @param showIndex   show index
     * @param seasonIndex season index
     * @return season for indexes
     */
    public static Season getSeason(final int showIndex, final int seasonIndex) {
        final Season season = new Season();
        season.setId((showIndex - 1) * SEASONS_PER_SHOW_COUNT + seasonIndex);
        season.setNumber(seasonIndex);
        season.setStartYear(1980 + seasonIndex);
        season.setEndYear(seasonIndex == 3 ? 1984 : 1982);
        season.setNote(seasonIndex == 2 ? "Show " + showIndex + " Season 2 note" : "");
        season.setPosition(seasonIndex - 1);
        season.setShow(showIndex);
        final List<Language> subtitles = new ArrayList<>();
        final Language language;
        switch (seasonIndex) {
            case 1:
                language = Language.EN;
                subtitles.add(Language.CZ);
                subtitles.add(Language.EN);
                break;
            case 2:
                language = Language.FR;
                break;
            case 3:
                language = Language.JP;
                subtitles.add(Language.EN);
                break;
            default:
                throw new IllegalArgumentException("Bad season index");
        }
        season.setLanguage(language);
        season.setSubtitles(subtitles);

        return season;
    }

    /**
     * Returns season.
     *
     * @param entityManager entity manager
     * @param id            season ID
     * @return season
     */
    public static Season getSeason(final EntityManager entityManager, final int id) {
        return entityManager.find(Season.class, id);
    }

    /**
     * Returns season with updated fields.
     *
     * @param id            season ID
     * @param entityManager entity manager
     * @return season with updated fields
     */
    public static Season updateSeason(final int id, final EntityManager entityManager) {
        final Season season = getSeason(entityManager, id);
        updateSeason(season);
        season.setPosition(POSITION);

        return season;
    }

    /**
     * Returns count of seasons.
     *
     * @param entityManager entity manager
     * @return count of seasons
     */
    public static int getSeasonsCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Season s", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts seasons deep equals.
     *
     * @param expected expected seasons
     * @param actual   actual seasons
     */
    public static void assertSeasonsDeepEquals(final List<Season> expected, final List<Season> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertSeasonDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected expected season
     * @param actual   actual season
     */
    public static void assertSeasonDeepEquals(final Season expected, final Season actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getStartYear(), actual.getStartYear());
        assertEquals(expected.getEndYear(), actual.getEndYear());
        assertEquals(expected.getLanguage(), actual.getLanguage());
        assertEquals(expected.getSubtitles(), actual.getSubtitles());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
        assertEquals(expected.getShow(), actual.getShow());
    }

}
