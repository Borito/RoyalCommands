package org.royaldev.royalcommands.nms.NoSupport;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.nms.api.NMSFace;

public class NMSHandler implements NMSFace {
    @Override
    public int getPing(Player p) {
        throw new UnsupportedOperationException("No NMS support enabled!");
    }

    @Override
    public String getVersion() {
        return "NoSupport";
    }

    @Override
    public boolean hasSupport() {
        return false;
    }

}
