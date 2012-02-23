package org.royaldev.royalcommands;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Help {

    public static HashMap<String, String> helpdb = new HashMap<String, String>();
    static HashMap<String, HashMap<String, Object>> commands = (HashMap<String, HashMap<String, Object>>) RoyalCommands.commands;

    public static void reloadHelp() {
        helpdb.clear();
        for (String cmd : commands.keySet()) {
            if (commands.get(cmd) == null || commands.get(cmd).get("description") == null) continue;
            String desc = commands.get(cmd).get("description").toString();
            helpdb.put(cmd, desc);
        }
        if (RoyalCommands.otherHelp) {
            for (Plugin p : RoyalCommands.plugins) {
                if ((p instanceof RoyalCommands) || !p.isEnabled()) continue;
                HashMap<String, HashMap<String, Object>> commands = (HashMap<String, HashMap<String, Object>>) p.getDescription().getCommands();
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
