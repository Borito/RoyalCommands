package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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

    /**
     * Get the custom spawn location of a world. If none is set, it will return the default.
     *
     * @param world World to get spawn for
     * @return Custom spawn or default spawn if not set
     */
    public static Location getWorldSpawn(World world) {
        ConfManager cm = new ConfManager("spawns.yml");
        String w = world.getName();
        Double x = cm.getDouble("spawns." + w + ".x");
        Double y = cm.getDouble("spawns." + w + ".y");
        Double z = cm.getDouble("spawns." + w + ".z");
        Float yaw = cm.getFloat("spawns." + w + ".yaw");
        Float pitch = cm.getFloat("spawns." + w + ".pitch");
        Location l;
        try {
            l = new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            l = world.getSpawnLocation();
        }
        return l;
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
            Location l = getWorldSpawn(p.getWorld());
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
