package org.royaldev.royalcommands.serializable;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
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
    /**
     * Material data on this ItemStack - defaults to 0
     */
    private byte materialData = 0;
    /**
     * The tag on the ItemStack, commonly used for written book data - defaults to null
     */
    private SerializableNBTTagCompound tag = null;

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
        materialData = i.getData().getData();
        tag = new SerializableNBTTagCompound(((CraftItemStack) i).getHandle().getTag());
    }

    /**
     * Returns a SIS to an ItemStack (keeps enchantments, amount, data, and durability)
     *
     * @return Original ItemStack
     */
    public ItemStack getItemStack() {
        CraftItemStack cis = new CraftItemStack(type, amount, durability, materialData);
        cis.setDurability(durability);
        for (SerializableCraftEnchantment sce : enchantments.keySet())
            cis.addUnsafeEnchantment(sce.getEnchantment(), enchantments.get(sce));
        if (tag != null) cis.getHandle().setTag(tag.getNBTTagCompound());
        return cis;
    }

}
