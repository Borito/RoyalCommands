package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdLevel extends BaseCommand {

    public CmdLevel(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command can only be used by players!");
            return true;
        }
        Player player = (Player) cs;
        player.setLevel(player.getLevel() + 1);
        cs.sendMessage(MessageColor.POSITIVE + "XP level raised by one!");
        return true;
    }
}
