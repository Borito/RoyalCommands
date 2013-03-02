package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTppos implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdTppos(RoyalCommands instance) {
        this.plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tppos")) {
            if (!plugin.isAuthorized(cs, "rcmds.tppos")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 5) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 3) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Double x = RUtils.getDouble(args[0]);
            Double y = RUtils.getDouble(args[1]);
            Double z = RUtils.getDouble(args[2]);
            if (x == null || y == null || z == null) {
                cs.sendMessage(ChatColor.RED + "One of the coordinates was invalid.");
                return true;
            }
            Player p = (cs instanceof Player) ? (Player) cs : null;
            Player toTeleport = (args.length > 4) ? plugin.getServer().getPlayer(args[4]) : p;
            if (toTeleport == null || plugin.isVanished(toTeleport, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            Location pLoc;
            World w = toTeleport.getWorld();
            if (args.length > 3) w = plugin.getServer().getWorld(args[3]);
            if (w == null) {
                cs.sendMessage(ChatColor.RED + "That world does not exist!");
                return true;
            }
            pLoc = new Location(w, x, y, z);
            if (!toTeleport.getName().equals(cs.getName()))
                cs.sendMessage(ChatColor.BLUE + "Teleporting " + ChatColor.GRAY + toTeleport.getName() + ChatColor.BLUE + " to x: " + ChatColor.GRAY + x + ChatColor.BLUE + ", y: " + ChatColor.GRAY + y + ChatColor.BLUE + ", z: " + ChatColor.GRAY + z + ChatColor.BLUE + " in world " + ChatColor.GRAY + w.getName() + ChatColor.BLUE + ".");
            toTeleport.sendMessage(ChatColor.BLUE + "Teleporting you to x: " + ChatColor.GRAY + x + ChatColor.BLUE + ", y: " + ChatColor.GRAY + y + ChatColor.BLUE + ", z: " + ChatColor.GRAY + z + ChatColor.BLUE + " in world " + ChatColor.GRAY + w.getName() + ChatColor.BLUE + ".");
            String error = RUtils.teleport(toTeleport, pLoc);
            if (!error.isEmpty()) {
                toTeleport.sendMessage(ChatColor.RED + error);
                return true;
            }
            return true;
        }
        return false;
    }

}
