package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdRealName implements CommandExecutor {

    RoyalCommands plugin;

    public CmdRealName(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("realname")) {
            if (!plugin.isAuthorized(cs, "rcmds.realname")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = null;
            for (Player p : plugin.getServer().getOnlinePlayers())
                if (p.getDisplayName().equalsIgnoreCase(args[0])) t = p;
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            cs.sendMessage(ChatColor.GRAY + t.getDisplayName() + ChatColor.BLUE + " = " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
