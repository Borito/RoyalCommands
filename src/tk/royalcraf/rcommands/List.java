package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.RoyalCommands;

public class List implements CommandExecutor {

    RoyalCommands plugin;

    public List(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("list")) {
            if (!plugin.isAuthorized(cs, "rcmds.list")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            Player p[] = plugin.getServer().getOnlinePlayers();
            String ps = "";
            int hid = 0;
            for (int i = 0; i < p.length; i++) {
                if (!plugin.isVanished(p[i])) {
                    if (ps == "") {
                        ps = ps.concat(p[i].getName());
                    } else {
                        ps = ps.concat(", " + p[i].getName());
                    }
                } else {
                    hid++;
                }
            }
            if (plugin.isAuthorized(cs, "rcmds.seehidden")) {
                if (hid > 0) {
                    cs.sendMessage(ChatColor.BLUE + "There are currently "
                            + ChatColor.GRAY + (p.length - hid) + "/" + hid
                            + ChatColor.BLUE + " out of " + ChatColor.GRAY
                            + plugin.getServer().getMaxPlayers()
                            + ChatColor.BLUE + " players online.");
                } else {
                    cs.sendMessage(ChatColor.BLUE + "There are currently "
                            + ChatColor.GRAY + p.length + ChatColor.BLUE
                            + " out of " + ChatColor.GRAY
                            + plugin.getServer().getMaxPlayers()
                            + ChatColor.BLUE + " players online.");
                }
                for (int i = 0; i < p.length; i++) {
                    if (plugin.isVanished(p[i])) {
                        if (ps == "") {
                            ps = ps.concat(ChatColor.GRAY + "[HIDDEN]"
                                    + ChatColor.WHITE + p[i].getName());
                        } else {
                            ps = ps.concat(", " + ChatColor.GRAY + "[HIDDEN]"
                                    + ChatColor.WHITE + p[i].getName());
                        }
                    }
                }
            } else {
                cs.sendMessage(ChatColor.BLUE + "There are currently "
                        + ChatColor.GRAY + (p.length - hid) + ChatColor.BLUE
                        + " out of " + ChatColor.GRAY
                        + plugin.getServer().getMaxPlayers() + ChatColor.BLUE
                        + " players online.");
            }
            cs.sendMessage("Online Players: " + ps);
            return true;
        }
        return false;
    }
}
