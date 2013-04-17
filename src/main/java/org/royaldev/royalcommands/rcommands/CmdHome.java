package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdHome implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("home")) {
            if (!plugin.isAuthorized(cs, "rcmds.home")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }

            Player p = (Player) cs;

            boolean homeSet;
            double homeX;
            double homeY;
            double homeZ;
            float homeYaw;
            float homePitch;
            World homeW;
            String name = "";
            if (args.length > 0) name = args[0];

            PConfManager pcm;
            if (name.contains(":") && plugin.isAuthorized(cs, "rcmds.others.home")) {
                if (!PConfManager.getPConfManager(name.split(":")[0]).exists()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(name.split(":")[0]);
                if (plugin.isAuthorized(op, "rcmds.exempt.home")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot go to that player's house!");
                    return true;
                }
                String[] ss = name.split(":");
                if (ss.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You must include the name of the player and home (player:home).");
                    return true;
                }
                pcm = PConfManager.getPConfManager(ss[0]);
                name = ss[1];
            } else {
                pcm = PConfManager.getPConfManager(cs.getName());
            }
            homeSet = (args.length > 0) ? pcm.getBoolean("home." + name + ".set") : pcm.getBoolean("home.home.set");
            if (homeSet) {
                if (args.length > 0) {
                    homeX = pcm.getDouble("home." + name + ".x");
                    homeY = pcm.getDouble("home." + name + ".y");
                    homeZ = pcm.getDouble("home." + name + ".z");
                    homeYaw = Float.parseFloat(pcm.getString("home." + name + ".yaw"));
                    homePitch = Float.parseFloat(pcm.getString("home." + name + ".pitch"));
                    homeW = plugin.getServer().getWorld(pcm.getString("home." + name + ".w"));
                } else {
                    homeX = pcm.getDouble("home.home.x");
                    homeY = pcm.getDouble("home.home.y");
                    homeZ = pcm.getDouble("home.home.z");
                    homeYaw = Float.parseFloat(pcm.getString("home.home.yaw"));
                    homePitch = Float.parseFloat(pcm.getString("home.home.pitch"));
                    homeW = plugin.getServer().getWorld(pcm.getString("home.home.w", ""));
                }
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "You don't have that home set!");
                return true;
            }
            if (homeW == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "World doesn't exist!");
                return true;
            }
            Location homeLoc = new Location(homeW, homeX, homeY, homeZ, homeYaw, homePitch);
            if (args.length > 0) {
                p.sendMessage(MessageColor.POSITIVE + "Going to home \"" + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + ".\"");
            } else {
                p.sendMessage(MessageColor.POSITIVE + "Going home.");
            }
            String error = RUtils.teleport(p, homeLoc);
            if (!error.isEmpty()) {
                p.sendMessage(MessageColor.NEGATIVE + error);
                return true;
            }
            return true;

        }
        return false;
    }
}
