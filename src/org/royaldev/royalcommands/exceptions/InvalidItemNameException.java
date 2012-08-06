package org.royaldev.royalcommands.exceptions;

/**
 * Lets me know when an item's name is invalid.
 *
 * @author jkcclemens
 * @since 0.2.5pre
 */
public class InvalidItemNameException extends Exception {

    /**
     * Constructor for an InvalidItemNameException - should be thrown when
     * an item's alias is not found.
     *
     * @param s Exception comment
     */
    public InvalidItemNameException(String s) {
        super(s);
    }

    /**
     * Constructor for an InvalidItemNameException - should be thrown when
     * an item's alias is not found.
     *
     * @param cause A throwable with the cause
     */
    public InvalidItemNameException(Throwable cause) {
        super(cause);
    }

}
