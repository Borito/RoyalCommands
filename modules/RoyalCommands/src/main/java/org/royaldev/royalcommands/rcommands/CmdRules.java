/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdRules extends TabCommand {

    public CmdRules(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        File rulesf = new File(this.plugin.getDataFolder() + File.separator + "rules.txt");
        if (!rulesf.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "The rules.txt file was not found! Tell an admin.");
            return true;
        }
        int tpage;
        if (args.length < 1) {
            tpage = 1;
        } else {
            try {
                tpage = Integer.valueOf(args[0]);
            } catch (Exception e) {
                cs.sendMessage(MessageColor.NEGATIVE + "The page number was invalid!");
                return true;
            }
        }
        int pages = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(rulesf));
            String line;
            List<String> rules = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = RUtils.colorize(line);
                rules.add(line);
                if (line.trim().equals("###")) pages++;
            }
            if (tpage > pages || tpage < 1) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such page!");
                return true;
            }
            if (tpage == pages) {
                cs.sendMessage(ChatColor.GOLD + "Page " + MessageColor.NEUTRAL + tpage + ChatColor.GOLD + " of " + MessageColor.NEUTRAL + pages + ChatColor.GOLD + ".");
            } else {
                cs.sendMessage(ChatColor.GOLD + "Page " + MessageColor.NEUTRAL + tpage + ChatColor.GOLD + " of " + MessageColor.NEUTRAL + pages + ChatColor.GOLD + ". " + MessageColor.NEUTRAL + "/" + cmd.getName() + " " + (tpage + 1) + ChatColor.GOLD + " for next page.");
            }
            int cpage = 0;
            for (String s : rules) {
                if (s.trim().equals("###")) {
                    cpage++;
                    s = "";
                }
                if (cpage == tpage && !s.equals("")) {
                    cs.sendMessage(s);
                }
            }
        } catch (Exception e) {
            cs.sendMessage(MessageColor.NEGATIVE + "The rules.txt file was not found! Tell an admin.");
            return true;
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ignored) {
            }
        }
        return true;
    }
}
