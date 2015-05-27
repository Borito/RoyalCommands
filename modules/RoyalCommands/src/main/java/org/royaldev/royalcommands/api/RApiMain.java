/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.api;

/**
 * The main API class. Other API classes can be found here.
 * <p/>
 * <strong>DO NOT CONSTRUCT THIS.</strong> Use <code>getAPI()</code> in RoyalCommands instead.
 */
public class RApiMain {

    private final RWorldApi worldApi = new RWorldApi();
    private final RPlayerApi playerApi = new RPlayerApi();

    /**
     * Gets the PlayerAPI registered by RoyalCommands.
     *
     * @return RPlayerApi
     */
    public RPlayerApi getPlayerAPI() {
        return playerApi;
    }

    /**
     * Gets the WorldAPI registered by RoyalCommands.
     *
     * @return RWorldApi
     */
    public RWorldApi getWorldAPI() {
        return worldApi;
    }

}
