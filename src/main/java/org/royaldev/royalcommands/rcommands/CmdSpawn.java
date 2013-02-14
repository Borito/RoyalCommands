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

    private RoyalCommands plugin;

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
        ConfManager cm = RoyalCommands.instance.getConf("spawns.yml");
        String w = world.getName();
        Location l = cm.getLocation("spawns." + w, world.getName());
        if (l == null) l = world.getSpawnLocation();
        return l;
    }

    /**
     * Gets the group-specific spawn for a player and a world.
     *
     * @param p     Player to get spawn for
     * @param world World to get spawn for
     * @return null if no group-specific spawn or Location if existent
     */
    private static Location getGroupSpawn(Player p, World world) {
        ConfManager cm = RoyalCommands.instance.getConf("spawns.yml");
        String group;
        try {
            group = RoyalCommands.permission.getPrimaryGroup(p);
        } catch (UnsupportedOperationException e) {
            group = null;
        } catch (NullPointerException e) {
            group = null;
        }
        if (group == null || group.isEmpty()) return null;
        group = "." + group.toLowerCase();
        return cm.getLocation("spawns." + world.getName() + group, world.getName());
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
            World w;
            if (args.length > 0) {
                if (!plugin.isAuthorized(cs, "rcmds.spawn.other")) {
                    cs.sendMessage(ChatColor.RED + "You don't have permission to spawn in other worlds.");
                    return true;
                }
                w = plugin.getServer().getWorld(args[0]);
                if (w == null) {
                    cs.sendMessage(ChatColor.RED + "No such world!");
                    return true;
                }
            } else w = p.getWorld();
            Location l = getGroupSpawn(p, w);
            if (l == null) l = getWorldSpawn(w);
            p.sendMessage(ChatColor.BLUE + "Going to spawn in " + ChatColor.GRAY + RUtils.getMVWorldName(w) + ChatColor.BLUE + ".");
            String error = RUtils.teleport(p, l);
            if (!error.isEmpty()) p.sendMessage(ChatColor.RED + error);
            return true;
        }
        return false;
    }

}
