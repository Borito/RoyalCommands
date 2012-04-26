package org.royaldev.royalcommands;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ConfigManager {

    private Yaml y = new Yaml();
    private Map c = null;
    private File f = null;
    private String path;

    public ConfigManager(String filepath) throws FileNotFoundException {
        path = filepath;
        f = new File(path);
        if (!f.exists()) return;
        FileInputStream fstream = new FileInputStream(f);
        c = y.loadAs(fstream, Map.class);
    }

    public void save() {
        FileWriter fstream;
        try {
            fstream = new FileWriter(path);
        } catch (IOException e) {
            return;
        }
        BufferedWriter b = new BufferedWriter(fstream);
        y.dump(c, b);
    }

    public void load() {
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            return;
        }
        c = y.loadAs(fis, Map.class);
    }

    public String getString(String path) {
        Object o = c.get(path);
        if (!(o instanceof String)) return null;
        return (String) o;
    }

    public Boolean getBoolean(String path) {
        Object o = c.get(path);
        if (!(o instanceof Boolean)) return null;
        return (Boolean) o;
    }

    public Double getDouble(String path) {
        Object o = c.get(path);
        if (!(o instanceof Double)) return null;
        return (Double) o;
    }

    public Long getLong(String path) {
        Object o = c.get(path);
        if (!(o instanceof Long)) return null;
        return (Long) o;
    }

    public Integer getInt(String path) {
        Object o = c.get(path);
        if (!(o instanceof Integer)) return null;
        return (Integer) o;
    }

    public Float getFloat(String path) {
        return Float.valueOf(String.valueOf(c.get(path)));
    }

    public List<String> getStringList(String path) {
        Object o = c.get(path);
        if (!(o instanceof List)) return null;
        List l = (List) c.get(path);
        List<String> ll = new ArrayList<String>();
        for (Object oo : l) {
            String s;
            if (!(oo instanceof String)) s = String.valueOf(oo);
            else s = (String) oo;
            ll.add(s);
        }
        return ll;
    }

    public void setStringList(String path, List<String> value) {
        c.put(path, value);
    }


}
