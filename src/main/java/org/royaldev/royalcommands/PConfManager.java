package org.royaldev.royalcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.playermanagers.YMLPConfManager;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
/**
 * Player configuration manager
 *
 * @author jkcclemens
 * @see ConfManager
 */
public class PConfManager {
    /**
     * YML player manager - will be set if using YML
     */
    private YMLPConfManager ymlpcm;

    /**
     * Constructor for the player userdata manager.
     * <p/>
     * If a userdata manager has already created, it will use that instead. If not, it will create a new one.
     *
     * @param t OfflinePlayer to get manager for
     */
    public PConfManager(OfflinePlayer t) {
        synchronized (RoyalCommands.instance.ymls) {
            Map<String, YMLPConfManager> ymls = RoyalCommands.instance.ymls;
            if (ymls.containsKey(t.getName())) ymlpcm = ymls.get(t.getName());
            else {
                ymlpcm = new YMLPConfManager(t);
                RoyalCommands.instance.ymls.put(t.getName(), ymlpcm);
            }
        }
    }

    /**
     * Constructor for the player userdata manager.
     * <p/>
     * If a userdata manager has already created, it will use that instead. If not, it will create a new one.
     *
     * @param t Name of player to get manager for
     */
    public PConfManager(String t) {
        synchronized (RoyalCommands.instance.ymls) {
            Map<String, YMLPConfManager> ymls = RoyalCommands.instance.ymls;
            if (ymls.containsKey(t)) ymlpcm = ymls.get(t);
            else {
                ymlpcm = new YMLPConfManager(t);
                RoyalCommands.instance.ymls.put(t, ymlpcm);
            }
        }
    }

    /**
     * Saves.
     */
    public void save() {
        ymlpcm.forceSave();
    }

    /**
     * Returns the backend configuration manager.
     * <p/>
     *
     * @return Object of the real backend configuration manager.
     */
    public Object getRealManager() {
        return ymlpcm;
    }

    /**
     * Returns if the configuration for the player exists.
     * <p/>
     * Will always be true if using H2.
     *
     * @return true if exists, false if not
     */
    public boolean exists() {
        return ymlpcm.exists();
    }

    /**
     * Returns the result of {@link #exists()}.
     *
     * @return true if exists, false if not
     */
    public boolean getConfExists() {
        return exists();
    }

    /**
     * Creates the file for the player if using YML.
     *
     * @return true if file created or using H2, false if not
     */
    public boolean createFile() {
        return ymlpcm.createFile();
    }

    /**
     * Gets a string from the userdata.
     *
     * @param node Path to get string from
     * @return String or null
     */
    public String getString(String node) {
        return ymlpcm.getString(node);
    }

    /**
     * Gets a boolean from the userdata.
     *
     * @param node Path to get boolean from
     * @return Boolean or null
     */
    public boolean getBoolean(String node) {
        return ymlpcm.getBoolean(node);
    }

    /**
     * Gets a long from the userdata.
     *
     * @param node Path to get long from
     * @return Long or null
     */
    public Long getLong(String node) {
        return ymlpcm.getLong(node);
    }

    /**
     * Gets a float from the userdata.
     *
     * @param node Path to get float from
     * @return Float or null
     */
    public Float getFloat(String node) {
        return ymlpcm.getFloat(node);
    }

    /**
     * Gets a double from the userdata.
     *
     * @param node Path to get double from
     * @return Double or null
     */
    public Double getDouble(String node) {
        return ymlpcm.getDouble(node);
    }

    /**
     * Gets a string list from the userdata.
     *
     * @param node Path to get string list from
     * @return String list or null
     */
    public List<String> getStringList(String node) {
        return ymlpcm.getStringList(node);
    }

    /**
     * Gets an integer from the userdata.
     *
     * @param node Path to get integer from
     * @return Integer or null
     */
    public Integer getInteger(String node) {
        return ymlpcm.getInteger(node);
    }

    public ItemStack getItemStack(String node) {
        return ymlpcm.getItemStack(node);
    }

    /**
     * Gets an object from the userdata.
     *
     * @param node Path to get object from
     * @return Object or null
     */
    public Object get(String node) {
        return ymlpcm.get(node);
    }

    /**
     * Gets a ConfigurationSection from the userdata.
     *
     * @param path Path to get ConfigurationSection from
     * @return ConfigurationSection or null if a) doesn't exist or b) using H2
     */
    public ConfigurationSection getConfigurationSection(String path) {
        return ymlpcm.getConfigurationSection(path);
    }

    /**
     * Sets a string in the userdata.
     *
     * @param value String to set
     * @param path  Path to set at
     */
    public void setString(String value, String path) {
        ymlpcm.setString(value, path);
    }

    /**
     * Sets a boolean in the userdata.
     *
     * @param value Boolean to set
     * @param path  Path to set at
     */
    public void setBoolean(boolean value, String path) {
        ymlpcm.set(value, path); // Got lazy
    }

    /**
     * Sets a long in the userdata.
     *
     * @param value Long to set
     * @param path  Path to set at
     */
    public void setLong(long value, String path) {
        ymlpcm.set(value, path);
    }

    /**
     * Sets a double in the userdata.
     *
     * @param value Double to set
     * @param path  Path to set at
     */
    public void setDouble(double value, String path) {
        ymlpcm.set(value, path);
    }

    /**
     * Sets an integer in the userdata.
     *
     * @param value Integer to set
     * @param path  Path to set at
     */
    public void setInteger(int value, String path) {
        ymlpcm.set(value, path);
    }

    public void setItemStack(ItemStack value, String path) {
        ymlpcm.setItemStack(value, path);
    }

    /**
     * Sets a float in the userdata.
     *
     * @param value Float to set
     * @param path  Path to set at
     */
    public void setFloat(float value, String path) {
        ymlpcm.set(value, path);
    }

    /**
     * Sets a string list in the userdata.
     *
     * @param value String list to set
     * @param path  Path to set at
     */
    public void setStringList(List<String> value, String path) {
        ymlpcm.set(value, path);
    }

    /**
     * Sets an object in the userdata.
     *
     * @param value Object to set
     * @param path  Path to set at
     */
    public void set(Object value, String path) {
        ymlpcm.set(value, path);
    }

}
