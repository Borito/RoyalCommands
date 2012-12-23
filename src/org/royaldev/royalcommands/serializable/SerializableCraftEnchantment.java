package org.royaldev.royalcommands.serializable;

import org.bukkit.craftbukkit.v1_4_6.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.io.Serializable;

@SuppressWarnings("unused")
/**
 * Serializable CraftEnchantment
 * <p/>
 * Needed for SerializableCraftInventory
 *
 * @author jkcclemens
 * @see SerializableItemStack
 * @see SerializableCraftInventory
 * @since 0.2.4pre
 */
public class SerializableCraftEnchantment implements Serializable {

    static final long serialVersionUID = 6021879788394423350L;

    /**
     * ID of the enchantment
     */
    final int id;

    /**
     * Serializable CraftEnchantment for saving to files
     *
     * @param ce CraftEnchantment to base off of
     */
    public SerializableCraftEnchantment(CraftEnchantment ce) {
        id = ce.getId();
    }

    /**
     * Serializable CraftEnchantment for saving to files
     *
     * @param ce Enchantment to base off of
     */
    public SerializableCraftEnchantment(Enchantment ce) {
        id = ce.getId();
    }

    /**
     * Returns an Enchantment out of a SCE
     *
     * @return Converted Enchantment
     */
    public Enchantment getEnchantment() {
        return Enchantment.getById(id);
    }

}
