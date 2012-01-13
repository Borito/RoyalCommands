package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.RoyalCommands;
import tk.royalcraf.royalcommands.Utils;

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
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                Block bb = Utils.getTarget(p);
                if (bb == null) {
                    cs.sendMessage(ChatColor.RED + "Can't strike there!");
                    return true;
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
