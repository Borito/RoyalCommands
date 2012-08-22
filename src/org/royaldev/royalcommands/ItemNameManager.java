package org.royaldev.royalcommands;

import org.apache.commons.lang.ArrayUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ItemNameManager {

    // <Aliases, <ID, Data>>
    private final Map<String[], Map<Integer, Integer>> items = new HashMap<String[], Map<Integer, Integer>>();

    public ItemNameManager(Iterable<String[]> values) {
        for (String[] s : values) {
            int id;
            int data;
            String[] aliases = s[2].split(",");
            try {
                id = Integer.valueOf(s[0]);
            } catch (NumberFormatException e) {
                Logger.getLogger("Minecraft").warning("[RoyalCommands] ID in IDs file is invalid: " + s[0]);
                id = -1;
            }
            try {
                data = Integer.valueOf(s[1]);
            } catch (NumberFormatException e) {
                Logger.getLogger("Minecraft").warning("[RoyalCommands] Data in IDs file is invalid: " + s[1]);
                data = -1;
            }
            Map<Integer, Integer> stuff = new HashMap<Integer, Integer>();
            stuff.put(id, data);
            synchronized (items) {
                items.put(aliases, stuff);
            }
        }
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
        Map.Entry<Integer, Integer> entry = items.get(aliases).entrySet().iterator().next();
        if (data == null || data.isEmpty()) data = String.valueOf(entry.getValue());
        return entry.getKey() + ":" + data;
    }

}
