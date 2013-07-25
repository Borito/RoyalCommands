package org.royaldev.royalcommands.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class InventoryListener implements Listener {

    private final RoyalCommands plugin;

    public InventoryListener(RoyalCommands instance) {
        plugin = instance;
    }

    public void saveAllInventories() {
        if (!Config.separateInv) return;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
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
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("worldmanager.inventory_separation.groups");
        Set<String> s = cs.getKeys(false);
        for (String group : s) {
            List<String> worlds = cs.getStringList(group);
            if (worlds.contains(w.getName())) return group;
        }
        return null;
    }

    private void saveEnderInventory(OfflinePlayer op, String world, Inventory i) {
        if (!Config.separateInv || !Config.separateEnder) return;
        World w = plugin.getServer().getWorld(world);
        String group = getWorldGroup(w);
        if (group == null) return;
        PConfManager pcm = PConfManager.getPConfManager(op);
        for (int slot = 0; slot < i.getSize(); slot++) {
            pcm.set("inventory." + group + ".ender.slot." + slot, i.getItem(slot));
        }
        pcm.set("inventory." + group + ".ender.size", i.getSize());
    }

    private void saveEnderInventory(Player p, Inventory i) {
        if (!Config.separateInv || !Config.separateEnder) return;
        World w = p.getWorld();
        String group = getWorldGroup(w);
        if (group == null) return;
        PConfManager pcm = PConfManager.getPConfManager(p);
        for (int slot = 0; slot < i.getSize(); slot++) {
            pcm.set("inventory." + group + ".ender.slot." + slot, i.getItem(slot));
        }
        pcm.set("inventory." + group + ".ender.size", i.getSize());
    }

    private Inventory getEnderInventory(Player p) {
        World w = p.getWorld();
        String group = getWorldGroup(w);
        if (group == null) return null;
        PConfManager pcm = PConfManager.getPConfManager(p);
        if (!pcm.exists()) pcm.createFile();
        Integer invSize = pcm.getInt("inventory." + group + ".ender.size");
        final Inventory i = Bukkit.createInventory(p, invSize);
        if (pcm.get("inventory." + group + ".ender.slot") == null) return i;
        for (int slot = 0; slot < invSize; slot++) {
            ItemStack is = pcm.getItemStack("inventory." + group + ".ender.slot." + slot);
            if (is == null) continue;
            i.setItem(slot, is);
        }
        return i;
    }

    public void saveInventory(OfflinePlayer op, String world, Inventory i) {
        if (!Config.separateInv) return;
        String group = getWorldGroup(plugin.getServer().getWorld(world));
        if (group == null) return;
        PConfManager pcm = PConfManager.getPConfManager(op);
        for (int slot = 0; slot < i.getSize(); slot++) {
            pcm.set("inventory." + group + ".slot." + slot, i.getItem(slot));
        }
        if (i instanceof PlayerInventory) {
            PlayerInventory pi = (PlayerInventory) i;
            pcm.set("inventory." + group + ".slot.helm", pi.getHelmet());
            pcm.set("inventory." + group + ".slot.chestplate", pi.getChestplate());
            pcm.set("inventory." + group + ".slot.leggings", pi.getLeggings());
            pcm.set("inventory." + group + ".slot.boots", pi.getBoots());
        }
        pcm.set("inventory." + group + ".size", i.getSize());
    }

    private void saveInventory(Player p, Inventory i) {
        if (!Config.separateInv) return;
        saveInventory(p, i, p.getWorld());
    }

    private void saveInventory(Player p, Inventory i, World w) {
        if (!Config.separateInv) return;
        String group = getWorldGroup(w);
        if (group == null) return;
        PConfManager pcm = PConfManager.getPConfManager(p);
        for (int slot = 0; slot < i.getSize(); slot++) {
            pcm.set("inventory." + group + ".slot." + slot, i.getItem(slot));
        }
        if (i instanceof PlayerInventory) {
            PlayerInventory pi = (PlayerInventory) i;
            pcm.set("inventory." + group + ".slot.helm", pi.getHelmet());
            pcm.set("inventory." + group + ".slot.chestplate", pi.getChestplate());
            pcm.set("inventory." + group + ".slot.leggings", pi.getLeggings());
            pcm.set("inventory." + group + ".slot.boots", pi.getBoots());
        }
        pcm.set("inventory." + group + ".size", i.getSize());
        if (Config.separateXP) {
            pcm.set("inventory." + group + ".xp", p.getExp());
            pcm.set("inventory." + group + ".xplevel", p.getLevel());
        }
    }

    public Inventory getOfflinePlayerEnderInventory(OfflinePlayer op, String world) {
        World w = plugin.getServer().getWorld(world);
        if (w == null) return null;
        String group = getWorldGroup(w);
        if (group == null) return null;
        PConfManager pcm = PConfManager.getPConfManager(op);
        if (!pcm.exists()) pcm.createFile();
        Integer invSize = pcm.getInt("inventory." + group + ".ender.size");
        final Inventory i = plugin.getServer().createInventory(null, InventoryType.PLAYER.getDefaultSize(), "OEC;" + world + ": " + op.getName());
        if (pcm.get("inventory." + group + ".ender.slot") == null) return i;
        for (int slot = 0; slot < invSize; slot++) {
            ItemStack is = pcm.getItemStack("inventory." + group + ".ender.slot." + slot);
            if (is == null) continue;
            i.setItem(slot, is);
        }
        return i;
    }

    public Inventory getOfflinePlayerInventory(OfflinePlayer op, String world) {
        World w = plugin.getServer().getWorld(world);
        if (w == null) return null;
        String group = getWorldGroup(w);
        if (group == null) return null;
        PConfManager pcm = PConfManager.getPConfManager(op);
        if (!pcm.exists()) pcm.createFile();
        Integer invSize = pcm.getInt("inventory." + group + ".size");
        final Inventory i = plugin.getServer().createInventory(null, InventoryType.PLAYER.getDefaultSize(), "OPI;" + world + ": " + op.getName());
        if (pcm.get("inventory." + group + ".slot") == null) return i;
        for (int slot = 0; slot < invSize; slot++) {
            ItemStack is = pcm.getItemStack("inventory." + group + ".slot." + slot);
            if (is == null) continue;
            i.setItem(slot, is);
        }
        return i;
    }

    private PlayerInventory getInventory(Player p) {
        String group = getWorldGroup(p.getWorld());
        if (group == null) return null;
        PConfManager pcm = PConfManager.getPConfManager(p);
        if (!pcm.exists()) pcm.createFile();
        Integer invSize = pcm.getInt("inventory." + group + ".size");
        final PlayerInventory i = p.getInventory();
        i.clear();
        if (pcm.get("inventory." + group + ".slot") == null) return i;
        for (int slot = 0; slot < invSize; slot++) {
            ItemStack is = pcm.getItemStack("inventory." + group + ".slot." + slot);
            if (is == null) continue;
            i.setItem(slot, is);
        }
        i.setHelmet(pcm.getItemStack("inventory." + group + ".slot.helm"));
        i.setChestplate(pcm.getItemStack("inventory." + group + ".slot.chestplate"));
        i.setLeggings(pcm.getItemStack("inventory." + group + ".slot.leggings"));
        i.setBoots(pcm.getItemStack("inventory." + group + ".slot.boots"));
        if (Config.separateXP) {
            Float xp = pcm.getFloat("inventory." + group + ".xp");
            Integer xpLevel = pcm.getInt("inventory." + group + ".xplevel");
            p.setExp(xp);
            p.setLevel(xpLevel);
        }
        return i;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!Config.separateInv) return;
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return; // only serve those configured
        getInventory(p); // sets inv of player
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!Config.separateInv) return;
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (!Config.separateInv) return;
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
        if (!Config.separateInv) return;
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!Config.separateInv) return;
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE) return; // this doesn't affect us
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onXP(PlayerExpChangeEvent e) {
        if (!Config.separateInv) return;
        Player p = e.getPlayer();
        if (getWorldGroup(p.getWorld()) == null) return;
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onEnderChestClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        Inventory i = e.getInventory();
        if (i.getType() != InventoryType.ENDER_CHEST) return;
        saveEnderInventory(p, i);
    }

    @EventHandler
    public void onEnderChestOpen(InventoryOpenEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        Inventory i = e.getInventory();
        if (i.getType() != InventoryType.ENDER_CHEST) return;
        Inventory ender = getEnderInventory(p);
        if (ender == null) return;
        i.setContents(ender.getContents());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!Config.separateInv) return;
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        if (e.getInventory().getType() != InventoryType.PLAYER) return;
        InventoryHolder thisIh = e.getInventory().getHolder();
        InventoryHolder playIh = p.getInventory().getHolder();
        if (thisIh == null || playIh == null) return;
        if (!thisIh.equals(playIh)) return; // only save their inv when they close /their/ inv
        String group = getWorldGroup(p.getWorld());
        if (group == null) return; // only manage worlds that are in the config
        saveInventory(p, p.getInventory());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        if (!Config.separateInv) return;
        Player p = e.getPlayer();
        String group = getWorldGroup(p.getWorld()); // get world group (new world)
        if (group == null) return; // only serve those configured
        saveInventory(p, p.getInventory(), e.getFrom()); // save old inventory
        getInventory(p); // sets inv of player
    }

    @EventHandler
    public void potionEffects(PlayerChangedWorldEvent e) {
        if (!Config.separateInv) return;
        if (!Config.removePotionEffects) return;
        Player p = e.getPlayer();
        Collection<PotionEffect> potionEffects = p.getActivePotionEffects();
        if (potionEffects.isEmpty()) return;
        for (PotionEffect pe : potionEffects) {
            if (!p.hasPotionEffect(pe.getType())) continue;
            p.removePotionEffect(pe.getType());
        }
    }

    @EventHandler
    public void closeOfflineInventory(InventoryCloseEvent e) {
        Inventory i = e.getInventory();
        if (i.getHolder() != null) return;
        String name = i.getName();
        String world;
        String opName;
        try {
            world = name.split(";")[1].split(":")[0];
            opName = name.split(": ")[1];
        } catch (Exception ex) {
            return;
        }
        if (name.startsWith("OEC;")) saveEnderInventory(plugin.getServer().getOfflinePlayer(opName), world, i);
        else if (name.startsWith("OPI;")) saveInventory(plugin.getServer().getOfflinePlayer(opName), world, i);
    }
}
