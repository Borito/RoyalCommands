package org.royaldev.royalcommands.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class GeneralConfManager extends YamlConfiguration {

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

    /**
     * Gets a Location from config
     * <p/>
     * This <strong>will</strong> throw an exception if the saved Location is invalid or missing parts.
     *
     * @param path      Path in the yml to fetch from
     * @param worldName World name to specify manually
     * @return Location or null if path does not exist or if config doesn't exist
     */
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

    /**
     * Gets a float. This method only casts a double from getDouble to a float.
     *
     * @param path Path to get float from
     * @return float
     */
    public float getFloat(String path) {
        return (float) getDouble(path);
    }
}
