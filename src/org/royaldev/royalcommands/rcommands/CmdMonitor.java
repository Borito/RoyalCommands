package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;
import java.util.Map;

public class CmdMonitor implements CommandExecutor {

    RoyalCommands plugin;

    public CmdMonitor(RoyalCommands instance) {
        plugin = instance;
    }

    public static Map<String, String> monitors = new HashMap<String, String>();
    public static Map<String, String> viewees = new HashMap<String, String>();
    public static Map<String, PlayerInventory> invs = new HashMap<String, PlayerInventory>();

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("monitor")) {
            if (!plugin.isAuthorized(cs, "rcmds.monitor")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (monitors.containsKey(p.getName())) {
                Player m = plugin.getServer().getPlayer(monitors.get(p.getName()));
                if (m != null) p.showPlayer(m);
                for (Player pl : plugin.getServer().getOnlinePlayers()) pl.showPlayer(p);
                viewees.remove(monitors.get(p.getName()));
                monitors.remove(p.getName());
                p.getInventory().setContents(invs.get(p.getName()).getContents());
                invs.remove(p.getName());
                cs.sendMessage(ChatColor.BLUE + "Stopped active monitoring.");
                return true;
            }
            String toWatch = (args.length > 0) ? args[0] : "";
            if (toWatch.equals("")) {
                cs.sendMessage(ChatColor.RED + "Monitoring all players is not yet supported.");
                cs.sendMessage(cmd.getUsage());
            } else {
                Player t = plugin.getServer().getPlayer(toWatch);
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.monitor")) {
                    cs.sendMessage(ChatColor.RED + "You can't monitor that player!");
                    return true;
                }
                monitors.put(p.getName(), t.getName());
                viewees.put(t.getName(), p.getName());
                cs.sendMessage(ChatColor.BLUE + "You are now monitoring " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                for (Player pl : plugin.getServer().getOnlinePlayers()) {
                    if (pl.equals(p)) continue;
                    pl.hidePlayer(p);
                }
                p.hidePlayer(t);
                invs.put(p.getName(), p.getInventory());
                p.getInventory().clear();
                p.getInventory().setContents(t.getInventory().getContents());
                RUtils.silentTeleport(p, t);
                return true;
            }
            return true;
        }
        return false;
    }

}
