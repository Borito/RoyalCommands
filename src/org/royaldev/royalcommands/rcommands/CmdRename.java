package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdRename implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdRename(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rename")) {
            if (!plugin.isAuthorized(cs, "rcmds.rename")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            String newName = RUtils.colorize(RoyalCommands.getFinalArg(args, 0));
            ItemStack is = p.getItemInHand();
            is = RUtils.renameItem(is, newName);
            p.setItemInHand(is);
            cs.sendMessage(ChatColor.BLUE + "Renamed your " + ChatColor.GRAY + RUtils.getItemName(is) + ChatColor.BLUE + " to " + ChatColor.GRAY + newName + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
