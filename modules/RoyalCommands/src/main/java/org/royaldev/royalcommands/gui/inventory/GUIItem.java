package org.royaldev.royalcommands.gui.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Represents an item in an {@link InventoryGUI}.
 */
public abstract class GUIItem {

    private Material material;
    private String name;
    private List<String> lore;

    public GUIItem(final Material material, final String name, final List<String> lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    /**
     * Gets the lore for this item. May be null.
     *
     * @return List or null
     */
    public List<String> getLore() {
        return this.lore;
    }

    /**
     * Sets the lore of this item. Old lore will be overridden.
     *
     * @param lore Lore to set
     */
    public void setLore(final List<String> lore) {
        this.lore = lore;
    }

    /**
     * Gets the Material of this item. May be null.
     *
     * @return Material or null
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Sets the Material of this item.
     *
     * @param material Material to set
     */
    public void setMaterial(final Material material) {
        this.material = material;
    }

    /**
     * Gets the custom name of this item. May be null.
     *
     * @return String or null
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the custom name of this item.
     *
     * @param name Name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Makes an ItemStack with the given Material, custom name, and lore.
     *
     * @return ItemStack
     */
    public ItemStack makeItemStack() {
        final ItemStack is = new ItemStack(this.getMaterial());
        final ItemMeta im = is.getItemMeta();
        im.setDisplayName(this.getName());
        im.setLore(this.getLore());
        is.setItemMeta(im);
        return is;
    }
}
