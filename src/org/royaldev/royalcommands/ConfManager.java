package org.royaldev.royalcommands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("unused")
/**
 * Configuration manager
 *
 * @author jkcclemens
 * @see PConfManager
 */
public class ConfManager {

    private FileConfiguration pconf = null;
    private File pconfl = null;

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param filename Filename (local) for the config
     */
    public ConfManager(String filename) {
        File dataFolder = RoyalCommands.dataFolder;
        synchronized (RoyalCommands.instance.confs) {
            if (RoyalCommands.instance.confs.containsKey(filename)) {
                pconf = RoyalCommands.instance.confs.get(filename);
                pconfl = new File(dataFolder + File.separator + filename);
                return;
            }
        }
        pconfl = new File(dataFolder + File.separator + filename);
        if (!pconfl.exists()) {
            try {
                pconfl.getParentFile().mkdirs();
                pconfl.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        pconf = YamlConfiguration.loadConfiguration(pconfl);
        synchronized (RoyalCommands.instance.confs) {
            RoyalCommands.instance.confs.put(filename, pconf);
        }
    }

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param file File object for the config
     */
    public ConfManager(File file) {
        File dataFolder = RoyalCommands.dataFolder;
        synchronized (RoyalCommands.instance.confs) {
            if (RoyalCommands.instance.confs.containsKey(file.getName())) {
                pconf = RoyalCommands.instance.confs.get(file.getName());
                pconfl = new File(dataFolder + File.separator + file.getName());
                return;
            }
        }
        pconfl = new File(dataFolder + File.separator + file.getName());
        if (!pconfl.exists()) {
            try {
                pconfl.getParentFile().mkdirs();
                pconfl.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        pconf = YamlConfiguration.loadConfiguration(pconfl);
        synchronized (RoyalCommands.instance.confs) {
            RoyalCommands.instance.confs.put(file.getName(), pconf);
        }
    }

    /**
     * Reloads the configuration from file.
     */
    public void reload() {
        if (pconfl == null) return;
        pconf = YamlConfiguration.loadConfiguration(pconfl);
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
     * Gets a boolean from config
     *
     * @param path Path in the yml to fetch from
     * @return Boolean or null if path does not exist or if config doesn't exist
     */
    public boolean getBoolean(String path, boolean def) {
        if (pconf == null) return def;
        return pconf.getBoolean(path, def);
    }

    /**
     * Gets an integer from config
     *
     * @param path Path in the yml to fetch from
     * @return Integer or null if path does not exist or if config doesn't exist
     */
    public Integer getInteger(String path, int def) {
        if (pconf == null) return def;
        return pconf.getInt(path, def);
    }

    /**
     * Gets an object from config
     *
     * @param path Path in the yml to fetch from
     * @return Object or null if path does not exist or if config doesn't exist
     */
    public Object get(String path, Object def) {
        if (pconf == null) return def;
        return pconf.get(path, def);
    }

    /**
     * Gets a long from config
     *
     * @param path Path in the yml to fetch from
     * @return Long or null if path does not exist or if config doesn't exist
     */
    public Long getLong(String path, long def) {
        if (pconf == null) return def;
        return pconf.getLong(path, def);
    }

    /**
     * Gets a double from config
     *
     * @param path Path in the yml to fetch from
     * @return Double or null if path does not exist or if config doesn't exist
     */
    public Double getDouble(String path, double def) {
        if (pconf == null) return def;
        return pconf.getDouble(path, def);
    }

    /**
     * Gets a string from config
     *
     * @param path Path in the yml to fetch from
     * @return String or null if path does not exist or if config doesn't exist
     */
    public String getString(String path, String def) {
        if (pconf == null) return def;
        return pconf.getString(path, def);
    }

    /**
     * Gets a float from config
     *
     * @param path Path in the yml to fetch from
     * @return Float or null if path does not exist or if config doesn't exist or if not valid float
     */
    public Float getFloat(String path, float def) {
        if (pconf == null) return def;
        try {
            return Float.valueOf(pconf.getString(path));
        } catch (Exception e) {
            return def;
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
        return pconf != null;
    }

    /**
     * Checks to see if config exists.
     * <p/>
     * Equivalent to getConfExists()
     *
     * @return boolean of existence
     */
    public boolean exists() {
        return pconf != null;
    }

}
