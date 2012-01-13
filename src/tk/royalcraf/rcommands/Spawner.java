package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Spawner implements CommandExecutor {

    RoyalCommands plugin;

    public Spawner(RoyalCommands instance) {
        this.plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawner")) {
            if (!plugin.isAuthorized(cs, "rcmds.spawner")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
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
            BlockIterator b = new BlockIterator(p, 0);
            if (!b.hasNext()) {
                cs.sendMessage(ChatColor.RED + "No block there!");
                return true;
            }
            Block bb = b.next();
            while (b.hasNext()) {
                if (b.next().getTypeId() != 0) {
                    bb = b.next();
                    break;
                }
            }
            if (bb == null) {
                cs.sendMessage(ChatColor.RED + "No block found!");
                return true;
            }
            if (bb.getType() != Material.MOB_SPAWNER) {
                cs.sendMessage(ChatColor.RED + "That's not a mob spawner!");
                return true;
            }
            if (!(bb.getState() instanceof CreatureSpawner)) {
                cs.sendMessage(ChatColor.RED + "That's not a mob spawner!");
                return true;
            }
            CreatureSpawner crs = (CreatureSpawner) bb.getState();
            CreatureType ct;
            try {
                ct = CreatureType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                cs.sendMessage(ChatColor.RED + "Invalid mob!");
                return true;
            }
            crs.setCreatureType(ct);
            cs.sendMessage(ChatColor.BLUE + "Spawner type set to " + ChatColor.GRAY + crs.getCreatureTypeId().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
