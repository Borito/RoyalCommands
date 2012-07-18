package org.royaldev.royalcommands.serializable;

import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.io.Serializable;

@SuppressWarnings("unused")
public class SerializableCraftEnchantment implements Serializable {

    final int id;

    public SerializableCraftEnchantment(CraftEnchantment ce) {
        id = ce.getId();
    }

    public SerializableCraftEnchantment(Enchantment ce) {
        id = ce.getId();
    }

    public Enchantment getEnchantment() {
        return Enchantment.getById(id);
    }

}
