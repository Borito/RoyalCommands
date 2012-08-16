package org.royaldev.royalcommands.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdMonitor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MonitorListener implements Listener {

    RoyalCommands plugin;

    public MonitorListener(RoyalCommands instance) {
        plugin = instance;
    }

    public static List<String> openInvs = new ArrayList<String>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!CmdMonitor.monitors.containsValue(e.getPlayer().getName())) return;
        Player p = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        RUtils.silentTeleport(p, e.getPlayer());
        e.getPlayer().hidePlayer(p);
    }

    @EventHandler
    public void onChangeHold(PlayerItemHeldEvent e) {
        if (!CmdMonitor.viewees.containsKey(e.getPlayer().getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        if (t == null) return;
        t.getInventory().setContents(e.getPlayer().getInventory().getContents());
        t.setItemInHand(e.getPlayer().getInventory().getItem(e.getNewSlot()));
    }

    @EventHandler
    public void onBlockViewee(BlockPlaceEvent e) {
        if (!CmdMonitor.viewees.containsKey(e.getPlayer().getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        if (t == null) return;
        t.getInventory().setContents(e.getPlayer().getInventory().getContents());
        t.setItemInHand(e.getPlayer().getItemInHand());
    }

    @EventHandler
    public void onItemDropViewee(PlayerDropItemEvent e) {
        if (!CmdMonitor.viewees.containsKey(e.getPlayer().getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        if (t == null) return;
        t.getInventory().setContents(e.getPlayer().getInventory().getContents());
        t.setItemInHand(e.getPlayer().getItemInHand());
    }

    @EventHandler
    public void onItemPickupViewee(PlayerPickupItemEvent e) {
        if (!CmdMonitor.viewees.containsKey(e.getPlayer().getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        if (t == null) return;
        t.getInventory().setContents(e.getPlayer().getInventory().getContents());
        t.setItemInHand(e.getPlayer().getItemInHand());
    }

    @EventHandler
    public void onDamageViewee(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!CmdMonitor.viewees.containsKey(p.getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(p.getName()));
        if (t == null) return;
        if (p.getHealth() < 1) return;
        t.setHealth(p.getHealth());
    }

    @EventHandler
    public void onRegainViewee(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!CmdMonitor.viewees.containsKey(p.getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(p.getName()));
        if (t == null) return;
        if (p.getHealth() < 1) return;
        if (p.getHealth() < t.getHealth()) return;
        t.setHealth(p.getHealth());
    }

    @EventHandler
    public void onFoodViewee(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!CmdMonitor.viewees.containsKey(p.getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(p.getName()));
        if (t == null) return;
        if (p.getFoodLevel() < 1) return;
        t.setFoodLevel(p.getFoodLevel());
        t.setSaturation(p.getSaturation());
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        if (!CmdMonitor.viewees.containsKey(p.getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(p.getName()));
        if (t == null) return;
        Inventory i = e.getInventory();
        if (i.getType().equals(InventoryType.WORKBENCH)) {
            Block b = RUtils.getTarget(p);
            if (!b.getType().equals(Material.WORKBENCH)) return;
            t.openWorkbench(b.getLocation(), false);
        }
        if (i.getType().equals(InventoryType.ENCHANTING)) {
            Block b = RUtils.getTarget(p);
            if (!b.getType().equals(Material.ENCHANTMENT_TABLE)) return;
            t.openEnchanting(b.getLocation(), false);
        }
        if (i.getType().equals(InventoryType.BREWING)) return;
        if (i.getType().equals(InventoryType.CRAFTING)) return;
        if (i.getType().equals(InventoryType.DISPENSER)) return;
        t.openInventory(e.getInventory());
        openInvs.add(t.getName());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!openInvs.contains(e.getWhoClicked().getName())) return;
        e.setCancelled(true);
    }

    /*@EventHandler
    public void onInvCloseMonitor(InventoryCloseEvent e) {
        if (!openInvs.contains(e.getPlayer().getName())) return;
        e.getPlayer().openInventory(e.getInventory());
    }*/
    // Seems a bit harsh ^

    @EventHandler
    public void onInvCloseViewee(InventoryCloseEvent e) {
        if (!CmdMonitor.viewees.containsKey(e.getPlayer().getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        if (t == null) return;
        t.closeInventory();
        openInvs.remove(t.getName());
    }

    @EventHandler
    public void onTele(PlayerTeleportEvent e) {
        if (!CmdMonitor.viewees.containsKey(e.getPlayer().getName())) return;
        Player p = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        if (p == null) return;
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
    public void joinViewee(PlayerJoinEvent e) {
        if (!CmdMonitor.viewees.containsKey(e.getPlayer().getName())) return;
        Player t = plugin.getServer().getPlayer(CmdMonitor.viewees.get(e.getPlayer().getName()));
        if (t == null) return;
        t.hidePlayer(e.getPlayer());
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
        if (e.getClickedBlock() == null) return; // Fixed NPE below?
        if (e.getClickedBlock().getState() instanceof Chest) {
            Chest c = (Chest) e.getClickedBlock().getState();
            final Inventory i = plugin.getServer().createInventory(c.getInventory().getHolder(), c.getInventory().getSize());
            i.setContents(c.getInventory().getContents());
            e.getPlayer().openInventory(i);
            e.getPlayer().sendMessage(ChatColor.BLUE + "Opened chest in read-only mode; you can't make changes.");
        }
        e.setCancelled(true);
    }

}
