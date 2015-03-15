package cz.vhromada.catalog.service.exceptions;

/**
 * A class represents exception in service operation.
 *
 * @author Vladimir Hromada
 */
public class ServiceOperationException extends RuntimeException {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of ServiceOperationException.
     *
     * @param message message
     */
    public ServiceOperationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance of ServiceOperationException.
     *
     * @param message message
     * @param cause   the cause
     */
    public ServiceOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
