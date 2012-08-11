package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    private final Logger log = RoyalCommands.instance.getLogger();

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
        setupWorlds();
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
            log.info("Setting flags on world " + ws + " as specified in worlds.yml.");
            w.setSpawnFlags(config.getBoolean(path + "spawnmonsters"), config.getBoolean(path + "spawnanimals"));
            w.setKeepSpawnInMemory(config.getBoolean("keepspawnloaded"));
            w.setPVP(config.getBoolean(path + "pvp"));
            w.setMonsterSpawnLimit(config.getInteger(path + "monsterspawnlimit"));
            w.setAnimalSpawnLimit(config.getInteger(path + "animalspawnlimit"));
            w.setWaterAnimalSpawnLimit(config.getInteger(path + "wateranimalspawnlimit"));
            w.setTicksPerAnimalSpawns(config.getInteger(path + "animalspawnticks"));
            w.setTicksPerMonsterSpawns(config.getInteger(path + "monsterspawnticks"));
            Difficulty d;
            try {
                d = Difficulty.valueOf(config.getString(path + "difficulty").toUpperCase());
            } catch (Exception e) {
                d = Difficulty.NORMAL;
            }
            w.setDifficulty(d);
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
        boolean contains = false;
        File[] fs = Bukkit.getServer().getWorldContainer().listFiles();
        if (fs == null) throw new NullPointerException("Could not read world files");
        for (File f : fs) {
            if (f.getName().equals(name)) contains = true;
        }
        if (!contains) throw new IllegalArgumentException("No such world!");
        WorldCreator wc = new WorldCreator(name);
        World w = wc.createWorld();
        loadedWorlds.add(w.getName());
        return w;
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
            config.setInteger(w.getMaxHeight(), path + "maxheight");
            config.setInteger(w.getMonsterSpawnLimit(), path + "monsterspawnlimit");
            config.setInteger(w.getAnimalSpawnLimit(), path + "animalspawnlimit");
            config.setInteger(w.getWaterAnimalSpawnLimit(), path + "wateranimalspawnlimit");
            config.setLong(w.getTicksPerAnimalSpawns(), path + "animalspawnticks");
            config.setLong(w.getTicksPerMonsterSpawns(), path + "monsterspawnticks");
            config.setString(w.getDifficulty().name(), path + "difficulty");
            config.setString(w.getWorldType().name(), path + "worldtype");
            config.setString(w.getEnvironment().name(), path + "environment");
            config.setLong(w.getSeed(), path + "seed");
            config.setBoolean(false, path + "freezetime");
            config.setBoolean(true, path + "loadatstartup");
        }
    }

}
