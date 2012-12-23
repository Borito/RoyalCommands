package org.royaldev.royalcommands.serializable;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serializable ItemStack
 * <p/>
 * Needed for SerializableCraftInventory
 *
 * @author jkcclemens
 * @see SerializableCraftInventory
 * @see SerializableCraftEnchantment
 * @since 0.2.4pre
 */
public class SerializableItemStack implements Serializable {

    static final long serialVersionUID = -5056202252395200062L;

    /**
     * Amount of items in the ItemStack - defaults to 0
     */
    private int amount = 0;
    /**
     * Durability, or damage, of the items in the ItemStack - defaults to 0
     */
    private short durability = 0;
    /**
     * Item ID of the ItemStack - defaults to 0
     */
    private int type = 0;
    /**
     * Map containing enchantments and their respective levels on this ItemStack - defaults to an empty map
     */
    private Map<SerializableCraftEnchantment, Integer> enchantments = new HashMap<SerializableCraftEnchantment, Integer>();
    private List<String> bookPages = null;
    private String bookTitle = null;
    private String bookAuthor = null;
    private final List<String> lores = new ArrayList<String>();
    private String displayName = null;

    /**
     * Serializable ItemStack for saving to files.
     *
     * @param i ItemStack to convert
     */
    public SerializableItemStack(ItemStack i) {
        if (i == null) return;
        amount = i.getAmount();
        durability = i.getDurability();
        for (Enchantment e : i.getEnchantments().keySet())
            enchantments.put(new SerializableCraftEnchantment(e), i.getEnchantments().get(e));
        type = i.getTypeId();
        ItemMeta im = i.getItemMeta();
        if (im instanceof BookMeta) {
            BookMeta bm = (BookMeta) im;
            bookPages = bm.getPages();
            bookAuthor = bm.getAuthor();
            bookTitle = bm.getTitle();
        }
        lores.addAll(im.getLore());
        displayName = im.getDisplayName();
    }

    /**
     * Returns a SIS to an ItemStack (keeps enchantments, amount, data, and durability)
     *
     * @return Original ItemStack
     */
    public ItemStack getItemStack() {
        ItemStack is = new ItemStack(type, amount, durability);
        is.setDurability(durability);
        for (SerializableCraftEnchantment sce : enchantments.keySet())
            is.addUnsafeEnchantment(sce.getEnchantment(), enchantments.get(sce));
        ItemMeta im = is.getItemMeta();
        if (im instanceof BookMeta) {
            BookMeta bm = (BookMeta) im;
            if (bookTitle != null) bm.setTitle(bookTitle);
            if (bookAuthor != null) bm.setAuthor(bookAuthor);
            if (bookPages != null) bm.setPages(bookPages);
        }
        im.setDisplayName(displayName);
        im.setLore(lores);
        is.setItemMeta(im);
        return is;
    }

}
