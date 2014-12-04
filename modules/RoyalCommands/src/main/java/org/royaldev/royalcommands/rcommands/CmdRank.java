package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdRank extends BaseCommand {

    public CmdRank(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player victim = this.plugin.getServer().getPlayer(args[0]);
        if (victim == null || this.plugin.isVanished(victim, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        String rank;
        try {
            if (!this.plugin.vh.usingVault()) throw new Exception();
            rank = this.plugin.vh.getPermission().getPrimaryGroup(victim);
        } catch (Exception e) {
            rank = null;
        }
        if (rank == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player has no rank.");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + victim.getName() + MessageColor.POSITIVE + " has the group " + MessageColor.NEUTRAL + rank + MessageColor.POSITIVE + ".");
        return true;
    }
}
