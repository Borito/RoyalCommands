package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

@ReflectCommand
public class CmdPlayerSearch extends BaseCommand {

    public CmdPlayerSearch(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final String search = RoyalCommands.getFinalArg(args, 0);
        final OfflinePlayer[] ops = this.plugin.getServer().getOfflinePlayers();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                int found = 0;
                for (OfflinePlayer op : ops) {
                    if (!op.getName().toLowerCase().contains(search.toLowerCase())) continue;
                    PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(op);
                    if (!pcm.exists()) continue;
                    long seen = pcm.getLong("seen");
                    if (seen < 1L) continue;
                    found++;
                    String lastseen = (op.isOnline()) ? " now" : RUtils.formatDateDiff(seen) + MessageColor.POSITIVE + " ago";
                    cs.sendMessage(MessageColor.POSITIVE + String.valueOf(found) + ". " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + " - Last seen" + MessageColor.NEUTRAL + lastseen + MessageColor.POSITIVE + ".");
                }
                cs.sendMessage(MessageColor.POSITIVE + "Search completed. " + MessageColor.NEUTRAL + found + MessageColor.POSITIVE + " results found.");
            }
        };
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, r);
        return true;
    }
}
