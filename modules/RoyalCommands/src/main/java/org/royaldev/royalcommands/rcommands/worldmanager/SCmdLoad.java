package org.royaldev.royalcommands.rcommands.worldmanager;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdWorldManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.io.File;

public class SCmdLoad extends SubCommand<CmdWorldManager> {

    private final Flag<String> nameFlag = new Flag<>(String.class, "name", "n");

    public SCmdLoad(final RoyalCommands instance, final CmdWorldManager parent) {
        super(instance, parent, "load", true, "Loads a world.", "<command> -[n,name] [name]", new String[0], new Short[0]);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (!Config.useWorldManager) {
            cs.sendMessage(MessageColor.NEGATIVE + "WorldManager is disabled!");
            return true;
        }
        if (!ca.hasContentFlag(this.nameFlag)) {
            cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
            return true;
        }
        final String name = ca.getFlag(this.nameFlag).getValue();
        boolean contains = false;
        File[] fs = this.plugin.getServer().getWorldContainer().listFiles();
        if (fs == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "The world directory is invalid!");
            return true;
        }
        for (File f : fs) if (f.getName().equals(name)) contains = true;
        if (!contains) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
            return true;
        }
        World w;
        try {
            w = RoyalCommands.wm.loadWorld(name);
        } catch (IllegalArgumentException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
            return true;
        } catch (NullPointerException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not read world folders!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "Loaded world " + MessageColor.NEUTRAL + w.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
