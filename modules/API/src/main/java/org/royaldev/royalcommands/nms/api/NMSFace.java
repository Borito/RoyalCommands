package org.royaldev.royalcommands.nms.api;

import org.bukkit.entity.Player;

public interface NMSFace {
    /**
     * Checks to see if this is offering NMS/CB internals support.
     * <p/>
     * Note that if this returns false, calling any method besides this will return the default or null.
     * If this is true, NMS support is enabled.
     *
     * @return true/false
     */
    public boolean hasSupport();

    /**
     * Gets the ping reported to the server by the client.
     *
     * @param p Player to get ping of
     * @return ping
     */
    public int getPing(Player p);
}