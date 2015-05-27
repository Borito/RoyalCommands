/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.rcommands.CmdAfk;

public final class AFKUtils {

    public static long getAfkTime(Player p) {
        synchronized (CmdAfk.afkdb) {
            return CmdAfk.afkdb.get(p);
        }
    }

    public static long getLastMove(Player p) {
        synchronized (CmdAfk.movetimes) {
            return CmdAfk.movetimes.get(p);
        }
    }

    public static boolean isAfk(Player p) {
        synchronized (CmdAfk.afkdb) {
            return CmdAfk.afkdb.containsKey(p);
        }
    }

    public static boolean moveTimesContains(Player p) {
        synchronized (CmdAfk.movetimes) {
            return CmdAfk.movetimes.containsKey(p);
        }
    }

    public static void removeLastMove(Player p) {
        synchronized (CmdAfk.movetimes) {
            CmdAfk.movetimes.remove(p);
        }
    }

    public static void setAfk(Player p, long time) {
        synchronized (CmdAfk.afkdb) {
            CmdAfk.afkdb.put(p, time);
        }
    }

    public static void setLastMove(Player p, long time) {
        synchronized (CmdAfk.movetimes) {
            CmdAfk.movetimes.put(p, time);
        }
    }

    public static void unsetAfk(Player p) {
        synchronized (CmdAfk.afkdb) {
            CmdAfk.afkdb.remove(p);
        }
    }

}
