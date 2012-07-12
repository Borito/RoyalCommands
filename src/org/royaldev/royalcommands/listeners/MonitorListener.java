package org.royaldev.royalcommands.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdMonitor;

public class MonitorListener implements Listener {

    RoyalCommands plugin;

    public MonitorListener(RoyalCommands instance) {
        plugin = instance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!CmdMonitor.monitors.containsValue(e.getPlayer().getName())) return;
        Player p = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        /*String pn = "";
        for (String pl : CmdMonitor.monitors.keySet()) {
            if (!CmdMonitor.monitors.get(pl).equals(e.getPlayer().getName())) continue;
            pn = pl;
        }
        if (pn.equals("")) {
            System.out.println("[RoyalCommands] A big boo-boo happened. Alert the dev.");
            return;
        }
        Player p = plugin.getServer().getPlayer(pn);*/
        RUtils.silentTeleport(p, e.getPlayer());
        e.getPlayer().hidePlayer(p);
    }

    @EventHandler
    public void onTele(PlayerTeleportEvent e) {
        if (!CmdMonitor.monitors.containsKey(e.getPlayer().getName())) return;
        Player p = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        RUtils.silentTeleport(p, e.getPlayer());
        e.getPlayer().hidePlayer(p);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!CmdMonitor.monitors.containsKey(e.getPlayer().getName())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (!CmdMonitor.monitors.containsKey(e.getPlayer().getName())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (CmdMonitor.monitors.containsKey(e.getPlayer().getName())) return;
        for (String pn : CmdMonitor.monitors.keySet()) {
            Player p = plugin.getServer().getPlayer(pn);
            if (p == null) continue;
            e.getPlayer().hidePlayer(p);
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if (!CmdMonitor.monitors.containsKey(e.getPlayer().getName())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        if (!CmdMonitor.monitors.containsKey(e.getPlayer().getName())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (!CmdMonitor.monitors.containsKey(e.getPlayer().getName())) return;
        e.setCancelled(true);
    }

}
