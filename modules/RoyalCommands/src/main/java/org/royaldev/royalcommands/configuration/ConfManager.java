package org.royaldev.royalcommands.configuration;

import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfManager extends GeneralConfManager {

    private static final Map<String, ConfManager> confs = new HashMap<>();
    private final Object saveLock = new Object();
    private final String path;
    private final String name;
    private File pconfl = null;

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param filename Filename (local) for the config
     */
    ConfManager(String filename) {
        super();
        File dataFolder = RoyalCommands.dataFolder;
        path = dataFolder + File.separator + filename;
        pconfl = new File(path);
        try {
            load(pconfl);
        } catch (Exception ignored) {
        }
        name = filename;
    }

    /**
     * Configuration file manager
     * <p/>
     * If file does not exist, it will be created.
     *
     * @param file File object for the config
     */
    ConfManager(File file) {
        this(file.getName());
    }

    /**
     * Just to prevent construction outside of package.
     */
    @SuppressWarnings("unused")
    private ConfManager() {
        path = "";
        name = "";
    }

    public static ConfManager getConfManager(String s) {
        synchronized (confs) {
            if (confs.containsKey(s)) return confs.get(s);
            final ConfManager cm = new ConfManager(s);
            confs.put(s, cm);
            return cm;
        }
    }

    public static boolean isManagerCreated(String s) {
        synchronized (confs) {
            return confs.containsKey(s);
        }
    }

    public static void saveAllManagers() {
        synchronized (confs) {
            for (ConfManager cm : confs.values()) cm.forceSave();
        }
    }

    public static void removeAllManagers() {
        Collection<ConfManager> oldConfs = new ArrayList<>();
        oldConfs.addAll(confs.values());
        synchronized (confs) {
            for (ConfManager cm : oldConfs) cm.discard(false);
        }
    }

    public static int managersCreated() {
        synchronized (confs) {
            return confs.size();
        }
    }

    public static Collection<ConfManager> getAllManagers() {
        synchronized (confs) {
            return Collections.synchronizedCollection(confs.values());
        }
    }

    public void reload() {
        forceSave();
        try {
            load(pconfl);
        } catch (Exception ignored) {
        }
    }

    public boolean exists() {
        return pconfl.exists();
    }

    public boolean createFile() {
        try {
            return pconfl.createNewFile();
        } catch (IOException ignored) {
            return false;
        }
    }

    public void forceSave() {
        synchronized (saveLock) {
            try {
                save(pconfl);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    /**
     * Removes the reference to this manager without saving.
     */
    public void discard() {
        discard(false);
    }

    /**
     * Removes the reference to this manager.
     *
     * @param save Save manager before removing references?
     */
    public void discard(boolean save) {
        synchronized (confs) {
            if (save) forceSave();
            confs.remove(name);
        }
    }
}
