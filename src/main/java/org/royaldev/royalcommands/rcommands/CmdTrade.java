package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdTrade implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdTrade(RoyalCommands instance) {
        plugin = instance;
    }

    public static void sendTradeRequest(Player target, Player sender) {
        tradedb.put(sender.getName(), target.getName());
        target.sendMessage(MessageColor.NEUTRAL + sender.getName() + MessageColor.POSITIVE + " has requested to trade with you.");
        target.sendMessage(MessageColor.POSITIVE + "Type " + MessageColor.NEUTRAL + "/trade " + sender.getName() + MessageColor.POSITIVE + " to accept.");
    }

    /**
     * Gets the trade inventory between two players. If no trade inventory has been made, this will return null.
     * <p/>
     * Note that the order of the player arguments does not matter. It may be called as
     * <code>getTradeInv(playerA, playerB)</code> or <code>getTradeInv(playerB, playerA)</code>.
     *
     * @param p One player
     * @param t Other player
     * @return Inventory or null if no such inventory
     */
    public static Inventory getTradeInv(Player p, Player t) {
        synchronized (trades) {
            for (final HashMap<String, String> set : trades.keySet()) {
                if ((set.containsKey(t.getName()) && set.get(t.getName()).equals(p.getName())) || (set.containsKey(p.getName()) && set.get(p.getName()).equals(t.getName())))
                    return trades.get(set);
            }
        }
        return null;
    }

    public static final HashMap<String, String> tradedb = new HashMap<String, String>();
    public static final HashMap<HashMap<String, String>, Inventory> trades = new HashMap<HashMap<String, String>, Inventory>();

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("trade")) {
            if (!plugin.isAuthorized(cs, "rcmds.trade")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (t.equals(p)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't trade with yourself!");
                return true;
            }
            Inventory inv = getTradeInv(p, t);
            if (inv != null) {
                p.sendMessage(MessageColor.POSITIVE + "Resumed trading with " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                p.openInventory(inv);
                return true;
            }
            if (tradedb.containsKey(t.getName())) {
                inv = plugin.getServer().createInventory(null, 36, "Trade");
                p.sendMessage(MessageColor.POSITIVE + "Opened trading interface.");
                p.openInventory(inv);
                t.openInventory(inv);
                final HashMap<String, String> trade = new HashMap<String, String>();
                trade.put(p.getName(), t.getName());
                trades.put(trade, inv);
                tradedb.remove(t.getName());
                return true;
            } else {
                sendTradeRequest(t, p);
                p.sendMessage(MessageColor.POSITIVE + "Sent a trade request to " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                return true;
            }
        }
        return false;
    }

}
