package org.royaldev.royalcommands.listeners;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.List;
import java.util.Set;

public class InventoryListener implements Listener {

    public void saveAllInventories() {
        for (Player p : RoyalCommands.instance.getServer().getOnlinePlayers()) {
            saveInventory(p, p.getInventory());
        }
    }

    /**
     * Gets the group of a world.
     *
     * @param w World to get group of
     * @return String of group name or null if no group
     */
    private String getWorldGroup(World w) {
        ConfigurationSection cs = RoyalCommands.instance.getConfig().getConfigurationSection("worldmanager.inventory_separation.groups");
        Set<String> s = cs.getKeys(false);
        for (String group : s) {
            List<String> worlds = cs.getStringList(group);
            if (worlds.contains(w.getName())) return group;
        }
        return null;
    }

    private void saveInventory(Player p, Inventory i) {
        saveInventory(p, i, p.getWorld());
    }

    private void saveInventory(Player p, Inventory i, World w) {
        String group = getWorldGroup(w);
        if (group == null) return;
        PConfManager pcm = new PConfManager(p);
        for (int slot = 0; slot < i.getSize(); slot++) {
            pcm.setItemStack(i.getItem(slot), "inventory." + group + ".slot." + slot);
        }
        if (i instanceof PlayerInventory) {
            PlayerInventory pi = (PlayerInventory) i;
            ItemStack is = pi.getHelmet();
            if (is != null) pcm.setItemStack(is, "inventory." + group + ".slot.helm");
            is = pi.getChestplate();
            if (is != null) pcm.setItemStack(is, "inventory." + group + ".slot.chestplate");
            is = pi.getLeggings();
            if (is != null) pcm.setItemStack(is, "inventory." + group + ".slot.leggings");
            is = pi.getBoots();
            if (is != null) pcm.setItemStack(is, "inventory." + group + ".slot.boots");
        }
        pcm.setInteger(i.getSize(), "inventory." + group + ".size");
    }

    private PlayerInventory getInventory(Player p) {
        String group = getWorldGroup(p.getWorld());
        if (group == null) return null;
        PConfManager pcm = new PConfManager(p);
        if (!pcm.exists()) pcm.createFile();
        Integer invSize = pcm.getInteger("inventory." + group + ".size");
        final PlayerInventory i = p.getInventory();
        i.clear();
        if (pcm.get("inventory." + group + ".slot") == null) return i;
        for (int slot = 0; slot < invSize; slot++) {
            ItemStack is = pcm.getItemStack("inventory." + group + ".slot." + slot);
            if (is == null) continue;
            i.setItem(slot, is);
        }
        p.getInventory().setHelmet(pcm.getItemStack("inventory." + group + ".slot.helm"));
        p.getInventory().setChestplate(pcm.getItemStack("inventory." + group + ".slot.chestplate"));
        p.getInventory().setLeggings(pcm.getItemStack("inventory." + group + ".slot.leggings"));
        p.getInventory().setBoots(pcm.getItemStack("inventory." + group + ".slot.boots"));
        return i;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return; // only serve those configured
        getInventory(p); // sets inv of player
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE) return; // this doesn't affect us
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        if (!e.getInventory().getHolder().equals(p.getInventory().getHolder())) return; // only save their inv when they close /their/ inv
        String group = getWorldGroup(p.getWorld());
        if (group == null) return; // only manage worlds that are in the config
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        String group = getWorldGroup(p.getWorld()); // get world group (new world)
        if (group == null) return; // only serve those configured
        saveInventory(p, p.getInventory(), e.getFrom()); // save old inventory
        getInventory(p); // sets inv of player
    }
}
