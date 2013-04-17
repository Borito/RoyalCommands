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

public class PConfManager extends GeneralConfManager {

    private static final Map<String, PConfManager> pcms = new HashMap<String, PConfManager>();

    public static PConfManager getPConfManager(OfflinePlayer p) {
        return getPConfManager(p.getName());
    }

    public static PConfManager getPConfManager(String s) {
        s = s.toLowerCase();
        synchronized (pcms) {
            if (pcms.containsKey(s)) return pcms.get(s);
            final PConfManager pcm = new PConfManager(s);
            pcms.put(s, pcm);
            return pcm;
        }
    }

    public static boolean isManagerCreated(OfflinePlayer p) {
        return isManagerCreated(p.getName());
    }

    public static boolean isManagerCreated(String s) {
        synchronized (pcms) {
            return pcms.containsKey(s);
        }
    }

    public static void saveAllManagers() {
        synchronized (pcms) {
            for (PConfManager pcm : pcms.values()) pcm.forceSave();
        }
    }

    public static void removeAllManagers() {
        Collection<PConfManager> oldConfs = new ArrayList<PConfManager>();
        oldConfs.addAll(pcms.values());
        synchronized (pcms) {
            for (PConfManager pcm : oldConfs) pcm.discard(false);
        }
    }

    public static int managersCreated() {
        synchronized (pcms) {
            return pcms.size();
        }
    }

    public static Collection<PConfManager> getAllManagers() {
        synchronized (pcms) {
            return Collections.synchronizedCollection(pcms.values());
        }
    }

    private File pconfl = null;
    private final Object saveLock = new Object();
    private final String playerName;

    /**
     * Player configuration manager
     *
     * @param p Player to manage
     */
    PConfManager(OfflinePlayer p) {
        super();
        File dataFolder = RoyalCommands.dataFolder;
        pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + p.getName().toLowerCase() + ".yml");
        try {
            load(pconfl);
        } catch (Exception ignored) {
        }
        playerName = p.getName();
    }

    /**
     * Player configuration manager.
     *
     * @param p Player to manage
     */
    PConfManager(String p) {
        super();
        File dataFolder = RoyalCommands.dataFolder;
        pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + p.toLowerCase() + ".yml");
        try {
            load(pconfl);
        } catch (Exception ignored) {
        }
        playerName = p;
    }

    /**
     * Just to prevent construction outside of package.
     */
    @SuppressWarnings("unused")
    private PConfManager() {
        playerName = "";
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
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Gets the name of the player this manager was created for.
     *
     * @return Player name
     */
    public String getManagerPlayerName() {
        return playerName;
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
            pcms.remove(playerName);
        }
    }
}
