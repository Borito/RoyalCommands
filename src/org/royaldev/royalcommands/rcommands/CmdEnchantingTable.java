package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdEnchantingTable implements CommandExecutor {

    RoyalCommands plugin;

    public CmdEnchantingTable(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("enchantingtable")) {
            if (!plugin.isAuthorized(cs, "rcmds.enchantingtable")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            p.openEnchanting(null, true);
            p.sendMessage(ChatColor.BLUE + "Opened an enchanting table for you.");
            return true;
        }
        return false;
    }

}
