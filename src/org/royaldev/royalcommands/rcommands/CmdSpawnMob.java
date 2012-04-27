package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSpawnMob implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSpawnMob(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawnmob")) {
            if (!plugin.isAuthorized(cs, "rcmds.spawnmob")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Block bb = RUtils.getTarget(p);
            EntityType c;
            Location l = bb.getLocation();
            l.setY(l.getY() + 1);
            try {
                c = EntityType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                cs.sendMessage(ChatColor.RED + "Invalid mob!");
                return true;
            }
            if (args.length > 1) {
                int i;
                try {
                    i = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Invalid amount specified!");
                    return true;
                }
                if (i < 0) {
                    cs.sendMessage(ChatColor.RED + "Invalid amount specified!");
                    return true;
                }
                if (i > plugin.spawnmobLimit) {
                    cs.sendMessage(ChatColor.RED + "The amount specified was larger than the allowed amount.");
                    cs.sendMessage(ChatColor.RED + "Setting amount to " + ChatColor.GRAY + plugin.spawnmobLimit + ChatColor.RED + ".");
                    i = plugin.spawnmobLimit;
                }
                for (int a = 0; a < i; a++) p.getWorld().spawnCreature(l, c);
                cs.sendMessage(ChatColor.BLUE + "Spawned " + ChatColor.GRAY + i + ChatColor.BLUE + " of " + ChatColor.GRAY + c.getName().toLowerCase() + ChatColor.BLUE + ".");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "Spawned " + ChatColor.GRAY + "1" + ChatColor.BLUE + " of " + ChatColor.GRAY + c.getName().toLowerCase() + ChatColor.BLUE + ".");
            p.getWorld().spawnCreature(l, c);
            return true;
        }
        return false;
    }

}
