package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.listeners.MonitorListener;

import java.util.HashMap;
import java.util.Map;

public class CmdMonitor implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdMonitor(RoyalCommands instance) {
        plugin = instance;
    }

    public static Map<String, String> monitors = new HashMap<String, String>();
    public static Map<String, String> viewees = new HashMap<String, String>();
    public static Map<String, ItemStack[]> invs = new HashMap<String, ItemStack[]>();
    public static Map<String, Location> locs = new HashMap<String, Location>();

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("monitor")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.monitor")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (monitors.containsKey(p.getName())) {
                Player m = plugin.getServer().getPlayer(monitors.get(p.getName()));
                RUtils.silentTeleport(p, locs.get(p.getName()));
                if (m != null) p.showPlayer(m);
                for (Player pl : plugin.getServer().getOnlinePlayers()) pl.showPlayer(p);
                viewees.remove(monitors.get(p.getName()));
                monitors.remove(p.getName());
                locs.remove(p.getName());
                if (MonitorListener.openInvs.contains(p.getName()))
                    MonitorListener.openInvs.remove(p.getName());
                p.getInventory().clear();
                p.getInventory().setContents(invs.get(p.getName()));
                invs.remove(p.getName());
                cs.sendMessage(MessageColor.POSITIVE + "Stopped active monitoring.");
                return true;
            }
            String toWatch = (args.length > 0) ? args[0] : "";
            if (toWatch.equals("")) {
                cs.sendMessage(MessageColor.NEGATIVE + "Monitoring all players is not yet supported.");
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
            } else {
                Player t = plugin.getServer().getPlayer(toWatch);
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                if (t.equals(p)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot monitor yourself!");
                    return true;
                }
                if (plugin.ah.isAuthorized(t, "rcmds.exempt.monitor")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't monitor that player!");
                    return true;
                }
                monitors.put(p.getName(), t.getName());
                viewees.put(t.getName(), p.getName());
                cs.sendMessage(MessageColor.POSITIVE + "You are now monitoring " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                for (Player pl : plugin.getServer().getOnlinePlayers()) {
                    if (pl.equals(p)) continue;
                    pl.hidePlayer(p);
                }
                p.hidePlayer(t);
                invs.put(p.getName(), p.getInventory().getContents());
                locs.put(p.getName(), p.getLocation());
                p.getInventory().clear();
                p.getInventory().setContents(t.getInventory().getContents());
                p.setItemInHand(t.getItemInHand());
                p.setHealth(t.getHealth());
                p.setFoodLevel(t.getFoodLevel());
                p.setSaturation(t.getSaturation());
                RUtils.silentTeleport(p, t);
                return true;
            }
            return true;
        }
        return false;
    }

}
