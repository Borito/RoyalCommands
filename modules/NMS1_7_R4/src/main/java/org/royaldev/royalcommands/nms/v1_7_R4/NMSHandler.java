package org.royaldev.royalcommands.nms.v1_7_R4;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.nms.api.NMSFace;

public class NMSHandler implements NMSFace {
    @Override
    public boolean hasSupport() {
        return true;
    }

    @Override
    public String getVersion() {
        return "v1_7_R4";
    }

    @Override
    public int getPing(Player p) {
        if (p instanceof CraftPlayer) return ((CraftPlayer) p).getHandle().ping;
        throw new IllegalArgumentException("Player was not a CraftPlayer!");
    }

    /*
    Saving this for when I need it. Don't feel like remaking it.
    public void setInventoryName(Inventory i, String name) {
        if (!(i instanceof CraftInventoryCustom)) throw new IllegalArgumentException("Not a CraftInventoryCustom");
        final CraftInventoryCustom cic = (CraftInventoryCustom) i;
        final IInventory ii = cic.getInventory();
        final Class<?> minecraftInventoryClass;
        try {
            minecraftInventoryClass = Class.forName("org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory.MinecraftInventory");
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Could not find MinecraftInventory class.");
        }
        if (!IInventory.class.isAssignableFrom(minecraftInventoryClass)) {
            throw new IllegalArgumentException("MinecraftInventory did not implement IInventory");
        }
        final Object mi = minecraftInventoryClass.cast(ii);
        try {
            final Field f = mi.getClass().getDeclaredField("title");
            f.setAccessible(true);
            f.set(mi, name);
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException("MinecraftInventory did not have title field.");
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }*/

}
