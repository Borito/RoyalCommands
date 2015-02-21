package org.royaldev.royalcommands.rcommands.nick;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

import java.util.UUID;

public class Nick {

    private final RPlayer rp;

    public Nick(final RPlayer rp) {
        this.rp = rp;
    }

    private PlayerConfiguration getPlayerConfiguration() {
        return this.getRPlayer().getPlayerConfiguration();
    }

    public void clear() {
        this.set(null);
    }

    public String get() {
        return this.getPlayerConfiguration().getString("nick.value", null);
    }

    public long getLastUpdate() {
        return this.getPlayerConfiguration().getLong("nick.last_update", -1L);
    }

    public void setLastUpdate(final long update) {
        this.getPlayerConfiguration().set("nick.last_update", update);
    }

    public RPlayer getRPlayer() {
        return this.rp;
    }

    public UUID getUUID() {
        return this.getRPlayer().getUUID();
    }

    public void set(final String name) {
        this.getPlayerConfiguration().set("nick.value", name);
        this.setLastUpdate();
        this.update();
    }

    public void setLastUpdate() {
        this.setLastUpdate(System.currentTimeMillis());
    }

    public void update() {
        final Player p = this.getRPlayer().getPlayer();
        if (p == null) return;
        p.setDisplayName(this.get());
        if (Config.nickPlayerList) p.setPlayerListName(this.get());
    }
}
