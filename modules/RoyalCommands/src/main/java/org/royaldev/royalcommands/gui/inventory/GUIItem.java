package org.royaldev.royalcommands.gui.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class GUIItem {

    private Material material;
    private String name;
    private List<String> lore;

    public GUIItem(final Material material, final String name, final List<String> lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public void setLore(final List<String> lore) {
        this.lore = lore;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(final Material material) {
        this.material = material;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ItemStack makeItemStack() {
        final ItemStack is = new ItemStack(this.getMaterial());
        final ItemMeta im = is.getItemMeta();
        im.setDisplayName(this.getName());
        im.setLore(this.getLore());
        is.setItemMeta(im);
        return is;
    }
}
