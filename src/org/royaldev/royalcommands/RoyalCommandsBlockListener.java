package org.royaldev.royalcommands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class RoyalCommandsBlockListener implements Listener {

    public static RoyalCommands plugin;

    public RoyalCommandsBlockListener(RoyalCommands instance) {
        plugin = instance;
    }

    @EventHandler(event = BlockPlaceEvent.class, priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (PConfManager.getPValBoolean(event.getPlayer(),
                "frozen")) {
            event.setCancelled(true);
        }
        if (PConfManager.getPValBoolean(event.getPlayer(),
                "jailed")) {
            event.setCancelled(true);
        }
        if (plugin.buildPerm) {
            if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build")) {
                event.getPlayer().sendMessage(plugin.noBuildMessage);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(event = BlockBreakEvent.class, priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (PConfManager.getPValBoolean(event.getPlayer(),
                "frozen")) {
            event.setCancelled(true);
        }
        if (PConfManager.getPValBoolean(event.getPlayer(),
                "jailed")) {
            event.setCancelled(true);
        }
        if (plugin.buildPerm) {
            if (!plugin.isAuthorized(event.getPlayer(), "rcmds.build")) {
                event.getPlayer().sendMessage(plugin.noBuildMessage);
                event.setCancelled(true);
            }
        }
    }

}
