package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSavePlayer implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSavePlayer(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("saveplayer")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.saveplayer")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!plugin.getNMSFace().hasSupport()) {
                cs.sendMessage(MessageColor.NEGATIVE + "No support for your Minecraft version!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
                return true;
            }
            if (!t.getName().equals(cs.getName()) && !plugin.ah.isAuthorized(cs, "rcmds.others.saveplayer")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot save other players' data!");
                return true;
            }
            try {
                plugin.getNMSFace().savePlayerData(t);
            } catch (IllegalArgumentException ex) {
                cs.sendMessage(MessageColor.NEGATIVE + "Couldn't save player data: " + MessageColor.NEUTRAL + ex.getMessage() + MessageColor.NEGATIVE + ".");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "Data saved.");
            return true;
        }
        return false;
    }
}
