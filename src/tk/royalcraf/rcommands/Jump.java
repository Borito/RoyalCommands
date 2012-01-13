package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Jump implements CommandExecutor {

    RoyalCommands plugin;

    public Jump(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("jump")) {
            if (!plugin.isAuthorized(cs, "rcmds.jump")) {
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
            Player p = (Player) cs;
            BlockIterator b = new BlockIterator(p, 0);
            if (!b.hasNext()) {
                cs.sendMessage(ChatColor.RED + "Cannot jump there!");
                return true;
            } else {
                Block bb = b.next();
                while (b.hasNext()) {
                    if (!(b.next().getTypeId() == 0)) {
                        bb = b.next();
                        break;
                    }
                }
                Location bLoc = new Location(p.getWorld(), bb.getLocation().getX(), bb.getLocation().getY() + 1, bb.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                Back.backdb.put(p, p.getLocation());
                p.teleport(bLoc);
                return true;

            }
        }
        return false;
    }

}
