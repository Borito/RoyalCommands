package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
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
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }

            Player p = (Player) cs;

            boolean homeSet;
            Double homeX;
            Double homeY;
            Double homeZ;
            Float homeYaw;
            Float homePitch;
            World homeW;
            String name = "";
            if (args.length > 0) name = args[0];

            PConfManager pcm;
            if (name.contains(":") && plugin.isAuthorized(cs, "rcmds.others.home")) {
                if (!new PConfManager(name.split(":")[0]).exists()) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(name.split(":")[0]);
                if (plugin.isAuthorized(op, "rcmds.exempt.home")) {
                    cs.sendMessage(ChatColor.RED + "You cannot go to that player's house!");
                    return true;
                }
                String[] ss = name.split(":");
                if (ss.length < 2) {
                    cs.sendMessage(ChatColor.RED + "You must include the name of the player and home (player:home).");
                    return true;
                }
                pcm = new PConfManager(ss[0]);
                name = ss[1];
            } else {
                pcm = new PConfManager(cs.getName());
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
                    homeW = plugin.getServer().getWorld(pcm.getString("home.home.w"));
                }
            } else {
                cs.sendMessage(ChatColor.RED + "You don't have that home set!");
                return true;
            }
            if (homeW == null) {
                cs.sendMessage(ChatColor.RED + "World doesn't exist!");
                return true;
            }
            if (homeX == null || homeY == null || homeZ == null || homeYaw == null || homePitch == null) {
                System.out.println("homeX: " + homeX);
                System.out.println("homeY: " + homeY);
                System.out.println("homeZ: " + homeZ);
                System.out.println("homeYaw: " + homeYaw);
                System.out.println("homePitch: " + homePitch);
                cs.sendMessage(ChatColor.RED + "Home was saved incorrectly!");
                return true;
            }
            Location homeLoc = new Location(homeW, homeX, homeY, homeZ, homeYaw, homePitch);
            if (args.length > 0) {
                p.sendMessage(ChatColor.BLUE + "Going to home \"" + ChatColor.GRAY + name + ChatColor.BLUE + ".\"");
            } else {
                p.sendMessage(ChatColor.BLUE + "Going home.");
            }
            String error = RUtils.teleport(p, homeLoc);
            if (!error.isEmpty()) {
                p.sendMessage(ChatColor.RED + error);
                return true;
            }
            return true;

        }
        return false;
    }
}
