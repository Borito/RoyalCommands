package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;
import java.util.List;

public class CmdIgnore implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdIgnore(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ignore")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.ignore")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage());
                return true;
            }
            String name = args[0].toLowerCase();

            Player t = plugin.getServer().getPlayer(name);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.ignore")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot ignore that player!");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            List<String> players = pcm.getStringList("ignoredby");
            if (players == null) players = new ArrayList<String>();
            for (String ignored : players) {
                if (ignored.toLowerCase().equals(cs.getName().toLowerCase())) {
                    players.remove(ignored);
                    pcm.set("ignoredby", players);
                    cs.sendMessage(MessageColor.POSITIVE + "You have stopped ignoring " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                    return true;
                }
            }
            players.add(cs.getName());
            pcm.set("ignoredby", players);
            cs.sendMessage(MessageColor.POSITIVE + "You are now ignoring " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
