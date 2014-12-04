package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdVtphere extends BaseCommand {

    public CmdVtphere(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player t = this.plugin.getServer().getPlayer(args[0]);
        if (t == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command cannot be used in console.");
            return true;
        }
        Player player = (Player) cs;
        cs.sendMessage(MessageColor.POSITIVE + "Teleporting player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to you.");
        t.teleport(player); // raw teleports in /vtphere;
        return true;
    }
}
