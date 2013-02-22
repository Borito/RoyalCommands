package org.royaldev.royalcommands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ItemNameManager {

    // <Aliases, <ID, Data>>
    private final Map<String[], Map<Integer, Short>> items = new HashMap<String[], Map<Integer, Short>>();
    private final List<Integer> ids = new ArrayList<Integer>();

    public ItemNameManager(Iterable<String[]> values) {
        for (String[] s : values) {
            if (s.length < 1) continue;
            int id;
            short data;
            String[] aliases;
            try {
                aliases = s[2].split(",");
            } catch (IndexOutOfBoundsException e) {
                Logger l = Logger.getLogger("Minecraft");
                l.warning("[RoyalCommands] Values passed in ItemNameManager invalid: ");
                for (String ss : s) l.warning("[RoyalCommands] - " + ss);
                continue;
            }
            try {
                id = Integer.valueOf(s[0]);
            } catch (NumberFormatException e) {
                RoyalCommands.instance.getLogger().warning("ID in IDs file is invalid: " + s[0]);
                id = -1;
            }
            try {
                data = Short.valueOf(s[1]);
            } catch (NumberFormatException e) {
                RoyalCommands.instance.getLogger().warning("Data in IDs file is invalid: " + s[1]);
                data = -1;
            }
            Map<Integer, Short> stuff = new HashMap<Integer, Short>();
            stuff.put(id, data);
            ids.add(id);
            synchronized (items) {
                items.put(aliases, stuff);
            }
        }
    }

    public boolean aliasExists(int i) {
        return ids.contains(i);
    }

    public boolean aliasExists(Material m) {
        return aliasExists(m.getId());
    }

    public boolean aliasExists(ItemStack is) {
        return aliasExists(is.getTypeId());
    }

    public String getIDFromAlias(String alias) {
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
        Map.Entry<Integer, Short> entry = items.get(aliases).entrySet().iterator().next();
        if (data == null || data.isEmpty()) data = String.valueOf(entry.getValue());
        return entry.getKey() + ":" + data;
    }

}
