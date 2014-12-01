package org.royaldev.royalcommands.rcommands.worldmanager;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdWorldManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

public class SCmdList extends SubCommand<CmdWorldManager> {

    public SCmdList(final RoyalCommands instance, final CmdWorldManager parent) {
        super(instance, parent, "list", true, "Lists all the loaded worlds.", "<command>", new String[0], new Short[0]);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (!Config.useWorldManager) {
            cs.sendMessage(MessageColor.NEGATIVE + "WorldManager is disabled!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "Worlds:");
        final StringBuilder sb = new StringBuilder();
        for (final World w : this.plugin.getServer().getWorlds()) {
            sb.append(MessageColor.NEUTRAL).append(RUtils.getMVWorldName(w)).append(MessageColor.RESET).append(", ");
        }
        cs.sendMessage(sb.substring(0, sb.length() - 4));
        return true;
    }
}
