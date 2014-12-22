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
import org.royaldev.royalcommands.tools.Vector2D;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryGUI {

    private static final String TAG_NAME = "IG:Tag";
    private final Inventory base;
    private final Map<UUID, ClickHandler> clickHandlers = new HashMap<>();
    private final UUID identifier = UUID.randomUUID();

    public InventoryGUI(final String name) {
        this(name, 45);
    }

    public InventoryGUI(final String name, final int size) {
        this.base = RoyalCommands.getInstance().getServer().createInventory(new GUIHolder(this), size, name);
    }

    public InventoryGUI(final Inventory base) {
        this.base = base;
    }

    private ItemStack tagItem(final ItemStack is, final UUID uuid) {
        if (is == null || is.getType() == Material.AIR) return is;
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

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof InventoryGUI)) return false;
        final InventoryGUI other = (InventoryGUI) obj;
        return other.getIdentifier().equals(this.getIdentifier());
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

    public UUID getIdentifier() {
        return this.identifier;
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

    public UUID getTag(final ItemStack is) {
        if (is == null) return null;
        final Attributes as = new Attributes(is);
        for (final Attribute a : as.values()) {
            if (!a.getName().equals(InventoryGUI.TAG_NAME)) continue;
            return a.getUUID();
        }
        return null;
    }

    public Vector2D getXYFromSlot(final int slot) {
        final int xraw = slot % 9;
        final int yraw = (slot - xraw) / 9;
        return new Vector2D(xraw + 1, yraw + 1);
    }

    public void replaceItemStack(final ItemStack original, final ItemStack replacement) {
        this.replaceItemStack(this.getTag(original), replacement);
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
