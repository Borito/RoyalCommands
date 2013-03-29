package org.royaldev.royalcommands.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

@SuppressWarnings("unused")
public class RoyalCommandsBlockListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsBlockListener(RoyalCommands instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        PConfManager pcm = PConfManager.getPConfManager(event.getPlayer());
        if (pcm.getBoolean("frozen")) event.setCancelled(true);
        if (pcm.getBoolean("jailed")) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockP(BlockPlaceEvent e) {
        if (e.isCancelled() || !plugin.buildPerm) return;
        if (plugin.isAuthorized(e.getPlayer(), "rcmds.build")) return;
        e.getPlayer().sendMessage(plugin.noBuildMessage);
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockB(BlockBreakEvent e) {
        if (e.isCancelled() || !plugin.buildPerm) return;
        if (plugin.isAuthorized(e.getPlayer(), "rcmds.build")) return;
        e.getPlayer().sendMessage(plugin.noBuildMessage);
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        PConfManager pcm = PConfManager.getPConfManager(event.getPlayer());
        if (pcm.getBoolean("frozen")) event.setCancelled(true);
        if (pcm.getBoolean("jailed")) event.setCancelled(true);
    }

}
