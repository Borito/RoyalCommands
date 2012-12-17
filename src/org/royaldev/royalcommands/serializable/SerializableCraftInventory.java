package org.royaldev.royalcommands.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_5.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
/**
 * Serializable CraftInventory
 * <p/>
 * Very useful for saving Inventories to a file.
 *
 * @author jkcclemens
 * @see SerializableItemStack
 * @see SerializableCraftEnchantment
 * @since 0.2.4pre
 */
public class SerializableCraftInventory implements Serializable {

    static final long serialVersionUID = 9206795899161571450L;

    /**
     * Name of the inventory
     */
    private final String name;
    /**
     * List of SISs in the inventory
     */
    private List<SerializableItemStack> contents = new ArrayList<SerializableItemStack>();
    /**
     * Number of slots in the inventory
     */
    private final int size;

    /**
     * Serializable CraftInventory for saving to files.
     *
     * @param i CraftInventory to base off of
     */
    public SerializableCraftInventory(CraftInventory i) {
        name = i.getName();
        for (ItemStack is : i.getContents()) contents.add(new SerializableItemStack(is));
        size = i.getSize();
    }

    /**
     * Serializable CraftInventory for saving to files.
     *
     * @param i Inventory to base off of
     */
    public SerializableCraftInventory(Inventory i) {
        name = i.getName();
        for (ItemStack is : i.getContents()) contents.add(new SerializableItemStack(is));
        size = i.getSize();
    }

    /**
     * Serializable CraftInventory for saving to files.
     *
     * @param name     Name of inventory
     * @param contents Contents of inventory
     * @param size     Size of inventory
     */
    public SerializableCraftInventory(String name, ItemStack[] contents, int size) {
        this.name = name;
        for (ItemStack is : contents) this.contents.add(new SerializableItemStack(is));
        this.size = size;
    }

    /**
     * Sets the contents of the inventory.
     *
     * @param contents New contents
     */
    public void setContents(ItemStack[] contents) {
        this.contents.clear();
        for (ItemStack is : contents) this.contents.add(new SerializableItemStack(is));
    }

    /**
     * Gets the size of inventory
     *
     * @return Size
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets name of inventory
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the contents of the inventory
     *
     * @return Contents
     */
    public ItemStack[] getContents() {
        ItemStack[] is = new ItemStack[contents.size()];
        for (int i = 0; i < contents.size(); i++) is[i] = contents.get(i).getItemStack();
        return is;
    }

    /**
     * Clears the inventory
     */
    public void clear() {
        ItemStack[] content = getContents();
        for (ItemStack is : content) is.setType(Material.AIR);
        setContents(content);
    }

    /**
     * Returns a CraftInventory out of a SCI
     *
     * @return Converted CraftInventory
     */
    public CraftInventory getCraftInventory() {
        Inventory i = Bukkit.createInventory(null, size, name);
        ItemStack[] is = new ItemStack[contents.size()];
        for (int x = 0; x < contents.size(); x++) is[x] = contents.get(x).getItemStack();
        i.setContents(is);
        return (CraftInventory) i;
    }

    /**
     * Returns an Inventory out of a SCI
     *
     * @return Converted Inventory
     */
    public Inventory getInventory() {
        Inventory i = Bukkit.createInventory(null, size, name);
        ItemStack[] is = new ItemStack[contents.size()];
        for (int x = 0; x < contents.size(); x++) is[x] = contents.get(x).getItemStack();
        i.setContents(is);
        return i;
    }

}
