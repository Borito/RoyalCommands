package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdHelmet implements CommandExecutor {

    RoyalCommands plugin;

    public CmdHelmet(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("helmet")) {
            if (!plugin.isAuthorized(cs, "rcmds.helmet")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            String name = args[0];
            if (name.trim().equalsIgnoreCase("none")) {
                p.getInventory().setHelmet(null);
                p.sendMessage(ChatColor.BLUE + "Removed your helmet.");
                return true;
            }
            ItemStack stack = RUtils.getItem(name, 1);
            if (stack == null) {
                p.sendMessage(ChatColor.RED + "Invalid item name!");
                return true;
            }
            p.getInventory().setHelmet(stack);
            p.sendMessage(ChatColor.BLUE + "Set your helmet to " + ChatColor.GRAY + stack.getType().name().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
