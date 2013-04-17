package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdHarm implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdHarm(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("harm")) {
            if (!plugin.isAuthorized(cs, "rcmds.harm")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That person is not online!");
                return true;
            }
            int toDamage;
            try {
                toDamage = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                cs.sendMessage(MessageColor.NEGATIVE + "The damage must be a number!");
                return false;
            }
            if (toDamage > t.getMaxHealth() || toDamage <= 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "The damage you entered is not within 1 and " + t.getMaxHealth() + "!");
                return true;
            }

            if (!cs.getName().equalsIgnoreCase(t.getName()) && plugin.isAuthorized(t, "rcmds.exempt.harm")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You may not harm that player.");
                return true;
            }
            t.damage(toDamage);
            t.sendMessage(MessageColor.NEGATIVE + "You have just been damaged by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + "!");
            cs.sendMessage(MessageColor.POSITIVE + "You just damaged " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + "!");
            return true;
        }
        return false;
    }

}
