/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilingdevil.devilstats.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author devil
 */
public class DevilStats {
    public class SendPluginInformation extends Exception {
        public SendPluginInformation(String ex) {
            super(ex);
        }
    }

    public enum ActionType {
        STARTUP,
        SHUTDOWN,
        JUST_CHECKING
    }

    public enum DataType {
        COUNT,
        AUTHOR,
        STATUS
    }

    private APIKey _key = null;

    public DevilStats(APIKey key) {
        this._key = key;
    }

    public String getData(DataType type) {
        String action = null;
        if (type.equals(DataType.COUNT)) {
            action = "count";
        } else if (type.equals(DataType.AUTHOR)) {
            action = "author";
        } else if (type.equals(DataType.STATUS)) {
            action = "status";
        }
        try {
            URL url = new URL("http://stats.smilingdevil.com/api?action=" + action + "&api_key="
                    + _key.getValue());
            URLConnection con = url.openConnection();
            return con.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean sendData(ActionType type) {
        try {
            // Get action type
            String action;
            if (type == ActionType.STARTUP) {
                action = "startup";
            } else if (type == ActionType.SHUTDOWN) {
                action = "shutdown";
            } else {
                action = "checking";
            }
            // Define URL for stats with action type
            URL url = new URL("http://stats.smilingdevil.com/api?action=" + action + "&api_key=" + _key.getValue());
            // Open connection with server
            URLConnection con = url.openConnection();
            // Get contents of page
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            // Store contents of page
            String returned = in.readLine();
            if (returned != null && returned.equals("SUCCESS")) { // Check if it's successful & not null
                return true; // We're all good
            } else {
                // Something went wrong, wasn't successful / was null
                throw new SendPluginInformation("An error occured while sending information: " + con);
            }
        } catch (Exception e) {
            return false;
        }
    }
}