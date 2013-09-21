package org.royaldev.royalcommands.rcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;

import java.util.Map;

@ReflectCommand
public class CmdWarp implements CommandExecutor {

    private final RoyalCommands plugin;

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

        ConfManager cm = ConfManager.getConfManager("warps.yml");
        if (!cm.exists()) return null;
        warpSet = cm.getBoolean("warps." + name + ".set");
        if (warpSet != null && !warpSet) return null;
        warpX = cm.getDouble("warps." + name + ".x");
        warpY = cm.getDouble("warps." + name + ".y");
        warpZ = cm.getDouble("warps." + name + ".z");
        warpYaw = Float.parseFloat(cm.getString("warps." + name + ".yaw"));
        warpPitch = Float.parseFloat(cm.getString("warps." + name + ".pitch"));
        warpW = Bukkit.getServer().getWorld(cm.getString("warps." + name + ".w"));
        if (warpW == null) {
            p.sendMessage(MessageColor.NEGATIVE + "World doesn't exist!");
            return null;
        }
        try {
            return new Location(warpW, warpX, warpY, warpZ, warpYaw, warpPitch);
        } catch (Exception e) {
            p.sendMessage(MessageColor.NEGATIVE + "There are no warps!");
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warp")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.warp")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (!(cs instanceof Player) && args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            if (args.length < 1) {
                ConfManager cm = ConfManager.getConfManager("warps.yml");
                if (!cm.exists() || cm.get("warps") == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "There are no warps!");
                    return true;
                }
                final Map<String, Object> opts = cm.getConfigurationSection("warps").getValues(false);
                if (opts.keySet().isEmpty()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "There are no warps!");
                    return true;
                }
                String warps = opts.keySet().toString();
                warps = warps.substring(1, warps.length() - 1);
                cs.sendMessage(MessageColor.POSITIVE + "Warps:");
                cs.sendMessage(warps);
                return true;
            }
            if (args.length == 1) {
                Player p = (Player) cs;
                Location warpLoc = pWarp(p, args[0].toLowerCase());
                if (warpLoc == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such warp!");
                    return true;
                }
                if (Config.warpPermissions && !plugin.ah.isAuthorized(cs, "rcmds.warp." + args[0].toLowerCase())) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You do not have permission for that warp!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Going to warp \"" + MessageColor.NEUTRAL + args[0].toLowerCase() + MessageColor.POSITIVE + ".\"");
                String error = RUtils.teleport(p, warpLoc);
                if (!error.isEmpty()) {
                    p.sendMessage(MessageColor.NEGATIVE + error);
                    return true;
                }
                return true;
            }
            if (args.length > 1) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.others.warp")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[1]);
                if (t == null || plugin.isVanished(t, cs)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                if (plugin.ah.isAuthorized(t, "rcmds.exempt.warp")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot warp that player!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Warping " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " \"" + MessageColor.NEUTRAL + args[0].toLowerCase() + MessageColor.POSITIVE + ".\"");
                Location warpLoc = pWarp(t, args[0].toLowerCase());
                if (warpLoc == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such warp!");
                    return true;
                }
                if (Config.warpPermissions && !plugin.ah.isAuthorized(t, "rcmds.warp." + args[0].toLowerCase())) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not have permission for that warp!");
                    return true;
                }
                String error = RUtils.teleport(t, warpLoc);
                if (!error.isEmpty()) {
                    cs.sendMessage(MessageColor.NEGATIVE + error);
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}
