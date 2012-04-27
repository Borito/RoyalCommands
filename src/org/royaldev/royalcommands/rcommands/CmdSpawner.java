package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSpawner implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSpawner(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawner")) {
            if (!plugin.isAuthorized(cs, "rcmds.spawner")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            Block bb = RUtils.getTarget(p);
            if (bb == null) {
                cs.sendMessage(ChatColor.RED + "No block found!");
                return true;
            }
            if (!(bb.getState() instanceof CreatureSpawner)) {
                cs.sendMessage(ChatColor.RED + "That's not a mob spawner!");
                return true;
            }
            CreatureSpawner crs = (CreatureSpawner) bb.getState();
            EntityType ct;
            try {
                ct = EntityType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                cs.sendMessage(ChatColor.RED + "Invalid mob!");
                return true;
            }
            crs.setSpawnedType(ct);
            cs.sendMessage(ChatColor.BLUE + "Spawner type set to " + ChatColor.GRAY + crs.getCreatureTypeName().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
