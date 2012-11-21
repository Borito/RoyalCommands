package org.royaldev.royalcommands.serializable;

import net.minecraft.server.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class SerializableInventory implements Inventory, Serializable {

    static final long serialVersionUID = -42L;
    private final Map<Integer, Map<String, Object>> contents = new HashMap<Integer, Map<String, Object>>();
    // HashMaps hate me? Hashtable is working right now
    //private final Map<Integer, SerializableNBTTagCompound> tags = new HashMap<Integer, SerializableNBTTagCompound>();
    private final Hashtable<Integer, SerializableNBTTagCompound> tags = new Hashtable<Integer, SerializableNBTTagCompound>();
    private final Hashtable<Integer, Map<SerializableCraftEnchantment, Integer>> enchants = new Hashtable<Integer, Map<SerializableCraftEnchantment, Integer>>();
    private int maxStackSize = 64;
    private int size = 27;
    private String name = "Inventory";
    private String title = "Inventory";

    private CraftItemStack getCIS(ItemStack is) {
        CraftItemStack cis;
        try {
            cis = (CraftItemStack) is;
        } catch (ClassCastException e) {
            cis = new CraftItemStack(is);
        }
        return cis;
    }

    private ItemStack setTag(ItemStack is, int slot) {
        if (tags.containsKey(slot)) {
            CraftItemStack cis = getCIS(is);
            SerializableNBTTagCompound stag = tags.get(slot);
            if (stag != null) cis.getHandle().tag = stag.getNBTTagCompound();
            is = cis;
        }
        if (enchants.containsKey(slot)) {
            Map<SerializableCraftEnchantment, Integer> encs = enchants.get(slot);
            for (SerializableCraftEnchantment sce : encs.keySet()) {
                is.addUnsafeEnchantment(sce.getEnchantment(), encs.get(sce));
            }
        }
        return is;
    }

    public SerializableInventory(final int size) {
        if (size % 9 != 0) return;
        this.size = size;
        for (int i = 0; i < size; i++) {
            contents.put(i, new HashMap<String, Object>());
        }
    }

    public SerializableInventory(Inventory inv) {
        size = inv.getSize();
        title = inv.getTitle();
        name = inv.getName();
        setContents(inv.getContents());
    }

    public SerializableInventory() {
        for (int i = 0; i < size; i++) {
            contents.put(i, new HashMap<String, Object>());
        }
    }

    public SerializableInventory(final int size, final String name) {
        if (size % 9 != 0) return;
        this.size = size;
        this.name = name;
        this.title = name;
        for (int i = 0; i < size; i++) {
            contents.put(i, new HashMap<String, Object>());
        }
    }

    public SerializableInventory(final InventoryHolder ih, final int size, final String name) {
        if (size % 9 != 0) return;
        this.size = size;
        this.name = name;
        this.title = name;
        for (int i = 0; i < size; i++) {
            contents.put(i, new HashMap<String, Object>());
        }
    }

    public Inventory deserialize() {
        final Inventory i = Bukkit.createInventory(null, size, name);
        synchronized (contents) {
            for (int in = 0; in < size; in++) {
                if (!contents.containsKey(in)) continue;
                Map<String, Object> sis = contents.get(in);
                ItemStack is;
                try {
                    is = ItemStack.deserialize(sis);
                } catch (NullPointerException e) {
                    i.setItem(in, null);
                    continue;
                }
                if (is == null) {
                    i.setItem(in, null);
                    continue;
                }
                is = setTag(is, in);
                i.setItem(in, is);
            }
        }
        i.setMaxStackSize(maxStackSize);
        return i;
    }

    @Override
    public int getSize() {
        return contents.size();
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public void setMaxStackSize(int i) {
        maxStackSize = i;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i < 0 || i > size) return null;
        Map<String, Object> item;
        try {
            synchronized (contents) {
                item = contents.get(i);
            }
        } catch (Exception e) {
            return null;
        }
        return ItemStack.deserialize(item);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i < 0 || i > size) return;
        synchronized (contents) {
            if (itemStack == null) {
                contents.put(i, new HashMap<String, Object>());
                return;
            }
            CraftItemStack cis = getCIS(itemStack);
            NBTTagCompound tag = cis.getHandle().tag;
            SerializableNBTTagCompound stag = (tag == null) ? null : new SerializableNBTTagCompound(tag);
            tags.put(i, stag);
            Map<SerializableCraftEnchantment, Integer> encs = new HashMap<SerializableCraftEnchantment, Integer>();
            for (Enchantment e : itemStack.getEnchantments().keySet()) {
                encs.put(new SerializableCraftEnchantment(e), itemStack.getEnchantments().get(e));
            }
            enchants.put(i, encs);
            contents.put(i, itemStack.serialize());
        }
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) {
        Inventory i = deserialize();
        HashMap<Integer, ItemStack> extra = i.addItem(itemStacks);
        setContents(i.getContents());
        return extra;
    }

    @Override
    /**
     * This method not supported by SerializableInventory yet
     */
    public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) {
        Inventory i = deserialize();
        HashMap<Integer, ItemStack> extra = i.removeItem(itemStacks);
        setContents(i.getContents());
        return extra;
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            if (!contents.containsKey(i)) continue;
            if (i > items.length) continue;
            synchronized (contents) {
                Map<String, Object> item = contents.get(i);
                if (item == null) item = new HashMap<String, Object>();
                ItemStack is;
                try {
                    is = ItemStack.deserialize(item);
                } catch (NullPointerException e) {
                    is = null;
                }
                if (is == null) is = new ItemStack(Material.AIR);
                items[i] = is;
            }
        }
        return items;
    }

    @Override
    public void setContents(ItemStack[] itemStacks) {
        if (itemStacks.length > size) return;
        for (int i = 0; i < itemStacks.length; i++) {
            synchronized (contents) {
                ItemStack is = itemStacks[i];
                if (is == null) {
                    contents.put(i, new HashMap<String, Object>());
                    continue;
                }
                CraftItemStack cis = getCIS(is);
                NBTTagCompound tag = cis.getHandle().tag;
                SerializableNBTTagCompound stag = (tag == null) ? null : new SerializableNBTTagCompound(tag);
                tags.put(i, stag);
                contents.put(i, is.serialize());
                Map<SerializableCraftEnchantment, Integer> encs = new HashMap<SerializableCraftEnchantment, Integer>();
                for (Enchantment e : is.getEnchantments().keySet()) {
                    encs.put(new SerializableCraftEnchantment(e), is.getEnchantments().get(e));
                }
                enchants.put(i, encs);
            }
        }
    }

    @Override
    public boolean contains(int i) {
        return deserialize().contains(i);
    }

    @Override
    public boolean contains(Material material) {
        return deserialize().contains(material);
    }

    @Override
    public boolean contains(ItemStack itemStack) {
        return deserialize().contains(itemStack);
    }

    @Override
    public boolean contains(int i, int i1) {
        return deserialize().contains(i, i1);
    }

    @Override
    public boolean contains(Material material, int i) {
        return deserialize().contains(material, i);
    }

    @Override
    public boolean contains(ItemStack itemStack, int i) {
        return deserialize().contains(itemStack, i);
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(int i) {
        return deserialize().all(i);
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) {
        return deserialize().all(material);
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {
        return deserialize().all(itemStack);
    }

    @Override
    public int first(int i) {
        return deserialize().first(i);
    }

    @Override
    public int first(Material material) {
        return deserialize().first(material);
    }

    @Override
    public int first(ItemStack itemStack) {
        return deserialize().first(itemStack);
    }

    @Override
    public int firstEmpty() {
        synchronized (contents) {
            for (int i = 0; i < size; i++) {
                if (!contents.containsKey(i)) continue;
                if (contents.get(i).isEmpty()) return i;
            }
        }
        return -1;
    }

    @Override
    public void remove(int i) {
        Inventory inv = deserialize();
        inv.remove(i);
        setContents(inv.getContents());
    }

    @Override
    public void remove(Material material) {
        Inventory inv = deserialize();
        inv.remove(material);
        setContents(inv.getContents());
    }

    @Override
    public void remove(ItemStack itemStack) {
        Inventory inv = deserialize();
        inv.remove(itemStack);
        setContents(inv.getContents());
    }

    @Override
    public void clear(int i) {
        if (i < 0 || i > size) return;
        synchronized (contents) {
            contents.put(i, new HashMap<String, Object>());
        }
    }

    @Override
    public void clear() {
        synchronized (contents) {
            for (int i = 0; i < size; i++) {
                contents.put(i, new HashMap<String, Object>());
            }
        }
    }

    @Override
    public List<HumanEntity> getViewers() {
        return new ArrayList<HumanEntity>();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYER;
    }

    @Override
    public InventoryHolder getHolder() {
        return null;
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return deserialize().iterator();
    }

    @Override
    public ListIterator<ItemStack> iterator(int i) {
        return deserialize().iterator(i);
    }
}
