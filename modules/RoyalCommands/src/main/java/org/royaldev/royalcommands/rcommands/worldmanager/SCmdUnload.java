package org.royaldev.royalcommands.rcommands.worldmanager;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdWorldManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

public class SCmdUnload extends SubCommand<CmdWorldManager> {

    private final Flag<String> nameFlag = new Flag<>(String.class, "name", "n");
    private final Flag ejectFlag = new Flag("eject", "e");

    public SCmdUnload(final RoyalCommands instance, final CmdWorldManager parent) {
        super(instance, parent, "unload", true, "Unloads a world. If the eject flag is specified, will kick all players on the world.", "<command> -[n,name] [name] -(e,eject)", new String[0], new Short[0]);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (!Config.useWorldManager) {
            cs.sendMessage(MessageColor.NEGATIVE + "WorldManager is disabled!");
            return true;
        }
        if (!ca.hasContentFlag(this.nameFlag)) {
            cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
            return true;
        }
        final boolean eject = ca.hasFlag(this.ejectFlag);
        final World w = this.plugin.getServer().getWorld(ca.getFlag(this.nameFlag).getValue());
        if (w == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "Unloading world...");
        if (eject) for (Player p : w.getPlayers()) p.kickPlayer("Your world is being unloaded!");
        final boolean success = this.plugin.getServer().unloadWorld(w, true);
        if (success) cs.sendMessage(MessageColor.POSITIVE + "World unloaded successfully!");
        else cs.sendMessage(MessageColor.NEGATIVE + "Could not unload that world.");
        return true;
    }
}
