package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSetlevel implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdSetlevel(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setlevel")) {
            if (!plugin.isAuthorized(cs, "rcmds.setlevel")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player) && args.length == 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            int lvl;
            try {
                lvl = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                cs.sendMessage(ChatColor.RED + "The level must be a number!");
                return true;
            }

            Player t = plugin.getServer().getPlayer((args.length > 1) ? args[1] : cs.getName());
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            t.setLevel(lvl);
            if (!cs.equals(t))
                cs.sendMessage(ChatColor.BLUE + "Set the level of " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " to " + ChatColor.GRAY + lvl + ChatColor.BLUE + ".");
            t.sendMessage(ChatColor.BLUE + "Your level has been set to " + ChatColor.GRAY + lvl + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
