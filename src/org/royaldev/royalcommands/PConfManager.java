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

    private static boolean useH2 = RoyalCommands.instance.useH2;

    private H2PConfManager h2pcm;
    private YMLPConfManager ymlpcm;

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

    public static void updateH2Status() {
        useH2 = RoyalCommands.instance.useH2;
    }

    public Object getRealManager() {
        if (useH2) return h2pcm;
        else return ymlpcm;
    }

    public boolean exists() {
        return useH2 || ymlpcm.exists();
    }

    public boolean getConfExists() {
        return exists();
    }

    public boolean createFile() {
        return useH2 || ymlpcm.createFile();
    }

    public String getString(String node) {
        if (useH2) return h2pcm.getString(node);
        else return ymlpcm.getString(node);
    }

    public boolean getBoolean(String node) {
        if (useH2) return h2pcm.getBoolean(node);
        else return ymlpcm.getBoolean(node);
    }

    public Long getLong(String node) {
        if (useH2) return h2pcm.getLong(node);
        else return ymlpcm.getLong(node);
    }

    public Float getFloat(String node) {
        if (useH2) return h2pcm.getFloat(node);
        else return ymlpcm.getFloat(node);
    }

    public Double getDouble(String node) {
        if (useH2) return h2pcm.getDouble(node);
        else return ymlpcm.getDouble(node);
    }

    public List<String> getStringList(String node) {
        if (useH2) return h2pcm.getStringList(node);
        else return ymlpcm.getStringList(node);
    }

    public Integer getInteger(String node) {
        if (useH2) return h2pcm.getInteger(node);
        else return ymlpcm.getInteger(node);
    }

    public Object get(String node) {
        if (useH2) return h2pcm.get(node);
        else return ymlpcm.get(node);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        if (useH2) return null;
        else return ymlpcm.getConfigurationSection(path);
    }

    public JSONObject getJSONObject(String path) {
        if (useH2) return h2pcm.getJSONObject(path);
        else return null;
    }

    public void setString(String value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.setString(value, path);
    }

    public void setBoolean(boolean value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path); // Got lazy
    }

    public void setLong(long value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    public void setDouble(double value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    public void setInteger(int value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    public void setFloat(float value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

    public void setStringList(List<String> value, String path) {
        if (useH2) {
            try {
                h2pcm.set(value, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else ymlpcm.set(value, path);
    }

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
