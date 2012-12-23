package org.royaldev.royalcommands.playermanagers;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("unused")
/**
 * Player configuration manager
 *
 * @author jkcclemens
 * @see ConfManager
 */
public class YMLPConfManager {

    private FileConfiguration pconf = null;
    private File pconfl = null;

    /**
     * Player configuration manager
     *
     * @param p Player to manage
     */
    public YMLPConfManager(OfflinePlayer p) {
        File dataFolder = RoyalCommands.dataFolder;
        pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + p.getName().toLowerCase() + ".yml");
        if (!pconfl.exists()) return;
        pconf = YamlConfiguration.loadConfiguration(pconfl);
    }

    /**
     * Player configuration manager.
     *
     * @param p Player to manage
     */
    public YMLPConfManager(String p) {
        File dataFolder = RoyalCommands.dataFolder;
        pconfl = new File(dataFolder + File.separator + "userdata" + File.separator + p.toLowerCase() + ".yml");
        if (!pconfl.exists()) return;
        pconf = YamlConfiguration.loadConfiguration(pconfl);
    }

    /**
     * Forces the file to save with current userdata.
     *
     * @return true if saved, false if otherwise
     */
    public boolean forceSave() {
        try {
            pconf.save(pconfl);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Creates configuration file if it doesn't exist.
     *
     * @return If file was created
     */
    public boolean createFile() {
        if (pconf != null) return false;
        try {
            pconfl.getParentFile().mkdirs();
            boolean success = pconfl.createNewFile();
            pconf = YamlConfiguration.loadConfiguration(pconfl);
            return success;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets a string in config
     *
     * @param value String to set
     * @param path  Path in the yml to set
     */
    public void setString(String value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a long in config
     *
     * @param value Long to set
     * @param path  Path in the yml to set
     */
    public void setLong(long value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a double in config
     *
     * @param value Double to set
     * @param path  Path in the yml to set
     */
    public void setDouble(double value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets an object in config
     *
     * @param value An object
     * @param path  Path in the yml to set
     */
    public void set(Object value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets an integer in config
     *
     * @param value Integer to set
     * @param path  Path in the yml to set
     */
    public void setInteger(Integer value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setItemStack(ItemStack value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a string list in config
     *
     * @param value String list to set
     * @param path  Path in the yml to set
     */
    public void setStringList(List<String> value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a configuration section in the config
     *
     * @param value ConfigurationSection to set
     * @param path  Path in the yml to set
     */
    public void setConfigurationSection(ConfigurationSection value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a boolean in config
     *
     * @param value Boolean to set
     * @param path  Path in the yml to set
     */
    public void setBoolean(Boolean value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a float in config
     *
     * @param value Float to set
     * @param path  Path in the yml to set
     */
    public void setFloat(Float value, String path) {
        if (pconf == null) return;
        pconf.set(path, value);
        try {
            pconf.save(pconfl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a string list from config
     *
     * @param path Path in the yml to fetch from
     * @return String list or null if path does not exist or if config doesn't exist
     */
    public List<String> getStringList(String path) {
        if (pconf == null) return null;
        return pconf.getStringList(path);
    }

    /**
     * Gets a boolean from config
     *
     * @param path Path in the yml to fetch from
     * @return Boolean or null if path does not exist or if config doesn't exist
     */
    public boolean getBoolean(String path) {
        return pconf != null && pconf.getBoolean(path);
    }

    /**
     * Gets an integer from config
     *
     * @param path Path in the yml to fetch from
     * @return Integer or null if path does not exist or if config doesn't exist
     */
    public Integer getInteger(String path) {
        if (pconf == null) return null;
        return pconf.getInt(path);
    }

    /**
     * Gets an object from config
     *
     * @param path Path in the yml to fetch from
     * @return Object or null if path does not exist or if config doesn't exist
     */
    public Object get(String path) {
        if (pconf == null) return null;
        return pconf.get(path);
    }

    /**
     * Gets an ItemStack from config
     *
     * @param path Path in the yml to fetch from
     * @return ItemStack or null if path does not exist or if config doesn't exist
     */
    public ItemStack getItemStack(String path) {
        if (pconf == null) return null;
        return pconf.getItemStack(path);
    }

    /**
     * Gets a long from config
     *
     * @param path Path in the yml to fetch from
     * @return Long or null if path does not exist or if config doesn't exist
     */
    public Long getLong(String path) {
        if (pconf == null) return null;
        return pconf.getLong(path);
    }

    /**
     * Gets a double from config
     *
     * @param path Path in the yml to fetch from
     * @return Double or null if path does not exist or if config doesn't exist
     */
    public Double getDouble(String path) {
        if (pconf == null) return null;
        return pconf.getDouble(path);
    }

    /**
     * Gets a string from config
     *
     * @param path Path in the yml to fetch from
     * @return String or null if path does not exist or if config doesn't exist
     */
    public String getString(String path) {
        if (pconf == null) return null;
        return pconf.getString(path);
    }

    /**
     * Gets a float from config
     *
     * @param path Path in the yml to fetch from
     * @return Float or null if path does not exist or if config doesn't exist or if not valid float
     */
    public Float getFloat(String path) {
        if (pconf == null) return null;
        try {
            return Float.valueOf(pconf.getString(path));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets a ConfigurationSection from config
     *
     * @param path Path in the yml to fetch from
     * @return ConfigurationSection or null if the path does not exist or if config doesn't exist
     */
    public ConfigurationSection getConfigurationSection(String path) {
        if (pconf == null) return null;
        return pconf.getConfigurationSection(path);
    }

    /**
     * Checks to see if config exists.
     * <p/>
     * Equivalent to exists()
     *
     * @return boolean of existence
     */
    public boolean getConfExists() {
        return pconf != null && pconfl.exists();
    }

    /**
     * Checks to see if config exists.
     * <p/>
     * Equivalent to getConfExists()
     *
     * @return boolean of existence
     */
    public boolean exists() {
        return pconf != null && pconfl.exists();
    }

}
