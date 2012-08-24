package org.royaldev.royalcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.royaldev.royalcommands.json.JSONObject;
import org.royaldev.royalcommands.playermanagers.H2PConfManager;
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
     * config.yml value for using H2 or not
     */
    private static boolean useH2 = RoyalCommands.instance.useH2;

    /**
     * H2 player manager - will be set if using H2
     */
    private H2PConfManager h2pcm;
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
        if (useH2) {
            try {
                synchronized (RoyalCommands.instance.h2s) {
                    Map<String, H2PConfManager> h2s = RoyalCommands.instance.h2s;
                    if (h2s.containsKey(t.getName())) h2pcm = h2s.get(t.getName());
                    else {
                        h2pcm = new H2PConfManager(t);
                        RoyalCommands.instance.h2s.put(t.getName(), h2pcm);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            synchronized (RoyalCommands.instance.ymls) {
                Map<String, YMLPConfManager> ymls = RoyalCommands.instance.ymls;
                if (ymls.containsKey(t.getName())) ymlpcm = ymls.get(t.getName());
                else {
                    ymlpcm = new YMLPConfManager(t);
                    RoyalCommands.instance.ymls.put(t.getName(), ymlpcm);
                }
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
        if (useH2) {
            try {
                synchronized (RoyalCommands.instance.h2s) {
                    Map<String, H2PConfManager> h2s = RoyalCommands.instance.h2s;
                    if (h2s.containsKey(t)) h2pcm = h2s.get(t);
                    else {
                        h2pcm = new H2PConfManager(t);
                        RoyalCommands.instance.h2s.put(t, h2pcm);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            synchronized (RoyalCommands.instance.ymls) {
                Map<String, YMLPConfManager> ymls = RoyalCommands.instance.ymls;
                if (ymls.containsKey(t)) ymlpcm = ymls.get(t);
                else {
                    ymlpcm = new YMLPConfManager(t);
                    RoyalCommands.instance.ymls.put(t, ymlpcm);
                }
            }
        }
    }

    /**
     * Updates the useH2 status from the plugin.
     */
    public static void updateH2Status() {
        useH2 = RoyalCommands.instance.useH2;
    }

    /**
     * Returns the backend configuration manager.
     * <p/>
     * If using H2, cast to {@link H2PConfManager}, else cast to {@link YMLPConfManager}.
     *
     * @return Object of the real backend configuration manager.
     */
    public Object getRealManager() {
        if (useH2) return h2pcm;
        else return ymlpcm;
    }

    /**
     * Returns if the configuration for the player exists.
     * <p/>
     * Will always be true if using H2.
     *
     * @return true if exists, false if not
     */
    public boolean exists() {
        return useH2 || ymlpcm.exists();
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
        return useH2 || ymlpcm.createFile();
    }

    /**
     * Gets a string from the userdata.
     *
     * @param node Path to get string from
     * @return String or null
     */
    public String getString(String node) {
        if (useH2) return h2pcm.getString(node);
        else return ymlpcm.getString(node);
    }

    /**
     * Gets a boolean from the userdata.
     *
     * @param node Path to get boolean from
     * @return Boolean or null
     */
    public boolean getBoolean(String node) {
        if (useH2) return h2pcm.getBoolean(node);
        else return ymlpcm.getBoolean(node);
    }

    /**
     * Gets a long from the userdata.
     *
     * @param node Path to get long from
     * @return Long or null
     */
    public Long getLong(String node) {
        if (useH2) return h2pcm.getLong(node);
        else return ymlpcm.getLong(node);
    }

    /**
     * Gets a float from the userdata.
     *
     * @param node Path to get float from
     * @return Float or null
     */
    public Float getFloat(String node) {
        if (useH2) return h2pcm.getFloat(node);
        else return ymlpcm.getFloat(node);
    }

    /**
     * Gets a double from the userdata.
     *
     * @param node Path to get double from
     * @return Double or null
     */
    public Double getDouble(String node) {
        if (useH2) return h2pcm.getDouble(node);
        else return ymlpcm.getDouble(node);
    }

    /**
     * Gets a string list from the userdata.
     *
     * @param node Path to get string list from
     * @return String list or null
     */
    public List<String> getStringList(String node) {
        if (useH2) return h2pcm.getStringList(node);
        else return ymlpcm.getStringList(node);
    }

    /**
     * Gets an integer from the userdata.
     *
     * @param node Path to get integer from
     * @return Integer or null
     */
    public Integer getInteger(String node) {
        if (useH2) return h2pcm.getInteger(node);
        else return ymlpcm.getInteger(node);
    }

    /**
     * Gets an object from the userdata.
     *
     * @param node Path to get object from
     * @return Object or null
     */
    public Object get(String node) {
        if (useH2) return h2pcm.get(node);
        else return ymlpcm.get(node);
    }

    /**
     * Gets a ConfigurationSection from the userdata.
     *
     * @param path Path to get ConfigurationSection from
     * @return ConfigurationSection or null if a) doesn't exist or b) using H2
     */
    public ConfigurationSection getConfigurationSection(String path) {
        if (useH2) return null;
        else return ymlpcm.getConfigurationSection(path);
    }

    /**
     * Gets a JSONObject from the userdata.
     *
     * @param path Path to get JSONObject from
     * @return JSONObject or null if a) doesn't exist or b) using YML
     */
    public JSONObject getJSONObject(String path) {
        if (useH2) return h2pcm.getJSONObject(path);
        else return null;
    }

    /**
     * Sets a string in the userdata.
     *
     * @param value String to set
     * @param path  Path to set at
     */
    public void setString(String value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.setString(value, path);
    }

    /**
     * Sets a boolean in the userdata.
     *
     * @param value Boolean to set
     * @param path  Path to set at
     */
    public void setBoolean(boolean value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path); // Got lazy
    }

    /**
     * Sets a long in the userdata.
     *
     * @param value Long to set
     * @param path  Path to set at
     */
    public void setLong(long value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    /**
     * Sets a double in the userdata.
     *
     * @param value Double to set
     * @param path  Path to set at
     */
    public void setDouble(double value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    /**
     * Sets an integer in the userdata.
     *
     * @param value Integer to set
     * @param path  Path to set at
     */
    public void setInteger(int value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    /**
     * Sets a float in the userdata.
     *
     * @param value Float to set
     * @param path  Path to set at
     */
    public void setFloat(float value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    /**
     * Sets a string list in the userdata.
     *
     * @param value String list to set
     * @param path  Path to set at
     */
    public void setStringList(List<String> value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    /**
     * Sets an object in the userdata.
     *
     * @param value Object to set
     * @param path  Path to set at
     */
    public void set(Object value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

}
