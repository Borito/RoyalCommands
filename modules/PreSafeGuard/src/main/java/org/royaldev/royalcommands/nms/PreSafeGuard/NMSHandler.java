package org.royaldev.royalcommands.nms.PreSafeGuard;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.nms.api.NMSFace;

public class NMSHandler implements NMSFace {
    @Override
    public boolean hasSupport() {
        return true;
    }

    @Override
    public int getPing(Player p) {
        if (p instanceof CraftPlayer) return ((CraftPlayer) p).getHandle().ping;
        throw new IllegalArgumentException("Player was not a CraftPlayer!");
    }
}
