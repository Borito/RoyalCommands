/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.trade.Trade;
import org.royaldev.royalcommands.shaded.mkremins.fanciful.FancyMessage;
import org.royaldev.royalcommands.wrappers.player.MemoryRPlayer;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ReflectCommand
public class CmdTrade extends BaseCommand {

    public static final Map<UUID, UUID> tradedb = new HashMap<>();
    public static final Map<Map<UUID, UUID>, Inventory> trades = new HashMap<>();

    public CmdTrade(final RoyalCommands instance, final String name) {
        super(instance, name, true);
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
        synchronized (CmdTrade.trades) {
            for (final Map<UUID, UUID> set : CmdTrade.trades.keySet()) {
                if ((set.containsKey(t.getUniqueId()) && set.get(t.getUniqueId()).equals(p.getUniqueId())) || (set.containsKey(p.getUniqueId()) && set.get(p.getUniqueId()).equals(t.getUniqueId())))
                    return CmdTrade.trades.get(set);
            }
        }
        return null;
    }

    public static void sendTradeRequest(Player target, Player sender) {
        CmdTrade.tradedb.put(sender.getUniqueId(), target.getUniqueId());
        // @formatter:off
        new FancyMessage(sender.getName())
                .color(MessageColor.NEUTRAL.cc())
                .formattedTooltip(RUtils.getPlayerTooltip(sender))
            .then(" has requested to trade with you.")
                .color(MessageColor.POSITIVE.cc())
            .send(target);
        new FancyMessage("Type ")
                .color(MessageColor.POSITIVE.cc())
            .then("/trade " + sender.getName())
                .color(MessageColor.NEUTRAL.cc())
                .tooltip("Click to execute this command.")
                .command("/trade " + sender.getName())
            .then(" to accept.")
                .color(MessageColor.POSITIVE.cc())
            .send(target);
        // @formatter:on
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player p = (Player) cs;
        final RPlayer rp = MemoryRPlayer.getRPlayer(p);
        final UUID other = MemoryRPlayer.getRPlayer(args[0]).getUUID();
        Trade trade = Trade.getTradeFor(rp.getUUID(), other);
        if (trade == null) {
            trade = new Trade(rp.getUUID(), other);
        }
        trade.showInventoryGUI(rp.getUUID());
        return true;
        /*final Player p = (Player) cs;
        final Player t = this.plugin.getServer().getPlayer(args[0]);
        if (t == null || this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (t.equals(p)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You can't trade with yourself!");
            return true;
        }
        if (!Config.differentGamemodeTrade && t.getGameMode() != p.getGameMode()) {
            new FancyMessage("You cannot trade with ").color(MessageColor.NEGATIVE.cc()).then(t.getName()).color(MessageColor.NEUTRAL.cc()).formattedTooltip(RUtils.getPlayerTooltip(t)).then(" because they have a different gamemode than you!").color(MessageColor.NEGATIVE.cc()).send(cs);
            return true;
        }
        Inventory inv = CmdTrade.getTradeInv(p, t);
        if (inv != null) {
            new FancyMessage("Resumed trading with ").color(MessageColor.POSITIVE.cc()).then(t.getName()).color(MessageColor.NEUTRAL.cc()).formattedTooltip(RUtils.getPlayerTooltip(t)).then(".").color(MessageColor.POSITIVE.cc()).send(p);
            p.openInventory(inv);
            return true;
        }
        if (CmdTrade.tradedb.containsKey(t.getUniqueId())) {
            inv = this.plugin.getServer().createInventory(null, 36, "Trade");
            p.sendMessage(MessageColor.POSITIVE + "Opened trading interface.");
            p.openInventory(inv);
            t.openInventory(inv);
            final Map<UUID, UUID> trade = new HashMap<>();
            trade.put(p.getUniqueId(), t.getUniqueId());
            CmdTrade.trades.put(trade, inv);
            CmdTrade.tradedb.remove(t.getUniqueId());
            return true;
        } else {
            CmdTrade.sendTradeRequest(t, p);
            new FancyMessage("Sent a trade request to ").color(MessageColor.POSITIVE.cc()).then(t.getName()).color(MessageColor.NEUTRAL.cc()).formattedTooltip(RUtils.getPlayerTooltip(t)).then(".").color(MessageColor.POSITIVE.cc()).send(p);
            return true;
        }*/
    }
}
