package org.royaldev.royalcommands.nms.v1_5_R2;

import net.minecraft.server.v1_5_R2.DedicatedPlayerList;
import net.minecraft.server.v1_5_R2.EntityPlayer;
import net.minecraft.server.v1_5_R2.PlayerList;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.nms.api.NMSFace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NMSHandler implements NMSFace {
    @Override
    public boolean hasSupport() {
        return true;
    }

    @Override
    public String getVersion() {
        return "v1_5_R2";
    }

    @Override
    public int getPing(Player p) {
        if (p instanceof CraftPlayer) return ((CraftPlayer) p).getHandle().ping;
        throw new IllegalArgumentException("Player was not a CraftPlayer!");
    }

    @Override
    public void savePlayerData(Player p) {
        final Server s = p.getServer();
        if (!(s instanceof CraftServer)) throw new IllegalArgumentException("Server was not a CraftServer!");
        final CraftServer cs = (CraftServer) s;
        final DedicatedPlayerList dpl;
        try {
            Field f = CraftServer.class.getDeclaredField("playerList");
            f.setAccessible(true);
            Object o = f.get(cs);
            if (!(o instanceof DedicatedPlayerList)) throw new IllegalArgumentException("Player list isn't DPL!");
            dpl = (DedicatedPlayerList) o;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        EntityPlayer ep = dpl.f(p.getName());
        if (ep == null) throw new IllegalArgumentException("Couldn't get that player!");
        try {
            Method m = PlayerList.class.getDeclaredMethod("b", EntityPlayer.class);
            m.setAccessible(true);
            m.invoke(dpl, ep);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
