package com.smilingdevil.devilstats.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author devil
 */
@SuppressWarnings("unused")
public class DevilStats {
    @SuppressWarnings("serial")
    public class SendPluginInformationException extends Exception {
        public SendPluginInformationException(String ex) {
            super(ex);
        }
    }

    @SuppressWarnings("serial")
    public class NotMainClassException extends Exception {
        public NotMainClassException() {
            super("You are not accessing DevilStats from your main class. Please use your main class instead!");
        }
    }

    private String version = null;
    private String name = null;
    private String author = null;

    private enum ActionType {
        STARTUP, SHUTDOWN, JUST_CHECKING
    }

    private enum DataType {
        COUNT, AUTHOR, STATUS
    }

    public DevilStats(JavaPlugin given) throws DevilStats.NotMainClassException {
        if (given == null) {
            throw new DevilStats.NotMainClassException();
        } else {
            name = given.getDescription().getName();
            version = given.getDescription().getVersion();
            ArrayList<String> authors = given.getDescription().getAuthors();
            author = authors.get(0);
            Logger log = Logger.getLogger("Minecraft");
            log.log(Level.INFO, "[DevilStats] Successfully hooked into " + name + " version " + version);
        }
    }

    public void sendStartup() {
        sendData(DevilStats.ActionType.STARTUP);
    }

    public void sendShutdown() {
        sendData(DevilStats.ActionType.SHUTDOWN);
    }

    public void checkConnection() {
        sendData(DevilStats.ActionType.JUST_CHECKING);
    }

    public String getCount() {
        return getData(DevilStats.DataType.COUNT);
    }

    public String getAuthor() {
        return getData(DevilStats.DataType.AUTHOR);
    }

    public String getStatus() {
        return getData(DevilStats.DataType.STATUS);
    }

    private String getData(DevilStats.DataType type) {
        String action;
        action = type.name().toLowerCase();
        try {
            URL url = new URL("http://stats.smilingdevil.com/api?action=" + action + "&version=" + version + "&author=" + author + "&name=" + name);
            URLConnection con = url.openConnection();
            return con.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean sendData(DevilStats.ActionType type) {
        try {
            // Get action type
            String action;
            action = type.name().toLowerCase();
            // Define URL for stats with action type
            URL url = new URL("http://stats.smilingdevil.com/api?action="
                    + action + "&version=" + version + "&author=" + author
                    + "&name=" + name);
            // Open connection with server
            URLConnection con = url.openConnection();
            // Get contents of page
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            // Store contents of page
            String returned = in.readLine();
            if (returned != null && returned.equals("SUCCESS")) {
                return true; // We're all good
            } else {
                // Something went wrong, wasn't successful / was null
                throw new DevilStats.SendPluginInformationException(
                        "An error occured while sending information: " + con);
            }
        } catch (Exception e) {
            return false;
        }
    }
}