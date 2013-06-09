package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class Help {

    public static HashMap<String, String> helpdb = new HashMap<String, String>();
    private static ConfigurationSection commands = RoyalCommands.commands;

    public static void reloadHelp() {
        helpdb.clear();
        if (commands == null) {
            Logger.getLogger("Minecraft").warning("[RoyalCommands] Could not grab list of commands!");
            return;
        }
        for (String command : commands.getValues(false).keySet())
            helpdb.put(command, commands.getString(command + ".description", command));
        if (Config.otherHelp) {
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                if (p == null) continue;
                if ((p instanceof RoyalCommands) || !p.isEnabled()) continue;
                final InputStream is = p.getResource("plugin.yml");
                if (is == null) continue;
                final YamlConfiguration info = YamlConfiguration.loadConfiguration(is);
                final ConfigurationSection commands = info.getConfigurationSection("commands");
                if (commands == null) continue;
                for (String cmd : commands.getValues(false).keySet()) {
                    final String desc = commands.getString(cmd + ".description", cmd);
                    if (desc.isEmpty()) continue;
                    helpdb.put(cmd, desc);
                }
            }
        }
    }

    static {
        reloadHelp();
    }
}
