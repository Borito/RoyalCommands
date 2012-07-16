package org.royaldev.royalcommands.serializable;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializableItemStack implements Serializable {

    private int amount = 0;
    private short durability = 0;
    private int type = 0;
    private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
    private byte materialData = 0;

    /**
     * Serializable ItemStack for saving to files.
     *
     * @param i ItemStack to convert
     */
    public SerializableItemStack(ItemStack i) {
        if (i == null) return;
        amount = i.getAmount();
        durability = i.getDurability();
        enchantments = i.getEnchantments();
        type = i.getTypeId();
        materialData = i.getData().getData();
    }

    /**
     * Returns a SIS to an ItemStack (keeps enchantments, amount, data, and durability)
     *
     * @return Original ItemStack
     */
    public ItemStack getItemStack() {
        ItemStack i = new ItemStack(type, amount, durability, materialData);
        i.addUnsafeEnchantments(enchantments);
        return i;
    }

}
