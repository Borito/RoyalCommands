package org.royaldev.royalcommands.rcommands.trade;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.InventoryGUI;
import org.royaldev.royalcommands.rcommands.trade.clickhandlers.ToggleTradeAcceptance;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO: Shadow items (no real items stored in trade)

public class Trade {

    private final Map<Party, UUID> parties = new HashMap<>();
    private final Map<Party, Boolean> acceptances = new HashMap<>();
    private final InventoryGUI inventoryGUI;

    public Trade(final UUID trader, final UUID tradee) {
        this.parties.put(Party.TRADER, trader);
        this.parties.put(Party.TRADEE, tradee);
        this.acceptances.put(Party.TRADER, false);
        this.acceptances.put(Party.TRADEE, false);
        this.inventoryGUI = this.makeInventoryGUI();
    }

    private String getName(final Party party) {
        return RPlayer.getRPlayer(this.get(party)).getName();
    }

    public String getTradeStatusLore(final Party party) {
        final boolean accepted = this.hasAccepted(party);
        final StringBuilder sb = new StringBuilder().append(MessageColor.NEUTRAL).append(this.getName(party)).append(": " );
        sb.append(accepted ? MessageColor.POSITIVE : MessageColor.NEGATIVE).append(accepted ? "Accepted" : "Declined");
        return sb.toString();
    }

    private InventoryGUI makeInventoryGUI() {
        if (this.getInventoryGUI() != null) return null;
        final InventoryGUI inventoryGUI = new InventoryGUI();
        inventoryGUI.addItem(
            new ToggleTradeAcceptance(this),
            5, 3,
            Material.BOOK,
            MessageColor.RESET + "Toggle Acceptance",
            MessageColor.NEUTRAL + "This toggles your acceptance of the trade.",
            MessageColor.NEUTRAL + "Once both sides have accepted, the trade will process.",
            this.getTradeStatusLore(Party.TRADER),
            this.getTradeStatusLore(Party.TRADEE)

        );
        return inventoryGUI;
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

    public boolean hasAccepted(final Party party) {
        return this.acceptances.get(party);
    }

    public void showInventoryGUI(final UUID uuid) {
        final Party party = this.get(uuid);
        if (party == null) throw new IllegalArgumentException("No such UUID");
        final Player p = RPlayer.getRPlayer(uuid).getPlayer();
        if (p == null) return;
        p.openInventory(this.getInventoryGUI().getBase());
    }

    public boolean toggleAcceptance(final UUID uuid) {
        final Party party = this.get(uuid);
        if (party == null) throw new IllegalArgumentException("No such UUID");
        final boolean acceptance = !this.acceptances.get(party);
        this.acceptances.put(party, acceptance);
        return acceptance;
    }

    public static enum Party {
        TRADEE,
        TRADER
    }
}
