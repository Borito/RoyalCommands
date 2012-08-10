package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {

    private class WorldWatcher implements Listener {
        @EventHandler
        public void worldUnload(WorldUnloadEvent e) {
            synchronized (loadedWorlds) {
                if (loadedWorlds.contains(e.getWorld().getName()))
                    loadedWorlds.remove(e.getWorld().getName());
            }
        }

        @EventHandler
        public void worldLoad(WorldLoadEvent e) {
            synchronized (loadedWorlds) {
                if (!loadedWorlds.contains(e.getWorld().getName()))
                    loadedWorlds.add(e.getWorld().getName());
                addNewToConfig();
            }
        }
    }

    private final List<String> loadedWorlds = new ArrayList<String>();
    private final List<String> configuredWorlds = new ArrayList<String>();

    private final ConfManager config = new ConfManager("worlds.yml");

    public WorldManager() {
        if (!config.exists()) config.createFile();
        Bukkit.getPluginManager().registerEvents(new WorldWatcher(), RoyalCommands.instance);
        if (config.getConfigurationSection("worlds") != null) {
            synchronized (configuredWorlds) {
                for (String key : config.getConfigurationSection("worlds").getKeys(false)) {
                    configuredWorlds.add(key);
                }
            }
        }
        for (World w : Bukkit.getWorlds()) loadedWorlds.add(w.getName());
        addNewToConfig();
    }

    public ConfManager getConfig() {
        return config;
    }

    public void reloadConfig() {
        config.reload();
    }

    public void addNewToConfig() {
        for (String ws : loadedWorlds) {
            if (configuredWorlds.contains(ws)) continue;
            World w = Bukkit.getWorld(ws);
            if (w == null) continue;
            config.setString(ws, "worlds." + ws + ".name");
            config.setBoolean(w.getAllowMonsters(), "worlds." + ws + ".monsters");
            config.setBoolean(false, "worlds." + ws + ".freezetime");
        }
    }

}
