/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.royaldev.royalcommands.RoyalCommands;

public class ServerListener implements Listener {

    private final RoyalCommands plugin;

    public ServerListener(RoyalCommands instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent e) {
        final Plugin p = e.getPlugin();
        if (!p.getName().equals("Vault")) return;
        this.plugin.vh.removeVault();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent e) {
        final Plugin p = e.getPlugin();
        if (!p.getName().equals("Vault")) return;
        this.plugin.vh.setUpVault();

    }

}
