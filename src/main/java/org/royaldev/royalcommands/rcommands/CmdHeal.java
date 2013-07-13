package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdHeal implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdHeal(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("heal")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.heal")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                    return true;
                }
                Player t = (Player) cs;
                t.sendMessage(MessageColor.POSITIVE + "You have healed yourself!");
                t.setHealth(t.getMaxHealth());
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "You have healed " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            t.sendMessage(MessageColor.POSITIVE + "You have been healed by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + "!");
            t.setHealth(t.getMaxHealth());
            return true;
        }
        return false;
    }

}
