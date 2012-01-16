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
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration
                    .loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPValStringList(OfflinePlayer t, List<String> value, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration
                    .loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getPValStringList(OfflinePlayer t,  String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration
                    .loadConfiguration(pconfl);
            return pconf.getStringList(path);
        }
        return null;
    }

    public static void setPValBoolean(OfflinePlayer t, Boolean value,
                                      String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration
                    .loadConfiguration(pconfl);
            pconf.set(path, value);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getPValBoolean(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration
                    .loadConfiguration(pconfl);
            return pconf.getBoolean(path);
        }
        return false;
    }

    public static Object getPVal(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration
                    .loadConfiguration(pconfl);
            return pconf.get(path);
        }
        return false;
    }

    public static String getPValString(OfflinePlayer t, String path) {
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        if (pconfl.exists()) {
            FileConfiguration pconf = YamlConfiguration
                    .loadConfiguration(pconfl);
            return pconf.getString(path);
        }
        return "";
    }

    public static boolean getPConfExists(OfflinePlayer t) {
        File pconfl = new File(plugin.getDataFolder() + File.separator
                + "userdata" + File.separator + t.getName().toLowerCase()
                + ".yml");
        return pconfl.exists();
    }

}
