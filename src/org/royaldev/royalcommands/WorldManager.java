package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.royaldev.royalcommands.listeners.InventoryListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorldManager {

    private class WorldWatcher implements Listener {
        @EventHandler
        public void worldUnload(WorldUnloadEvent e) {
            if (e.isCancelled()) return;
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

        @EventHandler
        public void onWorldTele(PlayerChangedWorldEvent e) {
            World to = e.getPlayer().getWorld();
            if (!configuredWorlds.contains(to.getName())) return;
            if (!loadedWorlds.contains(to.getName())) return;
            GameMode gm;
            String mode = config.getString("worlds." + to.getName() + ".gamemode", "SURVIVAL").trim();
            if (mode == null) mode = "SURVIVAL"; // how?
            try {
                gm = GameMode.valueOf(mode);
            } catch (IllegalArgumentException ex) {
                gm = GameMode.SURVIVAL;
            }
            e.getPlayer().setGameMode(gm);
        }

        @EventHandler
        public void onWeather(WeatherChangeEvent e) {
            if (e.isCancelled()) return;
            World w = e.getWorld();
            if (!configuredWorlds.contains(w.getName())) return;
            if (!loadedWorlds.contains(w.getName())) return;
            boolean allowWeather = config.getBoolean("worlds." + w.getName() + ".weather", true);
            if (!allowWeather) e.setCancelled(true);
        }

        @EventHandler
        public void onThunder(ThunderChangeEvent e) {
            if (e.isCancelled()) return;
            World w = e.getWorld();
            if (!configuredWorlds.contains(w.getName())) return;
            if (!loadedWorlds.contains(w.getName())) return;
            boolean allowWeather = config.getBoolean("worlds." + w.getName() + ".weather", true);
            if (!allowWeather) e.setCancelled(true);
        }

    }

    private final List<String> loadedWorlds = new ArrayList<String>();
    private final List<String> configuredWorlds = new ArrayList<String>();

    public static InventoryListener il;

    private final ConfManager config = new ConfManager("worlds.yml");

    private final Logger log = RoyalCommands.instance.getLogger();

    public WorldManager() {
        if (!RoyalCommands.useWorldManager) return;
        if (!config.exists()) config.createFile();
        if (config.getConfigurationSection("worlds") != null) {
            synchronized (configuredWorlds) {
                for (String key : config.getConfigurationSection("worlds").getKeys(false)) {
                    configuredWorlds.add(key);
                }
            }
        }
        for (World w : Bukkit.getWorlds()) {
            if (loadedWorlds.contains(w.getName())) continue;
            loadedWorlds.add(w.getName());
        }
        addNewToConfig();
        setupWorlds();
        for (String s : loadedWorlds) {
            if (!configuredWorlds.contains(s)) continue;
            World w = Bukkit.getWorld(s);
            if (w == null) continue;
            boolean isStorming = config.getBoolean("worlds." + w.getName() + ".is_storming_if_weather_false", false);
            w.setStorm(isStorming);
        }
        il = new InventoryListener();
        Bukkit.getPluginManager().registerEvents(il, RoyalCommands.instance);
        Bukkit.getPluginManager().registerEvents(new WorldWatcher(), RoyalCommands.instance);
    }

    public ConfManager getConfig() {
        return config;
    }

    public void reloadConfig() {
        config.reload();
        setupWorlds();
    }

    public void setupWorlds() {
        for (String ws : configuredWorlds) {
            String path = "worlds." + ws + ".";
            World w = null;
            if (config.getBoolean(path + "loadatstartup") && Bukkit.getWorld(ws) == null) {
                try {
                    w = loadWorld(ws);
                } catch (IllegalArgumentException e) {
                    log.warning("Could not load world " + ws + " as specified in worlds.yml: " + e.getMessage());
                    continue;
                } catch (NullPointerException e) {
                    log.warning("Could not load world " + ws + " as specified in worlds.yml: " + e.getMessage());
                    continue;
                }
            }
            if (!loadedWorlds.contains(ws)) continue;
            if (w == null) w = Bukkit.getWorld(ws);
            if (w == null) {
                log.warning("Could not manage world " + ws + ": No such world");
                continue;
            }
            w.setSpawnFlags(config.getBoolean(path + "spawnmonsters", true), config.getBoolean(path + "spawnanimals", true));
            w.setKeepSpawnInMemory(config.getBoolean("keepspawnloaded", true));
            w.setPVP(config.getBoolean(path + "pvp", true));
            w.setMonsterSpawnLimit(config.getInteger(path + "monsterspawnlimit", 70));
            w.setAnimalSpawnLimit(config.getInteger(path + "animalspawnlimit", 15));
            w.setWaterAnimalSpawnLimit(config.getInteger(path + "wateranimalspawnlimit", 5));
            w.setTicksPerAnimalSpawns(config.getInteger(path + "animalspawnticks", 400));
            w.setTicksPerMonsterSpawns(config.getInteger(path + "monsterspawnticks", 1));
            Difficulty d;
            try {
                d = Difficulty.valueOf(config.getString(path + "difficulty", "normal").toUpperCase());
            } catch (Exception e) {
                d = Difficulty.NORMAL;
            }
            w.setDifficulty(d);
        }
    }

    public void addToLoadedWorlds(String name) {
        synchronized (loadedWorlds) {
            if (!loadedWorlds.contains(name)) loadedWorlds.add(name);
        }
    }

    public void addToLoadedWorlds(World w) {
        synchronized (loadedWorlds) {
            if (!loadedWorlds.contains(w.getName())) loadedWorlds.add(w.getName());
        }
    }

    public void removeFromLoadedWorlds(String name) {
        synchronized (loadedWorlds) {
            if (loadedWorlds.contains(name)) loadedWorlds.remove(name);
        }
    }

    public void removeFromLoadedWorlds(World w) {
        synchronized (loadedWorlds) {
            if (loadedWorlds.contains(w.getName())) loadedWorlds.remove(w.getName());
        }
    }

    /**
     * Attempts to load a world.
     *
     * @param name Name of world to load (i.e. folder name)
     * @return Loaded world
     * @throws IllegalArgumentException If there is no such world
     * @throws NullPointerException     If could not read the world container's files
     */
    public World loadWorld(String name) throws IllegalArgumentException, NullPointerException {
        if (Bukkit.getServer().getWorldContainer() == null)
            throw new NullPointerException("Could not read world files!");
        File world = new File(Bukkit.getServer().getWorldContainer(), name);
        if (!world.exists()) throw new IllegalArgumentException("No such world!");
        if (!world.isDirectory()) throw new IllegalArgumentException("World is not a directory!");
        WorldCreator wc = new WorldCreator(name);
        String generator = config.getString("worlds." + name + ".generator", "DefaultGen");
        if (generator.equals("DefaultGen")) generator = null;
        wc.generator(generator);
        World w = wc.createWorld();
        synchronized (loadedWorlds) {
            loadedWorlds.add(w.getName());
        }
        return w;
    }

    /**
     * Attempts to unload a world
     *
     * @param w World to unload
     * @return If world was unloaded
     */
    public boolean unloadWorld(World w) {
        boolean worked = Bukkit.unloadWorld(w, true);
        synchronized (loadedWorlds) {
            if (loadedWorlds.contains(w.getName()) && worked)
                loadedWorlds.remove(w.getName());
        }
        return worked;
    }

    /**
     * Attempts to unload a world
     *
     * @param name Name of world to unload
     * @return If world was unloaded
     */
    public boolean unloadWorld(String name) {
        boolean worked = Bukkit.unloadWorld(name, true);
        synchronized (loadedWorlds) {
            if (loadedWorlds.contains(name) && worked) loadedWorlds.remove(name);
        }
        return worked;
    }

    public void addNewToConfig() {
        for (String ws : loadedWorlds) {
            if (configuredWorlds.contains(ws)) continue;
            World w = Bukkit.getWorld(ws);
            if (w == null) continue;
            String path = "worlds." + ws + ".";
            config.setString(ws, path + "displayname");
            config.setBoolean(w.getAllowMonsters(), path + "spawnmonsters");
            config.setBoolean(w.getAllowAnimals(), path + "spawnanimals");
            config.setBoolean(w.getKeepSpawnInMemory(), path + "keepspawnloaded");
            config.setBoolean(w.canGenerateStructures(), path + "generatestructures");
            config.setBoolean(w.getPVP(), path + "pvp");
            config.setBoolean(true, path + "weather");
            config.setInteger(w.getMaxHeight(), path + "maxheight");
            config.setInteger(w.getMonsterSpawnLimit(), path + "monsterspawnlimit");
            config.setInteger(w.getAnimalSpawnLimit(), path + "animalspawnlimit");
            config.setInteger(w.getWaterAnimalSpawnLimit(), path + "wateranimalspawnlimit");
            config.setLong(w.getTicksPerAnimalSpawns(), path + "animalspawnticks");
            config.setLong(w.getTicksPerMonsterSpawns(), path + "monsterspawnticks");
            config.setString(w.getDifficulty().name(), path + "difficulty");
            config.setString(w.getWorldType().name(), path + "worldtype");
            config.setString(w.getEnvironment().name(), path + "environment");
            config.setString(Bukkit.getServer().getDefaultGameMode().name(), path + "gamemode");
            if (w.getGenerator() == null)
                config.setString("DefaultGen", path + "generator");
            config.setLong(w.getSeed(), path + "seed");
            config.setBoolean(false, path + "freezetime");
            config.setBoolean(true, path + "loadatstartup");
        }
    }

    /**
     * Gets a world based on its alias (case-insensitive).
     *
     * @param name Alias of world
     * @return World or null if no matching alias
     */
    public World getWorld(String name) {
        World w;
        for (String s : loadedWorlds) {
            String path = "worlds." + s + ".";
            if (config.getString(path + "displayname", "").equalsIgnoreCase(name)) {
                w = Bukkit.getWorld(s);
                return w;
            }
        }
        return null;
    }

    /**
     * Gets a world based on its alias (case-sensitive).
     *
     * @param name Alias of world
     * @return World or null if no matching alias
     */
    public World getCaseSensitiveWorld(String name) {
        World w;
        for (String s : loadedWorlds) {
            String path = "worlds." + s + ".";
            if (config.getString(path + "displayname", "").equals(name)) {
                w = Bukkit.getWorld(s);
                return w;
            }
        }
        return null;
    }

}
