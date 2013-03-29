package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfManager extends YamlConfiguration {

    private static final Map<String, ConfManager> confs = new HashMap<String, ConfManager>();

    public static ConfManager getConfManager(String s) {
        synchronized (confs) {
            if (confs.containsKey(s)) return confs.get(s);
            final ConfManager cm = new ConfManager(s);
            confs.put(s, cm);
            return cm;
        }
    }

    public static void saveAllManagers() {
        synchronized (confs) {
            for (ConfManager cm : confs.values()) cm.forceSave();
        }
    }

    public static int managersCreated() {
        synchronized (confs) {
            return confs.size();
        }
    }

    private File pconfl = null;
    private final Object saveLock = new Object();
    private final String path;
    private final String name;

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param filename Filename (local) for the config
     */
    ConfManager(String filename) {
        super();
        File dataFolder = RoyalCommands.dataFolder;
        path = dataFolder + File.separator + filename;
        pconfl = new File(path);
        try {
            load(pconfl);
        } catch (Exception ignored) {
        }
        name = filename;
    }

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param file File object for the config
     */
    ConfManager(File file) {
        this(file.getName());
    }

    /**
     * Just to prevent construction outside of package.
     */
    @SuppressWarnings("unused")
    private ConfManager() {
        path = "";
        name = "";
    }

    public void reload() {
        forceSave();
        try {
            load(pconfl);
        } catch (Exception ignored) {
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
            } catch (IOException ignored) {
            } catch (IllegalArgumentException ignored) {
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
        if (!isSet(path)) return null;
        String world = getString(path + ".w");
        double x = getDouble(path + ".x");
        double y = getDouble(path + ".y");
        double z = getDouble(path + ".z");
        float pitch = getFloat(path + ".pitch");
        float yaw = getFloat(path + ".yaw");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public Location getLocation(String path, String worldName) {
        if (!isSet(path)) return null;
        double x = getDouble(path + ".x");
        double y = getDouble(path + ".y");
        double z = getDouble(path + ".z");
        float pitch = getFloat(path + ".pitch");
        float yaw = getFloat(path + ".yaw");
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
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
        synchronized (confs) {
            if (save) forceSave();
            confs.remove(name);
        }
    }
}
