package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

public class CmdDeafen implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdDeafen(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deafen")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.deafen")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1 && !(cs instanceof Player)) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String name = (args.length < 1) ? cs.getName() : args[0];
            if (!name.equalsIgnoreCase(cs.getName()) && !plugin.ah.isAuthorized(cs, "rcmds.others.deafen")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to deafen other players!");
                return true;
            }
            Player t = plugin.getServer().getPlayer(name);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.deafen") && !t.getName().equals(cs.getName())) {
                cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to deafen that player!");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            Boolean isDeaf = pcm.getBoolean("deaf");
            if (isDeaf == null) isDeaf = false;
            pcm.set("deaf", !isDeaf);
            cs.sendMessage(MessageColor.POSITIVE + "Toggled deaf " + MessageColor.NEUTRAL + BooleanUtils.toStringOnOff(!isDeaf) + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
