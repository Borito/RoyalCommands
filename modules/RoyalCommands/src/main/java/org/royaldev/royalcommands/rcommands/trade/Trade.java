package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.ClickHandler;
import org.royaldev.royalcommands.gui.inventory.GUIItem;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;
import org.royaldev.royalcommands.rcommands.trade.clickhandlers.AddPartyCommand;
import org.royaldev.royalcommands.rcommands.trade.clickhandlers.GiveHelpBook;
import org.royaldev.royalcommands.rcommands.trade.clickhandlers.RemindOtherParty;
import org.royaldev.royalcommands.rcommands.trade.clickhandlers.RemoveItem;
import org.royaldev.royalcommands.rcommands.trade.clickhandlers.ToggleTradeAcceptance;
import org.royaldev.royalcommands.rcommands.trade.guiitems.AddCommandItem;
import org.royaldev.royalcommands.rcommands.trade.guiitems.HelpItem;
import org.royaldev.royalcommands.rcommands.trade.guiitems.RemindItem;
import org.royaldev.royalcommands.rcommands.trade.guiitems.ToggleAcceptanceItem;
import org.royaldev.royalcommands.rcommands.trade.tradables.Tradable;
import org.royaldev.royalcommands.rcommands.trade.tradables.TradableCommand;
import org.royaldev.royalcommands.rcommands.trade.tradables.TradableItem;
import org.royaldev.royalcommands.tools.Pair;
import org.royaldev.royalcommands.tools.Vector2D;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
* FIXME:
* Trade accept button sometimes doesn't update until reopen (?)
 */

public class Trade {

    private static final Map<Pair<UUID, UUID>, Trade> trades = new HashMap<>();
    private final Map<Party, UUID> parties = new HashMap<>();
    private final Map<Party, Boolean> acceptances = new HashMap<>();
    private final InventoryGUI inventoryGUI;
    private final UUID acceptButtonUUID = UUID.randomUUID();

    public Trade(final UUID trader, final UUID tradee) {
        this.parties.put(Party.TRADER, trader);
        this.parties.put(Party.TRADEE, tradee);
        this.acceptances.put(Party.TRADER, false);
        this.acceptances.put(Party.TRADEE, false);
        this.inventoryGUI = this.makeInventoryGUI();
        Trade.trades.put(new Pair<>(trader, tradee), this);
    }

    /**
     * Gets the trade that matches both UUIDs. The order they are provided in does not matter.
     *
     * @param first  First UUID to match
     * @param second Second UUID to match
     * @return A trade or null if none has been initiated
     */
    public static Trade getTradeFor(final UUID first, final UUID second) {
        for (final Map.Entry<Pair<UUID, UUID>, Trade> entry : Trade.trades.entrySet()) {
            final Pair<UUID, UUID> pair = entry.getKey();
            if ((pair.getFirst().equals(first) || pair.getSecond().equals(first)) && (pair.getFirst().equals(second) || pair.getSecond().equals(second))) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Gets all the trades involving the given UUID.
     *
     * @param uuid UUID to search for
     * @return A trade or null if none has been initiated
     */
    public static List<Trade> getTradesFor(final UUID uuid) {
        final List<Trade> trades = new ArrayList<>();
        for (final Map.Entry<Pair<UUID, UUID>, Trade> entry : Trade.trades.entrySet()) {
            final Pair<UUID, UUID> pair = entry.getKey();
            if (pair.getFirst().equals(uuid) || pair.getSecond().equals(uuid)) {
                trades.add(entry.getValue());
            }
        }
        return trades;
    }

    private void destroy() {
        for (final Map.Entry<Pair<UUID, UUID>, Trade> entry : Trade.trades.entrySet()) {
            if (!entry.getValue().equals(this)) continue;
            Trade.trades.remove(entry.getKey());
            break;
        }
    }

    private String getTradeName() {
        final String traderName = RPlayer.getRPlayer(this.get(Party.TRADER)).getName();
        final String tradeeName = RPlayer.getRPlayer(this.get(Party.TRADEE)).getName();
        final String tradeName = traderName + "/" + tradeeName;
        return tradeName.length() > 32 ? tradeName.substring(0, 32) : tradeName;
    }

    private InventoryGUI makeInventoryGUI() {
        if (this.getInventoryGUI() != null) return null;
        final InventoryGUI inventoryGUI = new InventoryGUI(this.getTradeName());
        inventoryGUI.addItem(
            new AddPartyCommand(this, Party.TRADER),
            5, 1,
            new AddCommandItem(this, Party.TRADER)
        );
        inventoryGUI.addItem(
            new AddPartyCommand(this, Party.TRADEE),
            5, 2,
            new AddCommandItem(this, Party.TRADEE)
        );
        inventoryGUI.addItem(
            this.acceptButtonUUID,
            new ToggleTradeAcceptance(this),
            5, 3,
            new ToggleAcceptanceItem(this)
        );
        inventoryGUI.addItem(
            new RemindOtherParty(this),
            5, 4,
            new RemindItem()
        );
        inventoryGUI.addItem(
            new GiveHelpBook(),
            5, 5,
            new HelpItem()
        );
        return inventoryGUI;
    }

    private List<Tradable> processSpecialItems(final ItemStack is) {
        final List<Tradable> trades = new ArrayList<>();
        if (this.getInventoryGUI().getClickHandler(is) instanceof RemoveItem) { // TODO: Do this better
            // TODO: Extract logic
            final ItemMeta im = is.getItemMeta();
            for (final String lore : im.getLore()) {
                if (!lore.startsWith(MessageColor.NEUTRAL + "/")) continue;
                trades.add(new TradableCommand(this, lore.substring((MessageColor.NEUTRAL + "/").length())));
            }
            return trades;
        }
        return null;
    }

    private void sendMessageToAll(final String message) {
        final Player trader = this.getPlayer(Party.TRADER);
        final Player tradee = this.getPlayer(Party.TRADEE);
        if (trader != null) trader.sendMessage(message);
        if (tradee != null) tradee.sendMessage(message);
    }

    private boolean trade() {
        final List<Tradable> tradeeTradables = this.getTradeablesFor(Party.TRADEE); // tradee has offered
        final List<Tradable> traderTradables = this.getTradeablesFor(Party.TRADER); // trader has offered
        if (!this.areBothPartiesOnline()) return false;
        boolean allSuccessful = true;
        for (final Tradable trade : tradeeTradables) {
            final boolean success = trade.trade(Party.TRADEE, Party.TRADER);
            if (success) trade.destroy();
            if (allSuccessful) allSuccessful = success;
        }
        for (final Tradable trade : traderTradables) {
            final boolean success = trade.trade(Party.TRADER, Party.TRADEE);
            if (success) trade.destroy();
            if (allSuccessful) allSuccessful = success;
        }
        if (allSuccessful) {
            this.destroy();
        } else {
            this.sendMessageToAll(MessageColor.NEGATIVE + "The trade did not successfully complete. Please check the trade to see any remaining items.");
            this.setAcceptance(Party.TRADER, false);
            this.setAcceptance(Party.TRADEE, false);
        }
        Party.TRADER.closeTrade(this);
        Party.TRADEE.closeTrade(this);
        return allSuccessful;
    }

    public void addItem(final ClickHandler clickHandler, final Party party, final GUIItem guiItem) {
        final int freeSlot = party.getNextFreeSlot(this.getInventoryGUI().getBase());
        if (freeSlot == -1) return; // TODO: Throw?
        final Vector2D xy = this.getInventoryGUI().getXYFromSlot(freeSlot);
        this.getInventoryGUI().addItem(clickHandler, xy.getX(), xy.getY(), guiItem);
    }

    public boolean areBothPartiesOnline() {
        return this.getPlayer(Party.TRADEE) != null && this.getPlayer(Party.TRADER) != null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Trade)) return false;
        final Trade t = (Trade) obj;
        return this.getInventoryGUI().equals(t.getInventoryGUI()) && this.parties.equals(t.parties);
    }

