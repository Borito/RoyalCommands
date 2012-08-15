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

import java.io.File;

public class CmdHome implements CommandExecutor {

    RoyalCommands plugin;

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
            Double homeX = null;
            Double homeY = null;
            Double homeZ = null;
            Float homeYaw = null;
            Float homePitch = null;
            World homeW = null;
            String name = "";
            if (args.length > 0) name = args[0];

            File pconfl;
            PConfManager pcm;
            if (name.contains(":") && RUtils.canActAgainst(cs, name.split(":")[0], "home")) {
                if (!new PConfManager(name.split(":")[0]).exists()) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(name.split(":")[0]);
                pcm = new PConfManager(name.split(":")[0].toLowerCase());
                name = name.split(":")[1];
            } else pcm = new PConfManager(cs.getName().toLowerCase());
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That home doesn't exist!");
                return true;
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
            }
            if (homeW == null) {
                cs.sendMessage(ChatColor.RED + "World doesn't exist!");
            }
            if (homeX == null || homeY == null || homeZ == null || homeYaw == null || homePitch == null) {
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
