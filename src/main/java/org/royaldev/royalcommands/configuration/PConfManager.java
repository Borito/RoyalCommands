package org.royaldev.royalcommands.configuration;

import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PConfManager extends GeneralConfManager {

    private static final Map<UUID, PConfManager> pcms = new HashMap<UUID, PConfManager>();
    private final Object saveLock = new Object();
    private final UUID playerUUID;
    private File pconfl = null;

    /**
     * Player configuration manager
     *
     * @param p Player to manage
     */
    PConfManager(OfflinePlayer p) {
        this(p.getUniqueId());
    }

    /**
     * Player configuration manager.
     *
     * @param u Player UUID to manage
     */
    PConfManager(UUID u) {
        super();
        File dataFolder = RoyalCommands.dataFolder;
        pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + u + ".yml");
        try {
            load(pconfl);
        } catch (Exception ignored) {
        }
        playerUUID = u;
    }

    /**
     * Just to prevent construction outside of package.
     */
    @SuppressWarnings("unused")
    private PConfManager() {
        playerUUID = null;
    }

    public static PConfManager getPConfManager(OfflinePlayer p) {
        return getPConfManager(p.getUniqueId());
    }

    public static PConfManager getPConfManager(UUID u) {
        synchronized (pcms) {
            if (pcms.containsKey(u)) return pcms.get(u);
            final PConfManager pcm = new PConfManager(u);
            pcms.put(u, pcm);
            return pcm;
        }
    }

    public static boolean isManagerCreated(OfflinePlayer p) {
        return isManagerCreated(p.getUniqueId());
    }

    public static boolean isManagerCreated(UUID u) {
        synchronized (pcms) {
            return pcms.containsKey(u);
        }
    }

    public static void saveAllManagers() {
        synchronized (pcms) {
            for (PConfManager pcm : pcms.values()) pcm.forceSave();
        }
    }

    public static void removeAllManagers() {
        Collection<PConfManager> oldConfs = new ArrayList<PConfManager>();
        synchronized (pcms) {
            oldConfs.addAll(pcms.values());
            for (PConfManager pcm : oldConfs) pcm.discard(false);
        }
    }

    public static int managersCreated() {
        synchronized (pcms) {
            return pcms.size();
        }
    }

    public static synchronized Collection<PConfManager> getAllManagers() {
        synchronized (pcms) {
            return Collections.synchronizedCollection(pcms.values());
        }
    }

    public boolean exists() {
        return pconfl.exists();
    }

    public boolean createFile() {
        try {
            return pconfl.createNewFile();
        } catch (IOException ignored) {
            return false;
        }
    }

    public void forceSave() {
        synchronized (saveLock) {
            try {
                save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the name of the player this manager was created for.
     *
     * @return Player name
     */
    public UUID getManagerPlayerUUID() {
        return playerUUID;
    }

    /**
     * Gets if this is the player's first join.
     *
     * @return true or false
     */
    public boolean isFirstJoin() {
        return getBoolean("first_join", true);
    }

    public void setFirstJoin(boolean firstJoin) {
        set("first_join", firstJoin);
    }

    /**
     * Removes the reference to this manager without saving.
     */
    public void discard() {
        discard(false);
    }

    /**
     * Removes the reference to this manager.
     *
     * @param save Save manager before removing references?
     */
    public void discard(boolean save) {
        if (save) forceSave();
        synchronized (pcms) {
            pcms.remove(playerUUID);
        }
    }
}
