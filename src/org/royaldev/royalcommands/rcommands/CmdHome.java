package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
            if (name.contains(":") && plugin.isAuthorized(cs, "rcmds.others.home")) {
                if (!PConfManager.getPConfExists(name.split(":")[0])) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(name.split(":")[0]);
                if (op.isOp() || (op.isOnline() && plugin.isAuthorized((Player) op, "rcmds.exempt.home"))) {
                    cs.sendMessage(ChatColor.RED + "You cannot go to that player's house!");
                    return true;
                }
                pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + name.split(":")[0].toLowerCase() + ".yml");
                name = name.split(":")[1];
            } else {
                pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + cs.getName().toLowerCase() + ".yml");
            }
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                homeSet = (args.length > 0) ? pconf.getBoolean("home." + name + ".set") : pconf.getBoolean("home.home.set");
                if (homeSet) {
                    if (args.length > 0) {
                        homeX = pconf.getDouble("home." + name + ".x");
                        homeY = pconf.getDouble("home." + name + ".y");
                        homeZ = pconf.getDouble("home." + name + ".z");
                        homeYaw = Float.parseFloat(pconf.getString("home." + name + ".yaw"));
                        homePitch = Float.parseFloat(pconf.getString("home." + name + ".pitch"));
                        homeW = plugin.getServer().getWorld(pconf.getString("home." + name + ".w"));
                    } else {
                        homeX = pconf.getDouble("home.home.x");
                        homeY = pconf.getDouble("home.home.y");
                        homeZ = pconf.getDouble("home.home.z");
                        homeYaw = Float.parseFloat(pconf.getString("home.home.yaw"));
                        homePitch = Float.parseFloat(pconf.getString("home.home.pitch"));
                        homeW = plugin.getServer().getWorld(pconf.getString("home.home.w"));
                    }
                } else {
                    cs.sendMessage(ChatColor.RED + "You don't have that home set!");
                    return true;
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
            p.teleport(homeLoc);
            return true;

        }
        return false;
    }
}