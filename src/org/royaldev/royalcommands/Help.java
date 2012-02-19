package org.royaldev.royalcommands;

import java.util.HashMap;

public class Help {

    public static HashMap<String, String> helpdb = new HashMap<String, String>();
    static HashMap<String, HashMap<String, Object>> commands = (HashMap<String, HashMap<String, Object>>) RoyalCommands.commands;
    
    static {
        for (String cmd : commands.keySet()) {
            if (commands.get(cmd) == null || commands.get(cmd).get("description") == null) continue;
            String desc = commands.get(cmd).get("description").toString();
            helpdb.put(cmd, desc);
        }
    }

}
