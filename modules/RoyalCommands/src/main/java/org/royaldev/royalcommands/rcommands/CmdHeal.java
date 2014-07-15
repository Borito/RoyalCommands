package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdHeal extends BaseCommand {

    public CmdHeal(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
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
}
