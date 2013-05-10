package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdInvmod implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdInvmod(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("invmod")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.invmod")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.invmod")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot modify that player's inventory.");
                return true;
            }
            p.openInventory(t.getInventory());
            String possessive = (t.getName().toLowerCase().endsWith("s")) ? "'" : "'s";
            cs.sendMessage(MessageColor.POSITIVE + "Opened " + MessageColor.NEUTRAL + t.getName() + possessive + MessageColor.POSITIVE + " inventory.");
            return true;
        }
        return false;
    }

}
