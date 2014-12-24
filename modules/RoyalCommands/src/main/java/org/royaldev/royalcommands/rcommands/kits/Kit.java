package org.royaldev.royalcommands.rcommands.kits;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.shaded.com.sk89q.util.config.ConfigurationNode;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private final String name;
    private final List<ItemStack> items;
    private final long cooldown;

    public Kit(final String name, final ConfigurationNode kit) {
        this.name = name;
        this.items = this.getItemStacks(kit.getNodeList("items", new ArrayList<ConfigurationNode>()));
        this.cooldown = kit.getLong("cooldown", -1L);
    }

    private ItemStack addEnchantments(final ItemStack is, final List<ConfigurationNode> enchantments) {
        for (final ConfigurationNode enchantment : enchantments) {
            final Enchantment realEnchantment = this.getEnchantment(enchantment);
            if (realEnchantment == null) continue;
            is.addEnchantment(realEnchantment, enchantment.getInt("level", 1));
        }
        return is;
    }

    private Enchantment getEnchantment(final ConfigurationNode enchantment) {
        return Enchantment.getByName(enchantment.getString("name"));
    }

    private ItemStack getItemStack(final ConfigurationNode item) {
        final ItemStack is = RoyalCommands.inm.getItemStackFromAlias(item.getString("type"));
        if (is == null) return null;
        is.setAmount(item.getInt("amount", 1));
        is.setDurability(item.getShort("damage", (short) 0));
        final ItemMeta im = is.getItemMeta();
        im.setDisplayName(item.getString("name"));
        im.setLore(item.getStringList("lore", new ArrayList<String>()));
        is.setItemMeta(im);
        this.addEnchantments(is, item.getNodeList("enchantments", new ArrayList<ConfigurationNode>()));
        return is;
    }

    private List<ItemStack> getItemStacks(final List<ConfigurationNode> items) {
        final List<ItemStack> itemStacks = new ArrayList<>();
        for (final ConfigurationNode item : items) {
            final ItemStack is = this.getItemStack(item);
            if (is == null) continue;
            itemStacks.add(is);
        }
        return itemStacks;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public List<ItemStack> getItems() {
        return new ArrayList<>(this.items);
    }

    public String getName() {
        return this.name;
    }
}
