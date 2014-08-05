package org.royaldev.royalcommands.rcommands.whitelist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdWhitelist;
import org.royaldev.royalcommands.rcommands.SubCommand;

public class SCmdRemove extends SubCommand<CmdWhitelist> {

    public SCmdRemove(final RoyalCommands instance, final CmdWhitelist parent) {
        super(instance, parent, "remove", true, "Removes a player from the whitelist.", "<command> [player]", new String[0], new Short[]{CompletionType.ONLINE_PLAYER.getShort()});
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (this.plugin.whl == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "The whitelist.yml file was invalid! Cannot use whitelist.");
            return true;
        }
        if (eargs.length < 1) {
            this.getParent().showHelp(cs, label);
            return true;
        }
        final String player = eargs[0];
        if (!Config.whitelist.contains(player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player is not whitelisted!");
            return true;
        }
        Config.whitelist.remove(player);
        this.plugin.whl.set("whitelist", Config.whitelist);
        this.getParent().reloadWhitelist();
        cs.sendMessage(MessageColor.POSITIVE + "Removed " + MessageColor.NEUTRAL + player + MessageColor.POSITIVE + " from whitelist.");
        return true;
    }
}
