package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdFeed implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdFeed(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("feed")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.feed")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't feed the console!");
                    return true;
                }
                Player t = (Player) cs;
                t.sendMessage(MessageColor.POSITIVE + "You have fed yourself!");
                t.setFoodLevel(20);
                t.setSaturation(20F);
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (!plugin.ah.isAuthorized(cs, "rcmds.others.feed")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "You have fed " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            t.sendMessage(MessageColor.POSITIVE + "You have been fed by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + "!");
            t.setFoodLevel(20);
            t.setSaturation(20F);
            return true;
        }
        return false;
    }

}
