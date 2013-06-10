package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdQuit implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdQuit(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("quit")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.quit")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            RUtils.silentKick(p, "You have left the game.");
            plugin.getServer().broadcastMessage(MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + " has left the game.");
            return true;
        }
        return false;
    }

}
