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
