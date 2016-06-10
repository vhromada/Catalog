package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entities.Genre;
import cz.vhromada.catalog.entities.Show;

/**
 * A class represents utility class for shows.
 *
 * @author Vladimir Hromada
 */
public final class ShowUtils {

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Count of shows
     */
    public static final int SHOWS_COUNT = 3;

    /**
     * Position
     */
    public static final int POSITION = 10;

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
    public static Show newShow(final Integer id) {
        final Show show = new Show();
        updateShow(show);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(id)));
        if (id != null) {
            show.setId(id);
            show.setPosition(id - 1);
        }

        return show;
    }

    /**
     * Updates show fields.
     *
     * @param show show
     */
    public static void updateShow(final Show show) {
        show.setCzechName("czName");
        show.setOriginalName("origName");
        show.setCsfd("Csfd");
        show.setImdbCode(1000);
        show.setWikiEn("enWiki");
        show.setWikiCz("czWiki");
        show.setPicture("Picture");
        show.setNote("Note");
    }

    /**
     * Returns shows.
     *
     * @return shows
     */
    public static List<Show> getShows() {
        final List<Show> shows = new ArrayList<>();
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
    public static Show getShow(final int index) {
        final Show show = new Show();
        show.setId(index);
        show.setCzechName("Show " + index + " czech name");
        show.setOriginalName("Show " + index + " original name");
        show.setCsfd("Show " + index + " CSFD");
        show.setImdbCode(index * 100);
        show.setWikiEn("Show " + index + " English Wikipedia");
        show.setWikiCz("Show " + index + " Czech Wikipedia");
        show.setPicture("Show " + index + " pc");
        show.setNote(index == 2 ? "Show 2 note" : "");
        show.setPosition(index - 1);
        final List<Genre> genres = new ArrayList<>();
        genres.add(GenreUtils.getGenre(index));
        if (index == 3) {
            genres.add(GenreUtils.getGenre(4));
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
    public static Show getShow(final EntityManager entityManager, final int id) {
        return entityManager.find(Show.class, id);
    }

    /**
     * Returns show with updated fields.
     *
     * @param id            show ID
     * @param entityManager entity manager
     * @return show with updated fields
     */
    public static Show updateShow(final EntityManager entityManager, final int id) {
        final Show show = getShow(entityManager, id);
        updateShow(show);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
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
    public static void assertShowsDeepEquals(final List<Show> expected, final List<Show> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
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
    public static void assertShowDeepEquals(final Show expected, final Show actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCzechName(), actual.getCzechName());
        assertEquals(expected.getOriginalName(), actual.getOriginalName());
        assertEquals(expected.getCsfd(), actual.getCsfd());
        assertEquals(expected.getImdbCode(), actual.getImdbCode());
        assertEquals(expected.getWikiEn(), actual.getWikiEn());
        assertEquals(expected.getWikiCz(), actual.getWikiCz());
        assertEquals(expected.getPicture(), actual.getPicture());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
        GenreUtils.assertGenresDeepEquals(expected.getGenres(), actual.getGenres());
        if (expected.getSeasons() == null) {
            assertNull(actual.getSeasons());
        } else {
            SeasonUtils.assertSeasonsDeepEquals(expected.getSeasons(), actual.getSeasons());
        }
    }

}
