package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdVtp implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdVtp(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vtp")) {
            if (!plugin.isAuthorized(cs, "rcmds.vtp")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player victim = plugin.getServer().getPlayer(args[0]);
            if (victim == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command cannot be used in console.");
                return true;
            }
            Player player = (Player) cs;
            cs.sendMessage(MessageColor.POSITIVE + "Teleporting you to player " + MessageColor.NEUTRAL + victim.getName() + MessageColor.POSITIVE + ".");
            player.teleport(victim); // raw teleports in /vtp
            return true;
        }
        return false;
    }
}
