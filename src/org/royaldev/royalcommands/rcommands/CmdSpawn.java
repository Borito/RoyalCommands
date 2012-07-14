package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.ConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSpawn implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSpawn(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (!plugin.isAuthorized(cs, "rcmds.spawn")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            p.sendMessage(ChatColor.BLUE + "Going to spawn.");
            ConfManager cm = new ConfManager("spawns.yml");
            String w = p.getWorld().getName();
            double x = cm.getDouble("spawns." + w + ".x");
            double y = cm.getDouble("spawns." + w + ".y");
            double z = cm.getDouble("spawns." + w + ".z");
            float yaw = cm.getFloat("spawns." + w + ".yaw");
            float pitch = cm.getFloat("spawns." + w + ".pitch");
            Location l;
            try {
                l = new Location(p.getWorld(), x, y, z, yaw, pitch);
            } catch (NullPointerException e) {
                l = p.getWorld().getSpawnLocation();
            }
            String error = RUtils.teleport(p, l);
            if (!error.isEmpty()) {
                p.sendMessage(ChatColor.RED + error);
                return true;
            }
            return true;
        }
        return false;
    }

}
