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
        final File dataFolder = RoyalCommands.dataFolder;
        this.path = dataFolder + File.separator + filename;
        this.pconfl = new File(this.path);
        try {
            this.load(this.pconfl);
        } catch (Exception ignored) {
        }
        this.name = filename;
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
        this.path = "";
        this.name = "";
    }

    public static ConfManager getConfManager(String s) {
        synchronized (ConfManager.confs) {
            if (ConfManager.confs.containsKey(s)) return ConfManager.confs.get(s);
            final ConfManager cm = new ConfManager(s);
            ConfManager.confs.put(s, cm);
            return cm;
        }
    }

    public static boolean isManagerCreated(String s) {
        synchronized (ConfManager.confs) {
            return ConfManager.confs.containsKey(s);
        }
    }

    public static void saveAllManagers() {
        synchronized (ConfManager.confs) {
            for (final ConfManager cm : ConfManager.confs.values()) cm.forceSave();
        }
    }

    public static void removeAllManagers() {
        final Collection<ConfManager> oldConfs = new ArrayList<>();
        oldConfs.addAll(ConfManager.confs.values());
        synchronized (ConfManager.confs) {
            for (final ConfManager cm : oldConfs) cm.discard(false);
        }
    }

    public static int managersCreated() {
        synchronized (ConfManager.confs) {
            return ConfManager.confs.size();
        }
    }

    public static Collection<ConfManager> getAllManagers() {
        synchronized (ConfManager.confs) {
            return Collections.synchronizedCollection(ConfManager.confs.values());
        }
    }

    public void reload() {
        forceSave();
        try {
            this.load(this.pconfl);
        } catch (Exception ignored) {
        }
    }

    public boolean exists() {
        return this.pconfl.exists();
    }

    public boolean createFile() {
        try {
            return this.pconfl.createNewFile();
        } catch (IOException ignored) {
            return false;
        }
    }

    public void forceSave() {
        synchronized (this.saveLock) {
            try {
                this.save(this.pconfl);
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
        this.discard(false);
    }

    /**
     * Removes the reference to this manager.
     *
     * @param save Save manager before removing references?
     */
    public void discard(boolean save) {
        synchronized (ConfManager.confs) {
            if (save) this.forceSave();
            ConfManager.confs.remove(this.name);
        }
    }
}
