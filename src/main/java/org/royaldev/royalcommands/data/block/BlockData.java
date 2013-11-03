package org.royaldev.royalcommands.data.block;


import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BlockData {

    private static final Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
    private final Plugin p;
    private final BlockLocation bl;
    private final Map<String, Object> thisData = new HashMap<String, Object>();

    public BlockData(Plugin p, BlockLocation bl) {
        this.p = p;
        this.bl = bl;
        synchronized (data) {
            if (data.isEmpty()) caughtLoad();
            synchronized (thisData) {
                if (data.containsKey(bl.toString())) thisData.putAll(data.get(bl.toString()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void load() throws IOException, ClassNotFoundException {
        final File f = new File(p.getDataFolder(), "blockdata.dat");
        if (!f.exists()) return;
        final ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(f)));
        final Object o = ois.readObject();
        ois.close();
        if (!(o instanceof Map)) return;
        HashMap<String, Map<String, Object>> data = (HashMap<String, Map<String, Object>>) o;
        synchronized (BlockData.data) {
            BlockData.data.clear();
            BlockData.data.putAll(data);
        }
        synchronized (thisData) {
            thisData.clear();
            if (BlockData.data.containsKey(bl.toString())) thisData.putAll(BlockData.data.get(bl.toString()));
        }
    }

    public boolean caughtLoad() {
        try {
            load();
            return true;
        } catch (EOFException ignored) {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save() throws IOException, ClassNotFoundException {
        final File f = new File(p.getDataFolder(), "blockdata.dat");
        if (!f.exists()) if (!f.createNewFile()) throw new IOException("Couldn't create blockdata.dat");
        data.put(bl.toString(), thisData);
        final ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(new File(p.getDataFolder(), "blockdata.dat"))));
        synchronized (data) {
            oos.writeObject(data);
        }
        oos.flush();
        oos.close();
    }

    public boolean caughtSave() {
        try {
            save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void set(String key, Object value) {
        synchronized (thisData) {
            thisData.put(key, value);
        }
    }

    public Object get(String key) {
        synchronized (thisData) {
            return thisData.get(key);
        }
    }

    public boolean contains(String key) {
        synchronized (thisData) {
            return thisData.containsKey(key);
        }
    }

    public void remove(String key) {
        synchronized (thisData) {
            thisData.remove(key);
        }
    }

    public static class BlockLocation implements Serializable {
        private static final long serialVersionUID = 3232014L;

        private String world;
        private int x;
        private int y;
        private int z;

        public BlockLocation(String world, int x, int y, int z) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlockLocation(Location l) {
            this.world = l.getWorld().getName();
            this.x = l.getBlockX();
            this.y = l.getBlockY();
            this.z = l.getBlockZ();
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public String getWorld() {
            return world;
        }

        public void setWorld(String world) {
            this.world = world;
        }

        @Override
        public String toString() {
            return String.format("BL:%s,%s,%s,%s", this.world, this.x, this.y, this.z);
        }
    }

}
