package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PConfManager extends YamlConfiguration {

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
        synchronized (pcms) {
            for (PConfManager pcm : pcms.values()) pcm.discard(false);
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
     * Gets a Location from config
     * <p/>
     * This <strong>will</strong> throw an exception if the saved Location is invalid or missing parts.
     *
     * @param path Path in the yml to fetch from
     * @return Location or null if path does not exist or if config doesn't exist
     */
    public Location getLocation(String path) {
        if (get(path) == null) return null;
        String world = getString(path + ".w");
        double x = getDouble(path + ".x");
        double y = getDouble(path + ".y");
        double z = getDouble(path + ".z");
        float pitch = getFloat(path + ".pitch");
        float yaw = getFloat(path + ".yaw");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    /**
     * Sets a location in config
     *
     * @param value Location to set
     * @param path  Path in the yml to set
     */
    public void setLocation(String path, Location value) {
        set(path + ".w", value.getWorld().getName());
        set(path + ".x", value.getX());
        set(path + ".y", value.getY());
        set(path + ".z", value.getZ());
        set(path + ".pitch", value.getPitch());
        set(path + ".yaw", value.getYaw());
    }

    public Float getFloat(String path) {
        return (float) getDouble(path);
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
