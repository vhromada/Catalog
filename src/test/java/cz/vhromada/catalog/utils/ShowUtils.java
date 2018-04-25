package cz.vhromada.catalog.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.common.utils.CollectionUtils;

/**
 * A class represents utility class for shows.
 *
 * @author Vladimir Hromada
 */
public final class ShowUtils {

    /**
     * ID
     */
    public static final int ID = 1;

    /**
     * Count of shows
     */
    public static final int SHOWS_COUNT = 3;

    /**
     * Position
     */
    public static final int POSITION = 10;

    /**
     * Show name
     */
    private static final String SHOW = "Show ";

    /**
     * Creates a new instance of ShowUtils.
     */
    private ShowUtils() {
    }

    /**
     * Returns show.
     *
     * @param id ID
     * @return show
     */
    public static cz.vhromada.catalog.domain.Show newShowDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Show show = new cz.vhromada.catalog.domain.Show();
        updateShow(show);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreDomain(id)));
        show.setPicture(id);
        if (id != null) {
            show.setId(id);
            show.setPosition(id - 1);
        } else {
            show.setPosition(0);
        }
        show.setSeasons(new ArrayList<>());

        return show;
    }

    /**
     * Returns show with seasons.
     *
     * @param id ID
     * @return show with seasons
     */
    public static cz.vhromada.catalog.domain.Show newShowWithSeasons(final Integer id) {
        final cz.vhromada.catalog.domain.Show show = newShowDomain(id);
        show.setSeasons(CollectionUtils.newList(SeasonUtils.newSeasonWithEpisodes(id)));

        return show;
    }

    /**
     * Updates show fields.
     *
     * @param show show
     */
    @SuppressWarnings("Duplicates")
    public static void updateShow(final cz.vhromada.catalog.domain.Show show) {
        show.setCzechName("czName");
        show.setOriginalName("origName");
        show.setCsfd("Csfd");
        show.setImdbCode(1000);
        show.setWikiEn("enWiki");
        show.setWikiCz("czWiki");
        show.setNote("Note");
    }

    /**
     * Returns show.
     *
     * @param id ID
     * @return show
     */
    public static Show newShow(final Integer id) {
        final Show show = new Show();
        updateShow(show);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(id)));
        show.setPicture(id);
        if (id != null) {
            show.setId(id);
            show.setPosition(id - 1);
        } else {
            show.setPosition(0);
        }

        return show;
    }

    /**
     * Updates show fields.
     *
     * @param show show
     */
    @SuppressWarnings("Duplicates")
    public static void updateShow(final Show show) {
        show.setCzechName("czName");
        show.setOriginalName("origName");
        show.setCsfd("Csfd");
        show.setImdbCode(1000);
        show.setWikiEn("enWiki");
        show.setWikiCz("czWiki");
        show.setNote("Note");
    }

    /**
     * Returns shows.
     *
     * @return shows
     */
    public static List<cz.vhromada.catalog.domain.Show> getShows() {
        final List<cz.vhromada.catalog.domain.Show> shows = new ArrayList<>();
        for (int i = 0; i < SHOWS_COUNT; i++) {
            shows.add(getShow(i + 1));
        }

        return shows;
    }

    /**
     * Returns show for index.
     *
     * @param index index
     * @return show for index
     */
    public static cz.vhromada.catalog.domain.Show getShow(final int index) {
        final int imdbMultiplier = 100;

        final cz.vhromada.catalog.domain.Show show = new cz.vhromada.catalog.domain.Show();
        show.setId(index);
        show.setCzechName(SHOW + index + " czech name");
        show.setOriginalName(SHOW + index + " original name");
        show.setCsfd(SHOW + index + " CSFD");
        show.setImdbCode(index * imdbMultiplier);
        show.setWikiEn(SHOW + index + " English Wikipedia");
        show.setWikiCz(SHOW + index + " Czech Wikipedia");
        show.setPicture(MovieUtils.MOVIES_COUNT + index);
        show.setNote(index == 2 ? SHOW + "2 note" : "");
        show.setPosition(index - 1);
        final List<Genre> genres = new ArrayList<>();
        genres.add(GenreUtils.getGenreDomain(index));
        if (index == 3) {
            genres.add(GenreUtils.getGenreDomain(4));
        }
        show.setGenres(genres);
        show.setSeasons(SeasonUtils.getSeasons(index));

        return show;
    }

    /**
     * Returns show.
     *
     * @param entityManager entity manager
     * @param id            show ID
     * @return show
     */
    public static cz.vhromada.catalog.domain.Show getShow(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Show.class, id);
    }

    /**
     * Returns show with updated fields.
     *
     * @param id            show ID
     * @param entityManager entity manager
     * @return show with updated fields
     */
    public static cz.vhromada.catalog.domain.Show updateShow(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Show show = getShow(entityManager, id);
        updateShow(show);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));
        show.setPosition(POSITION);

        return show;
    }

    /**
     * Returns count of shows.
     *
     * @param entityManager entity manager
     * @return count of shows
     */
    public static int getShowsCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Show s", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts shows deep equals.
     *
     * @param expected expected shows
     * @param actual   actual shows
     */
    public static void assertShowsDeepEquals(final List<cz.vhromada.catalog.domain.Show> expected, final List<cz.vhromada.catalog.domain.Show> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertShowDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts show deep equals.
     *
     * @param expected expected show
     * @param actual   actual show
     */
    public static void assertShowDeepEquals(final cz.vhromada.catalog.domain.Show expected, final cz.vhromada.catalog.domain.Show actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getCzechName()).isEqualTo(actual.getCzechName());
            softly.assertThat(expected.getOriginalName()).isEqualTo(actual.getOriginalName());
            softly.assertThat(expected.getCsfd()).isEqualTo(actual.getCsfd());
            softly.assertThat(expected.getImdbCode()).isEqualTo(actual.getImdbCode());
            softly.assertThat(expected.getWikiEn()).isEqualTo(actual.getWikiEn());
            softly.assertThat(expected.getWikiCz()).isEqualTo(actual.getWikiCz());
            softly.assertThat(expected.getPicture()).isEqualTo(actual.getPicture());
            softly.assertThat(expected.getNote()).isEqualTo(actual.getNote());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
            GenreUtils.assertGenresDeepEquals(expected.getGenres(), actual.getGenres());
            if (expected.getSeasons() == null) {
                softly.assertThat(actual.getSeasons()).isNull();
            } else {
                SeasonUtils.assertSeasonsDeepEquals(expected.getSeasons(), actual.getSeasons());
            }
        });
    }

    /**
     * Asserts shows deep equals.
     *
     * @param expected expected shows
     * @param actual   actual shows
     */
    public static void assertShowListDeepEquals(final List<Show> expected, final List<cz.vhromada.catalog.domain.Show> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertShowDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts show deep equals.
     *
     * @param expected expected show
     * @param actual   actual show
     */
    public static void assertShowDeepEquals(final Show expected, final cz.vhromada.catalog.domain.Show actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getCzechName()).isEqualTo(expected.getCzechName());
            softly.assertThat(actual.getOriginalName()).isEqualTo(expected.getOriginalName());
            softly.assertThat(actual.getCsfd()).isEqualTo(expected.getCsfd());
            softly.assertThat(actual.getImdbCode()).isEqualTo(expected.getImdbCode());
            softly.assertThat(actual.getWikiEn()).isEqualTo(expected.getWikiEn());
            softly.assertThat(actual.getWikiCz()).isEqualTo(expected.getWikiCz());
            softly.assertThat(actual.getPicture()).isEqualTo(expected.getPicture());
            softly.assertThat(actual.getNote()).isEqualTo(expected.getNote());
            softly.assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
            GenreUtils.assertGenreListDeepEquals(expected.getGenres(), actual.getGenres());
        });
    }

}
