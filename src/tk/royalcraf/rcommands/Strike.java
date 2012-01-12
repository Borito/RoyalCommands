package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Strike implements CommandExecutor {

    RoyalCommands plugin;

    public Strike(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("strike")) {
            if (!plugin.isAuthorized(cs, "rcmds.strike")) {
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
            if (args.length < 1) {
                BlockIterator b = new BlockIterator(p, 0);
                if (!b.hasNext()) {
                    cs.sendMessage(ChatColor.RED + "Cannot strike there!");
                    return true;
                }
                Block bb = b.next();
                while (b.hasNext()) {
                    if (!(b.next().getTypeId() == 0)) {
                        bb = b.next();
                        break;
                    }
                }
                p.getWorld().strikeLightning(bb.getLocation());
                return true;
            } else {
                if (!plugin.isAuthorized(cs, "rcmds.strike.others")) {
                    cs.sendMessage(ChatColor.RED
                            + "You don't have permission for that!");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[0].trim());
                if (target == null) {
                    cs.sendMessage(ChatColor.RED
                            + "That player does not exist!");
                    return true;
                }
                if (plugin.isVanished(target)) {
                    cs.sendMessage(ChatColor.RED
                            + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(target, "rcmds.exempt.strike")) {
                    cs.sendMessage(ChatColor.RED
                            + "You can't strike that player!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Smiting " + ChatColor.GRAY
                        + target.getName() + ChatColor.BLUE + ".");
                target.sendMessage(ChatColor.RED + "You have been smited by "
                        + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".");
                target.getWorld().strikeLightning(target.getLocation());
                return true;
            }
        }
        return false;
    }

}
