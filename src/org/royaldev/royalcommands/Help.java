package org.royaldev.royalcommands;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Help {

    public static HashMap<String, String> helpdb = new HashMap<String, String>();
    private static Map<String, Map<String, Object>> commands = RoyalCommands.commands;

    public static void reloadHelp() {
        helpdb.clear();
        if (commands == null) {
            Logger.getLogger("Minecraft").severe("[RoyalCommands] Could not grab list of commands!");
            return;
        }
        for (String cmd : commands.keySet()) {
            if (cmd == null || commands.get(cmd) == null || commands.get(cmd).get("description") == null) continue;
            String desc = commands.get(cmd).get("description").toString();
            helpdb.put(cmd, desc);
        }
        if (RoyalCommands.otherHelp) {
            for (Plugin p : RoyalCommands.plugins) {
                if (p == null) continue;
                if ((p instanceof RoyalCommands) || !p.isEnabled()) continue;
                if (p.getDescription() == null || p.getDescription().getCommands() == null) continue;
                Map<String, Map<String, Object>> commands = p.getDescription().getCommands();
                if (commands.keySet() == null) continue;
                for (String cmd : commands.keySet()) {
                    if (commands.get(cmd) == null || commands.get(cmd).get("description") == null) continue;
                    String desc = commands.get(cmd).get("description").toString();
                    helpdb.put(cmd, desc);
                }
            }
        }
    }

    static {
        reloadHelp();
    }
}
