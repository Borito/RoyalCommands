package org.royaldev.royalcommands.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class MiscListener implements Listener {
    private RoyalCommands plugin;

    public MiscListener(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(final ServerListPingEvent event) {
        event.setMotd(RUtils.colorize(plugin.currentServerTitle));
    }
}
