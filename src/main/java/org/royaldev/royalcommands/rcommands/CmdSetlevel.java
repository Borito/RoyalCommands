package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSetlevel implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSetlevel(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setlevel")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.setlevel")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player) && args.length == 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            int lvl;
            try {
                lvl = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                cs.sendMessage(MessageColor.NEGATIVE + "The level must be a number!");
                return true;
            }

            Player t = plugin.getServer().getPlayer((args.length > 1) ? args[1] : cs.getName());
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            t.setLevel(lvl);
            if (!cs.equals(t))
                cs.sendMessage(MessageColor.POSITIVE + "Set the level of " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + lvl + MessageColor.POSITIVE + ".");
            t.sendMessage(MessageColor.POSITIVE + "Your level has been set to " + MessageColor.NEUTRAL + lvl + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
