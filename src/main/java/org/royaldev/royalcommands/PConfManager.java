package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PConfManager extends YamlConfiguration {

    private File pconfl = null;
    private final Object saveLock = new Object();

    /**
     * Player configuration manager
     *
     * @param p Player to manage
     */
    public PConfManager(OfflinePlayer p) {
        super();
        File dataFolder = RoyalCommands.dataFolder;
        pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + p.getName().toLowerCase() + ".yml");
        try {
            load(pconfl);
        } catch (Exception ignored) {
        }
    }

    /**
     * Player configuration manager.
     *
     * @param p Player to manage
     */
    public PConfManager(String p) {
        super();
        File dataFolder = RoyalCommands.dataFolder;
        pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + p.toLowerCase() + ".yml");
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
}

