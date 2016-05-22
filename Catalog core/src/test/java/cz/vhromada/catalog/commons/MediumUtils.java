package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Medium;

/**
 * A class represents utility class for mediums.
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
    public static List<Medium> getMedia() {
        final List<Medium> media = new ArrayList<>();
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
    public static Medium getMedium(final int index) {
        final Medium medium = new Medium();
        medium.setId(index);
        medium.setNumber(index < 4 ? 1 : 2);
        medium.setLength(index * 100);

        return medium;
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected media
     * @param actual   actual media
     */
    public static void assertMediaDeepEquals(final List<Medium> expected, final List<Medium> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
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
    private static void assertMediumDeepEquals(final Medium expected, final Medium actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getLength(), actual.getLength());
    }

}
