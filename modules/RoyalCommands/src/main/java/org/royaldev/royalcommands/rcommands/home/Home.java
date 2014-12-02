package org.royaldev.royalcommands.rcommands.home;

import org.bukkit.Location;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.wrappers.RPlayer;

import java.util.UUID;

public class Home {

    private final UUID uuid;
    private final String name;
    private Location location;

    public Home(final UUID uuid, final String name, final Location location) {
        this.uuid = uuid;
        this.name = name;
        this.location = location;
    }

    /**
     * @param uuid     UUID of requester
     * @param notation Notation used
     * @return Home
     */
    public static Home fromNotation(final UUID uuid, final String notation) {
        final String[] parts = notation.split(":");
        final String playerName = parts.length > 1 ? parts[0] : null;
        final String homeName = parts[parts.length > 1 ? 1 : 0];
        final PConfManager pcm;
        if (playerName == null) pcm = PConfManager.getPConfManager(uuid);
        else pcm = PConfManager.getPConfManager(RoyalCommands.getInstance().getServer().getOfflinePlayer(playerName));
        return Home.fromPConfManager(pcm, homeName);
    }

    public static Home fromPConfManager(final PConfManager pcm, final String name) {
        if (!pcm.isSet("home." + name)) return null;
        try {
            return new Home(pcm.getManagerPlayerUUID(), name, pcm.getLocation("home." + name));
        } catch (final Exception ex) {
            return null;
        }
    }

    public void delete() {
        final PConfManager pcm = this.getRPlayer().getPConfManager();
        pcm.set("home." + this.getName(), null);
        pcm.forceSave();
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public RPlayer getRPlayer() {
        return RPlayer.getRPlayer(this.uuid);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void save() {
        final PConfManager pcm = this.getRPlayer().getPConfManager();
        pcm.setLocation("home." + this.getName(), this.getLocation());
        pcm.forceSave();
    }
}
