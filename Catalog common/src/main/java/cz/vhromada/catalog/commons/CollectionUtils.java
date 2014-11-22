package cz.vhromada.catalog.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class represents utility class for working with collections.
 *
 * @author Vladimir Hromada
 */
public final class CollectionUtils {

    /** Creates a new instance of CollectionUtils. */
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

}
