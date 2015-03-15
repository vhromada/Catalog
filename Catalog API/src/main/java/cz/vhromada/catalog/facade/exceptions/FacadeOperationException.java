package cz.vhromada.catalog.facade.exceptions;

/**
 * A class represents exception in facade operation.
 *
 * @author Vladimir Hromada
 */
public class FacadeOperationException extends RuntimeException {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of FacadeOperationException.
     *
     * @param message message
     */
    public FacadeOperationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance of FacadeOperationException.
     *
     * @param message message
     * @param cause   the cause
     */
    public FacadeOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
