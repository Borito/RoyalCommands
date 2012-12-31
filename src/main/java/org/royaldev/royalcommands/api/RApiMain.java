package org.royaldev.royalcommands.api;

/**
 * The main API class. Other API classes can be found here.
 * <p/>
 * <strong>DO NOT CONSTRUCT THIS.</strong> Use <code>getAPI()</code> in RoyalCommands instead.
 */
public class RApiMain {

    private RWorldApi worldApi = new RWorldApi();
    private RPlayerApi playerApi = new RPlayerApi();

    /**
     * Gets the WorldAPI registered by RoyalCommands.
     *
     * @return RWorldApi
     */
    public RWorldApi getWorldAPI() {
        return worldApi;
    }

    /**
     * Gets the PlayerAPI registered by RoyalCommands.
     *
     * @return RPlayerApi
     */
    public RPlayerApi getPlayerAPI() {
        return playerApi;
    }

}
