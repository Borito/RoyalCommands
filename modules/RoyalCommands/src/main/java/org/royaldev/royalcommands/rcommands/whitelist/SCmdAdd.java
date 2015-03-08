package org.royaldev.royalcommands.rcommands.whitelist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdWhitelist;
import org.royaldev.royalcommands.rcommands.SubCommand;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

public class SCmdAdd extends SubCommand<CmdWhitelist> {

    public SCmdAdd(final RoyalCommands instance, final CmdWhitelist parent) {
        super(instance, parent, "add", true, "Adds a player to the whitelist.", "<command> -p (player) -u (uuid)", new String[0], new Short[]{CompletionType.ONLINE_PLAYER.getShort()});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (this.plugin.whl == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "The whitelist.yml file was invalid! Cannot use whitelist.");
            return true;
        }
        final RPlayer rp = this.getParent().getRPlayer(ca, cs);
        if (rp == null) return true;
        final String uuid = rp.getUUID().toString();
        if (Config.whitelist.contains(uuid)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player is already whitelisted!");
            return true;
        }
        Config.whitelist.add(uuid);
        this.plugin.whl.set("whitelist", Config.whitelist);
        this.getParent().reloadWhitelist();
        cs.sendMessage(MessageColor.POSITIVE + "Added " + MessageColor.NEUTRAL + rp.getName() + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + uuid + MessageColor.POSITIVE + ") to whitelist.");
        return true;
    }
}
