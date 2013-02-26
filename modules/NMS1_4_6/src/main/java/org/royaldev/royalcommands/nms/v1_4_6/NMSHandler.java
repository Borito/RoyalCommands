package org.royaldev.royalcommands.nms.v1_4_6;

import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.nms.api.NMSFace;

public class NMSHandler implements NMSFace {
    @Override
    public boolean hasSupport() {
        return true;
    }

    @Override
    public String getVersion() {
        return "v1_4_6";
    }

    @Override
    public int getPing(Player p) {
        if (p instanceof CraftPlayer) return ((CraftPlayer) p).getHandle().ping;
        throw new IllegalArgumentException("Player was not a CraftPlayer!");
    }
}
