package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdCoords implements CommandExecutor {

    public final RoyalCommands plugin;

    public CmdCoords(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("coords")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.coords")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1 && cs instanceof Player) {
                Player p = (Player) cs;
                Location l = p.getLocation();
                cs.sendMessage(MessageColor.POSITIVE + "x: " + MessageColor.NEUTRAL + l.getX());
                cs.sendMessage(MessageColor.POSITIVE + "y: " + MessageColor.NEUTRAL + l.getY());
                cs.sendMessage(MessageColor.POSITIVE + "z: " + MessageColor.NEUTRAL + l.getZ());
                cs.sendMessage(MessageColor.POSITIVE + "pitch: " + MessageColor.NEUTRAL + l.getPitch());
                cs.sendMessage(MessageColor.POSITIVE + "yaw: " + MessageColor.NEUTRAL + l.getYaw());
                cs.sendMessage(MessageColor.POSITIVE + "world: " + MessageColor.NEUTRAL + l.getWorld().getName());
                return true;
            }
            if (!plugin.ah.isAuthorized(cs, "rcmds.others.coords")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            Location l = t.getLocation();
            cs.sendMessage(MessageColor.POSITIVE + "x: " + MessageColor.NEUTRAL + l.getX());
            cs.sendMessage(MessageColor.POSITIVE + "y: " + MessageColor.NEUTRAL + l.getY());
            cs.sendMessage(MessageColor.POSITIVE + "z: " + MessageColor.NEUTRAL + l.getZ());
            cs.sendMessage(MessageColor.POSITIVE + "pitch: " + MessageColor.NEUTRAL + l.getPitch());
            cs.sendMessage(MessageColor.POSITIVE + "yaw: " + MessageColor.NEUTRAL + l.getYaw());
            cs.sendMessage(MessageColor.POSITIVE + "world: " + MessageColor.NEUTRAL + l.getWorld().getName());
            return true;

        }
        return false;
    }

}
