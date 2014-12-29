package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.listeners.MonitorListener;
import org.royaldev.royalcommands.wrappers.player.MemoryRPlayer;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

import java.util.HashMap;
import java.util.Map;

@ReflectCommand
public class CmdMonitor extends BaseCommand {

    public static final Map<String, String> monitors = new HashMap<>();
    public static final Map<String, String> viewees = new HashMap<>();
    public static final Map<String, ItemStack[]> invs = new HashMap<>();
    public static final Map<String, Location> locs = new HashMap<>();

    public CmdMonitor(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        if (monitors.containsKey(p.getName())) {
            Player m = this.plugin.getServer().getPlayer(monitors.get(p.getName()));
            RUtils.silentTeleport(p, locs.get(p.getName()));
            if (m != null) p.showPlayer(m);
            for (Player pl : this.plugin.getServer().getOnlinePlayers()) pl.showPlayer(p);
            viewees.remove(monitors.get(p.getName()));
            monitors.remove(p.getName());
            locs.remove(p.getName());
            if (MonitorListener.openInvs.contains(p.getName())) MonitorListener.openInvs.remove(p.getName());
            p.getInventory().clear();
            p.getInventory().setContents(invs.get(p.getName()));
            invs.remove(p.getName());
            cs.sendMessage(MessageColor.POSITIVE + "Stopped active monitoring.");
            return true;
        }
        String toWatch = (args.length > 0) ? args[0] : "";
        if ("".equals(toWatch)) {
            cs.sendMessage(MessageColor.NEGATIVE + "Monitoring all players is not yet supported.");
        } else {
            Player t = this.plugin.getServer().getPlayer(toWatch);
            if (t == null || this.plugin.isVanished(t)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (t.equals(p)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot monitor yourself!");
                return true;
            }
            if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't monitor that player!");
                return true;
            }
            monitors.put(p.getName(), t.getName());
            viewees.put(t.getName(), p.getName());
            cs.sendMessage(MessageColor.POSITIVE + "You are now monitoring " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            for (Player pl : this.plugin.getServer().getOnlinePlayers()) {
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
            final RPlayer rp = MemoryRPlayer.getRPlayer(p);
            rp.getTeleporter().teleport(t, true);
            return true;
        }
        return true;
    }
}
