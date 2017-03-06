package cz.vhromada.catalog.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Medium;

/**
 * A class represents utility class for media.
 *
 * @author Vladimir Hromada
 */
public final class MediumUtils {

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Count of media
     */
    public static final int MEDIA_COUNT = 4;

    /**
     * Creates a new instance of MediumUtils.
     */
    private MediumUtils() {
    }

    /**
     * Returns medium.
     *
     * @param id ID
     * @return medium
     */
    public static cz.vhromada.catalog.domain.Medium newMediumDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Medium medium = new cz.vhromada.catalog.domain.Medium();
        medium.setLength(10);
        medium.setNumber(1);
        if (id != null) {
            medium.setId(id);
        }

        return medium;
    }

    /**
     * Returns medium.
     *
     * @param id ID
     * @return medium
     */
    public static Medium newMedium(final Integer id) {
        final Medium medium = new Medium();
        medium.setLength(10);
        medium.setNumber(1);
        if (id != null) {
            medium.setId(id);
        }

        return medium;
    }

    /**
     * Returns media.
     *
     * @return media
     */
    public static List<cz.vhromada.catalog.domain.Medium> getMedia() {
        final List<cz.vhromada.catalog.domain.Medium> media = new ArrayList<>();
        for (int i = 0; i < MEDIA_COUNT; i++) {
            media.add(getMedium(i + 1));
        }

        return media;
    }

    /**
     * Returns medium for index.
     *
     * @param index index
     * @return medium for index
     */
    public static cz.vhromada.catalog.domain.Medium getMedium(final int index) {
        final int lengthMultiplier = 100;

        final cz.vhromada.catalog.domain.Medium medium = new cz.vhromada.catalog.domain.Medium();
        medium.setId(index);
        medium.setNumber(index < 4 ? 1 : 2);
        medium.setLength(index * lengthMultiplier);

        return medium;
    }

    /**
     * Returns count of media.
     *
     * @param entityManager entity manager
     * @return count of media
     */
    public static int getMediaCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Medium m", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected media
     * @param actual   actual media
     */
    public static void assertMediaDeepEquals(final List<cz.vhromada.catalog.domain.Medium> expected, final List<cz.vhromada.catalog.domain.Medium> actual) {
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(expected.size()));
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertMediumDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected medium
     * @param actual   actual medium
     */
    private static void assertMediumDeepEquals(final cz.vhromada.catalog.domain.Medium expected, final cz.vhromada.catalog.domain.Medium actual) {
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getNumber(), is(expected.getNumber()));
        assertThat(actual.getLength(), is(expected.getLength()));
    }


    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of medium
     * @param actual   actual media
     */
    public static void assertMediumListDeepEquals(final List<Medium> expected, final List<cz.vhromada.catalog.domain.Medium> actual) {
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(expected.size()));
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertMediumDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected medium
     * @param actual   actual medium
     */
    private static void assertMediumDeepEquals(final Medium expected, final cz.vhromada.catalog.domain.Medium actual) {
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getNumber(), is(expected.getNumber()));
        assertThat(actual.getLength(), is(expected.getLength()));
    }

}
