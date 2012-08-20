package org.royaldev.royalcommands.playermanagers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.json.JSONArray;
import org.royaldev.royalcommands.json.JSONException;
import org.royaldev.royalcommands.json.JSONObject;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.royaldev.royalcommands.Converters.*;

@SuppressWarnings("unused")
/**
 * Player configuration manager
 *
 * @author jkcclemens
 * @see ConfManager
 */
public class H2PConfManager {

    private Connection c;
    private PreparedStatement stmt;
    private final Logger log = RoyalCommands.instance.getLogger();
    private JSONObject options;
    private OfflinePlayer t;
    private String name;

    /**
     * Player configuration manager
     *
     * @param p Player to manage
     */
    public H2PConfManager(OfflinePlayer p) throws SQLException, JSONException {
        t = p;
        name = t.getName().toLowerCase();
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            log.severe("Could not find H2 driver! Please make sure that h2.jar is in your classpath.");
        }
        String path = RoyalCommands.instance.getDataFolder() + File.separator + RoyalCommands.instance.h2Path;
        c = DriverManager.getConnection("jdbc:h2:" + path + ";AUTO_RECONNECT=TRUE", RoyalCommands.instance.h2User, RoyalCommands.instance.h2Pass);
        c.createStatement().execute("CREATE TABLE IF NOT EXISTS `userdata` (id int NOT NULL AUTO_INCREMENT, name text NOT NULL, options text);");
        createEntry();
        stmt = c.prepareStatement("SELECT options FROM `userdata` WHERE `name` = ?;");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        rs.last();
        options = new JSONObject(rs.getString("options"));
        stmt.close();
    }

    /**
     * Player configuration manager.
     *
     * @param p Player to manage
     */
    public H2PConfManager(String p) throws SQLException, JSONException {
        t = Bukkit.getOfflinePlayer(p);
        name = t.getName().toLowerCase();
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            log.severe("Could not find H2 driver! Please make sure that h2.jar is in your classpath.");
        }
        String path = RoyalCommands.instance.getDataFolder() + File.separator + RoyalCommands.instance.h2Path;
        c = DriverManager.getConnection("jdbc:h2:" + path + ";AUTO_RECONNECT=TRUE", RoyalCommands.instance.h2User, RoyalCommands.instance.h2Pass);
        c.createStatement().execute("CREATE TABLE IF NOT EXISTS `userdata` (id int NOT NULL AUTO_INCREMENT, name text NOT NULL, options text);");
        createEntry();
        stmt = c.prepareStatement("SELECT `options` FROM `userdata` WHERE `name` = ?;");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        rs.last();
        options = new JSONObject(rs.getString("options"));
        stmt.close();
    }

    /**
     * Creates entry in database for player.
     *
     * @return true if added, false if it already exists
     */
    private boolean createEntry() throws SQLException, JSONException {
        stmt = c.prepareStatement("SELECT 1 FROM `userdata` WHERE `name`=?;");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        rs.last();
        if (rs.getRow() > 0) return false;
        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("dispname", name);
        String options = jo.toString();
        stmt = c.prepareStatement("INSERT INTO `userdata` (name, options) VALUES (?, ?);");
        stmt.setString(1, name);
        stmt.setString(2, options);
        stmt.execute();
        stmt.close();
        return true;
    }

    /**
     * Gets an object from config
     *
     * @param path Path in the yml to fetch from
     * @return Object or null if path does not exist or if config doesn't exist
     */
    public Object get(String path) {
        return get(path, null);
    }

    /**
     * Gets an object from config
     * <p/>
     * Source modified from Bukkit's MemorySection
     *
     * @param path Path in the yml to fetch from
     * @param def  Default if result is null
     * @return Object or def if path does not exist or if config doesn't exist
     */
    public Object get(String path, Object def) {

        if (path.length() == 0) {
            return options;
        }

        final char separator = '.';
        int i1 = -1, i2;
        JSONObject section = options;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            section = section.optJSONObject(path.substring(i2, i1));
            if (section == null) return def;
        }

        String key = path.substring(i2);
        if (section == options) {
            Object result = options.opt(key);
            return (result == null) ? def : result;
        }
        Object result = section.opt(key);
        return (result == null) ? def : result;
    }

    /**
     * Sets an object in config
     * <p/>
     * Source modified from Bukkit's MemorySection
     *
     * @param value An object
     * @param path  Path in the yml to set
     */
    public void set(Object value, String path) throws SQLException, JSONException {
        final char separator = '.';
        int i1 = -1, i2;
        JSONObject section = options;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            JSONObject subSection = section.optJSONObject(node);
            if (subSection == null)
                section = section.put(node, new JSONObject()).optJSONObject(node);
            else section = subSection;
        }

        String key = path.substring(i2);
        if (section == options) options.put(key, value);
        section.put(key, value);

        stmt = c.prepareStatement("UPDATE `userdata` SET `options`=? WHERE `name`=?;");
        stmt.setString(1, options.toString());
        stmt.setString(2, name);
        stmt.execute();
        stmt.close();
    }

    /**
     * Gets a string list from config
     *
     * @param path Path in the yml to fetch from
     * @return String list or null if path does not exist or if config doesn't exist
     */
    public List<String> getStringList(String path) {
        Object o = get(path, null);
        if (o == null) return null;
        String s = o.toString();
        if (s.trim().equals("")) return null;
        JSONArray ja;
        try {
            ja = new JSONArray(s);
        } catch (JSONException e) {
            return null;
        }
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < ja.length(); i++) {
            try {
                if (!(ja.get(i) instanceof String)) continue;
                l.add((String) ja.get(i));
            } catch (JSONException ignored) {
            }
        }
        return l;
    }

    /**
     * Gets a boolean from config
     *
     * @param path Path in the yml to fetch from
     * @return Boolean or null if path does not exist or if config doesn't exist
     */
    public boolean getBoolean(String path) {
        return toBoolean(get(path, false));
    }

    /**
     * Gets an integer from config
     *
     * @param path Path in the yml to fetch from
     * @return Integer or null if path does not exist or if config doesn't exist
     */
    public Integer getInteger(String path) {
        return toInt(get(path, -1));
    }

    /**
     * Gets a long from config
     *
     * @param path Path in the yml to fetch from
     * @return Long or null if path does not exist or if config doesn't exist
     */
    public Long getLong(String path) {
        return toLong(get(path, -1L));
    }

    /**
     * Gets a double from config
     *
     * @param path Path in the yml to fetch from
     * @return Double or null if path does not exist or if config doesn't exist
     */
    public Double getDouble(String path) {
        return toDouble(get(path, -1D));
    }

    /**
     * Gets a string from config
     *
     * @param path Path in the yml to fetch from
     * @return String or null if path does not exist or if config doesn't exist
     */
    public String getString(String path) {
        return toStringy(get(path, ""));
    }

    /**
     * Gets a float from config
     *
     * @param path Path in the yml to fetch from
     * @return Float or null if path does not exist or if config doesn't exist or if not valid float
     */
    public Float getFloat(String path) {
        return toFloat(get(path, -1F));
    }

    public JSONObject getJSONObject(String path) {
        Object o = get(path, null);
        if (o == null) return null;
        if (o instanceof JSONObject) return (JSONObject) o;
        else return null;
    }

}
