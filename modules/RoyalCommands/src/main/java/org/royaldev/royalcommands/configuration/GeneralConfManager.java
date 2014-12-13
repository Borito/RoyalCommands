package org.royaldev.royalcommands.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class GeneralConfManager extends YamlConfiguration {

    /**
     * Gets a float. This method only casts a double from getDouble to a float.
     *
     * @param path Path to get float from
     * @return float
     */
    public float getFloat(final String path) {
        return (float) this.getDouble(path);
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
    public Location getLocation(final String path, final String worldName) {
        if (!this.isSet(path)) return null;
        final double x = this.getDouble(path + ".x");
        final double y = this.getDouble(path + ".y");
        final double z = this.getDouble(path + ".z");
        final float pitch = this.getFloat(path + ".pitch");
        final float yaw = this.getFloat(path + ".yaw");
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    /**
     * Gets a Location from config
     * <p/>
     * This <strong>will</strong> throw an exception if the saved Location is invalid or missing parts.
     *
     * @param path Path in the yml to fetch from
     * @return Location or null if path does not exist or if config doesn't exist
     */
    public Location getLocation(final String path) {
        if (!this.isSet(path)) return null;
        final String world = this.getString(path + ".w");
        final double x = this.getDouble(path + ".x");
        final double y = this.getDouble(path + ".y");
        final double z = this.getDouble(path + ".z");
        final float pitch = this.getFloat(path + ".pitch");
        final float yaw = this.getFloat(path + ".yaw");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    /**
     * Sets a location in config
     *
     * @param value Location to set
     * @param path  Path in the yml to set
     */
    public void setLocation(final String path, final Location value) {
        this.set(path + ".w", value.getWorld().getName());
        this.set(path + ".x", value.getX());
        this.set(path + ".y", value.getY());
        this.set(path + ".z", value.getZ());
        this.set(path + ".pitch", value.getPitch());
        this.set(path + ".yaw", value.getYaw());
    }
}
