package org.royaldev.royalcommands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ItemNameManager {

    // <Aliases, <ID, Data>>
    private final Map<String[], ItemStack> items = new HashMap<String[], ItemStack>();

    public ItemNameManager(Iterable<String[]> values) {
        for (String[] s : values) {
            if (s.length < 1) continue;
            ItemStack is;
            String[] aliases;
            try {
                aliases = s[2].split(",");
            } catch (IndexOutOfBoundsException e) {
                Logger l = Logger.getLogger("Minecraft");
                l.warning("[RoyalCommands] Values passed in ItemNameManager invalid: ");
                for (String ss : s) l.warning("[RoyalCommands] - " + ss);
                continue;
            }
            Material m;
            try {
                m = Material.valueOf(s[0]);
                is = new ItemStack(m);
            } catch (IllegalArgumentException ex) {
                RoyalCommands.instance.getLogger().warning("Material in items.csv is invalid: " + s[0]);
                continue;
            }
            if (is == null) {
                RoyalCommands.instance.getLogger().warning("Could not make ItemStack from \"" + Arrays.toString(s) + "\"");
                continue;
            }
            try {
                short data = Short.valueOf(s[1]);
                is.setDurability(data);
            } catch (NumberFormatException e) {
                RoyalCommands.instance.getLogger().warning("Data in items.csv file is invalid: " + s[1]);
                continue;
            }
            synchronized (items) {
                items.put(aliases, is);
            }
        }
    }

    public boolean aliasExists(Material m) {
        return aliasExists(new ItemStack(m));
    }

    public boolean aliasExists(ItemStack is) {
        return items.values().contains(is);
    }

    public ItemStack getItemStackFromAlias(String alias) {
        boolean found = false;
        String[] aliases = null;
        String data = null;
        if (alias.contains(":")) {
            String[] datas = alias.split(":");
            data = (datas.length > 1) ? datas[1] : "";
            alias = datas[0];
        }
        for (String[] s : items.keySet())
            if (ArrayUtils.contains(s, alias.toLowerCase())) {
                found = true;
                aliases = s;
                break;
            }
        if (!found) return null;
        ItemStack is = items.get(aliases);
        if (data != null && !data.isEmpty()) {
            try {
                is.setDurability(Short.parseShort(data));
            } catch (NumberFormatException ignored) {
            }
        }
        return is;
    }

}
