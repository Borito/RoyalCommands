package org.royaldev.royalcommands;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Help {

    public static HashMap<String, String> helpdb = new HashMap<String, String>();
    static Map<String, Map<String, Object>> commands = (Map<String, Map<String, Object>>) RoyalCommands.commands;

    public static void reloadHelp() {
        helpdb.clear();
        for (String cmd : commands.keySet()) {
            if (commands.get(cmd) == null || commands.get(cmd).get("description") == null) continue;
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
