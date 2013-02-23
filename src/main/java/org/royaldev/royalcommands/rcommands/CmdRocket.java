package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdRocket implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdRocket(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rocket")) {
            if (!plugin.isAuthorized(cs, "rcmds.rocket")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.rocket")) {
                cs.sendMessage(ChatColor.RED + "You cannot rocket that player!");
                return true;
            }
            t.setVelocity(new Vector(t.getVelocity().getX(), 4, t.getVelocity().getZ()));
            cs.sendMessage(ChatColor.BLUE + "You have rocketed " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
