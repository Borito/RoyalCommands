package org.royaldev.royalcommands.api;

import org.bukkit.World;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.WorldManager;

public class RWorldApi {

    /**
     * Gets a CraftBukkit world from a case-sensitive alias (MV/WM/vanilla).
     *
     * @param name Case-sensitive name of world
     * @return World or null if invalid name
     */
    public World getWorldCaseSensitive(String name) {
        return RoyalCommands.wm.getCaseSensitiveWorld(name);
    }

    /**
     * Gets a CraftBukkit world from an alias (MV/WM/vanilla).
     *
     * @param name Name of world
     * @return World or null if invalid name
     */
    public World getWorldFromName(String name) {
        return RoyalCommands.wm.getWorld(name);
    }

    /**
     * This will get the WorldManager where WM tasks such as loading worlds can be performed.
     *
     * @return WorldManager or null if RoyalCommands has not run its onEnable()
     */
    public WorldManager getWorldManager() {
        return RoyalCommands.wm;
    }

    /**
     * Gets the name of a world using aliases. If the world has a WorldManager alias,
     * it will be used first. If it has a MultiVerse alias, it will be used second.
     * If the world has no aliases, the world's vanilla name will be returned.
     *
     * @param w World to get name of
     * @return Name
     */
    public String getWorldName(World w) {
        return RUtils.getMVWorldName(w);
    }

}
