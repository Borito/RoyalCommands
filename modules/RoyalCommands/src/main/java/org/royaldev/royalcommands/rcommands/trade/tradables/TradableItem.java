package org.royaldev.royalcommands.rcommands.trade.tradables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.rcommands.trade.Party;
import org.royaldev.royalcommands.rcommands.trade.Trade;

import java.util.Map;

public class TradableItem implements Tradable {

    private final Trade trade;
    private final ItemStack stack;
    private Map<Integer, ItemStack> leftOver = null;

    public TradableItem(final Trade trade, final ItemStack stack) {
        this.trade = trade;
        this.stack = stack;
    }

    /**
     * Gets a clone of the item to be traded.
     *
     * @return Clone of ItemStack
     */
    public ItemStack getItem() {
        return this.stack.clone();
    }

    /**
     * Gets any items left over from after the trade. This will be null before {@link #trade(Party, Party)} is called.
     * After, it will be a Map. If it is empty ({@link java.util.Map#isEmpty()}), no items were left over.
     *
     * @return Map or null
     */
    public Map<Integer, ItemStack> getLeftOver() {
        return this.leftOver;
    }

    /**
     * Trades an item.
     *
     * @param from Party providing the item.
     * @param to   Party receiving the item.
     * @return If the trade was successful
     */
    @Override
    public boolean trade(final Party from, final Party to) {
        final Player p = this.trade.getPlayer(to);
        if (p == null) return false;
        this.leftOver = p.getInventory().addItem(this.stack);
        return this.leftOver.isEmpty();
    }
}
