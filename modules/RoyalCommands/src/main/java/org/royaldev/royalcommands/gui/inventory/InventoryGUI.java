package org.royaldev.royalcommands.gui.inventory;

import com.comphenix.attribute.Attributes;
import com.comphenix.attribute.Attributes.Attribute;
import com.comphenix.attribute.Attributes.Attribute.Builder;
import com.comphenix.attribute.Attributes.AttributeType;
import com.comphenix.attribute.Attributes.Operation;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryGUI {

    private final static String TAG_NAME = "IG:Tag";
    private final Inventory base;
    private final Map<UUID, ClickHandler> clickHandlers = new HashMap<>();

    public InventoryGUI() {
        this(45);
    }

    public InventoryGUI(final int size) {
        this.base = RoyalCommands.getInstance().getServer().createInventory(new GUIHolder(this), size);
    }

    public InventoryGUI(final Inventory base) {
        this.base = base;
    }

    private UUID getTag(final ItemStack is) {
        if (is == null) return null;
        final Attributes as = new Attributes(is);
        for (final Attribute a : as.values()) {
            if (!a.getName().equals(InventoryGUI.TAG_NAME)) continue;
            return a.getUUID();
        }
        return null;
    }

    private ItemStack tagItem(final ItemStack is, final UUID uuid) {
        final Builder b = Attributes.Attribute.newBuilder();
        final Attribute a = b.name(InventoryGUI.TAG_NAME).uuid(uuid).type(AttributeType.GENERIC_FOLLOW_RANGE).amount(0D).operation(Operation.ADD_NUMBER).build();
        final Attributes as = new Attributes(is);
        as.add(a);
        return as.getStack();
    }

    public void addItem(final ClickHandler clickHandler, final int x, final int y, final Material m, final String name, final String... lore) {
        final int slot = this.getSlot(x, y);
        if (slot > this.getBase().getSize() - 1) {
            throw new IllegalArgumentException("Location does not exist.");
        }
        final UUID uuid = UUID.randomUUID();
        final ItemStack is = this.tagItem(this.createItem(m, name, lore), uuid);
        this.getClickHandlers().put(uuid, clickHandler);
        this.getBase().setItem(slot, is);
    }

    public ItemStack createItem(final Material material, final String name, final String... lore) {
        return this.setItemMeta(new ItemStack(material), name, lore);
    }

    public Inventory getBase() {
        return this.base;
    }

    public ClickHandler getClickHandler(final ItemStack is) {
        final UUID uuid = this.getTag(is);
        return uuid == null ? null : this.getClickHandlers().get(uuid);
    }

    public Map<UUID, ClickHandler> getClickHandlers() {
        return this.clickHandlers;
    }

    public ItemStack getItemStack(final UUID uuid) {
        if (uuid == null) return null;
        for (final ItemStack is : this.getBase()) {
            if (!uuid.equals(this.getTag(is))) continue;
            return is;
        }
        return null;
    }

    public int getSlot(final int x, final int y) {
        return ((y - 1) * 9) + (x - 1);
    }

    public void replaceItemStack(final UUID uuid, final ItemStack replacement) {
        if (uuid == null) throw new IllegalArgumentException("UUID cannot be null");
        for (int i = 0; i < this.getBase().getSize(); i++) {
            final ItemStack is = this.getBase().getItem(i);
            if (!uuid.equals(this.getTag(is))) continue;
            this.getBase().setItem(i, this.tagItem(replacement, uuid));
            break;
        }
    }

    public ItemStack setItemMeta(final ItemStack is, final String name, final String... lore) {
        final ItemMeta im = is.getItemMeta();
        if (name != null) im.setDisplayName(name);
        if (lore != null) im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return is;
    }

    public void setName(final UUID uuid, final String name) {
        final ItemStack is = this.getItemStack(uuid);
        if (is == null) throw new IllegalArgumentException("No such ItemStack UUID found");
        this.replaceItemStack(uuid, this.setItemMeta(is, name, null));
    }

    public void setName(final ItemStack is, final String name) {
        this.setName(this.getTag(is), name);
    }

    public void updateItemStack(final ItemStack updated) {
        this.replaceItemStack(this.getTag(updated), updated);
    }
}
