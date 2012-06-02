package org.royaldev.royalcommands;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.rcommands.CmdAfk;

public class AFKUtils {

    public synchronized static void setAfk(Player p, long time) {
        CmdAfk.afkdb.put(p, time);
    }
    
    public synchronized static long getAfkTime(Player p) {
        return CmdAfk.afkdb.get(p);
    }
    
    public synchronized static boolean isAfk(Player p) {
        return CmdAfk.afkdb.containsKey(p);
    }
    
    public synchronized static boolean moveTimesContains(Player p) {
        return CmdAfk.movetimes.containsKey(p);
    }
    
    public synchronized static long getLastMove(Player p) {
        return CmdAfk.movetimes.get(p);
    }

}
