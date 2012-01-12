package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.RoyalCommands;

public class RageQuit implements CommandExecutor {

    RoyalCommands plugin;

    public RageQuit(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("ragequit")) {
            if (!plugin.isAuthorized(cs, "rcmds.ragequit")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (args.length < 1) {
                if (cs instanceof Player) {
                    plugin.getServer().broadcastMessage(
                            ChatColor.DARK_RED + cs.getName() + ChatColor.RED
                                    + " has ragequit!");
                    ((Player) cs).kickPlayer(ChatColor.DARK_RED
                            + "RAAAGGGEEEE!!!");
                    return true;
                }
            }
            if (args.length == 1) {
                if (!plugin.isAuthorized(cs, "rcmds.ragequit.others")) {
                    cs.sendMessage(ChatColor.RED
                            + "You don't have permission for that!");
                    return true;
                }
                Player victim = plugin.getServer().getPlayer(args[0]);
                if (victim == null) {
                    cs.sendMessage(ChatColor.RED
                            + "That player does not exist!");
                    return true;
                }
                if (plugin.isVanished(victim)) {
                    cs.sendMessage(ChatColor.RED
                            + "That player does not exist!");
                    return true;
                }
                plugin.getServer().broadcastMessage(
                        ChatColor.DARK_RED + victim.getName() + ChatColor.RED
                                + " has ragequit!");
                victim.kickPlayer(ChatColor.DARK_RED + "RAAAGGGEEEE!!!");
                return true;
            }
        }
        return false;
    }

}
