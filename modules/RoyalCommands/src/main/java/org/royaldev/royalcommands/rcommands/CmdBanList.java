package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Set;

@ReflectCommand
public class CmdBanList extends BaseCommand {

    public CmdBanList(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        final Set<OfflinePlayer> banList = this.plugin.getServer().getBannedPlayers();
        if (banList.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + "There are no banned players!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "There are " + MessageColor.NEUTRAL + banList.size() + MessageColor.POSITIVE + " banned players:");
        final StringBuilder sb = new StringBuilder();
        for (OfflinePlayer op : banList) {
            sb.append(MessageColor.NEUTRAL);
            sb.append(op.getName());
            sb.append(MessageColor.RESET);
            sb.append(", ");
        }
        cs.sendMessage(sb.substring(0, sb.length() - 4));
        return true;
    }
}
