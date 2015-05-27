/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.exceptions;

/**
 * Lets me know when an item's name is invalid.
 *
 * @author jkcclemens
 * @since 0.2.5pre
 */
public class InvalidItemNameException extends Exception {

    /**
     * Constructor for an InvalidItemNameException - should be thrown when an item's alias is not found.
     *
     * @param s Exception comment
     */
    public InvalidItemNameException(String s) {
        super(s);
    }

    /**
     * Constructor for an InvalidItemNameException - should be thrown when an item's alias is not found.
     *
     * @param cause A throwable with the cause
     */
    public InvalidItemNameException(Throwable cause) {
        super(cause);
    }

}
