/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.gui.inventory;

public interface ClickHandler {

    /**
     * Handles logic for click events. Returning false will cancel the click.
     *
     * @param clickEvent Click event
     * @return true if the click should be allowed, false if not
     */
    public boolean onClick(final ClickEvent clickEvent);

}