    public UUID get(final Party party) {
        return this.parties.get(party);
    }

    public Party get(final UUID uuid) {
        for (final Map.Entry<Party, UUID> entry : this.parties.entrySet()) {
            if (!entry.getValue().equals(uuid)) continue;
            return entry.getKey();
        }
        return null;
    }

    public InventoryGUI getInventoryGUI() {
        return this.inventoryGUI;
    }

    public String getName(final Party party) {
        return RPlayer.getRPlayer(this.get(party)).getName();
    }

    public Player getPlayer(final Party party) {
        return RPlayer.getRPlayer(this.get(party)).getPlayer();
    }

    public String getTradeStatusLore(final Party party) {
        final boolean accepted = this.hasAccepted(party);
        final StringBuilder sb = new StringBuilder().append(MessageColor.NEUTRAL).append(this.getName(party)).append(": ");
        sb.append(accepted ? MessageColor.POSITIVE : MessageColor.NEGATIVE).append(accepted ? "Accepted" : "Declined");
        return sb.toString();
    }

    public List<Tradable> getTradeablesFor(final Party party) {
        final List<Tradable> items = new ArrayList<>();
        final Inventory base = this.getInventoryGUI().getBase();
        for (int i = 0; i < base.getSize(); i++) {
            if (!party.canAccessSlot(i)) continue;
            final ItemStack item = base.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            final List<Tradable> special = this.processSpecialItems(item);
            if (special == null) items.add(new TradableItem(this, item));
            else items.addAll(special);
        }
        return items;
    }

    public boolean hasAccepted(final Party party) {
        return this.acceptances.get(party);
    }

    public boolean haveBothAccepted() {
        return this.hasAccepted(Party.TRADER) && this.hasAccepted(Party.TRADEE);
    }

    public boolean setAcceptance(final Party party, final boolean acceptance) {
        this.acceptances.put(party, acceptance);
        this.updateAcceptButton();
        if (this.haveBothAccepted()) this.trade();
        return acceptance;
    }

    public void showInventoryGUI(final UUID uuid) {
        final Party party = this.get(uuid);
        if (party == null) throw new IllegalArgumentException("No such UUID");
        final Player p = RPlayer.getRPlayer(uuid).getPlayer();
        if (p == null) return;
        p.openInventory(this.getInventoryGUI().getBase());
    }

    public boolean toggleAcceptance(final Party party) {
        return this.setAcceptance(party, !this.acceptances.get(party));
    }

    public boolean toggleAcceptance(final UUID uuid) {
        final Party party = this.get(uuid);
        if (party == null) throw new IllegalArgumentException("No such UUID");
        return this.toggleAcceptance(party);
    }

    public void updateAcceptButton() {
        final ItemStack updateButton = this.getInventoryGUI().getItemStack(this.acceptButtonUUID);
        final List<String> lore = new ToggleAcceptanceItem(this).getLore();
        this.getInventoryGUI().updateItemStack(this.getInventoryGUI().setItemMeta(
            updateButton,
            null,
            lore.toArray(new String[lore.size()])
        ));
    }
}
