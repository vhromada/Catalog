package cz.vhromada.catalog.dao.exceptions;

/**
 * A class represents exception in working with data storage.
 *
 * @author Vladimir Hromada
 */
public class DataStorageException extends RuntimeException {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of DataStorageException.
     *
     * @param message message
     */
    public DataStorageException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance of DataStorageException.
     *
     * @param message message
     * @param cause   the cause
     */
    public DataStorageException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
