package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class CmdAccountStatus implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdAccountStatus(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("accountstatus")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.accountstatus")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String name = args[0];
            final OfflinePlayer p = RUtils.getOfflinePlayer(name);
            name = p.getName();
            URL u;
            try {
                u = new URL("https://minecraft.net/haspaid.jsp?user=" + URLEncoder.encode(name, "UTF-8"));
            } catch (MalformedURLException ex) {
                cs.sendMessage(MessageColor.NEGATIVE + "An unthinkable error happened. Please let the developer know.");
                return true;
            } catch (UnsupportedEncodingException ex) {
                cs.sendMessage(MessageColor.NEGATIVE + "The UTF-8 encoding is not supported on this system!");
                return true;
            }
            boolean isPremium;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
                isPremium = br.readLine().equalsIgnoreCase("true");
            } catch (IOException ex) {
                cs.sendMessage(MessageColor.NEGATIVE + "Could not read from Minecraft's servers!");
                cs.sendMessage(MessageColor.NEGATIVE + ex.getMessage());
                return true;
            }
            String status = (isPremium) ? "premium" : "not premium";
            cs.sendMessage(MessageColor.NEUTRAL + name + MessageColor.POSITIVE + " is " + MessageColor.NEUTRAL + status + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
