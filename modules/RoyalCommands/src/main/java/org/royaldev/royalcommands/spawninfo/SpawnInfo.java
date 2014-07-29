package org.royaldev.royalcommands.spawninfo;

import com.comphenix.attribute.AttributeStorage;
import com.comphenix.attribute.Attributes;
import com.comphenix.attribute.NbtFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A class used in item-spawning to determine and store information about the spawn status of an item.
 * <br/>
 * This class is {@link Serializable}, but it can also be stored as a String, as seen here:
 * {@link org.royaldev.royalcommands.spawninfo.SpawnInfo#toString()}.
 *
 * @author jkcclemens
 * @since 3.2.1
 */
public class SpawnInfo implements Serializable {
    private static final long serialVersionUID = 3232013L;
    /**
     * List of <em>spawned</em> components used in the creation of this item.
     */
    private final List<String> components = new ArrayList<>();
    /**
     * Name of player that spawned this item. If item is not spawned, this should be null. If it has no spawner, this
     * should be null.
     */
    private String spawner;
    /**
     * Whether the item is spawned or not.
     */
    private boolean spawned;
    /**
     * Whether the item was made with spawned components.
     */
    private boolean hasComponents;

    /**
     * Constructs a SpawnInfo object assuming that the item was not spawned.
     */
    public SpawnInfo() {
        this.spawner = null;
        this.spawned = false;
        this.hasComponents = false;
    }

    /**
     * Constructs a SpawnInfo object from a stored String.
     *
     * @param stored String to restore SpawnInfo from
     * @throws IllegalArgumentException If <code>stored</code> is null or invalid
     * @see org.royaldev.royalcommands.spawninfo.SpawnInfo#toString()
     */
    public SpawnInfo(final String stored) {
        if (stored == null) throw new IllegalArgumentException("String cannot be null!");
        final String[] splitStored = stored.split("/");
        if (splitStored.length < 4) throw new IllegalArgumentException("Invalid stored string!");
        this.spawner = splitStored[1];
        this.spawned = splitStored[0].equalsIgnoreCase("true");
        this.hasComponents = splitStored[2].equalsIgnoreCase("true");
        if (splitStored[3].startsWith("[") && splitStored[3].endsWith("]"))
            this.components.addAll(Arrays.asList(splitStored[3].substring(1, splitStored[3].length() - 1).split(", ")));
    }

    /**
     * Constructs a SpawnInfo object, assigning values to "spawner" and "spawned."
     *
     * @param spawner Name of player that spawned the item
     * @param spawned If the item is spawned
     */
    public SpawnInfo(final String spawner, final boolean spawned) {
        this.spawner = spawner;
        this.spawned = spawned;
        this.hasComponents = false;
    }

    /**
     * Constructs a SpawnInfo object, assigning default values.
     *
     * @param spawner       Name of player that spawned item
     * @param spawned       If the item is spawned
     * @param hasComponents If the item was made with spawned items
     * @param components    The spawned items the item was made with
     */
    public SpawnInfo(final String spawner, final boolean spawned, boolean hasComponents, Collection<String> components) {
        this.spawner = spawner;
        this.spawned = spawned;
        this.hasComponents = hasComponents;
        this.components.addAll(components);
    }

    /**
     * Gets the <em>spawned</em> components used to make this item. If there are none, an empty list will be returned.
     * Never returns null.
     *
     * @return List of spawned components; never null
     */
    public List<String> getComponents() {
        return components;
    }

    /**
     * Gets the name of the player that spawned this item. <strong>May be null</strong> if the item was not spawned or
     * has no spawner.
     *
     * @return String or null
     */
    public String getSpawner() {
        return spawner;
    }

    /**
     * Sets the name of the player that spawned this item. Use null if the item was not spawned or has no spawner.
     *
     * @param spawner Name of player or null
     */
    public void setSpawner(String spawner) {
        this.spawner = spawner;
    }

    /**
     * Returns if the item was made with spawned components.
     *
     * @return boolean
     */
    public boolean hasComponents() {
        return hasComponents;
    }

    /**
     * Returns if the item is spawned.
     *
     * @return boolean
     */
    public boolean isSpawned() {
        return spawned;
    }

    /**
     * Sets if the item is spawned.
     *
     * @param spawned true if it was spawned, false if not
     */
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    /**
     * Sets if the item was made with spawned components.
     *
     * @param hasComponents true if made with spawned components, false if not
     */
    public void setHasComponents(boolean hasComponents) {
        this.hasComponents = hasComponents;
    }

    /**
     * Gets the SpawnInfo object as a String. This String can be used to reconstruct the same SpawnInfo object.
     *
     * @return String representing SpawnInfo object
     * @see SpawnInfo#SpawnInfo(String)
     */
    @Override
    public String toString() {
        return String.valueOf(this.spawned) + "/" + this.spawner + "/" + this.hasComponents + "/" + this.components.toString();
    }

    public static class SpawnInfoManager {
        private final static UUID uuid = UUID.fromString("553ade7d-86cd-469e-a4ff-c6fbb564d961");
        private final static UUID defaultUUID = UUID.fromString("4f0925a7-abf4-4a61-8c81-2028d453ff92");
        private final static Map<Material, Attributes.Attribute> defaults = new HashMap<>();

        static {
            try {
                defaults.put(Material.DIAMOND_AXE, createAttribute(6D, defaultUUID));
                defaults.put(Material.DIAMOND_PICKAXE, createAttribute(5D, defaultUUID));
                defaults.put(Material.DIAMOND_SPADE, createAttribute(4D, defaultUUID));
                defaults.put(Material.DIAMOND_SWORD, createAttribute(7D, defaultUUID));
                defaults.put(Material.GOLD_AXE, createAttribute(3D, defaultUUID));
                defaults.put(Material.GOLD_PICKAXE, createAttribute(2D, defaultUUID));
                defaults.put(Material.GOLD_SPADE, createAttribute(1D, defaultUUID));
                defaults.put(Material.GOLD_SWORD, createAttribute(4D, defaultUUID));
                defaults.put(Material.IRON_AXE, createAttribute(5D, defaultUUID));
                defaults.put(Material.IRON_PICKAXE, createAttribute(4D, defaultUUID));
                defaults.put(Material.IRON_SPADE, createAttribute(3D, defaultUUID));
                defaults.put(Material.IRON_SWORD, createAttribute(6D, defaultUUID));
                defaults.put(Material.STONE_AXE, createAttribute(4D, defaultUUID));
                defaults.put(Material.STONE_PICKAXE, createAttribute(3D, defaultUUID));
                defaults.put(Material.STONE_SPADE, createAttribute(2D, defaultUUID));
                defaults.put(Material.STONE_SWORD, createAttribute(5D, defaultUUID));
                defaults.put(Material.WOOD_AXE, createAttribute(3D, defaultUUID));
                defaults.put(Material.WOOD_PICKAXE, createAttribute(2D, defaultUUID));
                defaults.put(Material.WOOD_SPADE, createAttribute(1D, defaultUUID));
                defaults.put(Material.WOOD_SWORD, createAttribute(4D, defaultUUID));
            } catch (Exception ex) {
                ex.printStackTrace(); // wtf is going on
            }
        }

        /**
         * Applies the default attributes on items (e.g. Diamond Sword: +7 attack damage).
         *
         * @param is ItemStack to apply default attributes to
         * @return ItemStack with default attributes applied
         */
        public static ItemStack applyDefaultAttributes(ItemStack is) {
            if (!defaults.containsKey(is.getType())) return is;
            final Attributes attr = new Attributes(is);
            attr.add(defaults.get(is.getType()));
            return attr.getStack();
        }

        /**
         * Applies spawn information to an ItemStack.
         * <br/>
         * <strong>Note:</strong> ItemStacks containing AIR (ID: 0) will not have information applied, but will still
         * return the ItemStack.
         *
         * @param is ItemStack to apply information to
         * @param si SpawnInfo to apply
         * @return ItemStack with SpawnInfo applied; never null
         */
        public static ItemStack applySpawnInfo(ItemStack is, SpawnInfo si) {
            return applySpawnInfo(is, si.toString());
        }

        /**
         * Applies spawn information to an ItemStack.
         * <br/>
         * <strong>Note:</strong> ItemStacks containing AIR (ID: 0) will not have information applied, but will still
         * return the ItemStack.
         *
         * @param is ItemStack to apply information to
         * @param s  SpawnInfo to apply (String form)
         * @return ItemStack with SpawnInfo applied; never null
         */
        public static ItemStack applySpawnInfo(ItemStack is, String s) {
            if (is.getType() == Material.AIR) return is; // air; do not apply
            is = applyDefaultAttributes(is);
            final AttributeStorage as = AttributeStorage.newTarget(is, uuid);
            as.setData(s);
            return as.getTarget();
        }

        private static Attributes.Attribute createAttribute(double amount, UUID uuid) {
            return Attributes.Attribute.newBuilder().name("default").type(Attributes.AttributeType.GENERIC_ATTACK_DAMAGE).operation(Attributes.Operation.ADD_NUMBER).amount(amount).uuid(uuid).build();
        }

        /**
         * Retrieve the data stored in the item's attribute.
         *
         * @return The stored data, or defaultValue if not found.
         */
        public static String getData(AttributeStorage as) {
            Attributes attr = new Attributes(as.getTarget());
            try {
                final Method getAttribute = AttributeStorage.class.getDeclaredMethod("getAttribute", Attributes.class, UUID.class);
                final Field uniqueKey = AttributeStorage.class.getDeclaredField("uniqueKey");
                getAttribute.setAccessible(true);
                uniqueKey.setAccessible(true);
                final Attributes.Attribute current = (Attributes.Attribute) getAttribute.invoke(as, attr, uniqueKey.get(as));
                SpawnInfoManager.removeTagIfEmpty(attr);
                return current != null ? current.getName() : null;
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        /**
         * Gets SpawnInfo from an ItemStack. If the ItemStack has no SpawnInfo attached, a default SpawnInfo will be
         * returned.
         * <br/>
         * <strong>Note:</strong> ItemStacks containing AIR (ID: 0) will always return a blank SpawnInfo, since they
         * cannot store NBT data.
         *
         * @param is ItemStack to obtain SpawnInfo from
         * @return SpawnInfo; never null
         */
        public static SpawnInfo getSpawnInfo(ItemStack is) {
            if (is.getType() == Material.AIR) return new SpawnInfo(); // air cannot contain NBT data
            final AttributeStorage as = AttributeStorage.newTarget(is, uuid);
            String stored = SpawnInfoManager.getData(as);
            if (stored == null) stored = "false/null/false/null";
            return new SpawnInfo(stored);
        }

        /**
         * Removes the data stored in the attributes.
         */
        public static void removeData(AttributeStorage as) {
            final Attributes attributes = new Attributes(as.getTarget());
            try {
                final Method getAttribute = AttributeStorage.class.getDeclaredMethod("getAttribute", Attributes.class, UUID.class);
                final Field uniqueKey = AttributeStorage.class.getDeclaredField("uniqueKey");
                getAttribute.setAccessible(true);
                uniqueKey.setAccessible(true);
                final Attributes.Attribute current = (Attributes.Attribute) getAttribute.invoke(as, attributes, uniqueKey.get(as));
                if (current == null) return;
                attributes.remove(current);
                SpawnInfoManager.removeTagIfEmpty(attributes);
                final Field target = AttributeStorage.class.getDeclaredField("target");
                target.set(as, attributes.getStack());
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }

        /**
         * Removes the attributes applied in
         * {@link SpawnInfo.SpawnInfoManager#applyDefaultAttributes(org.bukkit.inventory.ItemStack)}.
         *
         * @param is ItemStack to remove default attributes from
         * @return ItemStack with default attributes removed
         */
        public static ItemStack removeDefaultAttributes(ItemStack is) {
            final Attributes attr = new Attributes(is);
            for (Attributes.Attribute a : attr.values()) {
                if (!a.getUUID().equals(defaultUUID)) continue;
                attr.remove(a);
            }
            return attr.getStack();
        }

        /**
         * Removes all SpawnInfo from an ItemStack, leaving it as it was prior to SpawnInfo application.
         *
         * @param is ItemStack to remove SpawnInfo from
         * @return ItemStack without SpawnInfo
         */
        public static ItemStack removeSpawnInfo(ItemStack is) {
            if (is.getType() == Material.AIR) return is; // silly air
            is = SpawnInfoManager.removeDefaultAttributes(is);
            final AttributeStorage as = AttributeStorage.newTarget(is, uuid);
            SpawnInfoManager.removeData(as);
            return as.getTarget();
        }

        public static void removeTagIfEmpty(Attributes a) {
            if (a.size() <= 0) return;
            NbtFactory.NbtCompound nbt = NbtFactory.fromItemTag(a.stack);
            nbt.remove("AttributeModifiers");
            if (nbt.isEmpty()) NbtFactory.setItemTag(a.stack, null);
        }

    }
}
