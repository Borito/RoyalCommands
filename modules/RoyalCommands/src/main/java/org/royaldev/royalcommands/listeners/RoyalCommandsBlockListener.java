package org.royaldev.royalcommands.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

@SuppressWarnings("unused")
public class RoyalCommandsBlockListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsBlockListener(RoyalCommands instance) {
        plugin = instance;
    }

    @EventHandler
    public void onBlockB(BlockBreakEvent e) {
        if (e.isCancelled() || !Config.buildPerm) return;
        if (plugin.ah.isAuthorized(e.getPlayer(), "rcmds.build")) return;
        e.getPlayer().sendMessage(Config.noBuildMessage);
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(event.getPlayer());
        if (pcm.getBoolean("frozen")) event.setCancelled(true);
        if (pcm.getBoolean("jailed")) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockP(BlockPlaceEvent e) {
        if (e.isCancelled() || !Config.buildPerm) return;
        if (plugin.ah.isAuthorized(e.getPlayer(), "rcmds.build")) return;
        e.getPlayer().sendMessage(Config.noBuildMessage);
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(event.getPlayer());
        if (pcm.getBoolean("frozen")) event.setCancelled(true);
        if (pcm.getBoolean("jailed")) event.setCancelled(true);
    }

}
