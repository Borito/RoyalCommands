package org.royaldev.royalcommands.rcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.ConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.util.Map;

public class CmdWarp implements CommandExecutor {

    RoyalCommands plugin;

    public CmdWarp(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static Location pWarp(Player p, String name) {
        Boolean warpSet;
        Double warpX;
        Double warpY;
        Double warpZ;
        Float warpYaw;
        Float warpPitch;
        World warpW;

        ConfManager cm = new ConfManager("warps.yml");
        warpSet = cm.getBoolean("warps." + name + ".set");
        if (warpSet != null && !warpSet) {
            p.sendMessage(ChatColor.RED + "That warp does not exist.");
            return null;
        }
        warpX = cm.getDouble("warps." + name + ".x");
        warpY = cm.getDouble("warps." + name + ".y");
        warpZ = cm.getDouble("warps." + name + ".z");
        warpYaw = Float.parseFloat(cm.getString("warps." + name + ".yaw"));
        warpPitch = Float.parseFloat(cm.getString("warps." + name + ".pitch"));
        warpW = Bukkit.getServer().getWorld(cm.getString("warps." + name + ".w"));
        if (warpW == null) {
            p.sendMessage(ChatColor.RED + "World doesn't exist!");
            return null;
        }
        try {
            return new Location(warpW, warpX, warpY, warpZ, warpYaw, warpPitch);
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "There are no warps!");
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warp")) {
            if (!plugin.isAuthorized(cs, "rcmds.warp")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (!(cs instanceof Player) && args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            if (args.length < 1) {
                File pconfl = new File(plugin.getDataFolder() + File.separator + "warps.yml");
                if (pconfl.exists()) {
                    FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                    if (args.length < 1) {
                        if (pconf.get("warps") == null) {
                            cs.sendMessage(ChatColor.RED + "There are no warps!");
                            return true;
                        }
                        final Map<String, Object> opts = pconf.getConfigurationSection("warps").getValues(false);
                        if (opts.keySet().isEmpty()) {
                            cs.sendMessage(ChatColor.RED + "There are no warps!");
                            return true;
                        }
                        String warps = opts.keySet().toString();
                        warps = warps.substring(1, warps.length() - 1);
                        cs.sendMessage(ChatColor.BLUE + "Warps:");
                        cs.sendMessage(warps);
                        return true;
                    }
                    return true;
                }
            }
            if (args.length == 1) {
                Player p = (Player) cs;
                Location warpLoc = pWarp(p, args[0].toLowerCase());
                if (warpLoc == null) {
                    cs.sendMessage(ChatColor.RED + "No such warp!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Going to warp \"" + ChatColor.GRAY + args[0].toLowerCase() + ChatColor.BLUE + ".\"");
                String error = RUtils.teleport(p, warpLoc);
                if (!error.isEmpty()) {
                    p.sendMessage(ChatColor.RED + error);
                    return true;
                }
                return true;
            }
            if (args.length > 1) {
                if (!plugin.isAuthorized(cs, "rcmds.others.warp")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[1]);
                if (t == null || plugin.isVanished(t, cs)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.warp")) {
                    cs.sendMessage(ChatColor.RED + "You cannot warp that player!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Warping " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " \"" + ChatColor.GRAY + args[0].toLowerCase() + ChatColor.BLUE + ".\"");
                Location warpLoc = pWarp(t, args[0].toLowerCase());
                if (warpLoc == null) {
                    cs.sendMessage(ChatColor.RED + "No such warp!");
                    return true;
                }
                String error = RUtils.teleport(t, warpLoc);
                if (!error.isEmpty()) {
                    cs.sendMessage(ChatColor.RED + error);
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}
