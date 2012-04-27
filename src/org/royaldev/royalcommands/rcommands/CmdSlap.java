package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSlap implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSlap(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("slap")) {
            if (!plugin.isAuthorized(cs, "rcmds.slap")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player victim;
            victim = plugin.getServer().getPlayer(args[0]);
            if (victim == null || plugin.isVanished(victim)) {
                cs.sendMessage(ChatColor.RED + "That person is not online!");
                return true;
            }
            if (plugin.isAuthorized(victim, "rcmds.exempt.slap")) {
                cs.sendMessage(ChatColor.RED + "You may not slap that player.");
                return true;
            }
            Vector push = victim.getVelocity();
            push.setY(push.getY() + .5);
            push.setX(push.getX() + .5);
            push.setZ(push.getZ() + .5);
            victim.setVelocity(push);
            plugin.getServer().broadcastMessage(ChatColor.GOLD + cs.getName() + ChatColor.WHITE + " slaps " + ChatColor.RED + victim.getName() + ChatColor.WHITE + "!");
            return true;
        }
        return false;
    }

}
