package cz.vhromada.catalog.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.vhromada.catalog.common.Movable;

/**
 * A class represents utility class for working with collections.
 *
 * @author Vladimir Hromada
 */
public final class CollectionUtils {

    /**
     * Creates a new instance of CollectionUtils.
     */
    private CollectionUtils() {
    }

    /**
     * Creates a new list with data.
     *
     * @param data data
     * @param <T>  type of data
     * @return list with data
     */
    @SafeVarargs
    public static <T> List<T> newList(final T... data) {
        return new ArrayList<>(Arrays.asList(data));
    }

    /**
     * Returns sorted data.
     *
     * @param data data for sorting
     * @param <T>  type of data
     * @return sorted data
     */
    public static <T extends Movable> List<T> getSortedData(final List<T> data) {
        final List<T> sortedData = new ArrayList<>(data);
        sortedData.sort((o1, o2) -> {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }

            final int result = Integer.compare(o1.getPosition(), o2.getPosition());
            if (result == 0) {
                return Integer.compare(o1.getId(), o2.getId());
            }

            return result;
        });

        return sortedData;
    }

}
