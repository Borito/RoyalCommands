package org.royaldev.royalcommands.configuration;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public interface GeneralConfiguration extends ConfigurationSection, Configuration {

    float getFloat(String path);

    Location getLocation(String path, String worldName);

    Location getLocation(String path);

    void setLocation(String path, Location value);
}
