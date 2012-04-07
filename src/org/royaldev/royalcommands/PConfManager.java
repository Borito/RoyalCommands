package org.royaldev.royalcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PConfManager {

    static RoyalCommands plugin;

    public PConfManager(RoyalCommands instance) {
        PConfManager.plugin = instance;
    }

    public static void setPValString(OfflinePlayer t, String value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPValLong(OfflinePlayer t, long value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPValDouble(OfflinePlayer t, double value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPVal(OfflinePlayer t, Object value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPValInteger(OfflinePlayer t, Integer value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPValStringList(OfflinePlayer t, List<String> value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getPValStringList(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            return pconf.getStringList(path);
        }
        return null;
    }

    public static void setPValBoolean(OfflinePlayer t, Boolean value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getPValBoolean(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            return pconf.getBoolean(path);
        }
        return false;
    }

    public static Integer getPValInteger(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            return pconf.getInt(path);
        }
        return null;
    }

    public static Object getPVal(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            return pconf.get(path);
        }
        return null;
    }

    public static Long getPValLong(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            if (pconf.contains(path)) return pconf.getLong(path);
            return null;
        }
        return null;
    }

    public static Double getPValDouble(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            return pconf.getDouble(path);
        }
        return null;
    }

    public static String getPValString(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            return pconf.getString(path);
        }
        return null;
    }

    public static boolean getPConfExists(OfflinePlayer t) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + t.getName().toLowerCase() + ".yml");
        return pconfl.exists();
    }

    public static boolean getPConfExists(String name) {
        File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + name.toLowerCase() + ".yml");
        return pconfl.exists();
    }

}
