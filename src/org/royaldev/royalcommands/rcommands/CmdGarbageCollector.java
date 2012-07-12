package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdGarbageCollector implements CommandExecutor {

    RoyalCommands plugin;

    public CmdGarbageCollector(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("garbagecollector")) {
            if (!plugin.isAuthorized(cs, "rcmds.garbagecollector")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Runtime r = Runtime.getRuntime();
            long oldMem = r.freeMemory() / 1048576L;
            cs.sendMessage(ChatColor.BLUE + "Running Java garbage collector...");
            r.gc();
            int processors = r.availableProcessors();
            long maxMem = r.maxMemory() / 1048576L;
            long newMem = r.freeMemory() / 1048576L;
            if (r.maxMemory() < 0L) {
                cs.sendMessage(ChatColor.RED + "You may be using CACAO Java, which means that these values may be negative.");
                cs.sendMessage(ChatColor.RED + "Please switch to another JVM.");
            }
            cs.sendMessage(ChatColor.BLUE + "Used memory before: " + ChatColor.GRAY + (maxMem - oldMem) + " MB");
            cs.sendMessage(ChatColor.BLUE + "Current memory: " + ChatColor.GRAY + (maxMem - newMem) + " MB" + ChatColor.BLUE + "/" + ChatColor.GRAY + maxMem + " MB");
            cs.sendMessage(ChatColor.BLUE + "Memory freed: " + ChatColor.GRAY + (newMem - oldMem) + " MB");
            cs.sendMessage(ChatColor.BLUE + "Processors available to Java: " + ChatColor.GRAY + processors);
            return true;
        }
        return false;
    }

}
