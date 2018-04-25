package cz.vhromada.catalog.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.common.Language;
import cz.vhromada.common.utils.CollectionUtils;

/**
 * A class represents utility class for seasons.
 *
 * @author Vladimir Hromada
 */
public final class SeasonUtils {

    /**
     * ID
     */
    public static final int ID = 1;

    /**
     * Position
     */
    public static final int POSITION = 10;

    /**
     * Count of seasons
     */
    public static final int SEASONS_COUNT = 9;

    /**
     * Count of seasons in show
     */
    public static final int SEASONS_PER_SHOW_COUNT = 3;

    /**
     * Start year
     */
    private static final int START_YEAR = 2000;

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
    public static cz.vhromada.catalog.domain.Season newSeasonDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Season season = new cz.vhromada.catalog.domain.Season();
        updateSeason(season);
        if (id != null) {
            season.setId(id);
            season.setPosition(id - 1);
        } else {
            season.setPosition(0);
        }
        season.setEpisodes(new ArrayList<>());

        return season;
    }

    /**
     * Returns season with episodes.
     *
     * @param id ID
     * @return season with episodes
     */
    public static cz.vhromada.catalog.domain.Season newSeasonWithEpisodes(final Integer id) {
        final cz.vhromada.catalog.domain.Season season = newSeasonDomain(id);
        season.setEpisodes(CollectionUtils.newList(EpisodeUtils.newEpisodeDomain(id)));

        return season;
    }

    /**
     * Updates season fields.
     *
     * @param season season
     */
    @SuppressWarnings("Duplicates")
    public static void updateSeason(final cz.vhromada.catalog.domain.Season season) {
        season.setNumber(2);
        season.setStartYear(START_YEAR);
        season.setEndYear(START_YEAR + 1);
        season.setLanguage(Language.SK);
        season.setSubtitles(CollectionUtils.newList(Language.CZ));
        season.setNote("Note");
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
        if (id != null) {
            season.setId(id);
            season.setPosition(id - 1);
        } else {
            season.setPosition(0);
        }

        return season;
    }

    /**
     * Updates season fields.
     *
     * @param season season
     */
    @SuppressWarnings("Duplicates")
    public static void updateSeason(final Season season) {
        season.setNumber(2);
        season.setStartYear(START_YEAR);
        season.setEndYear(START_YEAR + 1);
        season.setLanguage(Language.SK);
        season.setSubtitles(CollectionUtils.newList(Language.CZ));
        season.setNote("Note");
    }

    /**
     * Returns seasons for show.
     *
     * @param show show
     * @return seasons for show
     */
    public static List<cz.vhromada.catalog.domain.Season> getSeasons(final int show) {
        final List<cz.vhromada.catalog.domain.Season> seasons = new ArrayList<>();
        for (int i = 1; i <= SEASONS_PER_SHOW_COUNT; i++) {
            seasons.add(getSeason(show, i));
        }

        return seasons;
    }

    /**
     * Returns season for index.
     *
     * @param index index
     * @return season for index
     */
    public static cz.vhromada.catalog.domain.Season getSeason(final int index) {
        final int showNumber = (index - 1) / SEASONS_PER_SHOW_COUNT + 1;
        final int seasonNumber = (index - 1) % SEASONS_PER_SHOW_COUNT + 1;

        return getSeason(showNumber, seasonNumber);
    }

    /**
     * Returns season for indexes.
     *
     * @param showIndex   show index
     * @param seasonIndex season index
     * @return season for indexes
     */
    public static cz.vhromada.catalog.domain.Season getSeason(final int showIndex, final int seasonIndex) {
        final int year = 1980;

        final cz.vhromada.catalog.domain.Season season = new cz.vhromada.catalog.domain.Season();
        season.setId((showIndex - 1) * SEASONS_PER_SHOW_COUNT + seasonIndex);
        season.setNumber(seasonIndex);
        season.setStartYear(year + seasonIndex);
        season.setEndYear(year + (seasonIndex == 3 ? 4 : 2));
        season.setNote(seasonIndex == 2 ? "Show " + showIndex + " Season 2 note" : "");
        season.setPosition(seasonIndex - 1);
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
        season.setEpisodes(EpisodeUtils.getEpisodes(showIndex, seasonIndex));

        return season;
    }

    /**
     * Returns season.
     *
     * @param entityManager entity manager
     * @param id            season ID
     * @return season
     */
    public static cz.vhromada.catalog.domain.Season getSeason(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Season.class, id);
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
    public static void assertSeasonsDeepEquals(final List<cz.vhromada.catalog.domain.Season> expected, final List<cz.vhromada.catalog.domain.Season> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
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
    public static void assertSeasonDeepEquals(final cz.vhromada.catalog.domain.Season expected, final cz.vhromada.catalog.domain.Season actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getNumber()).isEqualTo(actual.getNumber());
            softly.assertThat(expected.getStartYear()).isEqualTo(actual.getStartYear());
            softly.assertThat(expected.getEndYear()).isEqualTo(actual.getEndYear());
            softly.assertThat(expected.getLanguage()).isEqualTo(actual.getLanguage());
            softly.assertThat(expected.getSubtitles()).isEqualTo(actual.getSubtitles());
            softly.assertThat(expected.getNote()).isEqualTo(actual.getNote());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
            if (expected.getEpisodes() == null) {
                softly.assertThat(actual.getEpisodes()).isNull();
            } else {
                EpisodeUtils.assertEpisodesDeepEquals(expected.getEpisodes(), actual.getEpisodes());
            }
        });
    }

    /**
     * Asserts seasons deep equals.
     *
     * @param expected expected list of season
     * @param actual   actual seasons
     */
    public static void assertSeasonListDeepEquals(final List<Season> expected, final List<cz.vhromada.catalog.domain.Season> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
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
    public static void assertSeasonDeepEquals(final Season expected, final cz.vhromada.catalog.domain.Season actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getNumber()).isEqualTo(expected.getNumber());
            softly.assertThat(actual.getStartYear()).isEqualTo(expected.getStartYear());
            softly.assertThat(actual.getEndYear()).isEqualTo(expected.getEndYear());
            softly.assertThat(actual.getLanguage()).isEqualTo(expected.getLanguage());
            softly.assertThat(actual.getSubtitles())
                .hasSameSizeAs(expected.getSubtitles())
                .hasSameElementsAs(expected.getSubtitles());
            softly.assertThat(actual.getNote()).isEqualTo(expected.getNote());
            softly.assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        });
    }

}
