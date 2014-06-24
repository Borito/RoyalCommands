package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Set;

@ReflectCommand
public class CmdBanList implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdBanList(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("banlist")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            final Set<OfflinePlayer> banList = plugin.getServer().getBannedPlayers();
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
        return false;
    }

}
