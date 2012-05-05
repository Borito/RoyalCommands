package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdClearInventory implements CommandExecutor {

    RoyalCommands plugin;

    public CmdClearInventory(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearinventory")) {
            if (!plugin.isAuthorized(cs, "rcmds.clearinventory")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                Player p = (Player) cs;
                p.getInventory().clear();
                cs.sendMessage(ChatColor.BLUE + "You have cleared your inventory.");
                return true;
            }
        }
        if (args.length == 1) {
            if (!plugin.isAuthorized(cs, "rcmds.others.clearinventory")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0].trim());
            if (target == null || plugin.isVanished(target)) {
                cs.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }
            if (plugin.isAuthorized(target, "rcmds.exempt.clearinventory")) {
                cs.sendMessage(ChatColor.RED + "You cannot alter that player's inventory!");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "You have cleared the inventory of " + ChatColor.GRAY + target.getName() + ChatColor.BLUE + ".");
            target.sendMessage(ChatColor.RED + "Your inventory has been cleared.");
            target.getInventory().clear();
            return true;
        }
        return false;
    }

}
