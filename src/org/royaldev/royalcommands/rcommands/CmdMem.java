package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.DecimalFormat;

public class CmdMem implements CommandExecutor {

    RoyalCommands plugin;

    public CmdMem(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mem")) {
            if (!plugin.isAuthorized(cs, "rcmds.mem")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
            double memMax = Runtime.getRuntime().maxMemory() / 1048576;
            double memFree = memMax - memUsed;
            double percfree = (100 / memMax) * memFree;
            ChatColor color;
            if (percfree >= 60) color = ChatColor.GREEN;
            else if (percfree >= 35) color = ChatColor.YELLOW;
            else color = ChatColor.RED;
            DecimalFormat df = new DecimalFormat("00.00");
            cs.sendMessage(color + df.format(percfree) + "% " + ChatColor.WHITE + "free (" + color + memUsed + ChatColor.WHITE + " MB/" + memMax + " MB)");
            return true;
        }
        return false;
    }

}
